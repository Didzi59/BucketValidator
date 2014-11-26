package data;

public class Count {
	private int falsePositive;
	private int falseNegative;
	private int frequency;
	
	public Count(){
		this.falsePositive = 0;
		this.falseNegative = 0;
		this.frequency = 0;
	}
	
	public void incrementFalsePositive(){
		this.falsePositive++;
	}
	
	public void incrementFalseNegative(){
		this.falseNegative++;
	}
	
	public void incrementFrequency(){
		this.frequency++;
	}

	public int getFalsePositive() {
		return falsePositive;
	}

	public int getFalseNegative() {
		return falseNegative;
	}

	public int getFrequency() {
		return frequency;
	}
	
}
