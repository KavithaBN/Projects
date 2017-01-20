package com.example.grocerypickup;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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
import android.widget.TextView;
import android.widget.Toast;

public class SingleContactActivity  extends Activity {

	// JSON node keys
	private static final String TAG_TIMESLOT = "timeSlot";
	private static final String TAG_CUSTOMERID = "customerId";
	private static final String TAG_SOURCE = "source";
	private static final String TAG_DESTINATION = "destination";
	private static final String TAG_NUMOFPEOPLE = "numberOfPeople";
	private static final String TAG_BOOKINGID = "bookingId";
	

	private Spinner numOfPeople;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_contact);

		numOfPeople = (Spinner) findViewById(R.id.spinner5);

		// getting intent data
		Intent in = getIntent();

		// Get JSON values from previous intent
		String timestamp = in.getStringExtra(TAG_TIMESLOT);
		String customerid = in.getStringExtra(TAG_CUSTOMERID);
		String source = in.getStringExtra(TAG_SOURCE);
		String destination = in.getStringExtra(TAG_DESTINATION);
		String numberOfPeople = in.getStringExtra(TAG_NUMOFPEOPLE);
		String bookingid = in.getStringExtra(TAG_BOOKINGID);

		// Displaying all values on the screen
		TextView lbltimestamp = (TextView) findViewById(R.id.timestamp_label);
		TextView lblsource = (TextView) findViewById(R.id.source_label);
		TextView lbldestination = (TextView) findViewById(R.id.destination_label);
		TextView lblcustomerid = (TextView) findViewById(R.id.customer_label);
		TextView lblnumberofpeople = (TextView) findViewById(R.id.numberofpeople_label);
		TextView lblbookingid = (TextView) findViewById(R.id.bookingid_label);

		lbltimestamp.setText(timestamp);
		lblsource.setText(source);
		lbldestination.setText(destination);
		lblcustomerid.setText(customerid);
		lblnumberofpeople.setText(numberOfPeople);
		lblbookingid.setText(bookingid);

	}

	public void addExisting(View view)	{

		String numberOfPeople_string = String.valueOf(numOfPeople.getSelectedItem());
		TextView lblnumberofpeople = (TextView) findViewById(R.id.numberofpeople_label);
		TextView lblbookingid = (TextView) findViewById(R.id.bookingid_label);

		if(Integer.valueOf(numberOfPeople_string) + Integer.valueOf(lblnumberofpeople.getText().toString().substring(17, 18)) <= 4)	{

			int countPeople = Integer.valueOf(numberOfPeople_string) + Integer.valueOf(lblnumberofpeople.getText().toString().substring(17, 18));
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
			HttpResponse response;
			JSONObject json = new JSONObject();

			try {
				HttpPut put = new HttpPut("http://hitchhiker.mybluemix.net/rest/booking/addexisting/" + lblbookingid.getText().toString().substring(10,lblbookingid.getText().toString().length()));
				json.put("customerId", Main.getCustomerIdStr());
				json.put("bookingId", lblbookingid.getText().toString());
				json.put("numberOfPeople",numberOfPeople_string);

				StringEntity se = new StringEntity(json.toString(),HTTP.UTF_8);
				se.setContentType("application/json");
				System.out.println(se);
				put.setEntity(se);
				response = client.execute(put);
				if(response!=null){
					HttpEntity entity = response.getEntity();
					String content = EntityUtils.toString(entity);
					if(Integer.valueOf(content)<=100)	{
						Toast.makeText(getApplicationContext(), "Success.."+ "Probability of Ride Success:" +content, 
								Toast.LENGTH_SHORT).show();			
						startActivity(new Intent(getApplicationContext(),MyBooking.class));
						finish();
					}
					else if(Integer.valueOf(content)==111)	{
						Toast.makeText(getApplicationContext(), "Error: Customer and Joinee cannot be same", 
								Toast.LENGTH_SHORT).show();
					}
					else {
						Toast.makeText(getApplicationContext(), "Something went wrong", 
								Toast.LENGTH_SHORT).show();
					}	
				}
			} catch (Exception e)	{
				e.printStackTrace();
			}

		} else {
			Toast.makeText(getApplicationContext(), "Total number of riders should be 4 or less", 
					Toast.LENGTH_SHORT).show();
		}
	}
}