package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtNewConstructor;

import java.util.ArrayList;
import java.util.TreeMap;

class ConstructorAdapter {

    private TreeMap<String, String> fields;
    private ArrayList<String> keysInOrder;
    private CtClass targetClass;
    private CtConstructor ctConstructor;

    ConstructorAdapter(TreeMap<String, String> fields, ArrayList<String> keysInOrder, CtClass targetClass, CtConstructor ctConstructor) {
        this.fields = fields;
        this.keysInOrder = keysInOrder;
        this.targetClass = targetClass;
        this.ctConstructor = ctConstructor;
    }

    void adaptConstructor() throws CannotCompileException {
        // add default constructor - needed for setBody
        targetClass.addConstructor(CtNewConstructor.defaultConstructor(targetClass));

        String template = "{";

        for (String field : keysInOrder) {
            template += field + " = " + fields.get(field) + " ;";
        }

        // Since the arguments replace the defaults, we need to insert them after
        template += "java.util.TreeMap fields = new java.util.TreeMap();" +
                "Class targetClass = this.getClass();" +
                "for(int i = 0; i < targetClass.getDeclaredFields().length; i++)" +
                    "fields.put(targetClass.getDeclaredFields()[i].getName(), targetClass.getDeclaredFields()[i]);" +

                "Class currentClass = this.getClass();" +
                "while(currentClass.getSuperclass() != null){" +
                    "for(int i = 0 ; i < currentClass.getDeclaredFields().length ; i++){" +
                        "if(currentClass.getDeclaredFields()[i].getModifiers() != java.lang.reflect.Modifier.PRIVATE){" +
                        "fields.put(currentClass.getDeclaredFields()[i].getName(), currentClass.getDeclaredFields()[i]);" +
                        "}" +
                    "}" +
                    "currentClass = currentClass.getSuperclass();" +
                "}" +

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
                        "throw new RuntimeException(\"Argument \" + $1[i+1] + \" " +
                            "is not of the type toInsert.getName()\");" +
                    "}" +
                "}";
        template += "}";
        ctConstructor.setBody(template);
    }
}
