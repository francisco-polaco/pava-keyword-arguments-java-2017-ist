package ist.meic.pa;

import javassist.*;

public class KeyConstructors {

    public static void main(String[] args) throws NotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException, CannotCompileException {
        if(args.length != 1){
            System.out.println("Invalid arguments!");
            System.out.println("Usage: java " + KeyConstructors.class.getCanonicalName() + " <classname>");
            return;
        }
        ClassPool classPool = ClassPool.getDefault();
        //Create Javassist class loader
        CtClass targetClass = classPool.get(args[0]);
        CtConstructor[] constructors = targetClass.getConstructors();
        for (CtConstructor constructor : constructors) {
            if(constructor.hasAnnotation(KeywordArgs.class)){
                TemplateMaker templateMaker = new TemplateMaker(constructor, targetClass);
                templateMaker.makeTemplate(((KeywordArgs) constructor.getAnnotation(KeywordArgs.class)).value());
            }
        }





    }
}
