package com.example.grocerypickup;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Browser.BookmarkColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DeleteBooking extends ListActivity {

	private ProgressDialog pDialog;

	// URL to get contacts JSON
	private static String url = "http://hitchhiker.mybluemix.net/rest/booking/getbooking/" + Main.customerIdStr;

	// JSON Node names

	private static final String TAG_BOOKING = "booking";
	private static final String TAG_BOOKINGID = "bookingId";
	private static final String TAG_CUSTOMERID = "customerId";
	private static final String TAG_TIMESLOT = "timeSlot";
	private static final String TAG_NUMOFPEOPLE = "numberOfPeople";
	private static final String TAG_SOURCE = "source";
	private static final String TAG_DATE = "date";
	private static final String TAG_DESTINATION = "destination";
	private static final String TAG_JOINEE1 = "joinee1";
	private static final String TAG_JOINEE2 = "joinee2";
	private static final String TAG_JOINEE3 = "joinee3";
	
	private TextView txtVResult = null;
	
	// contacts JSONArray
	JSONArray bookings = null;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> bookingList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookingdashboard_activity);

		bookingList = new ArrayList<HashMap<String, String>>();

		ListView lv = getListView();

		// Listview on item click listener
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String timeslot = ((TextView) view.findViewById(R.id.timeSlot))
						.getText().toString();
				String pickup = ((TextView) view.findViewById(R.id.source))
						.getText().toString();
				String dropoff = ((TextView) view.findViewById(R.id.destination))
						.getText().toString();
				String numberofpeople = ((TextView) view.findViewById(R.id.numberofpeople))
						.getText().toString();
				String customerid = ((TextView) view.findViewById(R.id.customerid))
						.getText().toString();
				String bookingid = ((TextView) view.findViewById(R.id.bookingid))
						.getText().toString();
				String joinee1 = ((TextView) view.findViewById(R.id.joinee1))
						.getText().toString();
				String joinee2 = ((TextView) view.findViewById(R.id.joinee2))
						.getText().toString();
				String joinee3 = ((TextView) view.findViewById(R.id.joinee3))
						.getText().toString();

				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						DeleteBookingSingle.class);
				in.putExtra(TAG_TIMESLOT, timeslot);
				in.putExtra(TAG_SOURCE, pickup);
				in.putExtra(TAG_DESTINATION, dropoff);
				in.putExtra(TAG_NUMOFPEOPLE, numberofpeople);
				in.putExtra(TAG_CUSTOMERID, customerid);
				in.putExtra(TAG_BOOKINGID,bookingid);
				in.putExtra(TAG_JOINEE1,joinee1);
				in.putExtra(TAG_JOINEE2,joinee2);
				in.putExtra(TAG_JOINEE3,joinee3);
				startActivity(in);

			}
		});

		// Calling async task to get json
		new GetContacts().execute();
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(DeleteBooking.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
			
			if(jsonStr == null)	{
				txtVResult.append("No Bookings Found");
			}
			
			jsonStr ="{\"booking\":" + jsonStr + "}";

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					bookings = jsonObj.getJSONArray(TAG_BOOKING);

					// looping through All Contacts
					for (int i = 0; i < bookings.length(); i++) {
						JSONObject c = bookings.getJSONObject(i);
						String numberOfPeople = c.getString(TAG_NUMOFPEOPLE);

						String bookingId = c.getString(TAG_BOOKINGID);
						String customerId = c.getString(TAG_CUSTOMERID);
						String timeSlot = c.getString(TAG_TIMESLOT);

						String source = c.getString(TAG_SOURCE);
						String date = c.getString(TAG_DATE);
						String destination = c.getString(TAG_DESTINATION);
						String joinee1 = c.getString(TAG_JOINEE1);
						String joinee2 = c.getString(TAG_JOINEE2);
						String joinee3 = c.getString(TAG_JOINEE3);

						// tmp hashmap for single contact
						HashMap<String, String> booking = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						
						booking.put(TAG_BOOKINGID, ("BookingID:"+bookingId));
						booking.put(TAG_CUSTOMERID,("Primary CustomerID:"+customerId));
						booking.put(TAG_TIMESLOT,("TimeSlot:"+timeSlot));
						booking.put(TAG_NUMOFPEOPLE,("Number of People:"+numberOfPeople));
						booking.put(TAG_SOURCE,("PickUp:"+source));
						booking.put(TAG_DATE, date);
						booking.put(TAG_DESTINATION,("DropOff:"+destination));
						booking.put(TAG_JOINEE1, ("Joinee1:"+joinee1));
						booking.put(TAG_JOINEE2, ("Joinee2:"+joinee2));
						booking.put(TAG_JOINEE3, ("Joinee3:"+joinee3));

						// adding contact to contact list
						bookingList.add(booking);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(
					DeleteBooking.this, bookingList,
					R.layout.bookingdashlist, new String[] { TAG_TIMESLOT, TAG_SOURCE,
							TAG_DESTINATION, TAG_CUSTOMERID, TAG_NUMOFPEOPLE,TAG_BOOKINGID,TAG_JOINEE1,TAG_JOINEE2,TAG_JOINEE3}, new int[] { R.id.timeSlot,
							R.id.source, R.id.destination, R.id.customerid,R.id.numberofpeople,R.id.bookingid,R.id.joinee1,R.id.joinee2,R.id.joinee3});
			setListAdapter(adapter);
		}
	}
}