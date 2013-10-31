package com.hssdevelopment.poppas_haven;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewOrder extends Activity implements OnClickListener
{
	//Current Order Data
	private String customerName;
	private String drinkName;
	private String comments;
	private String milk;
	private String hotOrIced;
	private String minutesToArrival;
	private String drinkModifier;
	private String drinkSize;
	private String drinkTemperature;
	
	//Activity Context
	private Context context;
	
	//UI Controls
	private Button sendOrder;
	private TextView finalOrder;
	private EditText nameField;
	private EditText commentsField;
	private EditText minutesField;
	
	private TextView nameTextView;
	private TextView commentsTextView;
	private TextView arrivalTextView;
	private TextView minutesTextView;
	
	//Hash map for current drink order
	private HashMap<String, String> currentOrder;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review_order);
		
		context = this;
		
		Bundle extras = getIntent().getExtras();
		
		//Grab extras if they are available, Send back to original screen if
		//intents were not broadcasted
		if (extras != null)
		{
			try
			{
				currentOrder = (HashMap<String, String>)extras.getSerializable("current_order");
				
				drinkName = currentOrder.get("name");
				milk 	  =	currentOrder.get("milk");	
				drinkSize = currentOrder.get("size");
				drinkTemperature = currentOrder.get("drinkTemperature");
				drinkModifier = currentOrder.get("modifier");
			}
			
			catch (NullPointerException e)
			{
				e.printStackTrace();
				Toast.makeText(context, "An Error has Occured Loading Menu", Toast.LENGTH_LONG).show();
				finish();
			}
		}
		
		//Get reference to All UI elements
		finalOrder = (TextView)findViewById(R.id.order_field);
		sendOrder = (Button) findViewById(R.id.send_order);
		nameField = (EditText) findViewById(R.id.name_field);
		commentsField = (EditText) findViewById(R.id.comments_field);
		minutesField = (EditText) findViewById(R.id.minutes_field);
		
		nameTextView = (TextView)findViewById(R.id.name_textview);
		commentsTextView = (TextView)findViewById(R.id.comments_textview);
		arrivalTextView = (TextView)findViewById(R.id.arrival_textview);
		minutesTextView = (TextView)findViewById(R.id.minutes_textview);
		
		//Set final order field
		try
		{
			finalOrder.setText(drinkSize + " " + drinkTemperature +
								"\n" + milk + " " + drinkName + "\n" +
								"Modifiers: " + drinkModifier);
		}
		
		catch(NullPointerException e)
		{
			Toast.makeText(ReviewOrder.this, "An Error has Occured. Please order again", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		//Set OnClickListener for send order button
		sendOrder.setOnClickListener(this);
	}

	//This class sends a drink order to the hss development server
	//Upon succesful completion, this will update the UI to thank the customer
	//for their order
	private class SendPostRequest extends AsyncTask<String, Void, String>
	{
	  private List<NameValuePair> postData;
	  private boolean data;
	  private String response;
	  
	  SendPostRequest(List<NameValuePair> postData)
	  {
		  this.postData = postData;
		  this.data = true;
	  }
		protected String doInBackground(String... urls) 
		{
			for (String url : urls)
			{
				//Create new http Post request
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				
				try
				{
					if(data)
						httpPost.setEntity(new UrlEncodedFormEntity(postData));
					//If data is valid, send a new post request
					HttpResponse execute = client.execute(httpPost);
				}
				
				catch (Exception e)
				{
					e.printStackTrace();
					return "An Error Has Occured";
				}
			}
			
			return "success";
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			if(result.equals("success"));
			{
				//clear text fields
				//set text as Thank you for your Order
				//Disable Button
				sendOrder.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						Intent i = new Intent(v.getContext(), HomeScreen.class);
						startActivity(i);
					}
					
				});
				sendOrder.setText("Back To Home");
				
				final int SET_TO_INVISIBLE = 8;
				
				finalOrder.setGravity(Gravity.CENTER);
				finalOrder.setText("Thank You For Your Order!");
				nameField.setVisibility(SET_TO_INVISIBLE);
				commentsField.setVisibility(SET_TO_INVISIBLE);
				minutesField.setVisibility(SET_TO_INVISIBLE);
				nameTextView.setVisibility(SET_TO_INVISIBLE);
				commentsTextView.setVisibility(SET_TO_INVISIBLE);
				arrivalTextView.setVisibility(SET_TO_INVISIBLE);
				minutesTextView.setVisibility(SET_TO_INVISIBLE);
			}
		}
	}
	
	@Override
	public void onClick(View v) 
	{
		if(v == sendOrder)
		{
			//Grab customer name, comments, and minutes to arrival
			customerName = nameField.getText().toString();
			comments = commentsField.getText().toString();
			minutesToArrival = minutesField.getText().toString();
			
			//If customer leaves minutes to arrival field blank, assume 15 minutes
			if("".equals(minutesToArrival))
			{
				minutesToArrival = "15";
			}
			
			//Parse minutes to arrival string
			int mta = Integer.parseInt(minutesToArrival);
			
			//Check to make sure customer entered name
		    if("".equals(customerName))
		    {
		    	Toast.makeText(v.getContext(), "Please Fill in Name Field", Toast.LENGTH_SHORT).show();
		    }
		    //Check to make sure customer entered a time of 5 or more minutes
		    else if(mta < 5)
		    {
		    	Toast.makeText(v.getContext(),"Please enter a time of 5 or more minutes", Toast.LENGTH_LONG).show();
		    }
		    //If valid data, send post request
		    else
			{
		        if("".equals(comments))
		    			comments = "N/A";
			
		    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
		    	nameValuePairs.add(new BasicNameValuePair("postId", "696e636f6d696e6720616e64726f6964206f72646572"));
		    	nameValuePairs.add(new BasicNameValuePair ("name", customerName));
		    	nameValuePairs.add(new BasicNameValuePair ("modifier", drinkModifier));
		    	nameValuePairs.add(new BasicNameValuePair ("drink", drinkName));
		    	nameValuePairs.add(new BasicNameValuePair ("size", drinkSize));
		    	nameValuePairs.add(new BasicNameValuePair ("milk", milk));
		    	nameValuePairs.add(new BasicNameValuePair ("drinkTemperature", drinkTemperature));
		    	nameValuePairs.add(new BasicNameValuePair ("comments", comments));
		    	nameValuePairs.add(new BasicNameValuePair ("minutesToArrival", minutesToArrival));
			
		    	SendPostRequest spr = new SendPostRequest(nameValuePairs);
		    	spr.execute("http://www.hssdevelopment.com/coffee_connect.php");
			}
		    
		}
	}

}
