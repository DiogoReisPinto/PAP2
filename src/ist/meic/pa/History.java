package ist.meic.pa;


/**
 * The Class History.
 * Class for keeping record of an object reference in a program. 
 * Provides methods for getting and setting information of an object reference. Its used 
 * by Trace Method to store all the information
 */
public class History {
	
	public History(Object object, String behaviour, String fileName, int lineNumber, String condition) {
		super();
		this.setObject(object);
		this.behaviour = behaviour;
		this.fileName = fileName;
		this.lineNumber = lineNumber;
		this.condition = condition;
	}
	private Object object;
	private String behaviour;
	private String fileName;
	private int lineNumber;
	private String condition;
	
	
	/**
	 * gets the fileName where a object reference happened
	 * @return the File name where object was referenced
	 */
	public String getFileName() {
		return fileName;
	}
	
	
	/**
	 *  sets the fileName property to fileName
	 * @param fileName where object was referenced
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	/**
	 * get the line number of a reference of an object
	 * @return the lineNumber where referenced occurred
	 */
	public int getLineNumber() {
		return lineNumber;
	}
	
	/**
	 *  sets the lineNumber property to lineNumber
	 * @param lineNumber where object was referenced
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	
	/**
	 *  prints the information of an object reference taking in account if it used
	 *  as argument, call or return
	 * @return string with information of object reference
	 */
	public String toString(){
		String result = null;
		if(condition.matches("ARGUMENT"))
			result = "  -> " + behaviour + " on " + fileName + ":" + lineNumber + "\n";
		else if(condition.matches("CALL"))
			result = "  <- " + behaviour + " on " + fileName + ":" + lineNumber + "\n";
		else
			result = behaviour + " on " + fileName + ":" + lineNumber + "\n";
		return result;		
	}
	
	/**
	 * gets the object to which belongs the reference
	 * @return the object referenced
	 */
	public Object getObject() {
		return object;
	}
	
	/**
	 * sets the object to which belongs the reference
	 * @param object the new object to which reference will belong
	 */
	public void setObject(Object object) {
		this.object = object;
	}

}
