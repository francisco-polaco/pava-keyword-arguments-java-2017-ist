package ist.meic.pa.test;

import ist.meic.pa.KeywordArgs;

public class WidgetNotDeclaredInheritence extends WidgetNotDeclaredKeywordArg{
	int c;

	@KeywordArgs("b=a")
	public WidgetNotDeclaredInheritence(Object... args) {}

	public String toString() {
		return super.toString() + String.format(",c:%s",
				 c);
	}
}