package org.springfield.lou.controllers;

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
				System.out.println("FIELDS="+fields);
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
		FSList fslist = FSListManager.get(nodepath,false);
		JSONObject data = fslist.toJSONObject(screen.getLanguageCode(),fields);
		data.put("nodepath",nodepath);
		data.put("size", fslist.size());
		data.put("targetid",selector.substring(1));
		//screen.get(selector).parsehtml(data);  // old way if you don't use update in js
		screen.bind(selector,"client","itemselected",this);
		System.out.println("PATH="+nodepath+" DATA="+data.toJSONString());
		screen.get(selector).update(data);
	}
	
    public void itemselected(Screen s,JSONObject data) {
		if (actionmenu!=null && actionmenu.equals("true")) {
			lastitem = (String)data.get("itemid");
	       	screen.get(selector+"_actionmenu").attach(new FsActionMenuController()); 
	       	screen.get(selector+"_actionmenu").show();
	       	screen.bind(selector+"_actionmenu","actionselected","actionselected", this);
		} else {
			//System.out.println("LISTEVENT="+this+" "+data.get("itemid")+" ET="+data.get("eventtype"));
			sendEvent(data);
		}
    }
    
    public void actionselected(Screen s,JSONObject data) {
    	screen.get(selector+"_actionmenu").hide();
    	// we need to rewire the event to be able to send the item id from the action menu
    	String action = (String)data.get("itemid");
    	data.put("itemid", lastitem);
    	data.put("action", action);
		sendEvent(data);
    }
    
	 
}
