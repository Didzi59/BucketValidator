package data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import matcher.MatchingStrategy;

public class Dataset {
	
	private List<StackTrace> original;
	private List<Bucket> newBucketing;
	
	private GroundTruth gt;
	
	public Dataset(MatchingStrategy strategy, float matchingRate, GroundTruth gt) {
		this.gt = gt;
		this.original = gt.getAllStackTraces();
		this.newBucketing = strategy.executeMatching(this.original, matchingRate);
	}
	
	/**
	 * Returns the precision of a bucket
	 * @param b the bucket
	 * @return the precision
	 */
	public float getPrecision(Bucket b) {
		float res = (float) getMaxDuplicateNumber(b).getMax() / b.getBucketSize();
		return res;
	}
	
	/**
	 * Returns the recall of a bucket
	 * @param b the bucket
	 * @return the recall
	 */
	public float getRecall(Bucket b){
		int D = this.gt.getBucketById(getMaxDuplicateNumber(b).getId()).getBucketSize();
		float res = (float) getMaxDuplicateNumber(b).getMax() / D;
		return res;
	}
	
	/**
	 * Returns the average precision of all the buckets
	 * @return the average precision of all the buckets
	 */
	public float getAveragePrecision() {
		float avg = 0f;
		for (Bucket b : this.getNewBucketing()) {
			avg += this.getPrecision(b);
		}
		avg = avg / this.getNewBucketing().size();
		return avg;
	}
	
	/**
	 * Returns the average recall of all the buckets
	 * @return the average recall of all the buckets
	 */
	public float getAverageRecall() {
		float avg = 0f;
		for (Bucket b : this.getNewBucketing()) {
			avg += this.getRecall(b);
		}
		avg = avg / this.getNewBucketing().size();
		return avg;
	}
	
	/**
	 * Returns the maximum duplicate number of a given bucket
	 * @param b the bucket
	 * @return the duplicate number
	 */
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

	/**
	 * Returns all the buckets newly created
	 * @return the buckets
	 */
	public List<Bucket> getNewBucketing() {
		return newBucketing;
	}

	/**
	 * Sets the new bucketing
	 * @param newBucketing the new bucketing
	 */
	public void setNewBucketing(List<Bucket> newBucketing) {
		this.newBucketing = newBucketing;
	}

}
