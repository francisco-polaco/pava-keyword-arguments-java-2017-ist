import ist.meic.pa.KeywordArgs;

class Bar {
	int a;
	int b;

	@KeywordArgs("a=1,b=a")
	public Bar(Object... args) {}

	public String toString() {
		return String.format("a:%s,b:%s",
				a, b);
	}
}