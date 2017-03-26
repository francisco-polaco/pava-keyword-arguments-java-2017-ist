package ist.meic.pa.test;

import ist.meic.pa.KeywordArgs;

public class GroupBaseClass extends GroupSuperClass {
	private int nameOfField;
	private int xD;

	@KeywordArgs("")
	public GroupBaseClass(Object... args) {}
	
//	public String toString() {
//		return String.format("Nothing...");
//	}

	public GroupBaseClass(String xD){
		ola=xD;
		imprime();
	}
}
