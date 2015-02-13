package com.gzfgeh.gobang;

import java.io.OutputStream;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.gzfgeh.service.ReadFromServer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {
	private EditText emailAddr;
	private EditText userName;
	private EditText password;
	private static final String HOST = "10.0.0.88";
	private static final int PORT = 8989;
	private static final String SOCKET = "socket";
	private Socket socket;
    private OutputStream os;
    public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 0x8866){
				String data = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(data);
					String status = json.getString("status");
					if ("OK".equals(status)){
						Toast.makeText(Register.this, "注册OK", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(Register.this,MainActivity.class);
						startActivity(intent);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
    	
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		emailAddr = (EditText) findViewById(R.id.email_addr);
		userName = (EditText) findViewById(R.id.register_user_name);
		password = (EditText) findViewById(R.id.register_password);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					socket = new Socket(HOST,PORT);
					new Thread(new ReadFromServer(socket, handler)).start();
					os = socket.getOutputStream();
					//out = new PrintWriter(socket.getOutputStream());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void onRegister(View v) throws Exception{
		JSONObject clientKey = new JSONObject();
		String email = emailAddr.getText().toString();
		String name = userName.getText().toString();
		String key = password.getText().toString();
		
		if ("".equals(email) || "".equals(name) || "".equals(key)){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("警告");
			builder.setMessage("用户名、密码、邮箱不能为空");
			builder.setPositiveButton("确定", null);
			builder.show();
		}else{
			clientKey.put("email_addr", email);
			clientKey.put("user_name", name);
			clientKey.put("password", key);
			String content = String.valueOf(clientKey);
			
			JSONObject registerKey = new JSONObject();
			registerKey.put("register", content);
			String value = String.valueOf(registerKey);
			
			os.write((value + "\r\n").getBytes("UTF-8"));
	        Log.i(SOCKET, content);
		}
	}
	
}
