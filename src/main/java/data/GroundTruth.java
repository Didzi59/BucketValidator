package data;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import normalizer.Normalizer;
import parser.Parser;

public class GroundTruth {
	
	private List<Bucket> buckets;
	private Map<String,Count> counters;
	private Count max;//all the maximum values (false negative/positive and frequency count)
	//private static final String STACKS_PATH = "src/main/resources/original_stack";
	
	public int before = 0;
	public int after = 0;
	
	public GroundTruth(String path) {
		this.buckets = new LinkedList<Bucket>();
		this.setCounters(new HashMap<String, Count>());
		this.max = new Count();
		File folder = new File(path);//STACKS_PATH
		this.parseFolder(folder);
		this.computeCounters();
	}
	/**
	 * Compute the counters
	 */
	private void computeCounters() {
		List<StackTrace> listAll = new LinkedList<StackTrace>();
		for (Bucket b : this.buckets) {
			listAll.addAll(b.getTraces());
		}
		StackTrace[] tabAll = listAll.toArray(new StackTrace[listAll.size()]);
		for (int i = 0; i < tabAll.length; i++) {
			computeFrequency(tabAll[i]);
			for (int j = i+1; j < tabAll.length; j++) {
				computeFalseCounters(tabAll[i], tabAll[j]);
			}
		}
	}

	/**
	 * Computes the frequency for a stack
	 * @param s the stacktrace
	 */
	private void computeFrequency(StackTrace s) {
		for (String function : s.getFunctionCalls()) {
			// Count already exist
			Count c;
			if (this.getCounters().containsKey(function)) {
				c = this.getCounters().get(function);
				c.incrementFrequency();
			} else {
				c = new Count();
				c.incrementFrequency();
				this.getCounters().put(function, c);
			}
			if (c.getFrequency() > this.max.getFrequency()) this.max.incrementFrequency();
		}
	}

	/**
	 * Compute inter intra bucket for 2 stacks
	 * @param s the first stack
	 * @param t the second stack
	 */
	private void computeFalseCounters(StackTrace s, StackTrace t) {	
		if (s.getOriginalBucket().equals(t.getOriginalBucket())) { 
			// Same bucket
			for (String function : s.getFunctionCalls()) {
				// Count already exist
				Count c;
				if (this.getCounters().containsKey(function)) {
					c = this.getCounters().get(function);
					if (!t.getFunctionCalls().contains(function)) {
						c.incrementIntraBucket();
					}
				} else {
					c = new Count();
					if (!t.getFunctionCalls().contains(function)) {
						c.incrementIntraBucket();
					}
					this.getCounters().put(function, c);
				}
				if (c.getIntraBucket() > this.max.getIntraBucket()) this.max.incrementIntraBucket();				
			}
			for (String function : t.getFunctionCalls()) {
				// Count already exist
				Count c;
				if (this.getCounters().containsKey(function)) {
					c = this.getCounters().get(function);
					if (!s.getFunctionCalls().contains(function)) {
						c.incrementIntraBucket();
					}
				} else {
					c = new Count();
					if (!s.getFunctionCalls().contains(function)) {
						c.incrementIntraBucket();
					}
					this.getCounters().put(function, c);
				}
				if (c.getIntraBucket() > this.max.getIntraBucket()) this.max.incrementIntraBucket();				
			}
		} else { 
			// Distinct bucket
			for (String function : s.getFunctionCalls()) {
				// Count already exist
				Count c;
				if (this.getCounters().containsKey(function)) {
					c = this.getCounters().get(function);
					if (t.getFunctionCalls().contains(function)) {
						c.incrementInterBucket();
					}
				} else {
					c = new Count();
					if (t.getFunctionCalls().contains(function)) {
						c.incrementInterBucket();
					}
					this.getCounters().put(function, c);
				}
				if (c.getInterBucket() > this.max.getInterBucket()) this.max.incrementInterBucket();				
			}
		}
	}

	/**
	 * Parse all the folder and creates buckets and its stacks
	 * @param folder the folder containing the files to be parsed
	 */
	private void parseFolder(File folder) {
		if (folder.isDirectory()) {
			File[] list = folder.listFiles();
			if (list != null){
				for (int i = 0; i < list.length; i++) {
					parseFolder(list[i]);
				} 
			} else {
				System.err.println(folder + " : Reading error.");
			}
		} else {
			Bucket bucket = this.getOrCreateBucket(folder.getParentFile().getName());
			StackTrace stack = Parser.parse(folder);
			this.before += stack.getFunctionCalls().size();
			stack = Normalizer.removeRecurrence(stack);
			this.after += stack.getFunctionCalls().size();
			stack.setOriginalBucket(bucket);
			bucket.addStackTrace(stack);
		}
	}
	
	/**
	 * Prints on the screen how many functions are removed
	 */
	public void printRecursionRemoval(){
		float res = ((float) (this.before - this.after)/this.before) * 100;
		System.out.println("The stacks after the recursion removal are " + res + "% smaller than those before recursion removal");
	}
	
	/**
	 * Creates a bucket or returns a bucket given an id
	 * @param id the id of the bucket
	 * @return the bucket
	 */
	private Bucket getOrCreateBucket(String id) {
		for (Bucket b : this.buckets) {
			if (b.getId().equals(id)) return b;
		}
		Bucket b = new Bucket(id);
		this.buckets.add(b);
		return b;
	}
	
	/**
	 * Gets a bucket by its id
	 * @param id the id of the bucket
	 * @return a bucket
	 * @throws NoSuchElementException
	 */
	public Bucket getBucketById(String id) throws NoSuchElementException {
		for (Bucket b : this.buckets) {
			if (b.getId().equals(id)) return b;
		}
		throw new NoSuchElementException("No bucket with this id: "+id);
	}
	
	/**
	 * Get all the buckets
	 * @return a list of the buckets
	 */
	public List<Bucket> getBuckets() {
		return buckets;
	}
	
	/**
	 * Returns all the call stacks
	 * @return a list of all the call stacks
	 */
	public List<StackTrace> getAllStackTraces() {
		List<StackTrace> res = new LinkedList<StackTrace>();
		for (Bucket b : this.buckets) {
			res.addAll(b.getTraces());
		}
		return res;
	}
	
	/**
	 * 
	 * @param function
	 * @return
	 * @throws NoSuchElementException
	 */
	public float getInterBucket(String function) throws NoSuchElementException {
		float res = 0f;
		if (this.getCounters().containsKey(function)) {
			res = (float) this.getCounters().get(function).getInterBucket() / this.max.getInterBucket();
		} else {
			throw new NoSuchElementException("This function is not found: "+function);
		}
		return res;
	}
	
	/**
	 * Gets the intra bucket corresponding to a function
	 * @param function
	 * @return
	 * @throws NoSuchElementException
	 */
	public float getIntraBucket(String function) throws NoSuchElementException {
		float res = 0f;
		if (this.getCounters().containsKey(function)) {
			res = (float) this.getCounters().get(function).getIntraBucket() / this.max.getIntraBucket();
		} else {
			throw new NoSuchElementException("This function is not found: "+function);
		}
		return res;
	}
	
	/**
	 * Tells if a function is a probably faulty
	 * @param function the function suspected
	 * @param rate the rate
	 * @return true if and only if the function is faulty
	 */
	public boolean isProbableFaultyFunction(String function, float rate) {
		float intraBucketRate = this.getIntraBucket(function);
		float interBucketRate = this.getInterBucket(function);
		return ((intraBucketRate*intraBucketRate + interBucketRate*interBucketRate) < (rate * rate));
	}

	/**
	 * Gets the counters
	 * @return the counters
	 */
	public Map<String,Count> getCounters() {
		return counters;
	}

	/**
	 * Set the counters
	 * @param counters
	 */
	public void setCounters(Map<String,Count> counters) {
		this.counters = counters;
	}
}
