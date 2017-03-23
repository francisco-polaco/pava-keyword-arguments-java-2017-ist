package ist.meic.pa;

import javassist.*;

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
        for (CtConstructor constructor : constructors) {
            if(constructor.hasAnnotation(KeywordArgs.class)){
                TemplateMaker templateMaker = new TemplateMaker(constructor, targetClass);
                try {
                    templateMaker.makeTemplate(((KeywordArgs) constructor.getAnnotation(KeywordArgs.class)).value());
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
