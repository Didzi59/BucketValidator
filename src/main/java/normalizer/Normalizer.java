package normalizer;

import java.util.LinkedList;
import java.util.List;

import data.StackTrace;

public class Normalizer {
	
	/**
	 * Removes all the recurrence of a stack
	 * @param stack the stack to be processed
	 * @return a stack without recursion
	 */
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
	
}
