import ist.meic.pa.KeywordArgs;

public class RepeatedKeys {

    int a;

    @KeywordArgs("a=1,a=2,a=3")
    public RepeatedKeys(Object... args) {}

    @Override
    public String toString() {
        return "RepeatedKeys{" +
                "a=" + a +
                '}';
    }
}
