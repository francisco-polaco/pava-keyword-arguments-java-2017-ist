package ist.meic.pa;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import javassist.Translator;
import javassist.bytecode.*;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class KeyConstructorsExtended {

    public static void main(String[] args) throws Throwable {
        if(args.length != 1){
            System.out.println("Invalid arguments!");
            System.out.println("Usage: java " + KeyConstructorsExtended.class.getCanonicalName() + " <classname>");
            return;
        }
        Translator keyConstructorsTranslator = new KeyConstructorsTranslator();
        ClassPool classPool = ClassPool.getDefault();
        Loader classLoader = new Loader();
        classLoader.addTranslator(classPool, keyConstructorsTranslator);
        // To show a trace as presented on the project description
        try {
            classLoader.run(args[0], null);
        }catch (Throwable e){
            RuntimeException ex = null;
            while(e.getCause()!= null){
                if(e.getCause() instanceof RuntimeException){
                    ex = (RuntimeException) e.getCause();
                }
                e = e.getCause();
            }
            if(ex == null) throw e;
            else throw ex;
        }
    }


}
