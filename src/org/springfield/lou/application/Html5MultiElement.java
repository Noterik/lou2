package org.springfield.lou.application;

import java.util.Iterator;

import org.springfield.lou.screen.Screen;
import org.springfield.lou.screen.ScreenGroup;

public class Html5MultiElement {
	
	private String selector;
	private ScreenGroup screengroup = null;
	private Html5Application app;
	
	public Html5MultiElement(Html5Application a,String groupname,String s) {
		app = a;
		selector = s;
		screengroup = app.getScreenManager().getScreenGroup(groupname);
	}
	
	public Html5MultiElement(Html5Application a,String s) {
		app = a;
		selector = s;
	}
	
	public boolean css(String elementname,String value) {
		if (screengroup==null) { // ok its for all the screens
			Iterator<String> it = app.getScreenManager().getScreens().keySet().iterator();
			while(it.hasNext()){
				String next = (String) it.next();
				Screen s = app.getScreenManager().get(next);
				//System.out.println("SET MULTI CSS");
				s.setDiv(selector.substring(1),"style:"+elementname+":"+value);
			}
		} else {
			Iterator<Screen> it = screengroup.getScreens().iterator();
			while(it.hasNext()){
				Screen s = (Screen) it.next();
				//System.out.println("SET GR MULTI CSS");
				s.setDiv(selector.substring(1),"style:"+elementname+":"+value);
			}
		}
		return true;
	}
	
	public boolean hide() {
		return true;
	}
	
}
