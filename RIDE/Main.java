package com.example.grocerypickup;

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
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Main extends Activity{

	private EditText  username=null;
	private EditText  password=null;
	private TextView attempts;
	private Button login;
	public static String customerIdStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		username = (EditText)findViewById(R.id.editText1);
		password = (EditText)findViewById(R.id.editText2);
		login = (Button)findViewById(R.id.button1);
	}

	@Override
	protected void onPause()	{
		super.onPause();
		username = (EditText)findViewById(R.id.editText1);
		username.setText("");
		password = (EditText)findViewById(R.id.editText2);
		password.setText("");
	}

	public void login(View view){

		String username_string = username.getText().toString();
		String password_string = password.getText().toString();
		customerIdStr = username_string;
		if(Utility.isNotNull(username_string) && Utility.isNotNull(password_string))	{

			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
			HttpResponse response;
			JSONObject json = new JSONObject();

			try {
				HttpPost post = new HttpPost("http://hitchhiker.mybluemix.net/rest/login/" + username_string);
				json.put("customerId", username_string);
				json.put("customerPassword", password_string);
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
					}
					else if(content.equalsIgnoreCase("false"))	{
						Toast.makeText(getApplicationContext(), "Incorrect Credentials..", 
								Toast.LENGTH_SHORT).show();
						username = (EditText)findViewById(R.id.editText1);
						username.setText("");
						password = (EditText)findViewById(R.id.editText2);
						password.setText("");
					}
					else	{
						Toast.makeText(getApplicationContext(), "Something went Wrong... Try Again", 
								Toast.LENGTH_SHORT).show();
					}
				}	
			}	catch(Exception e)	{	

				e.printStackTrace();
			}

		}	else	{
			Toast.makeText(getApplicationContext(), "Field cannot be blank", 
					Toast.LENGTH_SHORT).show();
		}

	}

	public static String getCustomerIdStr() {
		return customerIdStr;
	}

	public static void setCustomerIdStr(String customerIdStr) {
		Main.customerIdStr = customerIdStr;
	}

	public void signUp(View view)	{

		startActivity(new Intent(this,SignUp.class));
	}

}