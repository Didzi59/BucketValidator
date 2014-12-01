package data;

public class Count {
	private int interBucket;
	private int intraBucket;
	private int frequency;
	
	public Count(){
		this.interBucket = 0;
		this.intraBucket = 0;
		this.frequency = 0;
	}
	
	/**
	 * Increments the inter-bucket value
	 */
	public void incrementInterBucket(){
		this.interBucket++;
	}
	
	/**
	 * Increments the intra-bucket value
	 */
	public void incrementIntraBucket(){
		this.intraBucket++;
	}
	
	/**
	 * Increments the frequency
	 */
	public void incrementFrequency(){
		this.frequency++;
	}

	/**
	 * Returns the inter-bucket value
	 * @return the inter-bucket value
	 */
	public int getInterBucket() {
		return interBucket;
	}

	/**
	 * Returns the intra-bucket value
	 * @return the intra-bucket value
	 */
	public int getIntraBucket() {
		return intraBucket;
	}

	/**
	 * Returns the frequency
	 * @return the frequency
	 */
	public int getFrequency() {
		return frequency;
	}
	
}
