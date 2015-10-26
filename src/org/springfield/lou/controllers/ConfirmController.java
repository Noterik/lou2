package org.springfield.lou.controllers;

import org.json.simple.JSONObject;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;

public class ConfirmController extends Html5Controller {
	
	String selector;
	
	public ConfirmController() {
	}
	
	public void attach(String s) {
		selector = s;
		FsNode node = getControllerNode(selector);
		if (node!=null) {
			//String template = node.getProperty("template");
			//screen.get(selector).template(template);
			JSONObject data = node.toJSONObject("en","question,answer1,answer2");
			screen.get(selector).parsehtml(data);
			screen.get(selector).show();
		}
	}
	

}
