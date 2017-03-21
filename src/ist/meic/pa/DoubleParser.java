package ist.meic.pa;

/**
 * Created by francisco on 21/03/2017.
 */
public class DoubleParser implements TypeParser {
    @Override
    public void parse(String type) {
        if(!type.matches("[0-9]+.?[0-9]*")) {
            Object o = new ExpressionParser().parse(type, s);
            if(!o.getClass().getSimpleName().equals("Double"))
                throw new RuntimeException("Invalid type match!");
        }
    }
}
