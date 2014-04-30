package ist.meic.pa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Trace {

	private static List<History> historyList = new CopyOnWriteArrayList<History>();
	private static boolean tracingComplete = false;
	
	public static void print(Object o){
		tracingComplete = true;
		boolean traced = false;
		boolean tracing = false;
		List<History> currentHistory = new CopyOnWriteArrayList<History>();
		List<String> traceInfo = new ArrayList<String>();
		currentHistory.addAll(historyList);
		Iterator<History> it = currentHistory.iterator();
		while(it.hasNext()){
			History history = it.next();
			if(o.equals(history.getObject())){
				if(!tracing){
					traceInfo.add("Tracing for " + o + "\n");
					tracing = true;
				}
				traced = true;
				traceInfo.add(history.toString());
			}
			currentHistory.remove(history);
		}
		if(!traced)
			traceInfo.add("Tracing for " + o + " is nonexistent!\n");
		tracingComplete = false;
		tracePrint(traceInfo);
		traceInfo.clear();
	}
	
	public static void storeHistory(Object object, String behaviour, String fileName, int lineNumber, String condition){
		History history = new History(object, behaviour, fileName, lineNumber, condition);
		historyList.add(history);
	}
	
	public static void getArgs(Object[] args, String behaviour, String fileName, int lineNumber){
		if(!tracingComplete){
		for(Object arg : args){
			History history = new History(arg, behaviour, fileName, lineNumber, "ARGUMENT");
			historyList.add(history);
		}
		}
	}
	
	public static void getReturn(Object arg, String behaviour, String fileName, int lineNumber){
		if(arg != null && !tracingComplete){
			History history = new History(arg, behaviour, fileName, lineNumber, "CALL");
			historyList.add(history);
		}
	}
	
	public static void tracePrint(List<String> traceInfo){
		for(String info : traceInfo){
			System.err.print(info);
			System.err.flush();
		}
	}
	
	public static void clear(Object object){
		for(History history : historyList){
			if(history.getObject().equals(object))
				historyList.remove(history);
		}
	}

}
