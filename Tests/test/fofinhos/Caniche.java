package ist.meic.pa.test.fofinhos;

import ist.meic.pa.KeywordArgs;

/**
 * Created by francisco on 27/03/2017.
 */
public class Caniche extends Cao{
    boolean irrequieto;

    @KeywordArgs("patas=3,pelo=3,irrequieto=true")
    public Caniche(Object... args) {}

    @Override
    public String toString() {
        return "Caniche{" + super.toString() +
                "irrequieto=" + irrequieto +
                '}';
    }
}
