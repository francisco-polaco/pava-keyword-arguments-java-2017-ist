package ist.meic.pa;

import javassist.*;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Pattern;

class TemplateMaker {

    private CtConstructor ctConstructor;
    private CtClass targetClass;
    private ArrayList<String> emptyKeywords = new ArrayList<>();
    private ArrayList<String> keywordsInOrder = new ArrayList<>();

    TemplateMaker(CtConstructor ctConstructor, CtClass targetClass) {
        this.ctConstructor = ctConstructor;
        this.targetClass = targetClass;
    }

    void makeTemplate(ArrayList<String> args) throws NotFoundException, ClassNotFoundException, IllegalAccessException, InstantiationException, CannotCompileException {
        validateKeywordArgs(args);
        TreeMap<String, String> keywordArgs = prepareKeywordArgs(args);
        TreeMap<String, CtField> classFields = getClassFields(keywordArgs);

        ArrayList<String> allKeywordArgs = new ArrayList<>();
        allKeywordArgs.addAll(keywordArgs.keySet());
        allKeywordArgs.addAll(emptyKeywords);

        if(classFields.size() == 0 && (allKeywordArgs.size() != 0))
            throw new RuntimeException("There is no fields in class " + targetClass.getSimpleName() + ", but there are keyword arguments for it.");

        // checks if every keyword argument exists in the class
        for (String keyword : allKeywordArgs){
            if(!allKeywordArgs.contains(keyword)){
                throw new RuntimeException("Unrecognized keyword @" + targetClass.getSimpleName() + ": " + keyword);
            }
        }

        // solve the keywordArgs dependencies
        solveDependencies(keywordArgs);
        new ConstructorAdapter(keywordArgs, targetClass, ctConstructor).adaptConstructor();
    }

    private void validateKeywordArgs(ArrayList<String> args) throws RuntimeException{
        for (String keywordArg : args){
            String pattern = "((_?[a-zA-z]+[0-9]*=.*,?)*)|((_?[a-zA-z]+[0-9]*,?)*)|\\s*";
            if(!Pattern.matches(pattern, keywordArg))
                throw new RuntimeException("KeywordArg @" + targetClass.getSimpleName()  + ": " + keywordArg + " has wrong format!");
        }
    }

    private TreeMap<String, String> prepareKeywordArgs(ArrayList<String> argsArray) {
        TreeMap<String, String> keywordArgs = new TreeMap<>();
        if(argsArray.size() != 0) {
            for(String args : reverse(argsArray)) {

                String[] keywords = args.split(",");
                for (String keyword : keywords) {
                    // needed to invalidate invalid keywordArgs which appear without a equal
                    if(keyword.equals("")) continue;
                    if(!keyword.contains("=") ) {
                        emptyKeywords.add(keyword);
                        continue;
                    }
                    String[] result = keyword.split("=");
                    if (result.length == 2) {
                        // #extension
                        keywordArgs.put(result[0], result[1]);
                        keywordsInOrder.add(result[0]);
                    }else
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

        ArrayList<String> entriesChecked = new ArrayList<>();
        for(String entry : keywordsInOrder){
            if(entriesChecked.contains(keywordArgs.get(entry))){
                // Checking from left to right
                keywordArgs.put(entry, keywordArgs.get(keywordArgs.get(entry)));
            }
            entriesChecked.add(entry);
        }

        for(String key : keywordArgs.keySet()){
            if(keywordArgs.keySet().contains(keywordArgs.get(key))){
                throw new RuntimeException("Impossible to solve variables dependencies.");
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
}
