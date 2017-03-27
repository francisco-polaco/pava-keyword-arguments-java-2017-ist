package ist.meic.pa.test.fofinhos;

import ist.meic.pa.KeywordArgs;

/**
 * Created by francisco on 27/03/2017.
 */
public class Cao extends Animal{
    int pelo;

    @KeywordArgs("patas=2,pelo=2")
    public Cao(Object... args) {}

    @Override
    public String toString() {
        return "Cao{" + super.toString() +
                "pelo=" + pelo +
                '}';
    }
}
