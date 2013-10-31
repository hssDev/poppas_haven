package com.hssdevelopment.poppas_haven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UI_Drink_Menu extends ListActivity 
{
	//Activity Context
	Context context;
	
	//UI Controls
	private ListView list;
	private TextView listTitle;
	private MenuAdapter ma;
	private View footer;
	
	//List View Child Click Handler
	private OnItemClickListener listClickHandler;
	
	//Dialog Builders
	private AlertDialog.Builder hotColdBuilder;
	private AlertDialog.Builder sizeBuilder;
	
	//Hash map for menu data
	private HashMap<String, List<String>> menuData;
	
	//Hash map for current drink order
	private HashMap<String, String> currentOrder;
	
	//name of drink user selects
	private String drinkName;
	
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState)
	{
		//Inflate the UI
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_drink_menu);
		
		//Get reference to current activity context
		context = this;
		
		//Initialize drinkOptions container
		ArrayList<String> drinkOptions = new ArrayList<String>();
		
		//Sample descriptions for the classics category to demo the full UI
		HashMap<String, String> descriptions = new HashMap<String, String>();
		descriptions.put("Latte", "The Classic Espresso Beverage");
		descriptions.put("Mocha", "Milk, Chocolate, Espresso");
		descriptions.put("Mayan Mocha", "Mocha with a twist");
		descriptions.put("Cappuccino", "Foam, Espresso, Milk");
		descriptions.put("Americano", "Shots Over Water");

		//Initialize menu adapter
		ma = new MenuAdapter(this, descriptions, drinkOptions);

		//Get reference to UI list and Title of the list
		list = (ListView)getListView();
		listTitle = (TextView)findViewById(R.id.select_beverage_textview);
		
		Bundle extras = getIntent().getExtras();
		
		//Grab extras if they are passed succesfully to this activity
		if (extras != null)
		{
			try
			{
				//Get menu Data map to inflate the correct list
				menuData = (HashMap<String, List<String>>) extras.getSerializable("menu_data");
				//Get name of category selected
				String list_title = extras.getString("category_name");
				//Get current order
				currentOrder = (HashMap<String, String>) extras.getSerializable("current_order");
				
				//Set name of list
				listTitle.setText(list_title);
				
				//Set drinkOptions to appropriate mapping from menu_data
				drinkOptions = (ArrayList<String>) menuData.get(list_title);
			}
			//If an error is caught getting extra, finish activity and return to 
			//main screen
			catch(NullPointerException e)
			{
				Toast.makeText(context, "An Error has occured", Toast.LENGTH_LONG).show();
				finish();
			}
		}
		
		//Set List Click Listener
		listClickHandler = new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View child, int position, long pos) 
			{
				//Get name of the drink that was passed
				TextView category = (TextView)child.findViewById(R.id.drink_name);
				drinkName = category.getText().toString();
				
				//Build new alert Dialog box to ask if the beverage is hot or iced
				hotColdBuilder = new AlertDialog.Builder(context);
				hotColdBuilder.setTitle("Hot or Iced?");
				
				//Assign buttons to different options
				hotColdBuilder.setPositiveButton("Iced", new DialogInterface.OnClickListener() 
			    {
					//If user selects iced option- insert iced into current order
					//and launch size option dialogue box with Iced = true;
					@Override
					 public void onClick(DialogInterface dialog, int which) 
					 {
						currentOrder.put("drinkTemperature", "Iced");
						UI_Drink_Menu.this.buildSizeOptions(true);
					 }
			    });  
				
				hotColdBuilder.setNegativeButton("Hot", new DialogInterface.OnClickListener() 
				{
				    @Override
				     public void onClick(DialogInterface dialog, int which) 
				     {
				    	//If user selects hot option- insert hot into current order
				    	//and launch size option dialogue box with Iced = false;
				    	currentOrder.put("drinkTemperature", "Hot");
				    	UI_Drink_Menu.this.buildSizeOptions(false);
				     }
			    });
				
				//Show dialogue box
				hotColdBuilder.show();
			}
		};
		
		//Set on click listener
		list.setOnItemClickListener(listClickHandler);
		
		//Inflate the footer
		footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
		list.addFooterView(footer);
		
		//Set list adapter
		ma = new MenuAdapter(this, descriptions, drinkOptions);
		list.setAdapter(ma);
	}
	/*This method builds a second dialogue box and launches the next menu option
	 * activity. It is called from the onChildClick listener in this activity.
	 * Once this method finishes running, this activity will finish
	 * 
	 */
	protected void buildSizeOptions(boolean isIced) 
	{
		//Create new container to bind to dialogue builder
		final ArrayList<String> sizeOptions = new ArrayList<String>();
		sizeOptions.add("12 oz");
		sizeOptions.add("16 oz");
		sizeOptions.add("20 oz");
		
		//If the user selected Iced beverage, add 24 oz option
		if(isIced)
			sizeOptions.add("24 oz");
		
		//convert arraylist to char sequence for the alert dialog builder
		final CharSequence[] drinkSizeOptions = sizeOptions.toArray(new CharSequence[sizeOptions.size()]);
		
		sizeBuilder = new AlertDialog.Builder(context);
		sizeBuilder.setTitle("Select Size");
		sizeBuilder.setItems(drinkSizeOptions, new DialogInterface.OnClickListener() 
				   {
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						//Create new intent
						Intent i = new Intent(context, UI_Milk_Options.class);
						//Put drink into currentOrder container
						currentOrder.put("name", drinkName);
						//Put drink size into currentOrder container
						currentOrder.put("size", sizeOptions.get(which));
						
						i.putExtra("current_order", currentOrder);
						startActivity(i);
					}
				});
		sizeBuilder.show();
	}
}
