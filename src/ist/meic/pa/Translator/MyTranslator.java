package ist.meic.pa.Translator;

import javassist.*;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import ist.meic.pa.Trace;

public class MyTranslator implements Translator {

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

	public static void makeTraceable(final CtClass cc) throws CannotCompileException{
		for(CtMethod ctMethod : cc.getDeclaredMethods()){
			if(cc.getSimpleName().equals("Trace") && !ctMethod.getName().equals("print"))
				continue;
			ctMethod.instrument(new ExprEditor(){
				public void edit(MethodCall m) throws CannotCompileException{
					if(!m.getClassName().equals("ist.meic.pa.History")){
					String behaviour = null;
					try {
						behaviour = m.getMethod().getLongName();
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					m.replace("{ist.meic.pa.Trace.getArgs($args, \"" + behaviour + "\", \"" + m.getFileName() + "\", " + m.getLineNumber() + ");" + 
							"  $_ = $proceed($$);" + 
							"  ist.meic.pa.Trace.getReturn(($w)$_, \"" + behaviour + "\", \"" + m.getFileName() + "\", " + m.getLineNumber() + ");}");
					}
				}
				public void edit(NewExpr m) throws CannotCompileException{
					if(!m.getClassName().equals("ist.meic.pa.History")){
					String behaviour = null;
					try {
						behaviour = m.getConstructor().getLongName();
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					m.replace("{$_ = $proceed($$);" +
							"	ist.meic.pa.Trace.clear(($w)$_);" + 
							"  ist.meic.pa.Trace.getReturn(($w)$_, \"" + behaviour + "\", \"" + m.getFileName() + "\", " + m.getLineNumber() + ");}");
					}
				}
			});
		}
	}

}
