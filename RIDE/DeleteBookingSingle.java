package com.example.grocerypickup;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeleteBookingSingle  extends Activity {

	// JSON node keys
	private static final String TAG_TIMESLOT = "timeSlot";
	private static final String TAG_CUSTOMERID = "customerId";
	private static final String TAG_SOURCE = "source";
	private static final String TAG_DESTINATION = "destination";
	private static final String TAG_NUMOFPEOPLE = "numberOfPeople";
	private static final String TAG_BOOKINGID = "bookingId";
	private static final String TAG_JOINEE1 = "joinee1";
	private static final String TAG_JOINEE2 = "joinee2";
	private static final String TAG_JOINEE3 = "joinee3";

	SetDirections setDirections = new SetDirections();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookingdashsingle);

		// getting intent data
		Intent in = getIntent();

		// Get JSON values from previous intent
		String timestamp = in.getStringExtra(TAG_TIMESLOT);
		String customerid = in.getStringExtra(TAG_CUSTOMERID);
		String source = in.getStringExtra(TAG_SOURCE);
		String destination = in.getStringExtra(TAG_DESTINATION);
		String numberOfPeople = in.getStringExtra(TAG_NUMOFPEOPLE);
		String bookingid = in.getStringExtra(TAG_BOOKINGID);
		String joinee1 = in.getStringExtra(TAG_JOINEE1);
		String joinee2 = in.getStringExtra(TAG_JOINEE2);
		String joinee3 = in.getStringExtra(TAG_JOINEE3);

		// Displaying all values on the screen
		TextView lbltimestamp = (TextView) findViewById(R.id.timestamp_label);
		TextView lblsource = (TextView) findViewById(R.id.source_label);
		TextView lbldestination = (TextView) findViewById(R.id.destination_label);
		TextView lblcustomerid = (TextView) findViewById(R.id.customer_label);
		TextView lblnumberofpeople = (TextView) findViewById(R.id.numberofpeople_label);
		TextView lblbookingid = (TextView) findViewById(R.id.bookingid_label);
		TextView lbljoinee1 = (TextView) findViewById(R.id.joinee1);
		TextView lbljoinee2 = (TextView) findViewById(R.id.joinee2);
		TextView lbljoinee3 = (TextView) findViewById(R.id.joinee3);

		lbltimestamp.setText(timestamp);
		lblsource.setText(source);
		lbldestination.setText(destination);
		lblcustomerid.setText(customerid);
		lblnumberofpeople.setText(numberOfPeople);
		lblbookingid.setText(bookingid);
		lbljoinee1.setText(joinee1);
		lbljoinee2.setText(joinee2);
		lbljoinee3.setText(joinee3);

		Button btn = (Button) findViewById(R.id.button211);
		source = source.substring(7, source.length());
		destination = destination.substring(8, destination.length());
		if(source.equals("India Market,Santa Clara"))	{
			setDirections.setSourceLatitude("37.354193");
			setDirections.setSourceLongitude("-122.014396");
			setDirections.setSourceNickName("New IndiaMarket");
			setDirections.setSourceAddress("2213 El Camino Real Santa Clara,CA 95050");
		}	else if(source.equals("Patel Brothers,Santa Clara"))	{
			setDirections.setSourceLatitude("37.353173");
			setDirections.setSourceLongitude("-121.960455");
			setDirections.setSourceNickName("Patel Bros");
			setDirections.setSourceAddress("2039 El Camino Real Santa Clara,CA 95050");
		}	else if(source.equals("India Market,Sunnyvale"))	{
			setDirections.setSourceLatitude("37.354227");
			setDirections.setSourceLongitude("-122.014353");
			setDirections.setSourceNickName("Indian Market");
			setDirections.setSourceAddress("899 E El Camino Real Sunnyvale,CA 94087");
		}	else if(source.equals("Costco, Senter"))	{
			setDirections.setSourceLatitude("37.309805");
			setDirections.setSourceLongitude("-121.851629");
			setDirections.setSourceNickName("Costco");
			setDirections.setSourceAddress("2201 Senter Rd San Jose,CA 95112");
		}	else if(source.equals("MLK Library"))	{
			setDirections.setSourceLatitude("37.335741");
			setDirections.setSourceLongitude("-121.884977");
			setDirections.setSourceNickName("MLK Library");
			setDirections.setSourceAddress("150 E San Fernando St San Jose,CA 95112");
		}	else if(source.equals("33S Third Street"))	{
			setDirections.setSourceLatitude("37.336053");
			setDirections.setSourceLongitude("-121.887730");
			setDirections.setSourceNickName("33S Third Street Apts");
			setDirections.setSourceAddress("33 S 3rd St San Jose,CA 95113");
		}	else if(source.equals("Villa Torino"))	{
			setDirections.setSourceLatitude("37.340655");
			setDirections.setSourceLongitude("-121.894561");
			setDirections.setSourceNickName("Villa Torino Apts");
			setDirections.setSourceAddress("29 W Julian St San Jose,CA 95110");
		}	else if(source.equals("Legacy Plaza"))	{
			setDirections.setSourceLatitude("37.341100");
			setDirections.setSourceLongitude("-121.898392");
			setDirections.setSourceNickName("Legacy Fountain");
			setDirections.setSourceAddress("190 Ryland St San Jose,CA 95110");
		}	else	{
			setDirections.setSourceLatitude(null);
			setDirections.setSourceLongitude(null);
		}

		if(destination.equals("India Market,Santa Clara"))	{
			setDirections.setDestinationLatitude("37.354193");
			setDirections.setDestinationLongitude("-122.014396");
			setDirections.setDestinationNickName("New India Market");
			setDirections.setDestinationAddress("2213 El Camino Real Santa Clara,CA 95050");
		}	else if(destination.equals("Patel Brothers,Santa Clara"))	{
			setDirections.setDestinationLatitude("37.353173");
			setDirections.setDestinationLongitude("-121.960455");
			setDirections.setDestinationNickName("Patel Bros");
			setDirections.setDestinationAddress("2039 El Camino Real Santa Clara,CA 95050");
		}	else if(destination.equals("India Market,Sunnyvale"))	{
			setDirections.setDestinationLatitude("37.354227");
			setDirections.setDestinationLongitude("-122.014353");
			setDirections.setDestinationNickName("Indian Market");
			setDirections.setDestinationAddress("899 E El Camino Real Sunnyvale,CA 94087");
		}	else if(destination.equals("Costco, Senter"))	{
			setDirections.setDestinationLatitude("37.309805");
			setDirections.setDestinationLongitude("-121.851629");
			setDirections.setDestinationNickName("Costco");
			setDirections.setDestinationAddress("2201 Senter Rd San Jose,CA 95112");
		}	else if(destination.equals("MLK Library"))	{
			setDirections.setDestinationLatitude("37.335741");
			setDirections.setDestinationLongitude("-121.884977");
			setDirections.setDestinationNickName("MLK Library");
			setDirections.setDestinationAddress("150 E San Fernando St San Jose,CA 95112");
		}	else if(destination.equals("33S Third Street"))	{
			setDirections.setDestinationLatitude("37.336053");
			setDirections.setDestinationLongitude("-121.887730");
			setDirections.setDestinationNickName("33S Third Street Apts");
			setDirections.setDestinationAddress("33 S 3rd St San Jose,CA 95113");
		}	else if(destination.equals("Villa Torino"))	{
			setDirections.setDestinationLatitude("37.340655");
			setDirections.setDestinationLongitude("-121.894561");
			setDirections.setDestinationNickName("Villa Torino Apts");
			setDirections.setDestinationAddress("29 W Julian St San Jose,CA 95110");
		}	else if(destination.equals("Legacy Plaza"))	{
			setDirections.setDestinationLatitude("37.341100");
			setDirections.setDestinationLongitude("-121.898392");
			setDirections.setDestinationNickName("Legacy Fountain");
			setDirections.setDestinationAddress("190 Ryland St San Jose,CA 95110");
		}	else	{
			setDirections.setDestinationLatitude(null);
			setDirections.setDestinationLongitude(null);
		}

		String initialTimeSlot = timestamp.substring(9, 11) + timestamp.substring(12, 14);
		String finalTimeSlot = timestamp.substring(17, 19) + timestamp.substring(20, 22);

		DateFormat sdf = new SimpleDateFormat("HH:mm");
		String currentTime = sdf.format(new Date());
		currentTime = currentTime.substring(0, 2) + currentTime.substring(3,5);

		if(Integer.valueOf(initialTimeSlot) <= Integer.valueOf(currentTime) && Integer.valueOf(currentTime) <= Integer.valueOf(finalTimeSlot))	{
			btn.setEnabled(true);
		} else	{
			btn.setEnabled(false);
		}
	} 

	public void deleteBooking(View view)	{
		
		TextView lblbookingid = (TextView) findViewById(R.id.bookingid_label);

		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
		HttpResponse response;
		JSONObject json = new JSONObject();

		try {
			HttpPut put = new HttpPut("http://hitchhiker.mybluemix.net/rest/booking/deletebooking/" + lblbookingid.getText().toString().substring(10,lblbookingid.getText().toString().length()));
			json.put("customerId", Main.getCustomerIdStr());
			json.put("bookingId", lblbookingid.getText().toString().substring(10,lblbookingid.getText().toString().length()));

			StringEntity se = new StringEntity(json.toString(),HTTP.UTF_8);
			se.setContentType("application/json");
			System.out.println(se);
			put.setEntity(se);
			response = client.execute(put);
			if(response!=null){
				HttpEntity entity = response.getEntity();
				String content = EntityUtils.toString(entity);
				if(content.equalsIgnoreCase("true"))	{
					Toast.makeText(getApplicationContext(), "Success..", 
							Toast.LENGTH_SHORT).show();			
					startActivity(new Intent(getApplicationContext(),MyBooking.class));
					finish();
				}
			}
		} catch (Exception e)	{
			e.printStackTrace();
		}
	}
	public void uber(View view)	{

		PackageManager pm = getApplicationContext().getPackageManager();
		try
		{
			pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
			Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.ubercab");
			String temp = "uber://?client_id=4JAarx81QFptmw8lVmiBf-ZV8Lgowrue&action=setPickup&pickup[latitude]="+setDirections.getSourceLatitude()+"&pickup[longitude]="+setDirections.getSourceLongitude()+"&pickup[nickname]="+setDirections.getSourceNickName()+"&pickup[formatted_address]="+setDirections.getSourceAddress()+"&dropoff[latitude]="+setDirections.getDestinationLatitude()+"&dropoff[longitude]="+setDirections.getDestinationLongitude()+"&dropoff[nickname]="+setDirections.getDestinationNickName()+"&dropoff[formatted_address]="+setDirections.getDestinationAddress()+"&product_id=a1111c8c-c720-46c3-8534-2fcdd730040d";
			launchIntent.setData(Uri.parse(temp));
			startActivity(launchIntent);
		}
		catch (PackageManager.NameNotFoundException e)
		{
			Toast.makeText(getApplicationContext(), "No UberApp found", 
					Toast.LENGTH_SHORT).show();
		}
	}
}