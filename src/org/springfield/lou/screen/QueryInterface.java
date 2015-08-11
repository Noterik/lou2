package org.springfield.lou.screen;


public interface QueryInterface {
	public interface QueryMultiInterface {
		public Html5Element getElement(String selector);
		public boolean css(String elementname,String value);
		public boolean css(String[] list);
		public boolean on(String eventtype,String callbackmethod);
		public boolean on(String eventtype,Object callbackobject,String callbackmethod);
	}

}
