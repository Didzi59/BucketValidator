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
	
	/**
	 * Adds a function to the stack trace
	 * @param call the function name
	 */
	public void addCall(String call) {
		this.functionCalls.add(call);
	}
	
	/**
	 * Returns all the function of this stack trace
	 * @return a list of the function names
	 */
	public LinkedList<String> getCalls() {
		return functionCalls;
	}
	
	/**
	 * Returns the bucket wrapping that stack trace
	 * @return a bucket
	 */
	public Bucket getOriginalBucket() {
		return originalBucket;
	}

	/**
	 * Set the original bucket wrapping this stack trace
	 * @param bucket the bucket
	 */
	protected void setOriginalBucket(Bucket bucket) {
		this.originalBucket = bucket;
	}
	
	/**
	 * Returns the id of the stack
	 * @return the stack id
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Returns all the function calls
	 * @return the list of the function calls
	 */
	public LinkedList<String> getFunctionCalls() {
		return functionCalls;
	}

	/**
	 * Sets the functions names
	 * @param functionCalls all the functions names
	 */
	public void setFunctionCalls(LinkedList<String> functionCalls) {
		this.functionCalls = functionCalls;
	}
		
}
