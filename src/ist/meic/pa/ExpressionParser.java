package ist.meic.pa;

import javassist.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by francisco on 21/03/2017.
 */
public class ExpressionParser  {

    public Object parse(String expression, String type)
            throws NotFoundException, CannotCompileException, InvocationTargetException, IllegalAccessException,
            NoSuchMethodException {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctEvaluator = pool.makeClass("Evaluator" + UUID.randomUUID());
        String template =
                "public static " + type + " eval() { " +
                        " return ("+ expression +");" +
                        "}";
        CtMethod ctMethod = CtNewMethod.make(template, ctEvaluator);
        ctEvaluator.addMethod(ctMethod);
        Class evaluator = ctEvaluator.toClass();
        Method meth = evaluator.getDeclaredMethod("eval");
        return meth.invoke(null);
    }
}