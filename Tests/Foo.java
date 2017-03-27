import ist.meic.pa.KeywordArgs;

class Foo {
	int a;
	int b;

	@KeywordArgs("a=b,b=1")
	public Foo(Object... args) {}

	public String toString() {
		return String.format("a:%s,b:%s",
				a, b);
	}
}