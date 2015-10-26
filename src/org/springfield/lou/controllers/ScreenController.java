package org.springfield.lou.controllers;

import org.json.simple.JSONObject;
import org.springfield.lou.homer.LazyHomer;



public class ScreenController extends Html5Controller {
	
	
	public void attach(String s) {
		selector = s;
		loadHtml();
	}
	
	private void loadHtml() {
		JSONObject data = new JSONObject();	
		data.put("language",screen.getLanguageCode());
		data.put("id",screen.getId());
		data.put("hostname",LazyHomer.getExternalIpNumber());
		if (screen.getProperty("jumper")!=null) data.put("jumper",screen.getProperty("jumper"));
		data.put("username",screen.getProperty("username"));
		data.put("screengroups",screen.getGroups().toString());
		String sg="";
		for (String s: screen.getGroups()) {  
			if (!sg.equals("")) {
				sg+=","+s;
			} else {
				sg = s;
			}
		}
		data.put("screengroups",sg);
		screen.get(selector).parsehtml(data);
	}
}
