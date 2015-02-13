package com.gzfgeh.gobang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Loading extends Activity {
	private final static int RESULT_CODE = 2;
	private String getStatus;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		
		try {
			Thread.sleep(1000);
			
			intent = getIntent();
			String status = intent.getStringExtra("status");
			if ("OK".equals(status)){
				intent = new Intent(Loading.this,Game.class);
				startActivity(intent);
			}else{
				intent = new Intent();
				intent.putExtra("result", "fail");
				setResult(RESULT_CODE, intent);
				finish();
			}
			overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void cancleClick(View v){
		intent = new Intent();
		intent.putExtra("result", "cancle");
		setResult(RESULT_CODE, intent);
		finish();
	}
}
