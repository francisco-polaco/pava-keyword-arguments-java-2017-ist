package ist.meic.pa;

import javassist.*;
import javassist.bytecode.*;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.MemberValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by francisco on 23/03/2017.
 */
public class KeyConstructorsTranslator implements Translator {
    @Override
    public void start(ClassPool classPool) throws NotFoundException, CannotCompileException {

    }

    @Override
    public void onLoad(ClassPool classPool, String s) throws NotFoundException, CannotCompileException {
        CtClass targetClass = classPool.get(s);
        CtConstructor[] constructors = targetClass.getConstructors();

        CtClass auxClass = targetClass;
        ArrayList<String> keyWords = new ArrayList<>();
        while(auxClass.getSuperclass() != null){
            for (CtConstructor constructor : auxClass.getConstructors()) {
                if(constructor.hasAnnotation(KeywordArgs.class)){
                    try {
                        keyWords.add(((KeywordArgs) constructor.getAnnotation(KeywordArgs.class)).value());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            auxClass = auxClass.getSuperclass();
        }

        for (CtConstructor constructor : constructors) {
            if(constructor.hasAnnotation(KeywordArgs.class)){
                TemplateMaker templateMaker = new TemplateMaker(constructor, targetClass);
                try {
                    templateMaker.makeTemplate(keyWords);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
            // case where there is no constructor in a non annotated class - set body behaviour
            else if (!constructor.getSignature().equals("()V"))
                targetClass.addConstructor(CtNewConstructor.defaultConstructor(targetClass));
        }

        CtClass ctClass = classPool.get("ist.meic.pa.KeywordArgs");
        ClassFile classFile = ctClass.getClassFile();

        if(classFile.getAttribute("RuntimeVisibleAnnotations") != null){
            ConstPool constPool = classFile.getConstPool();

            // remove the old annotations
            classFile.removeAttribute("RuntimeVisibleAnnotations");

            AnnotationsAttribute attributeSet =
                    new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);

            attributeSet.addAnnotation(builEnumdAnnotation(
                    constPool,
                    "java.lang.annotation.Retention",
                    "java.lang.annotation.RetentionPolicy",
                    "CLASS",
                    "value")
            );

            attributeSet.addAnnotation(tanga(
                    constPool,
                    "java.lang.annotation.Target",
                    "java.lang.annotation.ElementType",
                    "CONSTRUCTOR",
                    "value")
            );

            classFile.addAttribute(attributeSet);
        }



    }

    private javassist.bytecode.annotation.Annotation
    builEnumdAnnotation(ConstPool constPool, String type, String enumType, String enumValue, String attrName){

        javassist.bytecode.annotation.Annotation annotation =
                new javassist.bytecode.annotation.Annotation(type, constPool);

        EnumMemberValue enumMemberValue = new EnumMemberValue(constPool);
        enumMemberValue.setType(enumType);
        enumMemberValue.setValue(enumValue);

        annotation.addMemberValue(attrName, enumMemberValue);
        return annotation;
    }

    private javassist.bytecode.annotation.Annotation
        tanga(ConstPool constPool, String type, String enumType, String enumValue, String attrName){

        javassist.bytecode.annotation.Annotation annotation =
                new javassist.bytecode.annotation.Annotation(type, constPool);

        ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constPool);

        MemberValue[] elements = new MemberValue[1];
        EnumMemberValue enumMemberValue = new EnumMemberValue(constPool);
        enumMemberValue.setType(enumType);
        enumMemberValue.setValue(enumValue);

        elements[0] = enumMemberValue;

        arrayMemberValue.setValue(elements);

        annotation.addMemberValue(attrName, arrayMemberValue);
       return annotation;
    }

}
