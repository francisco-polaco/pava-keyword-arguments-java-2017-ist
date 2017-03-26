package ist.meic.pa.test;

import ist.meic.pa.KeywordArgs;

public class WidgetNotDeclaredKeywordArg {
	int a;
	int b;

	@KeywordArgs("a=3")
	public WidgetNotDeclaredKeywordArg(Object... args) {}

	public String toString() {
		return String.format("a:%s,b:%s",
				a, b);
	}
}