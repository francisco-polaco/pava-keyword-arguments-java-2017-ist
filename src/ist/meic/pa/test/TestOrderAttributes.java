package ist.meic.pa.test;

/*
Defaults são sempre resolvidos antes!
width:5,height:5,margin:5
width:5,height:10,margin:5
*/

public class TestOrderAttributes {
	public static void main(String[] args) {
		System.err.println(new ReverseOrder());
		System.err.println(new ReverseOrder("height",10));
	}
}
