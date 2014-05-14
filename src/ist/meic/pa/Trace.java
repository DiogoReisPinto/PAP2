package ist.meic.pa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The Class Trace.
 * Class for keeping information about all the trace information of each object in the program. 
 * Keeps a list of History objects with information of each object traced in the program.
 * Provides methods to add and retrieve information of each object traced.
 */
public class Trace {

	private static List<History> historyList = new CopyOnWriteArrayList<History>();
	private static List<ExceptionHistory> exceptions = new CopyOnWriteArrayList<ExceptionHistory>();
	private static boolean tracingComplete = false;
	
	/**
	 * method for printing the trace information of an object o
	 * looks in the history all the references to an object and adds a string
	 * explaining that reference to an array to be used as argument in the tracePrint method
	 * @argument Object o as the object to print the trace information
	 */
	public static void print(Object o){
		tracingComplete = true;
		boolean traced = false;
		boolean tracing = false;
		boolean exception = false;
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
				String exceptionName = inException(history);
				if(exceptionName != null && !exception){
					traceInfo.add(getException(exceptionName).toString());
					traceInfo.add("  " + history.toString());
					exception = true;
				} else if(exceptionName != null){
					traceInfo.add("  " + history.toString());
				} else{
					exception = false;
				traceInfo.add(history.toString());
				}
				traced = true;
			}
			currentHistory.remove(history);
		}
		if(!traced)
			traceInfo.add("Tracing for " + o + " is nonexistent!\n");
		tracingComplete = false;
		tracePrint(traceInfo);
		traceInfo.clear();
	}
	
	/**
	 * method for storing information to the trace history 
	 * @param object object of which information belongs
	 * @param behaviour string with the name of the method
	 * @param fileName name of the file 
	 * @param lineNumber lineNumber of the file
	 */
	public static void storeHistory(Object object, String behaviour, String fileName, int lineNumber, String condition){
		if(!tracingComplete){
		History history = new History(object, behaviour, fileName, lineNumber, condition);
		historyList.add(history);
		}
	}
	
	/**
	 * method for storing argument of a function call to the trace history
	 * @param args arguments in the function call
	 * @param behaviour string with the name of the method to which arguments belong
	 * @param fileName name of the file where the arguments were passed
	 * @param lineNumber lineNumber of the file where the argument were passed
	 */
	public static void getArgs(Object[] args, String behaviour, String fileName, int lineNumber){
		if(!tracingComplete){
		for(Object arg : args){
			History history = new History(arg, behaviour, fileName, lineNumber, "ARGUMENT");
			historyList.add(history);
		}
		}
	}
	
	/**
	 * method for storing return of a function call to the trace history
	 * @param arg returned object
	 * @param behaviour string with the name of the method
	 * @param fileName name of the file where return happened
	 * @param lineNumber lineNumber of the file where return happened
	 */
	public static void getReturn(Object arg, String behaviour, String fileName, int lineNumber){
		if(arg != null && !tracingComplete){
			History history = new History(arg, behaviour, fileName, lineNumber, "CALL");
			historyList.add(history);
		}
	}
	
	
	/**
	 * prints a requested trace information
	 * @param traceInfo information to print
	 */
	public static void tracePrint(List<String> traceInfo){
		for(String info : traceInfo){
			System.err.print(info);
			System.err.flush();
		}
	}
	
	
	/**
	 * deletes all the information of an object in the history list
	 * @param object object to delete information
	 */
	public static void clear(Object object){
		for(History history : historyList){
			if(history.getObject().equals(object))
				historyList.remove(history);
		}
	}
	
	/**
	 * method for adding information of catched exception
	 * @param name name of the exception
	 * @param blockStart line number where the exception catch started
	 * @param blockEnd line number where the exception catch ended
	 * @param fileName file name where catch occurred
	 */
	public static void addException(String name, int blockStart, int blockEnd, String fileName){
		ExceptionHistory eh = new ExceptionHistory(name, blockStart, blockEnd, fileName);
		exceptions.add(eh);
	}

	
	/**
	 * method to detect which history members belong to a catch of an exception and return that exception's name
	 * @param history an history object that represents an object on trace information
	 * @return string with exception name
	 */
	public static String inException(History history){
		String exceptionName = null;
		for(ExceptionHistory eh : exceptions){
			if(history.getLineNumber() >= eh.getBlockStart() && history.getLineNumber() < eh.getBlockEnd() && history.getFileName().matches(eh.getFileName()))
				exceptionName = eh.getName();
		}

		return exceptionName;
	}
	
	
	/**
	 * return history object of exception with name name
	 * @param name name of the exception
	 * @return history object of the exception with name name
	 */
	public static History getException(String name){
		History hi = null;
		for(History h : historyList){
			if(h.getObject().equals(name))
				hi = h;
		}
		return hi;
	}
}
