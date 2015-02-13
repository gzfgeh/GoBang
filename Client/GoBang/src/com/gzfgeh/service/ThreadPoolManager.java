package com.gzfgeh.service;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.util.Log;

public class ThreadPoolManager {
	/* ��ӡ ���� */
	private static final String TAG = "ThreadPoolManager";
	/* �̳߳صĴ�С*/
	private int poolSize;  
    private static final int MIN_POOL_SIZE = 1;  
    private static final int MAX_POOL_SIZE = 10;
    /* �̳߳� */
    private ExecutorService threadPool;
    /* ������� */
    private LinkedList<ThreadPoolTask> asyncTasks;
    /* ������ʽ */
    private int type;
    public static final int TYPE_FIFO = 0;
    public static final int TYPE_LIFO = 1;
    
    /* ��ѯ�߳� */
    private Thread queryPoolThread;
    /* ��ѯʱ�� */
    private static final int SLEEP_TIME = 100;
    
    /* ��ʼ������ */
    public ThreadPoolManager(int type, int poolSize){
    	this.type = (type == TYPE_FIFO) ? TYPE_FIFO : TYPE_LIFO;
    	
    	if (poolSize < MIN_POOL_SIZE) poolSize = MIN_POOL_SIZE;  
        if (poolSize > MAX_POOL_SIZE) poolSize = MAX_POOL_SIZE;  
        this.poolSize = poolSize;  
                  
        threadPool = Executors.newFixedThreadPool(this.poolSize);  
          
        asyncTasks = new LinkedList<ThreadPoolTask>();  
    }
    /* �������������������� */
    public void addAsyncTask(ThreadPoolTask task){
    	synchronized (asyncTasks) {
    		Log.i(TAG, "add task: ");
    		asyncTasks.addLast(task); 
		}
    }
    /* ���������������ȡ���� */
    private ThreadPoolTask getAsyncTask(){
    	synchronized (asyncTasks) {
    		if (asyncTasks.size() > 0){
    			ThreadPoolTask task = (this.type == TYPE_FIFO) ?   
                        asyncTasks.removeFirst() : asyncTasks.removeLast();  
                Log.i(TAG, "remove task: ");  
                return task;
    		}
    	}
    	return null;
    }
    /* �����̳߳���ѯ */
    public void start(){
    	if (queryPoolThread == null){
    		queryPoolThread = new Thread(new PoolRunnable());
    		queryPoolThread.start();
    	}
    }
    /* ������ѯ���ر��̳߳� */
    public void stop(){
    	queryPoolThread.interrupt();
    	queryPoolThread = null;
    }
    /* ʵ����ѯ��Runnable */
    private class PoolRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i(TAG, "��ʼ��ѯ");
			try {  
                while (!Thread.currentThread().isInterrupted()) {  
                    ThreadPoolTask task = getAsyncTask();  
                    if (task == null) {  
                        try {  
                            Thread.sleep(SLEEP_TIME);  
                        } catch (InterruptedException e) {  
                            Thread.currentThread().interrupt();  
                        }  
                        continue;  
                    }  
                    threadPool.execute(task);  
                }  
            } finally {  
                threadPool.shutdown();  
            }  
              
            Log.i(TAG, "������ѯ"); 
		}
    }
}
