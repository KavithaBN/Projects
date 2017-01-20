package com.example.grocerypickup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateBooking extends Activity {

	private Spinner timeSlot;
	private Spinner source;
	private Spinner destination;
	private Spinner numberOfPeople;
	private String bookingId;    


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createbooking_activity);

		timeSlot = (Spinner) findViewById(R.id.spinner1);
		source = (Spinner) findViewById(R.id.spinner3);
		destination = (Spinner) findViewById(R.id.spinner4);
		numberOfPeople = (Spinner) findViewById(R.id.spinner5);

	}

	public void createBooking(View view)	{

		String timeSlot_string = String.valueOf(timeSlot.getSelectedItem());
		String source_string = String.valueOf(source.getSelectedItem());
		String destination_string = String.valueOf(destination.getSelectedItem());
		String numberOfPeople_string = String.valueOf(numberOfPeople.getSelectedItem());

		Random randomGenerator = new Random();
		int ibookingID = randomGenerator.nextInt(10000);
		StringBuilder sb = new StringBuilder();
		sb.append("");
		sb.append(ibookingID);
		String bookingId = sb.toString();

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();	
		
		String finalTimeSlot = timeSlot_string.substring(8, 10) + timeSlot_string.substring(11, 13);

		DateFormat sdf = new SimpleDateFormat("HH:mm");
		String currentTime = sdf.format(new Date());
		currentTime = currentTime.substring(0, 2) + currentTime.substring(3,5);

		if(Integer.valueOf(finalTimeSlot)<=Integer.valueOf(currentTime))	{
			Toast.makeText(getApplicationContext(), "Booking time cannot be less than current time", 
					Toast.LENGTH_SHORT).show();
		} 

		else if(source_string.equals(destination_string))	{
			Toast.makeText(getApplicationContext(), "Please select different Source and Destination", 
					Toast.LENGTH_SHORT).show();
		}	else	{

			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
			HttpResponse response;
			JSONObject json = new JSONObject();

			try {
				HttpPost post = new HttpPost("http://hitchhiker.mybluemix.net/rest/booking/createbooking");
				json.put("customerId",Main.getCustomerIdStr());
				json.put("source", source_string);
				json.put("destination",destination_string);
				json.put("timeSlot",timeSlot_string);
				json.put("numberOfPeople",numberOfPeople_string);
				json.put("bookingId", bookingId);
				json.put("date", dateFormat.format(date));

				StringEntity se = new StringEntity(json.toString(),HTTP.UTF_8);
				se.setContentType("application/json");
				System.out.println(se);
				post.setEntity(se);
				response = client.execute(post);
				if(response!=null){
					HttpEntity entity = response.getEntity();
					String content = EntityUtils.toString(entity);
					if(content.equalsIgnoreCase("true"))	{
						Toast.makeText(getApplicationContext(), "Success..", 
								Toast.LENGTH_SHORT).show();			
						startActivity(new Intent(getApplicationContext(),MyBooking.class));
						finish();
					}	else	{
						Toast.makeText(getApplicationContext(), "Failed..", 
								Toast.LENGTH_SHORT).show();
					}
				}	
			}	catch(Exception e)	{	

				e.printStackTrace();
			}
		}
	}
}