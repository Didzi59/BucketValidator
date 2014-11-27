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

public class GraphCreater {
	
	public static void valuesIntraInterBucket(float rate) throws IOException{
		
		GroundTruth data = GroundTruth.GT;
		
		NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
		nf.setMaximumFractionDigits(5);
		
		File fff = new File("results/FaultyFunctions.txt");
		FileOutputStream fffos = new FileOutputStream(fff);
		BufferedWriter ffbw = new BufferedWriter(new OutputStreamWriter(fffos));
		
		File iivdf = new File("results/InterIntraValues.csv");
		FileOutputStream iivfos = new FileOutputStream(iivdf);
		BufferedWriter iivbw = new BufferedWriter(new OutputStreamWriter(iivfos));
		iivbw.write("Function;IntraBucket;InterBucket;Frequency");
		iivbw.newLine();
		
		for (String function : data.getCounters().keySet()) {
			float frequency = data.getCounters().get(function).getFrequency();
			float falsePositive = data.getFalsePositive(function);
			float falseNegative = data.getFalseNegative(function);
			
			iivbw.write("\""+function+"\"" +";"+nf.format(falseNegative)+";"+nf.format(falsePositive)+";"+nf.format(frequency));
			iivbw.newLine();
			if (data.isProbableFaultyFunction(function, rate)) {
				ffbw.write(function);
				ffbw.newLine();
			}
		}
		iivbw.close();
		ffbw.close();
	}
	
	public static void averageGraphs(float step) throws IOException {
		
		Dataset datasetEditDistance;//TODO change to singleton, to avoid doing the same job every time an instance is created
		Dataset datasetPrefixMatch;
		Dataset datasetLCS;
		NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
		nf.setMaximumFractionDigits(5);
		
		//The directory that should contain the graphs
		File results = new File("results");
		
		//If the directory does not exist, a new one is created
		if (!results.exists()) results.mkdir();
		
		//For each matching algorithm, a graph that depicts its precision and its recall is created
		String st = String.format(Locale.FRENCH, "%f", step);
		File af = new File("results/average_step_"+st+".csv");
		FileOutputStream afos = new FileOutputStream(af);
		BufferedWriter abw = new BufferedWriter(new OutputStreamWriter(afos));
		
		abw.write("Step;RecallED;PrecisionED;RecallLCS;PrecisionLCS;RecallPM;PrecisionPM");
		abw.newLine();
		
		for (float rate = step; rate < 1f; rate += step) {
			datasetEditDistance = new Dataset(new EditDistanceStrategy(), rate);
			datasetLCS = new Dataset(new LCSStrategy(), rate);
			datasetPrefixMatch = new Dataset(new PrefixMatchStrategy(), rate);
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
	
	public static void bucketScatter() throws IOException{
		Dataset datasetEditDistance;//TODO change to singleton, to avoid doing the same job every time an instance is created
		Dataset datasetPrefixMatch;
		Dataset datasetLCS;
		NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
		nf.setMaximumFractionDigits(5);
		
		File bsedf = new File("results/bucketScatterEditDistance.csv");
		File bslcsf = new File("results/bucketScatterLCS.csv");
		File bspmf = new File("results/bucketScatterPrefixMatch.csv");
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
			datasetEditDistance = new Dataset(new EditDistanceStrategy(), rate);
			datasetLCS = new Dataset(new LCSStrategy(), rate);
			datasetPrefixMatch = new Dataset(new PrefixMatchStrategy(), rate);
			
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			GraphCreater.averageGraphs(0.01f);
			GraphCreater.averageGraphs(0.05f);
			GraphCreater.valuesIntraInterBucket(.0001f);
			GraphCreater.bucketScatter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Couldn't create file");
		}

	}

}
