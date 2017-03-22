package ist.meic.pa;

import javassist.*;

import java.io.IOException;
import java.util.TreeMap;


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
        String template = "{";
        for (String field : fields.keySet()){
            template += field + " = " + fields.get(field) + " ;";
        }
        template += "String[] a = new String[3];" +
                "        java.lang.Class auxClass = this.getClass();" +
                "        for(java.lang.reflect.Field f : auxClass.getFields()){" +
                "            output.put(f.getName(), f);" +
                "        }" +
                "        for(CtField f : auxClass.getDeclaredFields()){" +
                "            output.put(f.getName(), f);" +
                "        }" +
                "for(int i=0; i<$args.length; i+=2){" +


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
