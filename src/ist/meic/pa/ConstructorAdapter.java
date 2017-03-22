package ist.meic.pa;

import javassist.*;

import java.util.TreeMap;

import static ist.meic.pa.KeyConstructors.classLoader;

/**
 * Created by francisco on 22/03/2017.
 */
public class ConstructorAdapter {

    private TreeMap<String, String> fields;
    private CtClass targetClass;

    public ConstructorAdapter(TreeMap<String, String> fields, CtClass targetClass) {
        this.fields = fields;
        this.targetClass = targetClass;
    }

    public void adaptConstructor() throws NotFoundException, CannotCompileException {
        /*ClassPool pool = ClassPool.getDefault();
        CtClass ctEvaluator = pool.makeClass("Evaluator" + UUID.randomUUID());
        String template =
                "public static " + type + " eval() { " +
                        " return ("+ expression +");" +
                        "}";
        CtMethod ctMethod = CtNewMethod.make(template, ctEvaluator);
        ctEvaluator.addMethod(ctMethod);
        Class evaluator = ctEvaluator.toClass();
        Method meth = evaluator.getDeclaredMethod("eval");
        return meth.invoke(null);*/

        CtConstructor ctConstructor = targetClass.getConstructor("([Ljava/lang/Object;)V");
        String template = "System.out.println(\"batata!\");";
        ctConstructor.setBody(template);
        //Obtain the run time class Foo
        Class rtFoo = null;
        try {
            rtFoo = classLoader.loadClass("ist.meic.pa.Widget");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Instantiate Foo
        try {
            Object foo = rtFoo.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //targetClass.addConstructor(ctConstructor);
    }
}
