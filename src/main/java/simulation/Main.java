package simulation;

import matcher.EditDistanceStrategy;
import matcher.LCSStrategy;
import matcher.PrefixMatchStrategy;
import data.Dataset;

public class Main {
	
	public static void main(String args[]) {
		Dataset datasetEditDistance;
		Dataset datasetPrefixMatch;
		Dataset datasetLCS;
		for (float rate = 0.05f; rate <= 1f; rate += 0.05f) {
			datasetEditDistance = new Dataset(new EditDistanceStrategy(), rate);
			datasetPrefixMatch = new Dataset(new PrefixMatchStrategy(), rate);
			datasetLCS = new Dataset(new LCSStrategy(), rate);
			System.out.println("********************* RATE = "+rate+" *********************");
			System.out.println("datasetEditDistance => Precision = "+datasetEditDistance.getAveragePrecision()+", Recall = "+datasetEditDistance.getAverageRecall());
			System.out.println("datasetPrefixMatch  => Precision = "+datasetPrefixMatch.getAveragePrecision()+", Recall = "+datasetPrefixMatch.getAverageRecall());
			System.out.println("datasetLCS          => Precision = "+datasetLCS.getAveragePrecision()+", Recall = "+datasetLCS.getAverageRecall());
		}
	}
	
}
