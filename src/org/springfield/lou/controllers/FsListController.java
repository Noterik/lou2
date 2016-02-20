package org.springfield.lou.controllers;

import java.lang.reflect.Method;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springfield.fs.*;
import org.springfield.lou.screen.Screen;

public class FsListController extends Html5Controller {
	
	private String nodepath;
	private String fields;
	private String template;
	private String actionmenu;
	private String lastitem;
	private Object filterObject;
	private String filterMethod;
	
	public FsListController() {
	}
	
	public FsListController(String n) {
		nodepath = n;
	}
	
	public void attach(String s) {
		selector = s;
		if (screen!=null) {
			FsNode node = getControllerNode(selector);
			if (node!=null) {
				nodepath = node.getProperty("nodepath");
				fields = node.getProperty("fields");
				template = node.getProperty("template");
				actionmenu = node.getProperty("actionmenu");
				model.observeTree(this,nodepath);
				screen.get(selector).loadScript(this);
				screen.get(selector).template(template);
				screen.get(selector).syncvars("controller/mouseovercss");
				fillList();
				bindOverride("itemselected");
			}
		}
	}
	
	public void treeChanged(String url) {
		fillList();
	}
	
	public void languageChanged() {
		fillList();	
	}
	
	private void fillList() {
		//FSList fslist = FSListManager.get(nodepath,false);
		FSList fslist = getList(nodepath);
		JSONObject data = fslist.toJSONObject(screen.getLanguageCode(),fields);
		data.put("nodepath",nodepath);
		data.put("size", fslist.size());
		data.put("targetid",selector.substring(1));
		//screen.get(selector).parsehtml(data);  // old way if you don't use update in js
		screen.bind(selector,"client","itemselected",this);
		//screen.bind(selector,"client","itemadd",this);	
		screen.get(selector).update(data);
	}
	
	public FSList getList(String nodepath) {
		FSList fslist = FSListManager.get(nodepath,false);
		if (filterObject!=null) {
			try {
				Method method = filterObject.getClass().getMethod(filterMethod,FSList.class);
				if (method!=null) {
					Object r = method.invoke(filterObject,fslist);
					fslist = (FSList)r;
				} else {
					System.out.println("MISSING METHOD IN FILTER CALL ="+method);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return fslist;
	}
	
	public void addFilter(Object caller,String method) {
		filterObject = caller;
		filterMethod = method;
	}
	
    public void itemadd(Screen s,JSONObject data) {
    	System.out.println("ITEM ADD PRESSED");
		sendEvent(data);
    }
	
    public void itemselected(Screen s,JSONObject data) {
    	String type = (String)data.get("eventtype"); // ugly hack needs to be fixed
    	if (type.equals("itemadd")) {
    		itemadd(s,data);
    		return;
    	}
		if (actionmenu!=null && actionmenu.equals("true")) {
			
			try {
				Double dd = Double.parseDouble((String)data.get("itemid"));
				lastitem = (String)data.get("itemid");
			} catch(Exception e) {
				return;
			}
		
	
			// since we can't have app.xml classes yet we need to 'copy' some vars
			
			screen.removeContent(selector.substring(1)+"_"+lastitem+"_actionmenu");
			
			FsNode cnode = model.getNode("/app/view/"+selector+"_actionmenu/controller/FsActionMenuController");
			screen.get(selector+"_"+lastitem+"_actionmenu").setControllerProperty("FsActionMenuController","nodepath", cnode.getProperty("nodepath"));
			screen.get(selector+"_"+lastitem+"_actionmenu").setControllerProperty("FsActionMenuController","mouseovercss", cnode.getProperty("mouseovercss"));

			screen.get(selector+"_"+lastitem).append("div class=\"actionmenu\"",selector.substring(1)+"_"+lastitem+"_actionmenu",new FsActionMenuController());
	       	//screen.get(selector+"_"+lastitem+"_actionmenu").attach(new FsActionMenuController()); 
	       	screen.get(selector+"_"+lastitem+"_actionmenu").show();
	       	screen.bind(selector+"_"+lastitem+"_actionmenu","actionselected","actionselected", this);
		} else {
			sendEvent(data);
		}
    }
    
    public void actionselected(Screen s,JSONObject data) {
    	screen.get(selector+"_"+lastitem+"_actionmenu").hide();
		screen.removeContent(selector.substring(1)+"_"+lastitem+"_actionmenu"); // weird !
    	// we need to rewire the event to be able to send the item id from the action menu
    	String action = (String)data.get("itemid");
    	data.put("itemid", lastitem);
    	data.put("action", action);
		sendEvent(data);
    }
    
	 
}
