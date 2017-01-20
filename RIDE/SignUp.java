package com.example.grocerypickup;

import java.io.InputStream;
import java.net.MalformedURLException;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class SignUp extends Activity{

	// Progress Dialog Object
	ProgressDialog prgDialog;
	//Error Message TextView Object
	TextView errorMsg;
	// Name Edittext Object
	EditText name;
	//Phone Number EditText Object
	EditText phoneNumber;
	//Password EditText Object
	EditText password;
	//Verify Password EditText Object
	EditText verifypassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_activity);
		// Find Name Edit View control by ID
		name = (EditText)findViewById(R.id.editText1);
		// Find Phone Number Edit View control by ID
		phoneNumber = (EditText)findViewById(R.id.editText2);
		// Find Password Edit View control by ID
		password = (EditText)findViewById(R.id.editText3);
		// Find Password Edit View control by ID
		verifypassword = (EditText)findViewById(R.id.editText4);
		// Instantiate Progress Dialog object
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
		prgDialog.setMessage("Please wait...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);
	}

	public void register(View view) throws MalformedURLException	{

		//Extract Name String
		String name_string = name.getText().toString();
		//Extract Phone String
		String phone_string = phoneNumber.getText().toString();
		//Extract Password String
		String password_string = password.getText().toString();
		//Extract Verify Password String
		String verifypassword_string = verifypassword.getText().toString();

		if(Utility.isNotNull(name_string) && Utility.isNotNull(phone_string) && Utility.isNotNull(password_string)
				&& Utility.isNotNull(verifypassword_string))	{
			if(phone_string.length()==10)	{	
				if(Utility.validate(name_string))	{
					if(password_string.equals(verifypassword_string))	{
						HttpClient client = new DefaultHttpClient();
						HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
						HttpResponse response;
						JSONObject json = new JSONObject();
						Random randomGenerator = new Random();
						int imessageId = randomGenerator.nextInt(10000);
						StringBuilder sb = new StringBuilder();
						sb.append("");
						sb.append(imessageId);
						String messageId = sb.toString();

						try {
							HttpPost post = new HttpPost("http://hitchhiker.mybluemix.net/rest/signup");
							json.put("customerId", phone_string);
							json.put("customerName", name_string);
							json.put("customerPassword", password_string);
							StringEntity se = new StringEntity(json.toString(),HTTP.UTF_8);
							se.setContentType("application/json");
							System.out.println(se);
							post.setEntity(se);
							post.setHeader("messageId", messageId);
							response = client.execute(post);
							if(response!=null){
								HttpEntity entity = response.getEntity();
								String content = EntityUtils.toString(entity);
								if(content.equalsIgnoreCase("true"))	{
									Toast.makeText(getApplicationContext(), "Success..", 
											Toast.LENGTH_SHORT).show();			
									startActivity(new Intent(this,Main.class));
									//finish();
								}
							}	
						}	catch(Exception e)	{	

							e.printStackTrace();
						}
					}	else	{
						Toast.makeText(getApplicationContext(), "Passwords do not match", 
								Toast.LENGTH_SHORT).show();			
					}
				}	else	{
					Toast.makeText(getApplicationContext(), "No Special Characters or numbers in name field", 
							Toast.LENGTH_SHORT).show();
				}
			}	else	{
				Toast.makeText(getApplicationContext(), "Phone length should be 10 digits", 
						Toast.LENGTH_SHORT).show();
			}

		}	else	{
			Toast.makeText(getApplicationContext(), "Values cannot be blank..", 
					Toast.LENGTH_SHORT).show();
		}
	}
}