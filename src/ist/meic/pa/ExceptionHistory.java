package ist.meic.pa;

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
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBlockStart() {
		return blockStart;
	}
	public void setBlockStart(int blockStart) {
		this.blockStart = blockStart;
	}
	public int getBlockEnd() {
		return blockEnd;
	}
	public void setBlockEnd(int blockEnd) {
		this.blockEnd = blockEnd;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
	public String toString() {
		return "ExceptionHistory [name=" + name + ", blockStart=" + blockStart
				+ ", blockEnd=" + blockEnd + ", fileName=" + fileName + "]";
	}

}
