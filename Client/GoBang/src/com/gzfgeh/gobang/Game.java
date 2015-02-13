package com.gzfgeh.gobang;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class Game extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		Toast.makeText(this, "µÇÂ¼³É¹¦", Toast.LENGTH_SHORT).show();
	}
}
