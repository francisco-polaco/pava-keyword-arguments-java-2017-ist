package ist.meic.pa;


public class Unknown {
	private int high;
	//private String batata;

	//protected String ola;


	@KeywordArgs("high=3+4")
	public Unknown(Object... args) {}
	
	public String toString() {
		return String.format("Nothing...");
	}
}
