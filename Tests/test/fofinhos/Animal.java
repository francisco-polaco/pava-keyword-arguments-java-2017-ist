package ist.meic.pa.test.fofinhos;

import ist.meic.pa.KeywordArgs;

/**
 * Created by francisco on 27/03/2017.
 */
public class Animal {
    int patas;

    @KeywordArgs("patas=1")
    public Animal(Object... args) {}

    @Override
    public String toString() {
        return "Animal{" +
                "patas=" + patas +
                '}';
    }
}
