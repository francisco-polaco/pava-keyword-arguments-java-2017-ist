package ist.meic.pa;


import javassist.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

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
        TreeMap<String, CtField> classFields = getClassFields(keywordArgs);

        // checks if every keyword argument exists in the class
        for (String keyword : keywordArgs.keySet()){
            if(!classFields.keySet().contains(keyword)){
                throw new RuntimeException("Unrecognized keyword @" + targetClass.getSimpleName() + ": " + keyword);
            }
        }

        // solve the keywordArgs dependencies
        solveDependencies(keywordArgs);
        new ConstructorAdapter(keywordArgs, targetClass, ctConstructor).adaptConstructor();
    }

    // Catarina god das regex's verifica sff!
    private void validateKeywordArgs(ArrayList<String> args) throws RuntimeException{
        for (String keywordArg : args){
            String pattern = "(([a-zA-z]+[0-9]*=.*,?)*)|(([a-zA-z]+[0-9]*,?)*)|\\s*";
            if(!Pattern.matches(pattern, keywordArg))
                throw new RuntimeException("KeywordArg @" + targetClass.getSimpleName()  + ": " + keywordArg + " has wrong format!");
        }
    }

    private TreeMap<String, String> prepareKeywordArgs(ArrayList<String> argsArray) {
        TreeMap<String, String> keywordArgs = new TreeMap<>();
        if(argsArray.size() != 0) {
            for(String args : reverse(argsArray)) {
                if(args.equals(""))
                    break;

                String[] keywords = args.split(",");
                for (String keyword : keywords) {
                    // needed to invalidate invalid keywordArgs which appear without a equal
                    if(!keyword.contains("=")) {
                        keywordArgs.put(keyword, "");
                        continue;
                    }
                    String[] result = keyword.split("=");
                    if (result.length == 2)
                        keywordArgs.put(result[0], result[1]);
                    else
                        throw new RuntimeException("KeywordArg @" + targetClass.getSimpleName() +": " + keyword + " has wrong format!");
                }
            }
        }
        return keywordArgs;
    }

    private TreeMap<String, CtField> getClassFields(TreeMap<String, String> args) throws NotFoundException {
        TreeMap<String, CtField> classFields = new TreeMap<>();

        CtClass auxClass = targetClass;
        while(auxClass.getSuperclass() != null){
            CtField[] fields = auxClass.getDeclaredFields();
            for (CtField field : fields) {
                if (field.getModifiers() != Modifier.PRIVATE) {
                    classFields.put(field.getName(), field);
                }
                // avoid the case where private field annotations get
                // into the base classes due to recursively adding annotations
                else {
                    if (targetClass != auxClass)
                        args.remove(field.getName());
                }
            }
            auxClass = auxClass.getSuperclass();
        }

        for(CtField f : targetClass.getDeclaredFields()){
            classFields.put(f.getName(), f);
        }

        return classFields;
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
            }
        }
    }

    private <T> ArrayList<T> reverse(ArrayList<T> list) {
        int length = list.size();
        ArrayList<T> result = new ArrayList<T>(length);

        for (int i = length - 1; i >= 0; i--) {
            result.add(list.get(i));
        }

        return result;
    }


    /*
    Deleted code:
        Reason: There is no need to evaluate the expressions to see if the types matches, because it's not relevant!
        ----------------------------------------------------------------------------
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
        -----------------------------------------------------------------------------
        Reason: Javassist solves the name resolution
        -----------------------------------------------------------------------------
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
        ------------------------------------------------------------------------------
        Reason: prints
        ------------------------------------------------------------------------------
        for (String key : classFields.keySet()) {
            System.out.println("Name: " + key + " Value: " + classFields.get(key));
        }

        System.out.println("KEYWORDARGS:");
        for (String key : keywordArgs.keySet()) {
            System.out.println("Arg: " + key + " Value: " + keywordArgs.get(key));
        }

        System.out.println("DEPENDENCIES SOLVED:");
        for (String key : keywordArgs.keySet()){
            System.out.println("Arg: " + key + " Value: " + keywordArgs.get(key));
        }
        ------------------------------------------------------------------------------
     */

}
