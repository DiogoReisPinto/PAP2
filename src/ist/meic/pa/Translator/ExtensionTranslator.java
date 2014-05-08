package ist.meic.pa.Translator;


import javassist.*;
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
		if(!cc.getSimpleName().matches("Trace") && !cc.getSimpleName().matches("History")){
//			for(CtMethod ctMethod : cc.getDeclaredMethods()){
//				ctMethod.
//			}
			cc.instrument(new ExprEditor(){
				public void edit(Handler h) throws CannotCompileException{
					Object exception = null;
					try {
						exception = h.getType().getSimpleName();
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					h.insertBefore("ist.meic.pa.Trace.storeHistory(\"Exception\", \"  Caught " + exception + "\", \"" + h.getFileName() + "\", " + h.getLineNumber() + ", \"CAST\");");
				}
				public void edit(Cast c) throws CannotCompileException{
					try {
						String behaviour = "  Cast to " + c.getType().getName();
						System.out.println(c.where().getLongName() + c.getLineNumber());
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
