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

public class UI_Sauce_Syrup_Menu extends ExpandableListActivity{

	//This list contains the headers for expandable list, in this case "sauce" and "syrup"
    List<String> groupHeaders;
    //This list contains the different lists of children of the headers
    //There will be 2 lists in this map - a sauce list and syrup list
    //The key will correspond to a group header and the resulting list is its' children
    Map<String, List<String>> childMap;
    
    //UI Controls
    ExpandableListView expListView;
    Button sendSelection;
    CheckBox drinkSelection;
    ExpandableListAdapter expListAdapter;
    TextView title;
    TextView orderField;
    Context context;

	//Hash map for current drink order
	private HashMap<String, String> currentOrder;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	//Inflate the UI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_sauce_syrup);
        context = this;
        
		Bundle extras = getIntent().getExtras();
		//Grab extras if they are available
		if (extras != null)
		{
			try
			{
				currentOrder = (HashMap<String, String>) extras.getSerializable("current_order");
			}
			
			catch (NullPointerException e)
			{
				e.printStackTrace();
				Toast.makeText(context, "An Error has Occured Loading Menu", Toast.LENGTH_LONG).show();
				finish();
			}
		}
		
		//Initialize sauce/syrup headers and child element test data
        groupHeaders = new ArrayList<String>();
        childMap = new LinkedHashMap<String, List<String>> ();
        
        //Pushing test data into Container
        groupHeaders.add("Sauce");
        groupHeaders.add("Syrup");
         
        //Sample Syrup Data
        ArrayList<String> syrupList = new ArrayList<String>();
        syrupList.add("Vanilla");
        syrupList.add("Mint");
        syrupList.add("Marshmellow");
        
        //Sample Sauce Data
        ArrayList<String> sauceList = new ArrayList<String>();
        sauceList.add("Chocolate");
        sauceList.add("Carmel");
        sauceList.add("Dark Chocolate");
        
        childMap.put("Sauce", sauceList);
        childMap.put("Syrup", syrupList);
       
        //Set menu title
        title = (TextView)findViewById(R.id.main_menu_title);
        title.setText(R.string.select_flavor);
        
        //get reference to expandable list view
        expListView = getExpandableListView();
        
        //Inflate the bottom of the menu and set order field
		View footer = getLayoutInflater().inflate(R.layout.next_button, null);
		expListView.addFooterView(footer);
		orderField = (TextView)findViewById(R.id.order_status);
		setOrderField();
		
		//create and set Expandable List Adapter
        expListAdapter = new ExpandableListAdapter(this, groupHeaders, childMap);
        expListView.setAdapter(expListAdapter);
        
        //Get reference to Review Order Button
		sendSelection = (Button)findViewById(R.id.next_button);

		//Download Sauce/Syrup from online database via http post request
		//SendPostRequest pt = new SendPostRequest();
		//pt.execute("http://www.hssdevelopment.com/menu.php");
 
		/*Set the child click event for this menu. Since Android recycles list views
		 *Get reference to checked box and store it in the menu adapter for reference
		 *later
		 */
        expListView.setOnChildClickListener(new OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) 
            {
            	
            	final CheckBox cb = (CheckBox)v.findViewById(R.id.user_selection);
            	final TextView tv = (TextView)v.findViewById(R.id.child_textview);
            	
            	String data = tv.getText().toString();

            	//If box is checked, uncheck it and remove it from adapter list of
            	//active checkboxes
            	if (cb.isChecked())
            	{
            		cb.setChecked(false);
            		expListAdapter.removeActiveCheckBox(groupPosition, childPosition, data);
            	}
            	
            	else
            	{
            		cb.setChecked(true);
            		expListAdapter.setActiveCheckBox(groupPosition, childPosition, data);
            	}
            	
            	Toast.makeText(v.getContext(), "Routed through click listener", Toast.LENGTH_SHORT).show();
            	expListAdapter.notifyDataSetChanged();
            	return true;
            }
        });
        
        sendSelection.setOnClickListener(new OnClickListener()
        {
        	//When review order is clicked, get all user choices and insert them
        	//into current order and start review order activity. 
			@Override
			public void onClick(View v) 
			{
				String modifier = expListAdapter.getUserChoices();
				currentOrder.put("modifier", modifier);
				Intent i = new Intent(v.getContext(), ReviewOrder.class);
				i.putExtra("current_order", currentOrder);
				startActivity(i);
			}
        });
    }
    //This function sets the order field to the current status the user
    //Has selected up to this point
    private void setOrderField() 
    {
    	String size = currentOrder.get("size");
    	String temperature = currentOrder.get("drinkTemperature");
    	String drink = currentOrder.get("name");
    	String milk = currentOrder.get("milk");
		orderField.setText("Order Status: " + size + " " + temperature + " "
							+ drink + "\n" + "Milk: " + milk); 
	}

    /*
     * Class being built to download Sauce and Syrup from HSS development
     * Once completed, this class will download the necessary data and 
     * Set the expandable list adapter with updated sauce and syrup data
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
*/
}

