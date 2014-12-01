package data;

import java.util.LinkedList;
import java.util.List;

public class Bucket {
	private String id;
	private List<StackTrace> traces;
		
	public Bucket(String id) {
		this.id = id;
		this.traces = new LinkedList<StackTrace>();
	}
	
	/**
	 * Returns the number of stacks traces
	 * @return the size of the bucket
	 */
	public int getBucketSize() {
		return this.traces.size();
	}

	/**
	 * Returns the id of the bucket
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns all the stacks the bucket contains
	 * @return the list of the stacks
	 */
	public List<StackTrace> getTraces() {
		return traces;
	}
	
	/**
	 * Adds a stack trace to the bucket
	 * @param st the stack trace
	 */
	public void addStackTrace(StackTrace st) {
		this.traces.add(st);
		//st.setOriginalBucket(this);
	}
	
	/**
	 * Tells if two objects are equals
	 * @return true if and only if the two objects are equals
	 */
	public boolean equals(Object o) {
		if (o instanceof Bucket) {
			Bucket b = (Bucket) o;
			return this.id == b.id;
		}
		return false;
	}
	
	/** Returns the corresponding hash code
	 * @return the corresponding hash code
	 */
	public int hashCode() {
		return super.hashCode();
	}
}
