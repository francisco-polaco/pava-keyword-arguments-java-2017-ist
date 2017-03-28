package ist.meic.pa;

import javassist.*;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Pattern;

class TemplateMaker {

    private CtConstructor ctConstructor;
    private CtClass targetClass;
    private ArrayList<String> emptyKeywords;
    private ArrayList<String> keywordsInOrder;

    TemplateMaker(CtConstructor ctConstructor, CtClass targetClass) {
        this.ctConstructor = ctConstructor;
        this.targetClass = targetClass;
        emptyKeywords = new ArrayList<>();
        keywordsInOrder = new ArrayList<>();
    }

    void makeTemplate(ArrayList<String> args) throws NotFoundException, CannotCompileException {
        validateKeywordArgs(args);
        TreeMap<String, String> keywordArgs = prepareKeywordArgs(args);
        TreeMap<String, CtField> classFields = getClassFields(keywordArgs);

        ArrayList<String> allKeywordArgs = new ArrayList<>();
        allKeywordArgs.addAll(keywordArgs.keySet());
        allKeywordArgs.addAll(emptyKeywords);

        if (classFields.size() == 0 && allKeywordArgs.size() != 0)
            throw new RuntimeException("There is no fields in class " +
                    targetClass.getSimpleName() + ", but there are keyword arguments for it.");

        // checks if every keyword argument exists in the class
        for (String keyword : allKeywordArgs) {
            if (!allKeywordArgs.contains(keyword)) {
                throw new RuntimeException("Unrecognized keyword @" +
                        targetClass.getSimpleName() + ": " + keyword);
            }
        }

        solveDependencies(keywordArgs);
        new ConstructorAdapter(keywordArgs, targetClass, ctConstructor).adaptConstructor();
    }

    private void validateKeywordArgs(ArrayList<String> args) throws RuntimeException {
        for (String keywordArg : args) {
            String pattern = "((_?[a-zA-z]+[0-9]*=.*,?)*)|((_?[a-zA-z]+[0-9]*,?)*)|\\s*";
            if (!Pattern.matches(pattern, keywordArg))
                throw new RuntimeException("KeywordArg @" +
                        targetClass.getSimpleName() + ": " + keywordArg + " has wrong format!");
        }
    }

    private TreeMap<String, String> prepareKeywordArgs(ArrayList<String> argsArray) {
        TreeMap<String, String> keywordArgs = new TreeMap<>();
        if (argsArray.size() != 0) {
            for (String args : reverse(argsArray)) {
                String[] keywords = args.split(",");
                for (String keyword : checkMethodCall(keywords)) {
                    parseKeyword(keywordArgs, keyword);
                }
            }
        }
        return keywordArgs;
    }

    private ArrayList<String> checkMethodCall(String[] keywords) {
        ArrayList<String> res = new ArrayList<>();
        String build = "";
        int open = 0;
        for (String keyword : keywords) {
            if (keyword.contains("(") && keyword.contains(")")) {
                if(countOf(keyword, "(") == countOf(keyword, ")"))
                    res.add(keyword);
                else{
                    open += countOf(keyword, "(");
                    open -= countOf(keyword, ")");
                    build += keyword;
                }

            } else if (keyword.contains("(")) {
                open+= countOf(keyword, "(");
                build += keyword;
            } else if (keyword.contains(")")) {
                if (open <= 0) throw new RuntimeException("Parenthesis don't match.");
                open -= countOf(keyword, ")") ;
                build += keyword;
            }
            if (open == 0) {
                res.add(build.equals("") ? keyword : build);
                build = "";
            } else build += ",";
        }
        return res;
    }

    private int countOf(String keyword, String c) {
        return keyword.length() - keyword.replace(c, "").length();
    }

    private void parseKeyword(TreeMap<String, String> keywordArgs, String keyword) {
        if (keyword.equals("")) return;
        if (!keyword.contains("=")) {
            emptyKeywords.add(keyword);
            return;
        }
        String[] nameAndValue = keyword.split("=");
        if (nameAndValue.length == 2) {
            // #extension
            keywordArgs.put(nameAndValue[0], nameAndValue[1]);
            keywordsInOrder.add(nameAndValue[0]);
        } else
            throw new RuntimeException("KeywordArg @" +
                    targetClass.getSimpleName() + ": " + keyword + " has wrong format!");
    }

    private TreeMap<String, CtField> getClassFields(TreeMap<String, String> args) throws NotFoundException {
        TreeMap<String, CtField> classFields = new TreeMap<>();
        CtClass auxClass = targetClass;
        while (auxClass.getSuperclass() != null) {
            CtField[] fields = auxClass.getDeclaredFields();
            for (CtField field : fields) {
                if (field.getModifiers() != Modifier.PRIVATE) {
                    classFields.put(field.getName(), field);
                }
                /* Avoid the case where private field annotations get
                 * into the base classes due to recursively adding annotations */
                else {
                    if (targetClass != auxClass)
                        args.remove(field.getName());
                }
            }
            auxClass = auxClass.getSuperclass();
        }

        for (CtField f : targetClass.getDeclaredFields()) {
            classFields.put(f.getName(), f);
        }

        return classFields;
    }

    private void solveDependencies(TreeMap<String, String> keywordArgs) {
        ArrayList<String> entriesChecked = new ArrayList<>();
        for (String entry : keywordsInOrder) {
            if (entriesChecked.contains(keywordArgs.get(entry))) {
                // Checking from left to right
                keywordArgs.put(entry, keywordArgs.get(keywordArgs.get(entry)));
            }
            entriesChecked.add(entry);
        }

        for (String key : keywordArgs.keySet()) {
            if (keywordArgs.keySet().contains(keywordArgs.get(key))) {
                throw new RuntimeException("Impossible to solve variables dependencies.");
            }
        }
    }

    private <T> ArrayList<T> reverse(ArrayList<T> list) {
        int length = list.size();
        ArrayList<T> result = new ArrayList<>(length);

        for (int i = length - 1; i >= 0; i--) {
            result.add(list.get(i));
        }

        return result;
    }
}
