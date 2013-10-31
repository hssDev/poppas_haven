/*
package com.hssdevelopment.poppas_haven;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderOptions extends ExpandableListActivity implements OnClickListener 
{
	List<String> groupList;
	
	Map<String, List<String>> modifierCollection;
	
	ExpandableListView expListView;
    ExpandableListAdapter expListAdapter;
	
	CheckBox twentyFourOunceCheckbox,
			 twentyOunceCheckbox,
			 sixteenOunceCheckbox,
			 twelveOunceCheckbox,
		 	 hotCheckbox,
			 coldCheckbox;
	
	TextView twentyFourOunceTextView,
			 twentyOunceTextView,
			 twelveOunceTextView,
			 sixteenOunceTextView,
			 hotTextView,
			 coldTextView,
			 flavorTextView;
	
	Button sendOrderDetails;
	
	//Order Details
	int sizeDetail = 0;
	String tempDetail;
	String drinkName;
	String milk;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//Set Layout
		setContentView(R.layout.size_temp_options);
		
		//Initialize Lists
		groupList = new ArrayList<String>();
		modifierCollection = new LinkedHashMap<String, List<String>>();
		
		//Create and Set adapter
        expListView = getExpandableListView();
        expListAdapter = new ExpandableListAdapter(this, groupList, modifierCollection);
        expListView.setAdapter(expListAdapter);
        

		//Get References to Buttons and Checkboxes
		twentyFourOunceCheckbox = (CheckBox)findViewById(R.id.twentyFourOunce_checkbox);
		twentyOunceCheckbox = (CheckBox)findViewById(R.id.twentyounce_checkbox); 
		sixteenOunceCheckbox = (CheckBox)findViewById(R.id.sixteenounce_checkbox);
		twelveOunceCheckbox = (CheckBox)findViewById(R.id.to_checkbox);
		hotCheckbox = (CheckBox)findViewById(R.id.hot_checkbox);
		coldCheckbox = (CheckBox)findViewById(R.id.cold_checkbox);
		
		twentyFourOunceTextView = (TextView)findViewById(R.id.twentyFourOunce_textview);
		twentyOunceTextView = (TextView)findViewById(R.id.twentyOunce_textview);
		twelveOunceTextView = (TextView)findViewById(R.id.to_textview);
		sixteenOunceTextView = (TextView)findViewById(R.id.so_textview);
		hotTextView = (TextView)findViewById(R.id.hotTextView);
		coldTextView = (TextView)findViewById(R.id.coldTextView);
		flavorTextView = (TextView)findViewById(R.id.flavor_options_title);
		
		//sendOrderDetails = (Button)findViewById(R.id.send_order_details);
		
		
		//Set On Click Listeners for Buttons
		twentyOunceCheckbox.setOnClickListener(this);
		sixteenOunceCheckbox.setOnClickListener(this);
		twelveOunceCheckbox.setOnClickListener(this);
		
		hotCheckbox.setOnClickListener(this);
		coldCheckbox.setOnClickListener(this);
		
		twentyOunceTextView.setOnClickListener(this);
		twelveOunceTextView.setOnClickListener(this);
		sixteenOunceTextView.setOnClickListener(this);
		
		hotTextView.setOnClickListener(this);
		coldTextView.setOnClickListener(this);
				
		sendOrderDetails.setOnClickListener(this);
		
		//Get Drink Name from extras
		Bundle extras = getIntent().getExtras();
		
		//Grab extras if they are available, Send back to original screen if
		//intents were not broadcasted
		if (extras != null)
		{
			drinkName = extras.getString("drink_name");
			milk = extras.getString("milk");
		}	
		
		SendPostRequest pt = new SendPostRequest();
		pt.execute("http://www.hssdevelopment.com/menu.php");
		
	}
	
	@Override
	public void onClick(View v) 
	{
		if(v == twentyFourOunceCheckbox || v == twentyFourOunceTextView)
		{
			if(twentyOunceCheckbox.isChecked())
				twentyOunceCheckbox.setChecked(false);
			
			if(sixteenOunceCheckbox.isChecked())
				sixteenOunceCheckbox.setChecked(false);
				
			
			if(twelveOunceCheckbox.isChecked())
				twelveOunceCheckbox.setChecked(false);
			
			twentyFourOunceCheckbox.setChecked(true);
			sizeDetail = 24;
		}
		
		if(v == twentyOunceCheckbox || v == twentyOunceTextView)
		{
			if(twentyFourOunceCheckbox.isChecked())
				twentyFourOunceCheckbox.setChecked(false);
			
			if(sixteenOunceCheckbox.isChecked())
				sixteenOunceCheckbox.setChecked(false);
				
			
			if(twelveOunceCheckbox.isChecked())
				twelveOunceCheckbox.setChecked(false);
			
			twentyOunceCheckbox.setChecked(true);
			sizeDetail = 20;
		}
		
		if(v == sixteenOunceCheckbox || v == sixteenOunceTextView)
		{
			if(twentyFourOunceCheckbox.isChecked())
				twentyFourOunceCheckbox.setChecked(false);
			
			if(twentyOunceCheckbox.isChecked())
				twentyOunceCheckbox.setChecked(false);
			
			if(twelveOunceCheckbox.isChecked())
				twelveOunceCheckbox.setChecked(false);
			
			sixteenOunceCheckbox.setChecked(true);
			sizeDetail = 16;
		}
		
		if(v == twelveOunceCheckbox || v == twelveOunceTextView)
		{
			if(twentyFourOunceCheckbox.isChecked())
				twentyFourOunceCheckbox.setChecked(false);
			
			if(twentyOunceCheckbox.isChecked())
				twentyOunceCheckbox.setChecked(false);
			
			if(sixteenOunceCheckbox.isChecked())
				sixteenOunceCheckbox.setChecked(false);
			
			twelveOunceCheckbox.setChecked(true);
			sizeDetail = 12;
		}
		
		if(v == hotCheckbox || v == hotTextView)
		{
			if (coldCheckbox.isChecked())
				coldCheckbox.setChecked(false);
			
			hotCheckbox.setChecked(true);
			tempDetail = "Hot";
			
			if(twentyFourOunceCheckbox.isChecked())
			{
				twentyFourOunceCheckbox.setChecked(false);
				twentyOunceCheckbox.setChecked(true);
				sizeDetail = 20;
			}	
				twentyFourOunceCheckbox.setVisibility(View.INVISIBLE);
				twentyFourOunceTextView.setVisibility(View.INVISIBLE);
		}	
		
		if(v == coldCheckbox || v == coldTextView)
		{
			if (hotCheckbox.isChecked())
				hotCheckbox.setChecked(false);
			
			coldCheckbox.setChecked(true);
			tempDetail = "Cold";
			
			twentyFourOunceCheckbox.setVisibility(View.VISIBLE);
			twentyFourOunceTextView.setVisibility(View.VISIBLE);
			
			twentyFourOunceTextView.setOnClickListener(this);
			twentyFourOunceCheckbox.setOnClickListener(this);

		}
		
		if(v == sendOrderDetails)
		{
			if(tempDetail == null || sizeDetail == 0)
				Toast.makeText(v.getContext(), "Please Select both a size and temperature option", Toast.LENGTH_SHORT).show();
			else
			{
				Intent i = new Intent(v.getContext(), ReviewOrder.class);
				
				i.putExtra("drink_name", drinkName);
				i.putExtra("milk", milk);
				i.putExtra("drink_size", sizeDetail);
				i.putExtra("drink_temp", tempDetail);
				
				startActivity(i);
			}
		}
	}
	
	private void setTemperatureFlags(Boolean icedFlag, Boolean hotFlag)
	{
		if(icedFlag)
		{
			//hotCheckbox.setVisibility(INVISIBLE);
		}
		
		if(hotFlag)
		{
			//coldCheckbox.setVisibility(INVISIBLE);
		}
	}
	
	
    private class SendPostRequest extends AsyncTask<String, Void, Map<String, List<String>>>
	{
	  private ArrayList<String> categoryList;
	  private Map<String, List<String>> modifierMap;
	  
		protected Map<String, List<String>> doInBackground(String... urls) 
		{
			for (String url : urls)
			{
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				
				try
				{
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			        nameValuePairs.add(new BasicNameValuePair("id", "modifier"));
			        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        
					HttpResponse execute = client.execute(httpPost);
					InputStream content = execute.getEntity().getContent();
					processStream(content);
				}
				
				catch (Exception e)
				{
					e.printStackTrace();
					modifierMap = new LinkedHashMap<String, List<String>>();
					return modifierMap;
				}
			}
			
			return modifierMap;
		}
		
		@Override
		protected void onPostExecute(Map<String, List<String>> result)
		{
			ArrayList<String>categories = new ArrayList<String>();
			for(String s : result.keySet())
			{
				categories.add(s);
			}
			
			expListAdapter.setLaptops(categories);
			expListAdapter.setCollection(result);
			expListAdapter.notifyDataSetChanged();
			
			groupList = expListAdapter.getLaptops();
			modifierCollection = expListAdapter.getLaptopCollections();
			
			
			flavorTextView.setText(R.string.select_flavor);
		}
		
		private void processStream(InputStream inputStream) 
		{
			XmlPullParserFactory factory;
			String name = "";
			String category = "";
			modifierMap = new LinkedHashMap<String, List<String>>();
			categoryList = new ArrayList<String>();
			
			try {
				factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();
				
				xpp.setInput(inputStream, null);
				int eventType = xpp.getEventType();
				
				while (eventType != XmlPullParser.END_DOCUMENT) 
				{
					if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("entry"))
					{
						eventType = xpp.next();

						while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("entry")))
						{
							//Process name tag
							if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("category"))
							{
								category = xpp.nextText();
								
								//If a category does not exist, create a blank array list for it
								if(!modifierMap.containsKey(category))
								{
									ArrayList<String> al = new ArrayList<String>();
									modifierMap.put(category, al);
								}
						
								eventType = xpp.next();
							}

							//Process name tag
							if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("name"))
							{
								name = xpp.nextText();
								categoryList = (ArrayList<String>) modifierMap.get(category);
								categoryList.add(name);
								eventType = xpp.next();
							}
							
						}
					}
		
					eventType = xpp.next();
				}
				
			}
			
			catch (XmlPullParserException e) {
				Log.d("PULLPARSER", "Xml Pull Parser Exception", e);
			}
			
			catch (IOException e){
				Log.d("PULLPARSER", "IO Exception", e);
			}
			
		}
	}	
};
*/