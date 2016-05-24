package org.springfield.lou.application;

import java.lang.reflect.Method;

public class PathBindObject {
	public String method;
	public String screenid;
	public String selector;
	public Object obj;
	public Method methodcall;
	
	public PathBindObject(String m,String s,String t,Object o,Method mc) {
		this.method = m;
		this.screenid = s;
		this.selector= t;
		this.methodcall = mc;
		this.obj = o;
	}
}
