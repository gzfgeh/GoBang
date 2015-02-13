package com.gzfgeh.gobang;

import java.util.HashMap;

import com.gzfgeh.service.Event.SendLogin;
import com.gzfgeh.service.SocketService;

import de.greenrobot.event.EventBus;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private static final int REQUEST_CODE = 1;
	private Intent intent;
	private String result;
	
	private Intent socketService;
	private HashMap<String, Object> map;
	private com.gzfgeh.service.Event.LoginEvent loginEvent;
	
	private static LinearLayout layout;				//����״̬����
	private TextView netStatus;						//����״̬��ʾ
	private NetChangeBroadcastReceiver receiver;	//����״̬�ı�
	
	private EditText name;
	private EditText password;
	private TextView link;							//ע������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		name = (EditText) findViewById(R.id.username);
		name.setText("a");
		password = (EditText) findViewById(R.id.password);
		password.setText("b");
		
		link = (TextView) findViewById(R.id.register_link);
		link.setClickable(true);
		link.setFocusable(true);
		
		netStatus = (TextView) findViewById(R.id.handle_net);
		netStatus.setClickable(true);
		netStatus.setFocusable(true);
		
		layout = (LinearLayout) findViewById(R.id.net_status);
		
		//ע�������
		receiver = new NetChangeBroadcastReceiver();
		IntentFilter filter=new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver,filter);
        
        //ע��Socket Server
        socketService = new Intent(this,SocketService.class);
        this.bindService(socketService, conn, BIND_AUTO_CREATE);
        
        //ע���ɨ��EventBus
        EventBus.getDefault().register(this);
        map = new HashMap<String, Object>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class NetChangeBroadcastReceiver extends BroadcastReceiver {
	    private static final String TAG="TAG";
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	        //NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
	        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
	        NetworkInfo info = manager.getActiveNetworkInfo();
	        
	        if (info != null && info.isAvailable()) {
	        	String name = wifiInfo.getTypeName();  
	            Log.d(TAG, "--��ǰ�������ƣ�" + name + "--" + info);  
	            layout.setVisibility(View.GONE); 
	        } else {
	        	layout.setVisibility(View.VISIBLE);
	        	Toast.makeText(MainActivity.this, "��ǰû����������", Toast.LENGTH_LONG).show();
	            Log.d(TAG, "--û������");
	        }
	    }
	}
	
	public void onLink(View v){
		switch (v.getId()) {
		case R.id.register_link:
			//ע��ҳ��
			intent = new Intent(this,Register.class);
			startActivity(intent);
			overridePendingTransition(R.anim.in_alpha,R.anim.out_alpha);
			break;
			
		case R.id.handle_net:
			//��������
			if(android.os.Build.VERSION.SDK_INT > 10 ){
		        //3.0���ϴ����ý���
		        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
			}else{
		        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			}
			break;
			
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
		EventBus.getDefault().unregister(this);
		this.unbindService(conn);
	}
	
	public void LoaderClick(View v){
		String userName = name.getText().toString();
		String passWord = password.getText().toString();
		
		map.put("user_name", userName);
		map.put("password", passWord);
		loginEvent.message = map;
		EventBus.getDefault().post(loginEvent);
	}
	
	public void onEventBackgroundThread(SendLogin sendLogin){
		intent = new Intent(MainActivity.this,Loading.class);
		intent.putExtra("status", sendLogin.message);
		startActivityForResult(intent, REQUEST_CODE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 2){
			if (requestCode == REQUEST_CODE){
				result = data.getStringExtra("result");
				if ("fail".equals(result)){
					Toast.makeText(this, "�û��������������", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(this, "���µ�¼", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	private ServiceConnection conn = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.i(TAG,"�󶨳ɹ�");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.i(TAG,"�����");
		}
		
	};
	
}
