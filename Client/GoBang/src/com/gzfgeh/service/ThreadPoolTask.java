package com.gzfgeh.service;

public abstract class ThreadPoolTask implements Runnable{
	
	public ThreadPoolTask() {
		super();
	}

	public abstract void run(); 
}
