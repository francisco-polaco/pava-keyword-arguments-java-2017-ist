package ist.meic.pa;

import javassist.*;

import java.io.IOException;
import java.util.Collection;
import java.util.TreeMap;


/**
 * Created by francisco on 22/03/2017.
 */
public class ConstructorAdapter {

    private TreeMap<String, String> fields;
    private TreeMap<String, CtField> classFields;
    private CtClass targetClass;
    private CtConstructor ctConstructor;

    public ConstructorAdapter(TreeMap<String, String> fields, TreeMap<String, CtField> classFields, CtClass targetClass, CtConstructor ctConstructor) {
        this.fields = fields;
        this.classFields = classFields;
        this.targetClass = targetClass;
        this.ctConstructor = ctConstructor;
    }

    public void adaptConstructor() throws NotFoundException, CannotCompileException {
        // add default constructor
        targetClass.addConstructor(CtNewConstructor.defaultConstructor(targetClass));

        String template = "{";
        for (String field : fields.keySet()){
            template += field + " = " + fields.get(field) + " ;";
        }
        Collection l = fields.keySet();

        // since the arguments replace the defaults, just insert after
        template += "java.util.TreeMap fields = new java.util.TreeMap();" +
                "Class targetClass = this.getClass();" +

                "for(int i = 0; i < targetClass.getDeclaredFields().length; i++)" +
                    "fields.put(targetClass.getDeclaredFields()[i].getName(), targetClass.getDeclaredFields()[i]);" +

                "        Class currentClass = this.getClass();\n" +
                "        while(currentClass.getSuperclass() != null){\n" +
                "            for(int i = 0 ; i < currentClass.getDeclaredFields().length ; i++){\n" +
                "                if(currentClass.getDeclaredFields()[i].getModifiers() != java.lang.reflect.Modifier.PRIVATE){\n" +
                "                    fields.put(currentClass.getDeclaredFields()[i].getName(), currentClass.getDeclaredFields()[i]);\n" +
                "                }\n" +
                "            }\n" +
                "           currentClass = currentClass.getSuperclass();"+
                "        }\n" +

                "java.util.Collection keySet = fields.keySet();" +
                "java.util.List args = java.util.Arrays.asList($1);" +
                "for(int i = 0; i < $1.length; i+=2) {" +
                    "if(!keySet.contains($1[i]))" +
                        "throw new RuntimeException(\"Unrecognized keyword: \" + $1[i]);" +
                    "java.lang.reflect.Field toInsert = (java.lang.reflect.Field) fields.get($1[i]);" +
                    "try {" +
                        "toInsert.set(this,$1[i+1]);" +
                    "}" +
                    "catch (Throwable t) {" +
                         "throw new RuntimeException(\"Argument \" + $1[i+1] + \" is not of the type toInsert.getName()\");" +
                    "}" +
                "}";
        template += "}";
        ctConstructor.setBody(template);
        try {
            targetClass.writeFile("instrus");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
