package com.gzfgeh.service;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class MyApplication extends Application {
	private static final String TAG = "MyApplication";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG,"MyApplication start");
		Intent socketService = new Intent(this,SocketService.class);
		startService(socketService);
		Log.i(TAG,"MyApplication is running");
	}
	
}
