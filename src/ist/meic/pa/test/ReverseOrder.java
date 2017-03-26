package ist.meic.pa.test;

import ist.meic.pa.KeywordArgs;

public class ReverseOrder {
	int width;
	int height;
	int margin;

	@KeywordArgs("height=margin,width=height,margin=5")
	public ReverseOrder(Object... args) {}

	public String toString() {
		return String.format("width:%s,height:%s,margin:%s",
				width, height, margin);
	}
}