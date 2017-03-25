package ist.meic.pa;


import javassist.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

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

    public void makeTemplate(ArrayList<String> args) throws NotFoundException, ClassNotFoundException, IllegalAccessException, InstantiationException, CannotCompileException {
        // syntax checker
        validateKeywordArgs(args);

        TreeMap<String, String> keywordArgs = prepareKeywordArgs(args);
        TreeMap<String, CtField> classFields = getClassFields();

       /* for (String key : classFields.keySet()) {
            System.out.println("Name: " + key + " Value: " + classFields.get(key));
        }

        System.out.println("KEYWORDARGS:");
        for (String key : keywordArgs.keySet()) {
            System.out.println("Arg: " + key + " Value: " + keywordArgs.get(key));
        }*/

        // checks if every keyword argument exists in the class
        if(!classFields.keySet().containsAll(keywordArgs.keySet())) {
            throw new RuntimeException("Invalid keywords arguments!");
        }

        // Assign variables which are in the arguments with multi levels
        solveDependencies(keywordArgs);

       /* System.out.println("DEPENDENCIES SOLVED:");
        for (String key : keywordArgs.keySet()){
            System.out.println("Arg: " + key + " Value: " + keywordArgs.get(key));
        }*/

        // makes complex attributions
        for (String key : keywordArgs.keySet()) {
            // Types considered:
            // byte, short, int, long, float, double, boolean, char
            ExpressionParser parser = new ExpressionParser();

            try {
                parser.parse(keywordArgs.get(key), classFields.get(key).getType().getSimpleName());
            } catch (Throwable e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Field \"" + classFields.get(key).getName() + "\" is not the same type as \"" + keywordArgs.get(key) + "\"");
            }
        }



        new ConstructorAdapter(keywordArgs, classFields, targetClass, ctConstructor).adaptConstructor();

    }

    private void solveDependencies(TreeMap<String, String> keywordArgs) throws NotFoundException {
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
                        if(aux.getValue().contains(entry.getKey()) && !aux.getKey().equals(entry.getKey())){
                            aux.setValue(aux.getValue().replace(entry.getKey(), "((" + getClassFields().get(entry.getKey()).getType().getSimpleName() + ")" +
                                    entry.getValue() + ")"));
                            changed = true;
                        }
                    }
                }
            }
        }
    }

    private TreeMap<String, String> prepareKeywordArgs(ArrayList<String> argsArray) {
        TreeMap<String, String> keysmap = new TreeMap<>();
        if(argsArray.size() != 0) {
            for(String args : reverse(argsArray)) {
                String[] keywords = args.split(",");
                for (String keyword : keywords) {
                    String[] result = keyword.split("=");
                    if (result.length == 2)
                        keysmap.put(result[0], result[1]);
                    else if(!keyword.contains("=")){
                        continue;
                    }
                    else
                        throw new RuntimeException("Keyword " + keyword + " has wrong format");
                }
            }
        }
        return keysmap;
    }


    private void validateKeywordArgs(ArrayList<String> args) throws RuntimeException{
    /*boolean complex = Pattern.matches("(((_?[a-zA-Z]+[0-9]*)=.*,?)*)", args);
    if (!complex)
        throw new RuntimeException("Keyword arguments have a wrong format!");
*/}


    private TreeMap<String, CtField> getClassFields() throws NotFoundException {
        TreeMap<String, CtField> output = new TreeMap<>();
        CtClass auxClass = targetClass;

        while(auxClass.getSuperclass() != null){
            for(int i = 0 ; i < auxClass.getDeclaredFields().length ; i++){
                if(auxClass.getDeclaredFields()[i].getModifiers() != Modifier.PRIVATE){
                    output.put(auxClass.getDeclaredFields()[i].getName(), auxClass.getDeclaredFields()[i]);
                }
            }
            auxClass = auxClass.getSuperclass();
        }

        for(CtField f : targetClass.getDeclaredFields()){
            output.put(f.getName(), f);
        }
        return output;
    }


    public <T> ArrayList<T> reverse(ArrayList<T> list) {
        int length = list.size();
        ArrayList<T> result = new ArrayList<T>(length);

        for (int i = length - 1; i >= 0; i--) {
            result.add(list.get(i));
        }

        return result;
    }

}
