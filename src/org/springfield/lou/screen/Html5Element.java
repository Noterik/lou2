package org.springfield.lou.screen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.management.modelmbean.ModelMBean;

import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.Html5ApplicationInterface;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.model.SmithersModel;

public class Html5Element {
	private String selector;
	private Screen screen;
	private Map<String, Object> variables;
	private String javascript=null;
	private Html5Controller controller = null;
	
	public Html5Element(Screen s,String sel) {
		selector = sel;
		screen  = s;
	}
	
	public boolean show() {
		screen.send("show("+selector+")");
		return true;
	}
	
	public boolean hide() {
		screen.send("hide("+selector+")");
		return true;
	}
	
	public boolean remove() {
		// need to remove this element and all that its tracking/doing
		screen.getApplication().removeEvents(controller);
		return true;
	}
	
	public boolean draggable() {
		screen.send("draggable("+selector+")");
		return true;
	}
	
	public boolean css(String elementname,String value) {
		screen.setDiv(selector.substring(1),"style:"+elementname+":"+value);
		return true;
	}
	
	public boolean html(String html) {
		screen.send("html("+selector.substring(1)+")="+html);
		return true;
	}
	
	public boolean append(String html) {
		screen.send("append("+selector.substring(1)+")="+html);
		return true;
	}
	
	public void syncvars(String vars) {
		// vars we want synced between client and server (one way for now)
		JSONObject data = new JSONObject();
		
		// get the 3 nodes we might want
		FsNode controllernode = controller.getControllerNode(selector);
		
		String[] items = vars.split(",");
		for (int i=0;i<items.length;i++) {
			String[] item = items[i].split("/");
			String type = item[0];
			String key = item[1];
		//	System.out.println("T="+type);
		//	System.out.println("K="+key);
			if (type.equals("controller")) {
				data.put(items[i], controllernode.getProperty(key));
			}
		}
		screen.send("syncvars("+selector.substring(1)+")="+data.toJSONString());
	}
	
	public boolean template(String template) {
			// extend for the real path
		    if (template==null || template.equals("")) {
		    	if (controller!=null) {
		    		template = controller.getDefaultTemplate();
		    	}
		    } else {
		    	String part = screen.getApplication().getAppname().substring(screen.getApplication().getAppname().lastIndexOf("/")+1);
		    	template = "/springfield/tomcat/webapps/ROOT/eddie/apps/"+part+"/components/"+template;	
		    }
			StringBuffer str = null;
				try {
					str = new StringBuffer();
					BufferedReader br;
					br = new BufferedReader(new FileReader(template));
					String line = br.readLine();
					while (line != null) {
						str.append(line);
						str.append("\n");
						line = br.readLine();
					 }
					br.close();
				} catch (FileNotFoundException e) {
					System.out.println("COULD NOT FIND TEMPLATE ("+selector+") : "+template);
				} catch (IOException e) {
					e.printStackTrace();
				}
				JSONObject data = new JSONObject();
				data.put("template", str.toString());
				screen.send("template("+selector.substring(1)+")="+data);
				return true;
	}
	
	public boolean update(JSONObject data) {
		if (javascript!=null) {
			screen.send("update("+selector.substring(1)+")="+data);
		} else {
			if (controller!=null) controller.update(data);
		}
	
		return true;
	}
	
	public boolean parsehtml(JSONObject json) {
		SmithersModel model = screen.getApplication().getModel();
		//System.out.println("SELECTOR="+selector);
		FsNode node = model.getNode("/app/view/"+selector);
		if (node!=null) {
			String template = node.getProperty("template");
			// extend for the real path
			String part = screen.getApplication().getAppname().substring(screen.getApplication().getAppname().lastIndexOf("/")+1);
			
		    if (template==null || template.equals("")) {
	    		template = controller.getDefaultTemplate();
		    } else {
				template = "/springfield/tomcat/webapps/ROOT/eddie/apps/"+part+"/components/"+template;	
		    }
			if (template!=null && !template.equals("")) {
				
				
				StringBuffer str = null;
				try {
					str = new StringBuffer();
					BufferedReader br;
					br = new BufferedReader(new FileReader(template));
					String line = br.readLine();
					while (line != null) {
						str.append(line);
						str.append("\n");
						line = br.readLine();
					 }
					br.close();
				} catch (FileNotFoundException e) {
					System.out.println("COULD NOT FIND TEMPLATE ("+selector+") : "+template);
				} catch (IOException e) {
					e.printStackTrace();
				}
				json.put("template", str.toString());
				screen.send("parsehtml("+selector.substring(1)+")="+json);
				//System.out.println("TEMPLATE ("+selector+") : parsehtml("+selector.substring(1)+")="+json);
				return true;
			} else {
				html("NO TEMPLATE IN VIEW NODE "+selector+" DEFINED");
				return false;
			}
		} else {
			html("NO VIEW NODE "+selector+" DEFINED");
			return false;
		}
	}
	
	public boolean loadScript(Html5Controller c) {
		controller = c;
		SmithersModel model = screen.getApplication().getModel();
		//System.out.println("SELECTOR="+selector);
		FsNode node = model.getNode("/app/view/"+selector+"/controller/"+controller.getControllerName());
		if (node!=null) {
			String scriptname = node.getProperty("javascript");
			if (scriptname==null || scriptname.equals("")) {
				scriptname = controller.getDefaultScript();
			} else {
				// extend for the real path
				String part = screen.getApplication().getAppname().substring(screen.getApplication().getAppname().lastIndexOf("/")+1);
				scriptname = "/springfield/tomcat/webapps/ROOT/eddie/apps/"+part+"/components/"+scriptname;	
			}
			if (scriptname!=null && !scriptname.equals("")) {
				
				StringBuffer str = null;
				try {
					str = new StringBuffer();
					BufferedReader br;
					br = new BufferedReader(new FileReader(scriptname));
					String line = br.readLine();
					while (line != null) {
						str.append(line);
						str.append("\n");
						line = br.readLine();
					 }
					br.close();
				} catch (FileNotFoundException e) {
					System.out.println("COULD NOT FIND JS : "+scriptname);
					return false;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}

				screen.setScript(selector, str.toString());
				javascript = scriptname;
				//System.out.println("SET JAVACRIPT ON "+selector+" "+javascript+" THIS="+this);
				return true;
			} else {
				html("NO SCRIPT IN VIEW NODE "+selector+" DEFINED");
				return false;
			}
		} else {
			//html("NO VIEW NODE "+selector+" DEFINED");
			return false;
		}

	}
	
	public boolean css(String[] list) {
		for (int i=0;i<list.length;i++) {
			screen.setDiv(selector.substring(1),"style:"+list[i]);
		}
		return true;
	}
	
	public boolean on(String eventtype,String callbackmethod) { // should this be removed and make the main app also a controller? (Daniel)
		screen.bind(selector,eventtype,callbackmethod,screen.getApplication());
		return true;
	}
	
	public boolean on(String eventtype,String callbackmethod,Object callbackobject) {
		return on(eventtype,"",callbackmethod,callbackobject);
	}
	
	public boolean on(String eventtype,String eventpadding,String callbackmethod,Object callbackobject) {
		screen.bind(selector,eventtype,eventpadding,callbackmethod,callbackobject);
		return true;
	}
	
	public boolean val(String value) {
		screen.send("val("+selector.substring(1)+")="+value);
		return true;
	}
	
	public void track(String vars,String callbackmethod,Object callbackobject) {
		screen.bind(selector,"track/"+vars,callbackmethod,callbackobject);
	}
	
	public boolean on(String eventtype,Html5Controller c) {
		controller = c;
		controller.setScreen(screen);
		controller.setModel(screen.getApplication().getModel());
		controller.attach(selector,eventtype);
		return true;
	}
	
	public boolean html(Html5Controller c) {
		controller = c;
		controller.setScreen(screen);
		controller.setModel(screen.getApplication().getModel());
		controller.attach(selector);
		return true;
	}
	
	public boolean attach(Html5Controller c) {
		controller = c;
		controller.setScreen(screen);
		controller.setModel(screen.getApplication().getModel());
		controller.attach(selector);
		return true;
	}
	
	public boolean append(String elementname,String elementid,Html5Controller c) {
		//html("<"+elementname+" id=\""+elementid+"\"></"+elementname+">"); should be append !
		append("<"+elementname+" id=\""+elementid+"\"></"+elementname+">");
		screen.get("#"+elementid).attach(c);
		return true;
	}
	
	
	public Object getVariable(String name) {
		return variables.get(name);
	}
	
	public void setVariable(String name,Object value) {
		variables.put(name,value);
	}

	public void setViewProperty(String name,String value) {
		SmithersModel m = screen.getApplication().getModel();
		FsNode view  = m.getNode("/app/view/"+selector);
		if (view==null) {
			view = new FsNode("view",selector);
			m.putNode("/app",view);
		}
		view.setProperty(name,value);
	}
	
	public void setControllerProperty(String controllername,String name,String value) {
		SmithersModel m = screen.getApplication().getModel();
		FsNode controller  = m.getNode("/app/view/"+selector+"/controller/"+controllername);
		
		if (controller==null) {
			controller = new FsNode("controller",controllername);
			m.putNode("/app/view/"+selector,controller);
		}
		controller.setProperty(name,value);
	}
	
	public Html5Controller getController() {
		return controller;
	}
	
	
}
