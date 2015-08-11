package org.springfield.lou.controllers;

import org.json.simple.JSONObject;
import org.springfield.fs.*;

public class FsNodeController extends Html5Controller {
	
	private String nodepath;
	private String fields;
	
	public FsNodeController() {
		
	}
	public void attach(String s) {
		selector = s;
		if (screen!=null) {
			FsNode node = getControllerNode(selector);
			if (node!=null) {
				nodepath = node.getProperty("nodepath");
				fields = node.getProperty("fields");
			}
			fillnode();
			model.observeNode(this,nodepath);
		}
	}
	
	public void nodeChanged(String url) {
		fillnode();
	}
	
	public void languageChanged() {
		fillnode();	
	}
	
	private void fillnode() {
		FsNode fsnode = Fs.getNode(nodepath);
		JSONObject data = fsnode.toJSONObject(screen.getLanguageCode(),fields);
		data.put("nodepath",nodepath);
		screen.get(selector).parsehtml(data);
	}
}
