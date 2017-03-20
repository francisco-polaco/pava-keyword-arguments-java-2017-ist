package ist.meic.pa;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
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

    private ArrayList<CtField> getClassFields() throws NotFoundException {
        ArrayList<CtField> output = new ArrayList<>();
        CtClass auxclass = targetClass;
        do {
            Collections.addAll(output, auxclass.getDeclaredFields());
            auxclass = auxclass.getSuperclass();
        } while(auxclass != null && auxclass.getSuperclass()!= null &&!auxclass.getSuperclass().equals(auxclass));
        return output;
    }


    public void makeTemplate(String args) throws NotFoundException {
        System.out.println(args);

        TreeMap<String, String> keysmap = new TreeMap<>();
        if(!args.equals("")) {
            String[] keywords = args.trim().split(",");
            for (String keyword : keywords) {
                String[] result = keyword.split("=");
                if (result.length == 2)
                    keysmap.put(result[0], result[1]);
                else
                    throw new BadKeyWordsException("Keyword " + keyword + " has wrong format");

            }
        }
        ArrayList<CtField> classFields = getClassFields();
        if(!classFields.containsAll(keysmap.entrySet()))
            throw new RuntimeException("Invalid heywords arguments!");


        for (Map.Entry<String, String> entry :
                keysmap.entrySet()) {

            System.out.println("Entry: " + entry.getKey() + ", Value: " + entry.getValue());
        }








    }

}
