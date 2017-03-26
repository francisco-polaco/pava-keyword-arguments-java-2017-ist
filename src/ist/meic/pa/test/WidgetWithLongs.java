package ist.meic.pa.test;

import ist.meic.pa.KeywordArgs;

public class WidgetWithLongs {
	long width;
	double height;
	long carater;

	@KeywordArgs("width=10.2,height=13,carater='c'")
	public WidgetWithLongs(Object... args) {}

	public String toString() {
		return String.format("width:%s,height:%s,carater:%s",
				width,height,carater);
	}
}