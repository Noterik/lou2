package org.springfield.lou.controllers;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.model.SmithersModel;
import org.springfield.lou.screen.Screen;

public class PropertyController extends Html5Controller {

	private String nodepath;
	private String field;
	
	public PropertyController() {
	}
	
	public PropertyController(String n,String p) {
		nodepath = n;
		field = p;
	}
	
	public void attach(String s,String eventtype) {
			if (screen!=null) {
				selector = s;
				screen.setDiv(selector.substring(1),"bind:"+eventtype,"setProperty",this); // still needs to be changed lowlevel
				model.observeNode(this,nodepath);
			}
			
	}
	
	public void attach(String s) {
		selector = s;
		FsNode node = getControllerNode(selector);
		if (node!=null) {
			String eventtype = node.getProperty("on");
			nodepath = node.getProperty("nodepath");
			field = node.getProperty("field");
			// set the value first time
			screen.bind(selector,eventtype,"setProperty",this);
			model.observeNode(this,nodepath);
		}
	}
	
	public void nodeChanged(FsNode node) {
		String value = node.getProperty(field);
		if (screen!=null) {
			screen.get(selector).val(value);
		}
		
	}
	
    public void setProperty(Screen s,JSONObject data) {
    	if (selector!=null) { // protect against no attach done
    		String value = (String)data.get(selector.substring(1)+".value");
    		model.setProperty(nodepath,field,value);
    	}
    }
	
}
