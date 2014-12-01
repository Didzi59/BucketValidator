package simulation;

import java.io.IOException;

import simulation.display.FileGenerator;
import data.GroundTruth;

public class Main {
	
	public static void main(String args[]) {
		
		try {
			
			String destination = args[0];
			GroundTruth gt = new GroundTruth(args[1]);
			
			FileGenerator fg = new FileGenerator(destination, gt);
			System.out.println("Generating average file (0.01 step)");
			fg.averageGraphs(0.01f);
			System.out.println("Generation complete!");
			System.out.println("Generating average file (0.05 step)");
			fg.averageGraphs(0.05f);
			System.out.println("Generation complete!");
			
			System.out.println("Generating inter/intra bucket file as well as the suspected faulty functions");
			fg.valuesIntraInterBucket(.0001f);
			System.out.println("Generation complete!");
			
			System.out.println("Generating scatter files depicting the generated buckets");
			fg.bucketScatter();
			System.out.println("Generation complete!");
			
			gt.printRecursionRemoval();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Couldn't create file");
		}
	}
	
}
