package org.springfield.lou.controllers;

import org.json.simple.JSONObject;
import org.springfield.fs.FsNode;
import org.springfield.lou.screen.Screen;

public class ShowController extends Html5Controller {

	String target; // element we are asked to show
	
	public ShowController() {
	}
	
	public ShowController(String t) {
		target = t;
	}
	
	public void attach(String s,String eventtype) {
		selector = s;
		if (screen!=null) {
			screen.bind(selector,eventtype,"pressed",this);
		}
	}
	
	public void attach(String s) {
		selector = s;
		FsNode node = getControllerNode(selector);
		if (node!=null) {
			target = node.getProperty("targetid");
			String eventtype = node.getProperty("on");
			screen.bind(selector,eventtype,"pressed",this);
		}
	}
	
    public void pressed(Screen s,JSONObject data) {
    	screen.get(target).show();
    }
    
}
