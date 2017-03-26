package ist.meic.pa.test;

/*
Espera de email do prof?
a:3,b:40,c:0
a:3,b:3,c:0
a:3,b:3,c:30
a:3,b:0
a:20,b:0
a:3,b:40
 */

public class TestNotDeclaredKeyword {
	public static void main(String[] args) {
		System.err.println(new WidgetNotDeclaredInheritence("b",40));
		System.err.println(new WidgetNotDeclaredInheritence());
		try {
			System.err.println(new WidgetNotDeclaredInheritence("c",30));
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		System.err.println(new WidgetNotDeclaredKeywordArg());
		System.err.println(new WidgetNotDeclaredKeywordArg("a",20));
		try {
			System.err.println(new WidgetNotDeclaredKeywordArg("b", 40));
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
}
