package ist.meic.pa;

import javassist.*;

import java.util.ArrayList;

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
        while (auxClass.getSuperclass() != null) {
            for (CtConstructor constructor : auxClass.getConstructors()) {
                if (constructor.hasAnnotation(KeywordArgs.class)) {
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
            if (constructor.hasAnnotation(KeywordArgs.class)) {
                TemplateMaker templateMaker = new TemplateMaker(constructor, targetClass);
                try {
                    templateMaker.makeTemplate(keyWords);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            // case where there is no constructor in a non annotated class - set body behaviour
            else if (!constructor.getSignature().equals("()V"))
                targetClass.addConstructor(CtNewConstructor.defaultConstructor(targetClass));
        }
    }

}
