package ist.meic.pa.test.fofinhos;

/**
 * Created by francisco on 27/03/2017.
 */
public class TestFofo {
    public static void main(String[] args) {
        System.err.println(new Animal());
        System.err.println(new Cao());
        System.err.println(new Caniche());
        System.err.println(new Caniche("patas", 50));
        System.err.println(new Caniche("patas", 50, "pelo", 30));
        System.err.println(new Caniche("patas", 50, "pelo", 30, "irrequieto", false));
    }
}
