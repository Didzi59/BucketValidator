package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.StackTrace;

public class Parser {
	
	private static final String FUNCTION_NAME_PATTERN = "^([0-9]+\\|){2}([^|]*\\|)([^|]+)\\|";
	private static final int FUNCTION_NAME_GROUP = 3;

	public static StackTrace parse(File file) {
		String filename = file.getName();
		StackTrace stack = new StackTrace(filename.substring(0, filename.length()-4));

		//Parse file
		Scanner scanner;
		try {
			scanner = new Scanner(file);		
			while (scanner.hasNextLine()) {
			    String line = scanner.nextLine();			    
			    Pattern pattern = Pattern.compile(FUNCTION_NAME_PATTERN);
			    Matcher matcher = pattern.matcher(line);
			    if (matcher.find()) {
			    	String functionCall = matcher.group(FUNCTION_NAME_GROUP);
			    	//System.out.println(functionCall);
			    	stack.addCall(functionCall);
			    }
			}	 
			scanner.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return stack;
	}
	
}
