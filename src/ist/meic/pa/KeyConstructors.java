package ist.meic.pa;

import javassist.ClassPool;
import javassist.Loader;
import javassist.Translator;

public class KeyConstructors {

    public static void main(String[] args) throws Throwable {
        if(args.length != 1){
            System.out.println("Invalid arguments!");
            System.out.println("Usage: java " + KeyConstructors.class.getCanonicalName() + " <classname>");
            return;
        }
        Translator keyConstructorsTranslator = new KeyConstructorsTranslator();
        ClassPool classPool = ClassPool.getDefault();
        Loader classLoader = new Loader();
        classLoader.addTranslator(classPool, keyConstructorsTranslator);
        classLoader.run(args[0], null);
    }
}
