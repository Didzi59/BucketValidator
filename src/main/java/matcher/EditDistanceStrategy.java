package matcher;

import java.util.List;

import data.StackTrace;

public class EditDistanceStrategy extends StrategyMatching {

	protected float similarityScore(StackTrace s, StackTrace t) {
		List<String> stack1 = s.getFunctionCalls();
		List<String> stack2 = t.getFunctionCalls();
		
		int stackSize1 = stack1.size();
		int stackSize2 = stack2.size();
		
		// stackSize1 + 1, stackSize2 + 1, because finally return results[stackSize1][stackSize2]
		int[][] results = new int[stackSize1 + 1][stackSize2 + 1];
		
		//initialization
		for(int i = 0; i <= stackSize1; i++){
			results[i][0] = i;
		}
		for(int j = 0; j <= stackSize2; j++){
			results[0][j] = j;
		}
		
		//iterate though, and check last char
		for (int i = 0; i < stackSize1; i++) {
			//char c1 = word1.charAt(i);
			String s1 = stack1.get(i);
			for (int j = 0; j < stackSize2; j++) {
				//char c2 = word2.charAt(j);
				String s2 = stack2.get(j);
	 
				//if last two chars equal
				if (s1.equals(s2)) {
					//update results value for +1 length
					results[i + 1][j + 1] = results[i][j];
				} else {
					int replace = results[i][j] + 1;
					int insert = results[i][j + 1] + 1;
					int delete = results[i + 1][j] + 1;
	 
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					results[i + 1][j + 1] = min;
				}
			}
		}
	 
		int distance = results[stackSize1][stackSize2];
		//System.out.println("distance is " + distance);
		int maxSize = Math.max(stackSize1, stackSize2);
		//System.out.println("max size is " + maxSize);
		float similarity = (float)(maxSize - distance)/maxSize;
		return similarity;
	}
}
