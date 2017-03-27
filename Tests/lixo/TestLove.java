package ist.meic.pa.test.lixo;

public class TestLove {
	public static void main(String[] args) {
		System.err.println(new Love());
		System.err.println(new Love("heartBpm", 200));
		System.err.println(new Love("personName", "DM")); // This should not be allowed
	}
}
