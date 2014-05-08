package ist.meic.pa.Translator;


import javassist.*;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.analysis.ControlFlow;
import javassist.bytecode.analysis.ControlFlow.Block;
import javassist.bytecode.analysis.ControlFlow.*;
import javassist.bytecode.stackmap.BasicBlock;
import javassist.expr.ExprEditor;
import javassist.expr.Handler;
import javassist.expr.Cast;
import javassist.expr.FieldAccess;
import ist.meic.pa.Trace;

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
	
	public void makeTraceable(final CtClass cc) throws CannotCompileException{
		MyTranslator.makeTraceable(cc);
		if(!cc.getSimpleName().matches("Trace") && !cc.getSimpleName().matches("History") && !cc.getSimpleName().matches("ExceptionHistory")){
			for(CtMethod ct : cc.getDeclaredMethods()){
					ControlFlow cf = null;
					try {
						cf = new ControlFlow(ct);
					} catch (BadBytecode e) {
						// TODO Auto-generated catch block
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					h.insertBefore("ist.meic.pa.Trace.storeHistory(\"" + exception + "\", \"  Handling " + exception + "\", \"" + h.getFileName() + "\", " + h.getLineNumber() + ", \"CAST\");");
				}
				public void edit(Cast c) throws CannotCompileException{
					try {
						String behaviour = "  Cast to " + c.getType().getName();
						c.replace("{ist.meic.pa.Trace.storeHistory($1, \"" + behaviour + "\", \"" + c.getFileName() + "\", " + c.getLineNumber() + ", \"CAST\");"
//								+ "System.out.println($1);"
								+ "$_ = $proceed($$);}");
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				public void edit(FieldAccess f) throws CannotCompileException{
					String field = null;
					try {
						field = f.getField().getName();
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
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
