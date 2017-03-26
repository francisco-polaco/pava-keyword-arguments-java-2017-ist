package ist.meic.pa.test;

public class PlainSimpleWidget {
	int width;
	int height;
	int margin;

	public PlainSimpleWidget(Object... args) {}

	public String toString() {
		return String.format("width:%s,height:%s,margin:%s",
				width, height, margin);
	}
}