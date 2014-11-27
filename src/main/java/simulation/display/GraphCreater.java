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
import data.Dataset;

public class GraphCreater {

	
	public static void averageGraphs() throws IOException {
		
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
		//Edit Distance
		File edf = new File("results/EditDistanceAverage.csv");
		FileOutputStream edfos = new FileOutputStream(edf);
		BufferedWriter edbw = new BufferedWriter(new OutputStreamWriter(edfos));
		edbw.write("Step;Precision;Recall;");
		edbw.newLine();
		for (float rate = 0.01f; rate < 1f; rate += 0.01f) {
			datasetEditDistance = new Dataset(new EditDistanceStrategy(), rate);
			edbw.write(String.format("%.2f", rate)+";"+nf.format(datasetEditDistance.getAveragePrecision())+";"+nf.format(datasetEditDistance.getAverageRecall())+";");
			edbw.newLine();
		}
		edbw.close();
		//Longest Common Subsequence
		File lcsf = new File("results/LongestCommonSubsequenceAverage.csv");
		FileOutputStream lcsfos = new FileOutputStream(lcsf);
		BufferedWriter lcsbw = new BufferedWriter(new OutputStreamWriter(lcsfos));
		lcsbw.write("Step;Precision;Recall;");
		lcsbw.newLine();
		for (float rate = 0.01f; rate < 1f; rate += 0.01f) {
			datasetLCS = new Dataset(new LCSStrategy(), rate);
			lcsbw.write(String.format("%.2f", rate)+";"+nf.format(datasetLCS.getAveragePrecision())+";"+nf.format(datasetLCS.getAverageRecall())+";");
			lcsbw.newLine();
		}
		lcsbw.close();
		//Prefix Match
		File pmf = new File("results/PrefixMatchAverage.csv");
		FileOutputStream pmfos = new FileOutputStream(pmf);
		BufferedWriter pmbw = new BufferedWriter(new OutputStreamWriter(pmfos));
		pmbw.write("Step;Precision;Recal;");
		pmbw.newLine();
		for (float rate = 0.01f; rate < 1f; rate += 0.01f) {
			datasetPrefixMatch = new Dataset(new PrefixMatchStrategy(), rate);
			pmbw.write(String.format("%.2f", rate)+";"+nf.format(datasetPrefixMatch.getAveragePrecision())+";"+nf.format(datasetPrefixMatch.getAverageRecall())+";");
			pmbw.newLine();
		}
		pmbw.close();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			GraphCreater.averageGraphs();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Couldn't create file");
		}

	}

}
