package ist.meic.pa;


import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.NotFoundException;

/**
 * Created by francisco on 20/03/2017.
 */
public class TemplateMaker {

    private CtConstructor ctConstructor;
    private CtClass targetClass;



    public TemplateMaker(CtConstructor ctConstructor, CtClass targetClass) {
        this.ctConstructor = ctConstructor;
        this.targetClass = targetClass;
    }

    private TreeMap<String, CtField> getClassFields() throws NotFoundException {
        TreeMap<String, CtField> output = new TreeMap<>();
        CtClass auxClass = targetClass;
        do {
            for(CtField f : auxClass.getDeclaredFields()){
                if(!output.containsKey(f.getName())) {
                    /*
                     * If there is a field in a class and its super class
                     * with the same name, we ignore the super class field.
                    */
                    output.put(f.getName(), f);
                }
            }
            auxClass = auxClass.getSuperclass();
        } while(auxClass != null && auxClass.getSuperclass()!= null && !auxClass.getSuperclass().equals(auxClass));
        return output;
    }


    public void makeTemplate(String args) throws NotFoundException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        System.out.println(args);
        lexValidation(args);

        TreeMap<String, String> keysmap = prepareMapArgsVal(args);

        TreeMap<String, CtField> classFields = getClassFields();

        System.out.println("CLASSFIELDS:");
        for ( String key : classFields.keySet()) {
            System.out.println("NAME: " + key + " Value: " + classFields.get(key));
        }

        System.out.println("KEYMAP:");

        for (Map.Entry<String, String> entry :
                keysmap.entrySet()) {

            System.out.println("Entry: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        /* ------- possible code ----------*/

        if(!classFields.keySet().containsAll(keysmap.keySet())) {
            throw new RuntimeException("Invalid keywords arguments!");
        }


        for (String key : keysmap.keySet()) {
            TypeParser parser = (TypeParser) Class.forName(this.getClass().getPackage() + "." +
                    classFields.get(key).getType().getSimpleName() + "Parser").newInstance();
            parser.parse(keysmap.get(key));




            //getClass().forName()
            //classFields.get(key).getType()


            }



        /* ------- end of possible code ----------*/

    }

    private TreeMap<String, String> prepareMapArgsVal(String args) {
        TreeMap<String, String> keysmap = new TreeMap<>();
        if(!args.equals("")) {
            String[] keywords = args.split(",");
            for (String keyword : keywords) {
                String[] result = keyword.split("=");
                if (result.length == 2)
                    keysmap.put(result[0], result[1]);
                else
                    throw new BadKeyWordsException("Keyword " + keyword + " has wrong format");

            }
        }
        return keysmap;
    }


    private void lexValidation(String args) {
        boolean simple = Pattern.matches("(([a-zA-Z]+)=[a-zA-Z0-9]+)?", args);
        if(!simple) {
            boolean complex = Pattern.matches("((([a-zA-Z]+)=[a-zA-Z0-9]+,?)*)", args);
            if (!complex)
                throw new BadKeyWordsException("Keyword arguments have a wrong format!");
        }
    }

}
