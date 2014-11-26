package data;

import java.util.LinkedList;

public class StackTrace {
	private LinkedList<String> functionCalls;
	private Bucket originalBucket;
	private String id;
	
	public StackTrace(String id) {
		super();
		this.id = id;
		this.functionCalls = new LinkedList<String>();
	}
	
	public void addCall(String call) {
		this.functionCalls.add(call);
	}
	
	public LinkedList<String> getCalls() {
		return functionCalls;
	}
	
	public Bucket getOriginalBucket() {
		return originalBucket;
	}

	protected void setOriginalBucket(Bucket bucket) {
		this.originalBucket = bucket;
	}
	
	public String getId() {
		return this.id;
	}
	
	public LinkedList<String> getFunctionCalls() {
		return functionCalls;
	}

	public void setFunctionCalls(LinkedList<String> functionCalls) {
		this.functionCalls = functionCalls;
	}

	public void print() {
		System.out.println("*********** Stacktrace "+this.id+" *********** => "+this.originalBucket.getId());
//		for (String f : this.functionCalls) {
//			System.out.println(f);
//		}
	}
		
}
