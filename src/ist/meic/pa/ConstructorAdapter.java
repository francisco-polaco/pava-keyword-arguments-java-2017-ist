package ist.meic.pa;

import javassist.*;

import java.util.TreeMap;

public class ConstructorAdapter {

    private TreeMap<String, String> fields;
    private CtClass targetClass;
    private CtConstructor ctConstructor;

    public ConstructorAdapter(TreeMap<String, String> fields, CtClass targetClass, CtConstructor ctConstructor) {
        this.fields = fields;
        this.targetClass = targetClass;
        this.ctConstructor = ctConstructor;
    }

    public void adaptConstructor() throws NotFoundException, CannotCompileException {
        // add default constructor - needed for setBody
        targetClass.addConstructor(CtNewConstructor.defaultConstructor(targetClass));

        String template = "{";
        for (String field : fields.keySet()){
            // let the field assume the default value
            if(!fields.get(field).equalsIgnoreCase(""))
                template += field + " = " + fields.get(field) + " ;";
        }


        /*java.lang.String value = "";
        for (java.lang.reflect.Constructor constructor : getClass().getConstructors()) {
            if(constructor.isAnnotationPresent(ist.meic.pa.KeywordArgs.class)){
                value = ((ist.meic.pa.KeywordArgs) constructor.getAnnotation(ist.meic.pa.KeywordArgs.class)).value();
            }
        }
        java.lang.String[] keywords = value.split(",");
        java.util.ArrayList listCustomFields = new java.util.ArrayList();
        for(int i = 0 ; i < keywords.length ; i++){
            listCustomFields.add(keywords[i].split("=")[0]);
        }*/

        // since the arguments replace the defaults, just insert after
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
                   "currentClass = currentClass.getSuperclass();"+
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
                         "throw new RuntimeException(\"Argument \" + $1[i+1] + \" is not of the type toInsert.getName()\");" +
                    "}" +
                "}";
        template += "}";
        ctConstructor.setBody(template);
    }
}
