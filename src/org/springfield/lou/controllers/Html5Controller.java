package org.springfield.lou.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springfield.marge.*;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.model.SmithersModel;
import org.springfield.lou.screen.Screen;

public class Html5Controller implements MargeObserver {
	
	public Screen screen = null; // online filled if its a single view
	public String selector;
	public String defaultscript = null;
	public String defaulttemplate = null;
	public SmithersModel model;
	
	private  ArrayList<String> overridelist = new ArrayList<String>();
	
	public Html5Controller() {
		
	}
	
	public String getScreenId() {
		return screen.getId();
	}
	
	public String getSelector() {
		return selector;	
	}
	
	public void setModel(SmithersModel m) {
		model = m;
	}
	
	public void setScreen(Screen s) {
		screen = s;
		screen.observerController(this);
	}
	
	public void attach(String selector,String eventtype) {
		// should be overridden to be useful
	}
	
	public void attach(String selector) {
		// should be overridden to be useful
	}
	
	public void remoteSignal(String from,String method,String url) {
		FsNode n = Fs.getNode(url);
		if (n!=null) {
			nodeChanged(n);
		} else {
			treeChanged(url);
		}
	}
	
	public String getDefaultScript() {
		if (defaultscript!=null) return defaultscript;
		return "/springfield/tomcat/webapps/lou/eddie/components/default/"+getControllerName()+".js";
	}
	
	public String getDefaultTemplate() {
		if (defaulttemplate!=null) return defaulttemplate;
		return "/springfield/tomcat/webapps/lou/eddie/components/default/"+getControllerName()+".mst";
	}
	
	public void setDefaultScript(String ds) {
		defaultscript = "/springfield/tomcat/webapps/lou/eddie/components/default/"+ds;
	}
	
	public void setDefaultTemplate(String dt) {
		defaultscript = "/springfield/tomcat/webapps/lou/eddie/components/default/"+dt;
	}
	
	public void nodeChanged(FsNode node) {
		
	}
	
	public void treeChanged(String url) {
		
	}
	
	public void languageChanged() {
		
	}
	
	public void destroyed() {
		// needs to signal marge to remove any observers
		Marge.removeObserver(this);
	}
	
	public FsNode getViewNode(String selector) {
		FsNode node = model.getNode("/app/view/"+selector);
		return node;
	}
	
	public String getControllerName() {
		String cname = this.getClass().getName();
		cname = cname.substring(cname.lastIndexOf(".")+1);
		return cname;
	}
	
	public FsNode getControllerNode(String selector) {
		//String cname = this.getClass().getName();
		//cname = cname.substring(cname.lastIndexOf(".")+1);
		FsNode node = model.getNode("/app/view/"+selector+"/controller/"+getControllerName());
		return node;
	}
	
	public void bind(String eventtype,String methodname,Object callbackobject) {
		screen.bind(selector+"/controller/"+getControllerName(),eventtype,methodname, callbackobject);
	}
	
	 public void update(JSONObject data) {
	 }
	
	 public void sendEvent(JSONObject data) {
		// String lookup = selector.substring(1)+"/controller/"+getControllerName()+"/"+data.get("eventtype");
		String lookup = selector.substring(1)+"/"+data.get("eventtype");
		 screen.event("controller", lookup, data);
	 }
	 
	 public void bindOverride(String eventtype) {
		 overridelist.add(eventtype);
		screen.bindOverride(selector,overridelist);
	 }
	
}
