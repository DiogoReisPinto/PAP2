package ist.meic.pa.Translator;


import javassist.*;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.analysis.ControlFlow;
import javassist.bytecode.analysis.ControlFlow.*;
import javassist.bytecode.stackmap.BasicBlock;
import javassist.expr.ExprEditor;
import javassist.expr.Handler;
import javassist.expr.Cast;
import javassist.expr.FieldAccess;
import ist.meic.pa.Trace;

/**
 * The Class ExtensionTranslator.
 * Implementation of a translator to each class loaded that will inject code
 * for making possible to trace objects.
 * Used for extensions in the project
 */
public class ExtensionTranslator implements Translator {

	@Override
	public void onLoad(ClassPool arg0, String arg1) throws NotFoundException,
			CannotCompileException {
		CtClass cc = arg0.get(arg1);
		cc.setModifiers(Modifier.PUBLIC);
		makeTraceable(cc);
		
	}

	@Override
	public void start(ClassPool arg0) throws NotFoundException,
			CannotCompileException {
		
	}
	
	/**
	 * method for injecting, to each class and each method of it, code for 
	 * store information about exceptions being handled, fields being accessed and written 
	 * and casts being made
	 * @param cc as the CtClass for which we want to inject code
	 * @throws CannotCompileException
	 */
	public void makeTraceable(final CtClass cc) throws CannotCompileException{
		TraceTranslator.makeTraceable(cc);
		if(!cc.getSimpleName().matches("Trace") && !cc.getSimpleName().matches("History") && !cc.getSimpleName().matches("ExceptionHistory")){
			for(CtMethod ct : cc.getDeclaredMethods()){
					ControlFlow cf = null;
					try {
						cf = new ControlFlow(ct);
					} catch (BadBytecode e) {
						e.printStackTrace();
					}
					for(BasicBlock b : cf.basicBlocks()){
						for(Catcher c : ((Block) b).catchers()){
							MethodInfo mi = ct.getMethodInfo();
							String name = c.type();
							int blockStart = c.block().position();
							int blockEnd = blockStart + c.block().length();
							ct.insertBefore("ist.meic.pa.Trace.addException(\"" + name + "\", " + mi.getLineNumber(blockStart) + ", " + mi.getLineNumber(blockEnd) + ", \"" + cc.getClassFile().getSourceFile() + "\");");
						}
					}
				}
			
			cc.instrument(new ExprEditor(){
				public void edit(Handler h) throws CannotCompileException{
					Object exception = null;
					try {
						exception = h.getType().getName();
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
					h.insertBefore("ist.meic.pa.Trace.storeHistory(\"" + exception + "\", \"  Handling " + exception + "\", \"" + h.getFileName() + "\", " + h.getLineNumber() + ", \"CAST\");");
				}
				public void edit(Cast c) throws CannotCompileException{
					try {
						String behaviour = "  Cast to " + c.getType().getName();
						c.replace("{ist.meic.pa.Trace.storeHistory($1, \"" + behaviour + "\", \"" + c.getFileName() + "\", " + c.getLineNumber() + ", \"CAST\");"
								+ "$_ = $proceed($$);}");
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
				}
				public void edit(FieldAccess f) throws CannotCompileException{
					String field = null;
					try {
						field = f.getField().getName();
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
					if(f.isWriter())
						f.replace("{String behaviour = \"  Wrote value to \" + \"" + field + "\";"
								+ "$_ = $proceed($$);"
								+ "ist.meic.pa.Trace.storeHistory(($w)$1, behaviour, \"" + f.getFileName() + "\", " + f.getLineNumber() + ", \"FIELD ACCESS\");}");
					else
						f.replace("{String behaviour = \"  Read value from \" + \"" + field + "\";"
								+ "$_ = $proceed($$);"
								+ "ist.meic.pa.Trace.storeHistory(($w)$_, behaviour, \"" + f.getFileName() + "\", " + f.getLineNumber() + ", \"FIELD ACCESS\");}");
				}
			
			});
		}
	
	}

}
