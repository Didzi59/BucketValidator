package data;

import java.util.List;

import matcher.EditDistanceStrategy;
import matcher.LCSStrategy;
import matcher.PrefixMatchStrategy;
import matcher.StrategyMatching;

public class Dataset {
	
	private List<StackTrace> original;
	private List<Bucket> newBucketing;
	
	private static final float MATCHING_RATE = 0.25f;
	
	public Dataset(int threshold, StrategyMatching strategy) {
		this.original = createOriginal(threshold);
		this.newBucketing = strategy.executeMatching(this.original, MATCHING_RATE);
	}
	
	private List<StackTrace> createOriginal(int threshold) {
		return GroundTruth.GT.getAllStackTraces(); //TODO
	}
	
	public static void main(String args[]) {
		//List<Bucket> newBucketing = new EditDistanceStrategy().executeMatching(GroundTruth.GT.getAllStackTraces(), MATCHING_RATE);
		Dataset data = new Dataset(0, new EditDistanceStrategy());
		Dataset data2 = new Dataset(0, new PrefixMatchStrategy());
		Dataset data3 = new Dataset(0, new LCSStrategy());
		for (Bucket b : data.newBucketing) {
			b.print();
		}
		//Bucket b = data.getOrCreateBucket("658");
		//Normalizer.removeRecurrence(b).print();
	}

}
