package matcher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import data.Bucket;
import data.StackTrace;

public abstract class MatchingStrategy {
	
	public List<Bucket> executeMatching(List<StackTrace> original, float rate) {
		List<Bucket> res = new LinkedList<Bucket>();
		int count = 1;
		for (Iterator<StackTrace> iterator = original.iterator(); iterator.hasNext();) {
		    StackTrace s = iterator.next();
		    Bucket b = new Bucket("B"+count);
		    count++;
		    res.add(b);
		    b.addStackTrace(s);
		    iterator.remove();
			for (Iterator<StackTrace> iterator2 = original.iterator(); iterator2.hasNext();) {
			    StackTrace t = iterator2.next();
			    if (similarityScore(s, t) >= rate) {
			        // Remove the current element from the iterator and the list.
			    	b.addStackTrace(t);
			        iterator2.remove();
			    }
			}
			iterator = original.iterator();
		}
		return res;
	}
	
	protected abstract float similarityScore(StackTrace s, StackTrace t);
	
}
