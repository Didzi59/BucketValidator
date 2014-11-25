package data;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import parser.Parser;

public class Dataset {
	
	private static final String STACKS_PATH = "/gfs/bossutj/original_stack";
	private List<Bucket> buckets;
	
	
	public Dataset() {
		this.buckets = new LinkedList<Bucket>();
	}
	
	public Dataset(File folder) {
		this.buckets = new LinkedList<Bucket>();
		this.parseFolder(folder);
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
			//stack.setBucket(bucket);
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
	
	public List<Bucket> getBuckets() {
		return buckets;
	}

	public void print() {
		System.out.println("*********** DATASET ***********");
		for (Bucket b : this.buckets) {
			b.print();
		}
	}
	
	public static void main(String args[]) {
		File repertoire = new File(STACKS_PATH);
		Dataset data = new Dataset(repertoire);
		//data.parseFolder(repertoire);
		//data.print();
		
		Bucket b = data.getOrCreateBucket("658");
		//Normalizer.removeRecurrence(b).print();
	}
}
