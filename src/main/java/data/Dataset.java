package data;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import parser.Parser;

public class Dataset {
	
	private Map<String, LinkedList<StackTrace>> dataset;
	
	public Dataset(File folder) {
		this.dataset = new HashMap<String, LinkedList<StackTrace>>();
		this.parseFolder(folder);
	}
	
	public void parseFolder(File folder) {
		if (folder.isDirectory()) {
			File[] list = folder.listFiles();
			if (list != null){
				for (int i = 0; i < list.length; i++) {
					// Appel récursif sur les sous-répertoires
					parseFolder(list[i]);
				} 
			} else {
				System.err.println(folder + " : Reading error.");
			}
		} else {
			System.out.println("*********************************" +folder.getAbsolutePath());
			StackTrace stack = Parser.parse(folder);
			String bucket = stack.getBucket();
			LinkedList<StackTrace> stacks = new LinkedList<StackTrace>();
			if(this.dataset.containsKey(bucket)){
				stacks = (LinkedList<StackTrace>) this.dataset.get(bucket);	
			}
			stacks.add(stack);
			dataset.put(bucket, stacks);
		}
	}
	
	
	
	public static void main(String args[]) {
		File repertoire = new File("/gfs/bossutj/original_stack");
		new Dataset(repertoire).parseFolder(repertoire);
	}
}
