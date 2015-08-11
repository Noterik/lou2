package org.springfield.lou.screen;

import java.util.ArrayList;
import java.util.Map;

public class ScreenGroup {
	private ArrayList<Screen>screens = new ArrayList<Screen>();
	
	public void add(Screen s) {
		screens.add(s);
	}
	
    public ArrayList<Screen> getScreens(){
    	return this.screens;
    }
    
    public boolean isMember(Screen s) {
    	if (screens.indexOf(s)!=-1) {
    		return true;
    	}
    	return false;
    }
    
}
