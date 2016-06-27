package org.springfield.lou.screen;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.restlet.Application;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.Html5Application;
import org.springfield.lou.application.PathBindObject;
import org.springfield.lou.controllers.Html5Controller;

public class BindManager {
    private Map<String, ArrayList<PathBindObject>> pathbindobjects = new HashMap<String, ArrayList<PathBindObject>>();
    private Html5Application app;
	protected Stack<BindEvent> eventqueue  = new Stack<BindEvent>();
	private BindThread normalthread;
    
    public BindManager(Html5Application a) {
    	this.app = a;
    	normalthread = new BindThread("normal",this);
    }
    
 	public void onPathUpdate(String paths,String methodname,Html5Controller callbackobject) {
 		try {
		Method method = callbackobject.getClass().getMethod(methodname,String.class,FsNode.class);

		
		String screenid = callbackobject.getScreenId();
		String targetid = callbackobject.getSelector();
		String[] vars=paths.split(",");
		for (int i=0;i<vars.length;i++) {
			ArrayList<PathBindObject> list = pathbindobjects.get(vars[i]);
			if (list!=null) {
				// find the screen id and targetid
				list.add(new PathBindObject(methodname,screenid,targetid,callbackobject,method));
			} else {
				list = new ArrayList<PathBindObject>();
				list.add(new PathBindObject(methodname,screenid,targetid,callbackobject,method));
				pathbindobjects.put(vars[i], list);
			}
		}

		} catch(Exception e) {
 			return;
 		}
 	}
 	
 	public synchronized void onPathRemove(Screen s) {
 		if (s==null) return;
 		String screenid = s.getId();
 		//System.out.println("BIND REMOVE SCREEN S="+s+" id="+screenid);
		Iterator<String> it = pathbindobjects.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			ArrayList<PathBindObject> binds = pathbindobjects.get(key);
			for (int i=binds.size()-1;i>=0;i--) {
				PathBindObject bind  = binds.get(i);
				if (bind.screenid.equals(screenid)) {
					binds.remove(i);
					//System.out.println("REMOVE ON BIND "+key);
				}
			}
			
		}
 	}
 	
    public void setProperty(String path,String value) {
    	eventqueue.push(new BindEvent(path,value));
    	System.out.println("SET PROPERTY BIND="+path+" "+value+" QS="+eventqueue.size()+" TR="+this);
    	if (eventqueue.size()>0) {
    		normalthread.check();
    	//	checkNormalQueue(); // direct delivery for testing.
    	}
    
    }
 	
    public void deliverProperty(String path,String value) {
    	int counter = 0;
    	long starttime = new Date().getTime();
    	String[] parts = path.split("/"); 
    	String key = parts[1];
    	String nodeid = parts[2];
    	String propertyname = parts[3];
   	
    	FsNode node = new FsNode(key,nodeid);
    	node.setProperty(propertyname, value);
   	
    	key = "/"+key+"/";
		ArrayList<PathBindObject> binds = pathbindobjects.get(key);
		if (binds!=null) {
			for (int i=0;i<binds.size();i++) {
				PathBindObject bind  = binds.get(i);
				try {
					bind.methodcall.invoke(bind.obj,key,node);
					counter++;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}	 
		long endtime = new Date().getTime();
		//System.out.println("SET PROPERTY TIME="+(endtime-starttime)+" bindsize="+pathbindobjects.size()+" calls="+counter);
    }
    
    public void checkNormalQueue() {
    	while (eventqueue.size()>0) {
    		BindEvent b = eventqueue.pop();
    		deliverProperty(b.path,b.value);
    	}
    }

}
