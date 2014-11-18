package data;

import java.util.LinkedList;

public class StackTrace {
	private LinkedList<String> calls;
	private int bucket;
		
	public StackTrace(int bucket) {
		super();
		this.bucket = bucket;
		this.calls = new LinkedList<String>();
	}
	
	public void addCall(String call) {
		this.calls.add(call);
	}
	
	public LinkedList<String> getCalls() {
		return calls;
	}
	
	public int getBucket() {
		return bucket;
	}
	
}
