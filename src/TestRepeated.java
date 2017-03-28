import org.omg.CORBA.CODESET_INCOMPATIBLE;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;

public class TestRepeated {
	public static void main(String[] args) throws ClassNotFoundException {

		System.err.println(new RepeatedKeys());
		System.out.println("=====");
		Class c = Class.forName("RepeatedKeys");
		Constructor[] constructors = c.getConstructors();

		for (Constructor constructor : constructors){
			for (Annotation a : constructor.getAnnotations()){
				System.out.println(a.toString());
			}
		}

	}
}
