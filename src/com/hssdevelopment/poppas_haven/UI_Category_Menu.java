package com.hssdevelopment.poppas_haven;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*Class UI_Category_Menu
 * Purpose: Download Menu Categories and possible beverages via HSS Development Server
 * After Download, Display drink categories for the user and allow them to select
 * a drink category. When user selects a category, pass new beverage data structure
 * to the next activity that will contain the new beverage. 
 * HashMap<String, String> currentOrder structure:
 * category = category the beverage is from 
 * name = name of Beverage
 * modifier = any possible modifiers for beverage
 * size = size of the beverage (12, 16, 20, 24)
 * milk = type of milk (if any) for this beverage
 * drinkTemperature = hot or iced beverage
 * comments = additional comments from the comments field in review activity
 */
public class UI_Category_Menu extends ListActivity 
{
	//Blank Field Constant for current order initialization
	private static final String BLANK_FIELD ="n/a";
	
	//UI Controls
	private ListView list;
	private View footer;
	
	//Menu Categories List Adapter
	private MenuAdapter menuCategoriesAdapter;
	
	//List View Child Click Handler
	private OnItemClickListener listClickHandler;
	
	//Array List of Menu Categories
	private ArrayList<String> menuCategories;
	
	//Hash Map of Menu Data
	private LinkedHashMap<String, List<String>> menuData;
	
	//Current Drink Order Data Structure
	//This structure will be passed between order-related activities
	private HashMap<String, String> currentOrder;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		//Inflate the UI
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_category_menu);
		
		//Initialize Order Map
		currentOrder = new HashMap<String, String> ();
		initializeDrinkOrder(currentOrder);
		
		//Maps to fill list adapters
		menuCategories = new ArrayList<String>();
		menuData = new LinkedHashMap<String, List<String>>();
		
		//Load initial message onto menu until Menu Data is downloaded from server
		//and updated
		menuCategories.add("Loading...");
		
		//Sample Descriptions to be completed by staff at Poppa's Haven
		//This is place holder data until descriptions are created
		HashMap<String, String> descriptions = new HashMap<String, String>();
		descriptions.put("House Coffee", "Our Delicious Brewed Coffee");
		descriptions.put("Espresso", "Fresh Ground Espresso");
		descriptions.put("Classics", "Our Favorite Drinks");
		descriptions.put("Tea/Chai/Yerba Mate/Matcha", "Our Delicious Tea-Based Beverages");
		descriptions.put("Blended", "Our Delicious Blended Beverages");
		descriptions.put("Other", "Everything else");
		
		//Menu Adapter to populate list
		menuCategoriesAdapter = new MenuAdapter(this, descriptions, menuCategories);
		
		//Acquire reference to list
		list = (ListView)getListView();
		
		//Set List Click Listener
		listClickHandler = new OnItemClickListener(){
			@Override
			//On Item Click: Launch next Activity to select beverage
			public void onItemClick(AdapterView<?> parent, View child, int position, long pos) 
			{
				TextView category = (TextView)child.findViewById(R.id.drink_name);
				String category_name = category.getText().toString();
				
				//Create New Intent and adding the following to the next activity:
				//Name of the category selected to map to appropriate drink
				//Map of current menu data from server
				//Map of current drink order
				Intent i = new Intent(child.getContext(), UI_Drink_Menu.class);
				i.putExtra("category_name", category_name);
				i.putExtra("menu_data", menuData);
				i.putExtra("current_order", currentOrder);
				startActivity(i);
				finish();
			}
		};
		
		//Set the listener
		list.setOnItemClickListener(listClickHandler);
		
		//Inflate menu footer
		footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
		list.addFooterView(footer);
		
		//Create new adapter and inflate it with loading message 
		menuCategoriesAdapter = new MenuAdapter(this, descriptions, menuCategories);
		list.setAdapter(menuCategoriesAdapter);
		
		//Spawn thread and download data from hssdevelopment server via
		//http post request
		SendPostRequest pt = new SendPostRequest();
		pt.execute("http://www.hssdevelopment.com/menu.php");
		
	}
	
	//Initializes map data structure that can be passed between activities
	//containing drink information.
	private void initializeDrinkOrder(HashMap<String, String> currentOrder) 
	{
		currentOrder.put("category",BLANK_FIELD);
		currentOrder.put("name", BLANK_FIELD);
		currentOrder.put("modifier", BLANK_FIELD);
		currentOrder.put("size", BLANK_FIELD);
		currentOrder.put("milk", BLANK_FIELD);
		currentOrder.put("drinkTemperature", BLANK_FIELD);
		currentOrder.put("comments", BLANK_FIELD);
	}

	/*
	 * SendPostRequest:
	 * Purpose: Provide a helper class to download xml data from hssdevelopment server
	 * and to populate the list menu in the UI with data from the server
	 * 
	 */
	private class SendPostRequest extends AsyncTask<String, Void, Map<String, List<String>>>
	{
	  //Error message for post request
	  private static final String ERROR_MESSAGE = "Error downloading Data from Server";
	  //containers to hold server data
	  private ArrayList<String> categories;
	  //container to hold server data on menu
	  private HashMap<String, List<String>> serverData;
	  
		protected Map<String, List<String>> doInBackground(String... urls) 
		{
			//For each URL, send server request to download data. This will only
			//Ever be called with one URL. Function must take an arbitrary amount of
			//Arguments, but only one url will be used. 
			
			for (String url : urls)
			{
				//Create new post request
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				
				try
				{
					//Create request ID and send to server
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			        nameValuePairs.add(new BasicNameValuePair("id", "menu"));
			        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			    
					HttpResponse execute = client.execute(httpPost);
					
					//Get content from the server and process data into containers
					InputStream content = execute.getEntity().getContent();
					processStream(content);
				}
				
				catch (Exception e)
				{
					//If an error occurs, add an error message that will reach the
					//list adapter
					e.printStackTrace();
					serverData = new LinkedHashMap<String, List<String>>();
					serverData.put(ERROR_MESSAGE, new ArrayList<String>());
					return serverData;
				}
			}
			
			//Return downloaded data to server. If an error occurs, return
			//Map with error message to be handled by OnPostExecute
			return serverData;
		}		
		
		
		@Override
		protected void onPostExecute(Map<String, List<String>> result)
		{
			//Handle the resulting map returned by DoInBackground
			ArrayList<String>categories = new ArrayList<String>();
			for(String s : result.keySet())
			{
				if(s.equals(ERROR_MESSAGE))
				{
					//Disable On Click Listnere so Application will not continue
					list.setOnClickListener(null);
				}
				
				categories.add(s);
			}
			
			//Update list view with downloaded data
			menuData = (LinkedHashMap<String, List<String>>) result;
			menuCategories = categories;
			
			//Remove loading message
			menuCategories.remove("Loading...");
			//Set list adapter
			menuCategoriesAdapter.setList(menuCategories);
			//Call the UI to redraw the list
			menuCategoriesAdapter.notifyDataSetChanged();
		}
		
		private void processStream(InputStream inputStream) 
		{		
			XmlPullParserFactory factory;
			//Strings to temporarily hold parsed data
			String name = "";
			String category = "";
			//Containers used to structure and hold data
			//serverData will contain a map of all the categories from the
			//server as keys and each key will map to a list of beverages
			serverData = new LinkedHashMap<String, List<String>>();
			categories = new ArrayList<String>();
			
			try 
			{
				//Set up pull parser
				factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();
				
				xpp.setInput(inputStream, null);
				int eventType = xpp.getEventType();
				/*
				 * Document must be formatted in this way:
				 * <entry>
				 * <category>Insert Category </category>
				 * <name>Insert Name </name>
				 * </entry>
				 * 
				 */
				while (eventType != XmlPullParser.END_DOCUMENT) 
				{
					if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("entry"))
					{
						eventType = xpp.next();

						while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("entry")))
						{
							//Process category tag
							if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("category"))
							{
								category = xpp.nextText();
								
								//If a category does not exist, create a blank array list for it
								if(!serverData.containsKey(category))
								{
									ArrayList<String> al = new ArrayList<String>();
									serverData.put(category, al);
								}
						
								eventType = xpp.next();
							}

							//Process name tag
							if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("name"))
							{
								name = xpp.nextText();
								categories = (ArrayList<String>) serverData.get(category);
								categories.add(name);
								eventType = xpp.next();
							}
							
						}
					}
		
					eventType = xpp.next();
				}
				
			}
			
			catch (XmlPullParserException e) 
			{
				Log.d("PULLPARSER", "Xml Pull Parser Exception", e);
			}
			
			catch (IOException e)
			{
				Log.d("PULLPARSER", "IO Exception", e);
			}

		}
	}
    
}
	
	
	

