package com.gzfgeh.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ReadFromServer implements Runnable{
	private Socket s;
	private Handler handler;
	private BufferedReader br;
	private static final String SOCKET2 = "socket";
	
	public ReadFromServer(Socket s, Handler handler) throws Exception{
		super();
		this.s = s;
		this.handler = handler;
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String content = null;
		try{
			while ((content = br.readLine()) != null){
				Log.i(SOCKET2, content);
				Message msg = new Message();
				msg.what = 0x8866;
				msg.obj = content;
				handler.sendMessage(msg);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
