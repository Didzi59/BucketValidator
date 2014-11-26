package data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import matcher.EditDistanceStrategy;
import matcher.LCSStrategy;
import matcher.PrefixMatchStrategy;
import matcher.StrategyMatching;

public class Dataset {
	
	private List<StackTrace> original;
	private List<Bucket> newBucketing;
	
	private static final float MATCHING_RATE = 0.25f;
	
	public Dataset(StrategyMatching strategy, float matchingRate) {
		this.original = GroundTruth.GT.getAllStackTraces();
		this.newBucketing = strategy.executeMatching(this.original, matchingRate);
	}
	
	public float getPrecision(Bucket b) {
		float res = (float) getMaxDuplicateNumber(b).getMax() / b.getBucketSize();
		return res;
	}
	
	public float getRecall(Bucket b){
		int D = GroundTruth.GT.getBucketById(getMaxDuplicateNumber(b).getId()).getBucketSize();
		float res = (float) getMaxDuplicateNumber(b).getMax() / D;
		return res;
	}
	
	public float getAveragePrecision() {
		float avg = 0f;
		for (Bucket b : this.newBucketing) {
			avg += this.getPrecision(b);
		}
		avg = avg / this.newBucketing.size();
		return avg;
	}
	
	public float getAverageRecall() {
		float avg = 0f;
		for (Bucket b : this.newBucketing) {
			avg += this.getRecall(b);
		}
		avg = avg / this.newBucketing.size();
		return avg;
	}
	
	private static Couple getMaxDuplicateNumber(Bucket b) {
		int max = 0;
		int tmp;
		String id;
		String id_max = "";
		Map<String, Integer> memory = new HashMap<String, Integer>(); 
		for (StackTrace s : b.getTraces()) {
			tmp = 1;
			id = s.getOriginalBucket().getId();
			if (memory.containsKey(id)) {
				tmp = memory.get(id);
				tmp++;		
			}
			memory.put(id, tmp);
			if (tmp > max) {
				max = tmp;
				id_max = id;
			}
		}
		return new Couple(id_max, max);
	}
	
	private static class Couple {
		String id;
		int max;
		public Couple(String id, int max) {
			this.id = id;
			this.max = max;
		}
		public String getId() {
			return id;
		}
		public int getMax() {
			return max;
		}
	}
	
	public static void main(String args[]) {
		//List<Bucket> newBucketing = new EditDistanceStrategy().executeMatching(GroundTruth.GT.getAllStackTraces(), MATCHING_RATE);
		Dataset data = new Dataset(new EditDistanceStrategy(), MATCHING_RATE);
		Dataset data2 = new Dataset(new PrefixMatchStrategy(), MATCHING_RATE);
		Dataset data3 = new Dataset(new LCSStrategy(), MATCHING_RATE);
		for (Bucket b : data.newBucketing) {
			b.print();
			System.out.println("d = "+getMaxDuplicateNumber(b).getMax());
			System.out.println("ID = "+getMaxDuplicateNumber(b).getId());
			System.out.println("precision = "+data.getPrecision(b));
			System.out.println("recall = "+data.getRecall(b));
		}
		//Bucket b = data.getOrCreateBucket("658");
		//Normalizer.removeRecurrence(b).print();
	}

}
