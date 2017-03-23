package ist.meic.pa;

import javassist.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


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

        CtConstructor ctConstructor = targetClass.getConstructor("([Ljava/lang/Object;)V");
        // insert the defaults
        String template = "{";
        for (String field : fields.keySet()){
            template += field + " = " + fields.get(field) + " ;";
        }
        // since the arguments replace the defaults, just insert after
        template +=
                "java.util.TreeMap fields = new java.util.TreeMap();" +
                "Class targetClass = this.getClass();" +

                "for(int i = 0; i < targetClass.getFields().length; i++)" +
                    "fields.put(targetClass.getFields()[i].getName(), targetClass.getFields()[i]);" +

                "for(int i = 0; i < targetClass.getDeclaredFields().length; i++)" +
                    "fields.put(targetClass.getDeclaredFields()[i].getName(), targetClass.getDeclaredFields()[i]);" +

                "java.util.Collection keySet = fields.keySet();" +
                "java.util.List args = java.util.Arrays.asList($1);" +
                "for(int i = 0; i < $1.length; i+=2) {" +
                    "if(!keySet.contains($1[i]))" +
                        "throw new RuntimeException(\"There is no field with the name\" + $1[i]);" +
                    "java.lang.reflect.Field toInsert = (java.lang.reflect.Field ) fields.get($1[i]);" +
                    "try {" +
                        "toInsert.set(this,$1[i+1]);" +
                    "}" +
                    "catch (Throwable t) {" +
                         "throw new RuntimeException(\"Argument\" + $1[i+1] + \"is not of the type toInsert.getName()\");" +
                    "}" +
                "}";
        template += "}";
        ctConstructor.setBody(template);
        try {
            targetClass.writeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
