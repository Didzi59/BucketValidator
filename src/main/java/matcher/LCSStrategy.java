package matcher;

import java.util.List;

import data.StackTrace;

public class LCSStrategy extends StrategyMatching {

	protected float similarityScore(StackTrace s, StackTrace t) {
		List<String> stack1 = s.getFunctionCalls();
		List<String> stack2 = t.getFunctionCalls();
		
		int stackSize1 = stack1.size();
		int stackSize2 = stack2.size();
		int[][] table = new int[stackSize1+1][stackSize2+1];
	
		// Initialize table that will store LCS's of all prefix strings.
		// This initialization is for all empty string cases.
		for (int i=0; i<=stackSize1; i++)
	  		table[i][0] = 0;
		for (int i=0; i<=stackSize2; i++)
	  		table[0][i] = 0;
	
		// Fill in each LCS value in order from top row to bottom row,
		// moving left to right.
		for (int i = 1; i<=stackSize1; i++) {
	  		for (int j = 1; j<=stackSize2; j++) {
	
	    		// If last characters of prefixes match, add one to former value.
	    		//if (x.charAt(i-1) == y.charAt(j-1))
	  			if (stack1.get(i-1).equals(stack2.get(j-1)))
	      			table[i][j] = 1+table[i-1][j-1];
	    		// Otherwise, take the maximum of the two adjacent cases.
	    		else
	      			table[i][j] = Math.max(table[i][j-1], table[i-1][j]);
	  			
	    		//System.out.print(table[i][j]+" ");
	  			//System.out.println(a.get(i-1)+" and "+b.get(j - 1));
	  		}
	  		//System.out.println();
		}
		
		int LCS = table[stackSize1][stackSize2];
		//System.out.println("LCS is "+ LCS);
		int maxSize = Math.max(stack1.size(), stack2.size());
		//System.out.println("max size is "+ maxSize);
		float similarity = (float) LCS/maxSize;
		return similarity;
	}

}
