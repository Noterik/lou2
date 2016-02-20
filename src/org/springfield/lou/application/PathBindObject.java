package org.springfield.lou.application;

public class PathBindObject {
	public String method;
	public String screenid;
	public String selector;
	
	public PathBindObject(String m,String s,String t) {
		this.method = m;
		this.screenid = s;
		this.selector= t;
	}
}
