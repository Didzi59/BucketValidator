package normalizer;

import java.util.LinkedList;
import java.util.List;

import data.StackTrace;

public class Normalizer {
	
	public void afficheListe(StackTrace maListe){
		for(String s : maListe.getFunctionCalls()) System.out.println(s);
	}
	
	public static StackTrace removeRecurrence(StackTrace stack){
		StackTrace newStack = new StackTrace(stack.getId());
		LinkedList<String> functionCalls = stack.getFunctionCalls();
		int stackLength = functionCalls.size();
		for(int i = 0; i < stackLength;i++){
			for(int j = stackLength - 1; j > i;j--){
				if(functionCalls.get(i).equals(functionCalls.get(j))){
					List<String> subStack1 = functionCalls.subList(0, i + 1);
					List<String> subStack2 = functionCalls.subList(j + 1, stackLength);			
					LinkedList<String> newFunctionCalls = new LinkedList<String>();
					newFunctionCalls.addAll(subStack1);
					newFunctionCalls.addAll(subStack2);
					newStack.setFunctionCalls(newFunctionCalls);
					return removeRecurrence(newStack);
				}
			}
		}
		return stack;
	}
	
//	public static Bucket removeRecurrence(Bucket bucket){
//		Bucket newBucket = new Bucket(bucket.getId());
//		for (StackTrace t : bucket.getTraces()) {
//			StackTrace s = new StackTrace(t.getId());
//			s = Normalizer.removeRecurrence(t);
//			newBucket.addStackTrace(s);
//		}
//		return newBucket;
//	}
	
	
	public static void main(String[] args){
		StackTrace stack = new StackTrace("1");
		StackTrace strings = new StackTrace("2");
		
		stack.addCall("sqlzeDumpFFDC");
		stack.addCall("sqlzeSqlCode");
		stack.addCall("sqlnn_erds");
		stack.addCall("sqlno_prop_pipe");
		stack.addCall("PIPE");
		stack.addCall("sqlno_crule_pipe_r");
		stack.addCall("sqlno_crule_pipe");
		stack.addCall("sqlno_plan_qun");
		stack.addCall("sqlno_call_sf");
		stack.addCall("sqlno_each_opr");
		stack.addCall("sqlno_call_sf");
		stack.addCall("sqlno_walk_qun");
		stack.addCall("sqlno_call_sf");
		stack.addCall("sqlno_each_opr");
		stack.addCall("sqlno_call_sf");
		stack.addCall("sqlno_walk_qun");
		stack.addCall("sqlno_call_sf");
		stack.addCall("sqlno_each_opr");
		stack.addCall("sqlno_call_sf");
		stack.addCall("sqlno_walk_qun");
		stack.addCall("sqlno_call_sf");
		stack.addCall("sqlno_each_opr");

		
		strings.addCall("a");
		strings.addCall("b");
		strings.addCall("b");
		strings.addCall("b");
		strings.addCall("a");
		strings.addCall("b");
		strings.addCall("c");
		strings.addCall("d");
		strings.addCall("d");
		strings.addCall("c");
		strings.addCall("d");
		strings.addCall("c");
		
		stack.print();
		Normalizer.removeRecurrence(stack).print();
		strings.print();
		Normalizer.removeRecurrence(strings).print();
	}
	
}
