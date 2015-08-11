package org.springfield.lou.controllers;

import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.FSListManager;
import org.springfield.fs.FsNode;
import org.springfield.lou.screen.Screen;

public class FsActionMenuController extends Html5Controller {
	
	private String nodepath;
	private String fields;
	private String template;

	public void attach(String s) {
		selector = s;
		if (screen!=null) {
			FsNode node = getControllerNode(selector);
			if (node!=null) {
				nodepath = node.getProperty("nodepath");
				template = node.getProperty("template");
				
				model.observeTree(this,nodepath);
				screen.get(selector).loadScript(this);
				screen.get(selector).syncvars("controller/mouseovercss");
				screen.get(selector).template(template);
				fillMenu();
				bindOverride("itemselected");
			}
		}
	}
	
	private void fillMenu() {
		FSList fslist = FSListManager.get(nodepath,false);
		JSONObject data = fslist.toJSONObject(screen.getLanguageCode(),"name,action");
		data.put("nodepath",nodepath);
		data.put("size", fslist.size());
		data.put("targetid",selector.substring(1));
		screen.bind(selector,"client","actionselected",this);
		screen.get(selector).update(data);
	}
	
    public void actionselected(Screen s,JSONObject data) {
		sendEvent(data);
    }
}
