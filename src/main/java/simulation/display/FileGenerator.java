package simulation.display;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.util.Locale;

import matcher.EditDistanceStrategy;
import matcher.LCSStrategy;
import matcher.PrefixMatchStrategy;
import data.Bucket;
import data.Dataset;
import data.GroundTruth;

public class FileGenerator {
	
	private String destination;
	private GroundTruth gt;
	
	public FileGenerator(String destination, GroundTruth gt){
		this.gt = gt;
		this.destination = destination;
		this.initFile();
	}
	
	private void initFile(){
		
		this.destination = this.destination + "/results";
		
		File results = new File(this.destination);
		if (!results.exists()){
			results.mkdir();
		} 
	}
	
	/**
	 * Creates a CSV file that contains the inter/intra values of all the buckets and a text file that contains the (probably) faulty function names 
	 * @param rate
	 * @throws IOException 
	 */
	public void valuesIntraInterBucket(float rate) throws IOException{
		
		NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
		nf.setMaximumFractionDigits(5);
		
		File fff = new File(this.destination+"/FaultyFunctions.txt");
		FileOutputStream fffos = new FileOutputStream(fff);
		BufferedWriter ffbw = new BufferedWriter(new OutputStreamWriter(fffos));
		
		File iivdf = new File(this.destination+"/InterIntraValues.csv");
		FileOutputStream iivfos = new FileOutputStream(iivdf);
		BufferedWriter iivbw = new BufferedWriter(new OutputStreamWriter(iivfos));
		iivbw.write("Function;IntraBucket;InterBucket;Frequency");
		iivbw.newLine();
		
		for (String function : this.gt.getCounters().keySet()) {
			float frequency = this.gt.getCounters().get(function).getFrequency();
			float falsePositive = this.gt.getInterBucket(function);
			float falseNegative = this.gt.getIntraBucket(function);
			
			iivbw.write("\""+function+"\"" +";"+nf.format(falseNegative)+";"+nf.format(falsePositive)+";"+nf.format(frequency));
			iivbw.newLine();
			if (this.gt.isProbableFaultyFunction(function, rate)) {
				ffbw.write(function);
				ffbw.newLine();
			}
		}
		iivbw.close();
		ffbw.close();
	}
	
	/**
	 * Creates a CSV file that contains the average recall and precision for each algorithm
	 * @param step
	 * @throws IOException
	 */
	public void averageGraphs(float step) throws IOException {
		
		Dataset datasetEditDistance;
		Dataset datasetPrefixMatch;
		Dataset datasetLCS;
		NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
		nf.setMaximumFractionDigits(5);
		
		//For each matching algorithm, a graph that depicts its precision and its recall is created
		String st = String.format(Locale.FRENCH, "%f", step);
		File af = new File(this.destination+"/average_step_"+st+".csv");
		FileOutputStream afos = new FileOutputStream(af);
		BufferedWriter abw = new BufferedWriter(new OutputStreamWriter(afos));
		
		abw.write("Step;RecallED;PrecisionED;RecallLCS;PrecisionLCS;RecallPM;PrecisionPM");
		abw.newLine();
		
		for (float rate = step; rate < 1f; rate += step) {
			datasetEditDistance = new Dataset(new EditDistanceStrategy(), rate,this.gt);
			datasetLCS = new Dataset(new LCSStrategy(), rate,this.gt);
			datasetPrefixMatch = new Dataset(new PrefixMatchStrategy(), rate,this.gt);
			abw.write(
					String.format("%.2f", rate)+";"
					+nf.format(datasetEditDistance.getAverageRecall())+";"
					+nf.format(datasetEditDistance.getAveragePrecision())+";"
					+nf.format(datasetLCS.getAverageRecall())+";"
					+nf.format(datasetLCS.getAveragePrecision())+";"
					+nf.format(datasetPrefixMatch.getAverageRecall())+";"
					+nf.format(datasetPrefixMatch.getAveragePrecision())+";"
					);
			abw.newLine();
		}
		abw.close();
	}
	
	/**
	 * Creates 3 CSV files (one per algorithms) that depicts the bucket scatter
	 * @throws IOException
	 */
	public void bucketScatter() throws IOException{
		Dataset datasetEditDistance;
		Dataset datasetPrefixMatch;
		Dataset datasetLCS;
		NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
		nf.setMaximumFractionDigits(5);
		
		File bsedf = new File(this.destination+"/bucketScatterEditDistance.csv");
		File bslcsf = new File(this.destination+"/bucketScatterLCS.csv");
		File bspmf = new File(this.destination+"/bucketScatterPrefixMatch.csv");
		FileOutputStream bsedfos = new FileOutputStream(bsedf);
		FileOutputStream bslcsfos = new FileOutputStream(bslcsf);
		FileOutputStream bspmfos = new FileOutputStream(bspmf);
		BufferedWriter bsedw = new BufferedWriter(new OutputStreamWriter(bsedfos));
		BufferedWriter bslcsw = new BufferedWriter(new OutputStreamWriter(bslcsfos));
		BufferedWriter bspmw = new BufferedWriter(new OutputStreamWriter(bspmfos));
		
		bsedw.write("Bucket;Recall;Precision;");
		bslcsw.write("Bucket;Recall;Precision;");
		bspmw.write("Bucket;Recall;Precision;");
		bsedw.newLine();
		bslcsw.newLine();
		bspmw.newLine();
		
		for(float rate = 0.25f; rate < 1; rate += 0.25f){
			datasetEditDistance = new Dataset(new EditDistanceStrategy(), rate,this.gt);
			datasetLCS = new Dataset(new LCSStrategy(), rate,this.gt);
			datasetPrefixMatch = new Dataset(new PrefixMatchStrategy(), rate,this.gt);
			
			bsedw.write(";;;");
			bsedw.newLine();
			bsedw.write(String.format("%.2f", rate)+";;;");
			bsedw.newLine();
			bsedw.write(";;;");
			bsedw.newLine();
			
			bslcsw.write(";;;");
			bslcsw.newLine();
			bslcsw.write(String.format("%.2f", rate)+";;;");
			bslcsw.newLine();
			bslcsw.write(";;;");
			bslcsw.newLine();
			
			bspmw.write(";;;");
			bspmw.newLine();
			bspmw.write(String.format("%.2f", rate)+";;;");
			bspmw.newLine();
			bspmw.write(";;;");
			bspmw.newLine();
			
			for (Bucket b : datasetEditDistance.getNewBucketing()) {
				float precision = datasetEditDistance.getPrecision(b);
				float recall = datasetEditDistance.getRecall(b);
				String bucketId = b.getId();
				
				bsedw.write(
						bucketId+";"
						+recall+";"
						+precision+";"
						);
				bsedw.newLine();
			}
			for (Bucket b : datasetLCS.getNewBucketing()) {
				float precision = datasetLCS.getPrecision(b);
				float recall = datasetLCS.getRecall(b);
				String bucketId = b.getId();
				
				bslcsw.write(
						bucketId+";"
						+recall+";"
						+precision+";"
						);
				bslcsw.newLine();
			}
			for (Bucket b : datasetPrefixMatch.getNewBucketing()) {
				float precision = datasetPrefixMatch.getPrecision(b);
				float recall = datasetPrefixMatch.getRecall(b);
				String bucketId = b.getId();
				
				bspmw.write(
						bucketId+";"
						+recall+";"
						+precision+";"
						);
				bspmw.newLine();
			}
		}
		bsedw.close();
		bslcsw.close();
		bspmw.close();
	}

}
