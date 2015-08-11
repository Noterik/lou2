package org.springfield.lou.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springfield.fs.FSList;
import org.springfield.fs.FSListManager;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.Html5Application;
import org.springfield.lou.application.Html5ApplicationInterface;
import org.springfield.lou.controllers.FsListController;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.homer.LazyHomer;
import org.springfield.marge.*;

public class SmithersModel {
	
	private Html5Application app;
	
	public SmithersModel(Html5Application a) {
		app = a;
		// create our master node in mojo
		FSListManager.put("/app"+app.getId().substring(7), new FSList());
		loadAppConfig();		
	}
	
	
	public boolean setProperty(String nodepath,String propertyname,String value) {
		Fs.setProperty(nodepath,propertyname,value);
		return true;
	}
	
	public void putNode(String uri,FsNode node) {
		if (uri.equals("/app")) {
			String listurl = "/app"+app.getId().substring(7);
			FSList list = FSListManager.get(listurl);
			if (list==null) {
				list = new FSList(listurl);
				FSListManager.put(listurl, list);
			}
			list.addNode(node);
		} else if (uri.startsWith("/app/")) {
			String listurl = "/app"+app.getId().substring(7)+uri.substring(4);
			FSList list = FSListManager.get(listurl);
			if (list==null) {
				list = new FSList(listurl);
				FSListManager.put(listurl, list);
			}
			list.addNode(node);
			
		}
	}
	
	public FSList getControllerList(String selector) {
		String listurl = "/app"+app.getId().substring(7)+"/view/"+selector;
		FSList list = FSListManager.get(listurl);
		return list;
	}
	
	
	public FsNode getNode(String uri) {
		if (uri.startsWith("/domain/")) {
			
		} else if (uri.startsWith("/app/")) {
			// memory app
			String listurl = "/app"+app.getId().substring(7)+uri.substring(4);
			int pos = listurl.lastIndexOf("/");
			String id = listurl.substring(pos+1);
			listurl = listurl.substring(0, pos);
			pos = listurl.lastIndexOf("/");
			String name = listurl.substring(pos+1);
			listurl = listurl.substring(0, pos);
			FSList list = FSListManager.get(listurl);
			if (list!=null) {
				for(Iterator<FsNode> iter = list.getNodes().iterator() ; iter.hasNext(); ) {
					FsNode n = (FsNode)iter.next();	
					if (n.getId().equals(id) && n.getName().equals(name)) {
						return n;
					}
				}
			}
		}
		return null;
	}
	
	public void observeNode(MargeObserver o,String url) {
		Marge.addObserver(url, o);
	}
	
	public void observeTree(MargeObserver o,String url) {
		Marge.addObserver(url+"*", o);
	}
	
	public void loadAppConfig() {
		// lets find out what is the active version for this app
		String basepath = "/springfield/tomcat/webapps/ROOT/eddie/";
		if (LazyHomer.isWindows()) basepath = "C:\\springfield\\tomcat\\webapps\\ROOT\\eddie\\";
		String part = app.getAppname().substring(app.getAppname().lastIndexOf("/")+1);
		String filename = basepath+File.separator+"apps"+File.separator+part+File.separator+"components"+File.separator+"app.xml";
		File file = new File(filename);
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(filename));
				StringBuffer str = new StringBuffer();
				String line = br.readLine();
				while (line != null) {
					str.append(line);
					str.append("\n");
					line = br.readLine();
				}
				br.close();
				
				String body = str.toString();
				Document result = DocumentHelper.parseText(body);
				for(Iterator<Node> iter = result.getRootElement().nodeIterator(); iter.hasNext(); ) {
					
					Node child = (Node)iter.next();
					//System.out.println("C="+child.getName());
					if (child.getName()!=null && child.getName().equals("view")) {
						Element view = (Element)child;
						String viewid = view.attributeValue("id");	
						FsNode viewnode = new FsNode("view",viewid);
						putNode("/app",viewnode);
						
						for(Iterator<Node> iter2 = view.nodeIterator(); iter2.hasNext();) {
							Node child2 = (Node)iter2.next();
							String id = child2.getName();
							if(id!=null) {
								if (id.equals("controller")) {
									Element controller = (Element)child2;
									String controllerid = controller.attributeValue("id");	
									FsNode controllernode = new FsNode("controller",controllerid);
									putNode("/app/view/"+viewid,controllernode);
									for(Iterator<Node> iter3 = controller.nodeIterator(); iter3.hasNext();) {
										Node child3 = (Node)iter3.next();
										String id2 = child3.getName();
										if (id2!=null) {
											if (id2.equals("model")) {
												
											} else {
												controllernode.setProperty(id2,child3.getText());
											}
										}
									}
								} else {
									viewnode.setProperty(id,child2.getText());
								}
							}
						}
	
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
