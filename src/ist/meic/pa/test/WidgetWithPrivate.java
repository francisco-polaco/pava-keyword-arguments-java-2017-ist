package ist.meic.pa.test;

import ist.meic.pa.KeywordArgs;

public class WidgetWithPrivate {
	int width;

	@KeywordArgs("width=100")
	public WidgetWithPrivate(Object... args) {}

	public String toString() {
		return String.format("width:%s",
				width);
	}
}