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

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Drink_Menu extends ExpandableListActivity{

    List<String> groupList;
    Map<String, List<String>> laptopCollection;
    ExpandableListView expListView;
    Button sendSelection;
    CheckBox drinkSelection;
    ExpandableListAdapter expListAdapter;
    TextView title;
    Context context;

    //Extras
    String drink;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drink__menu);
        context = this;
        groupList = new ArrayList<String>();
        laptopCollection = new LinkedHashMap<String, List<String>> ();
        
        //Pushing test data into Collections
        groupList.add("Sauce");
        groupList.add("Syrup");
        
        ArrayList<String> syrupList = new ArrayList<String>();
        syrupList.add("Vanilla");
        syrupList.add("Mint");
        syrupList.add("Marshmellow");
        
        ArrayList<String> sauceList = new ArrayList<String>();
        sauceList.add("Chocolate");
        sauceList.add("Carmel");
        sauceList.add("Dark Chocolate");
        
        laptopCollection.put("Sauce", sauceList);
        laptopCollection.put("Syrup", syrupList);
       
        
        title = (TextView)findViewById(R.id.main_menu_title);
        
        expListView = getExpandableListView();
        
		View footer = getLayoutInflater().inflate(R.layout.next_button, null);
		expListView.addFooterView(footer);
		
        expListAdapter = new ExpandableListAdapter(this, groupList, laptopCollection);
        expListView.setAdapter(expListAdapter);
        

		
		sendSelection = (Button)findViewById(R.id.next_button);

		//SendPostRequest pt = new SendPostRequest();
		//pt.execute("http://www.hssdevelopment.com/menu.php");
 
        expListView.setOnChildClickListener(new OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
            	
            	final CheckBox cb = (CheckBox)v.findViewById(R.id.select);
            	final TextView tv = (TextView)v.findViewById(R.id.child_textview);
            	
            	drinkSelection = expListAdapter.getActiveCheckBox();
            	
            	if(drinkSelection == null)
            	{
            		drinkSelection = cb;
            		//Toast.makeText(v.getContext(), "Fired Drink Menu Null", Toast.LENGTH_SHORT).show();
            		drinkSelection.setChecked(true);
            		expListAdapter.setActiveCheckBox(drinkSelection, groupPosition, childPosition);
            		drink = tv.getText().toString();
            	}
            	
            	else
            	{
            		//Toast.makeText(v.getContext(), "Fired Drink Menu non-null", Toast.LENGTH_SHORT).show();
            		expListAdapter.getActiveCheckBox().setChecked(false);
            		drinkSelection = cb;
            		drinkSelection.setChecked(true);
                	expListAdapter.setActiveCheckBox(cb, groupPosition, childPosition);
                	drink = tv.getText().toString();
                	expListAdapter.notifyDataSetChanged();
            	}
				
                return true;
            }
        });
        
        sendSelection.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) 
			{
				if(drink == null)
					Toast.makeText(v.getContext(), "Please Select a Beverage", Toast.LENGTH_SHORT).show();
				else
				{
				//Toast.makeText(v.getContext(), drink, Toast.LENGTH_SHORT).show();
				//Intent i = new Intent(v.getContext(), OrderOptions.class);
				Intent i = new Intent(v.getContext(), UI_Milk_Options.class);
				//i.putExtra("drink_name", drink);
				startActivity(i);
				}
			}
        });
    }
    
    private class SendPostRequest extends AsyncTask<String, Void, Map<String, List<String>>>
	{
	  private ArrayList<String> drinks;
	  private Map<String, List<String>> coffeeMap;
	  
		protected Map<String, List<String>> doInBackground(String... urls) 
		{
			for (String url : urls)
			{
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				
				try
				{
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			        nameValuePairs.add(new BasicNameValuePair("id", "menu"));
			        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        
					HttpResponse execute = client.execute(httpPost);
					InputStream content = execute.getEntity().getContent();
					processStream(content);
				}
				
				catch (Exception e)
				{
					e.printStackTrace();
					coffeeMap = new LinkedHashMap<String, List<String>>();
					return coffeeMap;
				}
			}
			
			return coffeeMap;
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
			
			title.setText(R.string.select_drink);
		}
		
		private void processStream(InputStream inputStream) 
		{			
			XmlPullParserFactory factory;
			String name = "";
			String category = "";
			String results = "";
			coffeeMap = new LinkedHashMap<String, List<String>>();
			drinks = new ArrayList<String>();
			
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
								if(!coffeeMap.containsKey(category))
								{
									ArrayList<String> al = new ArrayList<String>();
									coffeeMap.put(category, al);
								}
						
								eventType = xpp.next();
							}

							//Process name tag
							if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("name"))
							{
								name = xpp.nextText();
								drinks = (ArrayList<String>) coffeeMap.get(category);
								drinks.add(name);
								eventType = xpp.next();
								//results += name + ",";
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
    
}
 */

   




