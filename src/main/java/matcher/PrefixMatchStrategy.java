package matcher;

import java.util.Iterator;
import java.util.List;

import data.StackTrace;

public class PrefixMatchStrategy extends MatchingStrategy {
	
	protected float similarityScore(StackTrace s, StackTrace t) {
		List<String> stack1 = s.getFunctionCalls();
		List<String> stack2 = t.getFunctionCalls();
		
		int maxLength = Math.max(stack1.size(), stack2.size());
		int prefixCount = 0;
		
		Iterator<String> it1 = stack1.iterator();
		Iterator<String> it2 = stack2.iterator();
		while (it1.hasNext() && it2.hasNext() && it1.next().equals(it2.next())) {
			prefixCount++;
		}
		
		float res = (float) prefixCount / maxLength;
		return res;
	}

}
