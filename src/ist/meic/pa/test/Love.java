package ist.meic.pa.test;

import ist.meic.pa.KeywordArgs;

public class Love {
	int heartBpm;
	String personName;

	@KeywordArgs("heartBpm=100")
	public Love(Object... args) {}

	public String toString() {
		return super.toString() + String.format("heartBpm:%s, personName:%s",
				heartBpm, personName);
	}
}