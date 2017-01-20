package com.example.grocerypickup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
			//Thread for displaying the splash Screen
			Thread splash_screen = new Thread()	{
				
				public void run()	{
					try{
						sleep(5000);
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						startActivity(new Intent(getApplicationContext(),Main.class));
						finish();
					}
				}
			};
			splash_screen.start();
	}
}