package ist.meic.pa;

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
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
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
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}

}
