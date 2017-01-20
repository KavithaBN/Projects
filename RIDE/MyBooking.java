package com.example.grocerypickup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MyBooking extends Activity{

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybookings_activity);
	}
	
	public void createBooking(View view)	{
		
		startActivity(new Intent(getApplicationContext(),CreateBooking.class));
		
	}
	
	public void getBooking(View view)	{
		
		startActivity(new Intent(getApplicationContext(),AddToExistingBooking.class));
		
	}
	
	public void delBooking(View view)	{
		
		startActivity(new Intent(getApplicationContext(),DeleteBooking.class));
		
	}
}
