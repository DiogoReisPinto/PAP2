package ist.meic.pa.dummys;

public class Test0 {

	private static String toPrint;
	public static void main(String[] args) {
		
		System.err.println("CALLED!");
		System.err.println(args[0]);
		

	}
	
	public String print(){
		System.err.println(toPrint);
		return null;
	}

}
