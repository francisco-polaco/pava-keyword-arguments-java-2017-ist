package ist.meic.pa;

/**
 * Created by francisco on 21/03/2017.
 */
public class BooleanParser implements TypeParser {
    @Override
    public void parse(String type) {
        if(!type.matches("true | false")) {
            Object o = new ExpressionParser().parse(type, s);
            if(!o.getClass().getSimpleName().equals("Boolean"))
                throw new RuntimeException("Invalid type match!");
        }
    }
}
