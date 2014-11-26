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
	
	public int getBucketSize() {
		return this.traces.size();
	}

	public String getId() {
		return id;
	}

	public List<StackTrace> getTraces() {
		return traces;
	}
	
	public void addStackTrace(StackTrace st) {
		this.traces.add(st);
		//st.setOriginalBucket(this);
	}
	
	public boolean equals(Object o) {
		if (o instanceof Bucket) {
			Bucket b = (Bucket) o;
			return this.id == b.id;
		}
		return false;
	}
	
	public int hashCode() {
		return super.hashCode();
	}
	
	public void print() {
		System.out.println("*********** Bucket "+this.id+" ***********");
		for (StackTrace t : this.traces) {
			t.print();
		}
	}
}
