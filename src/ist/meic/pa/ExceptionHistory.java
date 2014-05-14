package ist.meic.pa;

/**
 * The Class ExceptionHistory.
 * Class for keeping record of an exception catched in a program. 
 * Provides methods for getting and setting information of an exception catching. Its used 
 * by Trace Method to store all the information
 */
public class ExceptionHistory {
	
	public ExceptionHistory(String name, int blockStart, int blockEnd, String fileName) {
		super();
		this.name = name;
		this.blockStart = blockStart;
		this.blockEnd = blockEnd;
		this.setFileName(fileName);
	}
	private String name;
	private int blockStart;
	private int blockEnd;
	private String fileName;
	
	
	/**
	 * gets the name of the exception
	 * @return exception name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * sets the name of the exception
	 * @param name of the exception
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * gets the line number where exception catch started
	 * @return line number where catch started
	 */
	public int getBlockStart() {
		return blockStart;
	}
	
	/**
	 * sets the line number of started catch to blockStart
	 * @param blockStart line number where catch starts
	 */
	public void setBlockStart(int blockStart) {
		this.blockStart = blockStart;
	}
	
	/**
	 * gets the line number where exception catch ended
	 * @return line number where catch ended
	 */
	public int getBlockEnd() {
		return blockEnd;
	}
	
	/**
	 * sets the line number of ended catch to blockEnd
	 * @param blockEnd line number where catch ends
	 */
	public void setBlockEnd(int blockEnd) {
		this.blockEnd = blockEnd;
	}
	
	/**
	 * gets the file name where exception was catched
	 * @return the file name where exception was catched
	 */
	public String getFileName() {
		return fileName;
	}
	
	
	/**
	 * sets the filename where exception was catched to fileName
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * prints all the information about an exception catched
	 * @return string with catch information
	 */
	@Override
	public String toString() {
		return "ExceptionHistory [name=" + name + ", blockStart=" + blockStart
				+ ", blockEnd=" + blockEnd + ", fileName=" + fileName + "]";
	}

}
