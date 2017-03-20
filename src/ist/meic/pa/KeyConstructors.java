package ist.meic.pa;

import javassist.CtClass;

public class KeyConstructors {

    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Invalid arguments!");
            System.out.println("Usage: java " + KeyConstructors.class.getCanonicalName() + " <classname>");
            return;
        }
        // Write the greatest code here :D
        System.out.println("Hello World!");
        CtClass batata;
    }
}
