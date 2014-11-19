package data;

import java.util.LinkedList;

public class StackTrace {
	private LinkedList<String> functionCalls;
	private String bucket;
		
	public StackTrace(String bucket) {
		super();
		this.bucket = bucket;
		this.functionCalls = new LinkedList<String>();
	}
	
	public void addCall(String call) {
		this.functionCalls.add(call);
	}
	
	public LinkedList<String> getCalls() {
		return functionCalls;
	}
	
	public String getBucket() {
		return bucket;
	}

	
}
