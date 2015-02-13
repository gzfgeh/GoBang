package com.gzfgeh.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SocketService extends Service {
	private static final String TAG = "SocketService";
	
	public static final String HOST = "10.0.0.88";//"10.0.0.88"
	public static final int PORT = 8989;
	
	private int mode = 0;
	private static final int REGISTER_MODE = 1;
	private static final int LOGIN_MODE = 2;
	private static final int COMMAND_MODE = 3;
	
	private static Socket socket = null;
	private boolean threadFlag = false;
	private ReadThread readThread;
	private WriteThread writeThread;
	
	private Event.SendRegister sendRegister;
	private Event.SendLogin sendLogin;
	private Event.SendCommand sendCommand;
	private HashMap<String, Object> message;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i(TAG, "注册开始");
		super.onCreate();
		EventBus.getDefault().register(this);
		initSocket();
		Log.i(TAG, "注册成功");
	}

	private void initSocket() {
		// TODO Auto-generated method stub
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (socket == null){
					try {
						Log.i(TAG, "initSocket成功");
						socket = new Socket(HOST,PORT);
						threadFlag = true;
						readThread = new ReadThread(socket);
						readThread.start();
						writeThread = new WriteThread(socket);
						writeThread.start();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	public void onEventBackgroundThread(Event.RegisterEvent registerEvent){
		mode = REGISTER_MODE;
		message = registerEvent.message;
	}
	
	public void onEventBackgroundThread(Event.LoginEvent loginEvent){
		mode = LOGIN_MODE;
		message = loginEvent.message;
	}

	public void onEventBackgroundThread(Event.CommandEvent commandEvent){
		mode = COMMAND_MODE;
		message = commandEvent.message;
	}
	
	public class ReadThread extends Thread{
		private Socket so;
		
		public ReadThread(Socket so) {
			super();
			this.so = so;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (threadFlag){
				if (null != so){
					try {
						InputStream in = so.getInputStream();
						byte[] buffer = new byte[1024];
						int length = -1;
						while (!so.isClosed() && !so.isInputShutdown() && ((length = in.read(buffer)) != -1)){
							if (length > 0){
								switch (mode){
								case REGISTER_MODE:
									sendRegister = new Event.SendRegister();
									sendRegister.message = OperationData.getData(buffer);
									EventBus.getDefault().post(sendRegister);
									mode = 0;
									break;
								case LOGIN_MODE:
									sendLogin = new Event.SendLogin();
									sendLogin.message = OperationData.getData(buffer);
									EventBus.getDefault().post(sendLogin);
									mode = 0;
									break;
								case COMMAND_MODE:
									sendCommand = new Event.SendCommand();
									sendCommand.message = OperationData.getData(buffer);
									EventBus.getDefault().post(sendCommand);
									mode = 0;
									break;
								default:
									sendCommand = new Event.SendCommand();
									sendCommand.message = OperationData.getData(buffer);
									EventBus.getDefault().post(sendCommand);
									mode = 0;
									break;
								}
							}
						}
						Thread.sleep(500);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
	}
	
	
	public class WriteThread extends Thread{
		private Socket so;

		public WriteThread(Socket so) {
			super();
			this.so = so;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(threadFlag){
				try {
					if (mode != 0){
						OutputStream os = so.getOutputStream();
						switch (mode) {
						case REGISTER_MODE:
							os.write(OperationData.sendData("Register", message));
							break;
						case LOGIN_MODE:
							os.write(OperationData.sendData("Login", message));
							break;
						case COMMAND_MODE:
							os.write(OperationData.sendData("Command", message));
							break;
						default:
							break;
						}
					}
					Thread.sleep(500);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}
	
	
}
