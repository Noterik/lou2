package org.springfield.lou.screen;

public class BindThread extends Thread {
	private boolean running = false;
	private String type;
	private BindManager parent;
	
	public BindThread(String t,BindManager b) {
		super("bind thread "+t);
		type = t;
		parent = b;
		running = true;
		start();
	}
	
	public void run() {
		while (running) {
			try {	
				doWork();
				synchronized (this) {
					this.wait(10*1000);
				}
			} catch(Exception e) {
				//e.printStackTrace();
			}
		}
	}
	
	private void doWork() {
		parent.checkNormalQueue();
	}
	
	public void check() {
		synchronized (this) {
			this.notify();
		}
	}
}
