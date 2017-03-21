package ist.meic.pa;

/**
 * Created by francisco on 21/03/2017.
 */
public class CharParser implements TypeParser {
    @Override
    public void parse(String type) {
        if(!type.matches("\'.\'")) {
            Object o = new ExpressionParser().parse(type, s);
            if(!o.getClass().getSimpleName().equals("Char"))
                throw new RuntimeException("Invalid type match!");
        }
    }
}
