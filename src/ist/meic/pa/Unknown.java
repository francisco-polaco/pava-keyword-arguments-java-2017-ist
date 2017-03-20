package ist.meic.pa;


public class Unknown {
	private String high;
	private String batata;




	@KeywordArgs("high=600,weight=100")
	public Unknown(Object... args) {}
	
	public String toString() {
		return String.format("Nothing...");
	}
}
