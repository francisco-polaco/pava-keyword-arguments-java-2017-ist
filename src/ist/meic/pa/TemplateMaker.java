package ist.meic.pa;


import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import ist.meic.pa.exception.BadKeyWordsException;
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

    public void makeTemplate(String args) throws NotFoundException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        System.out.println("KeywordArgs = " + args);

        // syntax checker
        validateKeywordArgs(args);

        TreeMap<String, String> keywordArgs = prepareKeywordArgs(args);
        TreeMap<String, CtField> classFields = getClassFields();

        System.out.println("CLASSFIELDS:");
        for (String key : classFields.keySet()) {
            System.out.println("Name: " + key + " Value: " + classFields.get(key));
        }

        System.out.println("KEYWORDARGS:");
        for (String key : keywordArgs.keySet()) {
            System.out.println("Arg: " + key + " Value: " + keywordArgs.get(key));
        }

        // checks if every keyword argument exists in the class
        if(!classFields.keySet().containsAll(keywordArgs.keySet())) {
            throw new RuntimeException("Invalid keywords arguments!");
        }

        // Assign variables which are in the arguments with multi levels
        solveDependencies(keywordArgs);

        System.out.println("DEPENDENCIES SOLVED:");
        for (String key : keywordArgs.keySet()){
            System.out.println("Arg: " + key + " Value: " + keywordArgs.get(key));
        }

        // makes complex attributions
        for (String key : keywordArgs.keySet()) {
            // Types considered:
            // byte, short, int, long, float, double, boolean, char
            // upper case the first letter of the primitive data types - remeber ctclass primitive types are != java
            String typeName = classFields.get(key).getType().getSimpleName().substring(0,1).toUpperCase() +
                    classFields.get(key).getType().getSimpleName().substring(1);


            TypeParser parser = (TypeParser) Class.forName(this.getClass().getPackage().getName() + "." +
                    typeName + "Parser").newInstance();

            parser.parse(keywordArgs.get(key));
        }


    }

    private void solveDependencies(TreeMap<String, String> keywordArgs) {
        boolean changed = true;
        while(changed){
            changed = false;
            for(Map.Entry<String,String> entry : keywordArgs.entrySet()) {
                if (keywordArgs.containsKey(entry.getValue())) {
                    entry.setValue(keywordArgs.get(entry.getValue()));
                    changed = true;
                }
                // @KeywordArgs("result=40+5,value=Math.sin(result)")
                else{
                    for(Map.Entry<String,String> aux : keywordArgs.entrySet()){
                        if(aux.getValue().contains(entry.getKey())){
                            aux.setValue(aux.getValue().replace(entry.getKey(), entry.getValue()));
                            changed = true;
                        }
                    }
                }
            }
        }
    }

    private TreeMap<String, String> prepareKeywordArgs(String args) {
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


    private void validateKeywordArgs(String args) {
        /*
        boolean simple = Pattern.matches("(([a-zA-Z]+)=[a-zA-Z0-9]+)?", args);
        if(!simple) {
            boolean complex = Pattern.matches("((([a-zA-Z]+)=[a-zA-Z0-9]+,?)*)", args);
            if (!complex)
                throw new BadKeyWordsException("Keyword arguments have a wrong format!");
        }*/
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



}
