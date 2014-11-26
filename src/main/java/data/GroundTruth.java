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
	private Count max;
	private static final String STACKS_PATH = "/gfs/bossutj/original_stack"; //TODO: changer chemin
	public static final GroundTruth GT = new GroundTruth();
	
	private GroundTruth() {
		this.buckets = new LinkedList<Bucket>();
		this.counters = new HashMap<String, Count>();
		this.max = new Count();
		File folder = new File(STACKS_PATH);
		this.parseFolder(folder);
		this.computeCounters();
	}
	
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

	private void computeFrequency(StackTrace s) {
		for (String function : s.getFunctionCalls()) {
			// Count already exist
			Count c;
			if (this.counters.containsKey(function)) {
				c = this.counters.get(function);
				c.incrementFrequency();
			} else {
				c = new Count();
				c.incrementFrequency();
				this.counters.put(function, c);
			}
			if (c.getFrequency() > this.max.getFrequency()) this.max.incrementFrequency();
		}
	}

	private void computeFalseCounters(StackTrace s, StackTrace t) {	
		if (s.getOriginalBucket().equals(t.getOriginalBucket())) { // Same bucket
			// First matching function
		} else { // Distinct bucket
			// First non-matching function
		}
		
	}

	public void parseFolder(File folder) {
		if (folder.isDirectory()) {
			File[] list = folder.listFiles();
			if (list != null){
				for (int i = 0; i < list.length; i++) {
					// Appel récursif sur les sous-répertoires
					parseFolder(list[i]);
				} 
			} else {
				System.err.println(folder + " : Reading error.");
			}
		} else {
			//System.out.println("*********************************" +folder.getAbsolutePath());
			Bucket bucket = this.getOrCreateBucket(folder.getParentFile().getName());	
			StackTrace stack = Parser.parse(folder);
			stack = Normalizer.removeRecurrence(stack);
			stack.setOriginalBucket(bucket);
			bucket.addStackTrace(stack);
		}
	}
	
	private Bucket getOrCreateBucket(String id) {
		for (Bucket b : this.buckets) {
			if (b.getId().equals(id)) return b;
		}
		Bucket b = new Bucket(id);
		this.buckets.add(b);
		return b;
	}
	
	public Bucket getBucketById(String id) throws NoSuchElementException {
		for (Bucket b : this.buckets) {
			if (b.getId().equals(id)) return b;
		}
		throw new NoSuchElementException("No bucket with this id: "+id);
	}
	
	public List<Bucket> getBuckets() {
		return buckets;
	}
	
	public List<StackTrace> getAllStackTraces() {
		List<StackTrace> res = new LinkedList<StackTrace>();
		for (Bucket b : this.buckets) {
			res.addAll(b.getTraces());
		}
		return res;
	}

	public void print() {
		System.out.println("*********** DATASET ***********");
		for (Bucket b : this.buckets) {
			b.print();
		}
	}
	
	public static void main(String args[]) {
		GroundTruth data = GroundTruth.GT;
		//data.print();
		for (String s : data.counters.keySet()) {
			System.out.println(s+" => "+data.counters.get(s).getFrequency());
		}
		System.out.println(data.max.getFrequency());
		//Bucket b = data.getOrCreateBucket("658");
		//Normalizer.removeRecurrence(b).print();
	}
}
