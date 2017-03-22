package ist.meic.pa;

/**
 * Created by francisco on 21/03/2017.
 */
public class IntParser extends NumericParser{
    @Override
    public void parse(String type){
        super.numericParse(type, "Integer", "int");
    }
}
