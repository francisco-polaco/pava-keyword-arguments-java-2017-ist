package ist.meic.pa.test;

import ist.meic.pa.KeywordArgs;

public class GroupSuperClass {

	public String ola;

	private String nameOfField;
	private String nameOfSuperClassField;

	@KeywordArgs("")
	public GroupSuperClass(Object... args) {}

	public GroupSuperClass(String xD){
		ola=xD;
		imprime();
	}
	
//	public String toString() {
//		return String.format("Nothing...");
//	}

	public void imprime(){
		System.out.println(ola);
	}
}
