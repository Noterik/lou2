/* 
* ScreenManager.java
* 
* Copyright (c) 2012 Noterik B.V.
* 
* This file is part of Lou, related to the Noterik Springfield project.
*
* Lou is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Lou is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Lou.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.springfield.lou.screen;

import java.util.*;

/**
 * ScreenManager
 * 
 * @author Daniel Ockeloen
 * @copyright Copyright: Noterik B.V. 2012
 * @package org.springfield.lou.screen
 *
 */
public class ScreenManager {
	// we also keep track of all the instances so we can do a cascading delete on all managers if needed of screens
	private static ArrayList<ScreenManager>managers = new ArrayList<ScreenManager>();
	private Map<String, Screen> openscreens;
	private Map<String, ScreenGroup> screengroups;
	
    public ScreenManager() {
    	openscreens = new HashMap<String, Screen>();
    	screengroups = new HashMap<String, ScreenGroup>();
    	// add manager to instances we might need them for global actions
    	managers.add(this);	
    }
    
    public void put(Screen s) {
    	openscreens.put(s.getId(),s);
    }
    
    public static void globalremove(String id) {
		for (int i=0;i<managers.size();i++) {
			ScreenManager m = managers.get(i);
			m.remove(id);
		}    	
    }
    
    public void remove(String id){
    	Screen s = openscreens.get(id);
    	if (s!=null) s.destroyed();
    	openscreens.remove(id);

    }
    
    public Screen get(String screenid) {
    	return this.openscreens.get(screenid);
    }
    
    public ScreenGroup getScreenGroup(String name) {
    	return screengroups.get(name);
    }
    
    public void setScreenGroup(String name,ScreenGroup sg) {
    	screengroups.put(name,sg);
    }
    
	public String[] getGroups(Screen s) {
		List<String> list = new ArrayList<String>();
		
		Iterator<String> it = screengroups.keySet().iterator();
		while(it.hasNext()){
			String grname = it.next();
			ScreenGroup sg = (ScreenGroup)screengroups.get(grname);
			if (sg.isMember(s)) {
				list.add(grname);
			}
		}
		return  list.toArray(new String[list.size()]);
	}
    
    public Map<String, Screen> getScreens(){
    	return this.openscreens;
    }
    
    public int size(){
    	return this.openscreens.size();
    }
    
    public boolean hasRole(String role) {
		Set<String> keys = openscreens.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			String next = it.next();
			Screen screen = get(next);
			if (screen.getRole().equals(role)) {
				// found one
				return true;
			}
		}
    	return false;
    }
    
}
