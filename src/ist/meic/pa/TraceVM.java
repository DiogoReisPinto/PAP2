package ist.meic.pa;


import ist.meic.pa.Translator.TraceTranslator;
import javassist.*;

/**
 * The Class TraceVM.
 * Class for associating an translator to every class that is loaded. Is responsible for
 * receiving in its main method a program name and its arguments and start its execution.
 * It's our entry class for this project, starting all the trace process.
 */
public class TraceVM {
	
	private static ClassPool cp;
	
	public static void main(String[] args){
	    Translator t = new TraceTranslator();
	    cp = ClassPool.getDefault();
	    Loader cl=new Loader();
	    
	    try {
	    	cl.addTranslator(cp, t);
			if(args.length==0){
				System.err.println("Class name not provided!");
				return;
			}
			if(args.length>0){
				int argsSize = args.length-1;
				String[] arguments = new String[argsSize];
				System.arraycopy(args,1,arguments,0,argsSize);
				cl.run(args[0],arguments );
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}  
	}
		
		
}
