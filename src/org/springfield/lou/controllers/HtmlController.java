package org.springfield.lou.controllers;

import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;

public class HtmlController extends Html5Controller {
	
	private String nodepath;
	private String field;
	
	public HtmlController() {
	}
	
	public HtmlController(String n,String p) {
		nodepath = n;
		field = p;
	}
	
	public void attach(String s) {
		selector = s;
		FsNode node = getControllerNode(selector);
		if (node!=null) {
			nodepath = node.getProperty("nodepath");	
			field = node.getProperty("field");
			// set the value first time
			FsNode dnode = Fs.getNode(nodepath);
			if (dnode!=null) setValue(dnode);
			model.observeNode(this,nodepath);
		}
	}
	
	public void nodeChanged(FsNode node) {
		setValue(node);
	}
	
	public void languageChanged() {
		FsNode node = Fs.getNode(nodepath);
		if (node!=null) setValue(node);
	}
	
	public void setValue(FsNode node) {
		if (screen!=null) {
			// is there a language set in the screen?
			String l = screen.getLanguageCode();
			if (l==null) {
				String value = node.getProperty(field);
				screen.get(selector).html(value);
			} else {
				String value = node.getProperty(l+"_"+field);
				if (value!=null) {
					screen.get(selector).html(value);
				} else {
					value = node.getProperty(field);
					screen.get(selector).html(value);
				}
			}
		}
		
	}

	
	
}
