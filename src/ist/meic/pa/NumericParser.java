package ist.meic.pa;

/**
 * Created by francisco on 21/03/2017.
 */
public abstract class NumericParser implements TypeParser {


    protected void numericParse(String type, String matchType, String primitiveDataType) {
        if(!type.matches("[0-9]+")) {
            Object o = new ExpressionParser().parse(type, primitiveDataType);
            if(!o.getClass().getSimpleName().equals(matchType))
                throw new RuntimeException("Invalid type match!");
        }
    }
}
