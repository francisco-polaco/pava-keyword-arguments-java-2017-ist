package ist.meic.pa.test;

import ist.meic.pa.KeywordArgs;
import ist.meic.pa.test.WidgetWithPrivate;

public class SuperWithPrivate extends WidgetWithPrivate {
	int height;

	@KeywordArgs("height=200")
	public SuperWithPrivate(Object... args) {}

	public String toString() {
		return super.toString() + String.format("height:%s",
				height);
	}
}