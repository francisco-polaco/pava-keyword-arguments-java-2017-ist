package ist.meic.pa;

/**
 * Created by francisco on 21/03/2017.
 */
public class StringParser implements TypeParser {
    @Override
    public void parse(String type) {
        if(!type.matches("\".*\"")) {
            Object o = new ExpressionParser().parse(type, "String");
            if(!o.getClass().getSimpleName().equals("String"))
                throw new RuntimeException("Invalid type match!");
        }
    }
}
