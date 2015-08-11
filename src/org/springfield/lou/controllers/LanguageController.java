package org.springfield.lou.controllers;

import org.json.simple.JSONObject;
import org.springfield.fs.FsNode;
import org.springfield.lou.screen.Screen;

public class LanguageController extends Html5Controller {

		private String langcode;
		
		public LanguageController() {
		}
	
		public LanguageController(String lc) {
			langcode = lc;
		}
		
		public void attach(String s,String eventtype) {
			selector = s;
			if (screen!=null) {
				screen.bind(selector,eventtype,"pressed",this);
			}
		}
		
		public void attach(String selector) {
			FsNode node = getControllerNode(selector);
			if (node!=null) {
				String eventtype = node.getProperty("on");
				langcode = node.getProperty("language");
				screen.bind(selector,eventtype,"pressed",this);
				
			}
		}
		
	    public void pressed(Screen s,JSONObject data) {
	    	screen.setLanguageCode(langcode);
	    }

			
}
