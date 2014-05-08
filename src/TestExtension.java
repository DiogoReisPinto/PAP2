import ist.meic.pa.Trace;

import java.util.*;

class TestExt {

    private Object object1;

    public Object identity(Object o) {
        return o;
    }

    public void test() {
        object1 = new String("MyObj");
        
        String s = (String) identity(object1);
        
        Trace.print(s);
        
        Object o = object1;
        
        identity(o);

        Trace.print("MyObj");
        
        ArrayList<Object> objectList = new ArrayList<Object>();
        objectList.add(o);
        
        try{
        	objectList.get(3);
        } catch(IndexOutOfBoundsException e){
        	o = new String("OtherObj");
        }
        
        Trace.print(o);
        
        Object obj = new String("Object");
        
        
        Trace.print((String) obj);

    }
}

public class TestExtension {
	public static void main(String args[]) {
        (new TestExt()).test();
    }
}
