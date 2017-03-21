package ist.meic.pa;

/**
 * Created by francisco on 21/03/2017.
 */
public class FloatParser implements TypeParser {
    @Override
    public void parse(String type) {
        if(!type.matches("[0-9]+.?[0-9]*f?")) {
            Object o = new ExpressionParser().parse(type);
            if(!o.getClass().getSimpleName().equals("Float"))
                throw new RuntimeException("Invalid type match!");
        }
    }
}
