package com.hssdevelopment.poppas_haven;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UI_Milk_Options extends ListActivity
{
	//Purpose: Get Milk Options from the user for Milk Based Drink
	
	//Activity Context
	private Context context;
	//List for milk
	ArrayList<String> milkOptions;

	//UI Controls
	private ListView list;
	private TextView orderField;
	private MenuAdapter ma;
	private View footer;
	
	//List View Child Click Handler
	private OnItemClickListener listClickHandler;
	
	//Hash map for current drink order
	private HashMap<String, String> currentOrder;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		//Inflate UI
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_milk_options);
		context = this;
		
		//Initialize all Milk Options locally
		//This will be replaced by server data
		milkOptions = new ArrayList<String>();
		milkOptions.add("Regular(2%)");
		milkOptions.add("Non-Fat");
		milkOptions.add("Whole");
		milkOptions.add("Soy");
		milkOptions.add("Hemp");
		milkOptions.add("Breve");

		//Get reference to list
		list = (ListView)getListView();
		
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
			}
		}
		
		//Set list click handler for Milk Options
		//This will start a new activity when it is finished. 
		listClickHandler = new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View child, int position, long pos) 
			{	
				//Update currentOrder with milk option
				TextView category = (TextView)child.findViewById(R.id.drink_name);
				String milk = category.getText().toString();
				currentOrder.put("milk", milk);
				
				//Create New Intent
				Intent i = new Intent(child.getContext(), UI_Sauce_Syrup_Menu.class);
				i.putExtra("current_order", currentOrder);
				startActivity(i);
			}
		};
		
		//Set the listener
		list.setOnItemClickListener(listClickHandler);
		
		//Inflate the footer of the menu
		footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
		list.addFooterView(footer);
		
		//Set order field text
		orderField = (TextView)findViewById(R.id.order_status);
		setOrderField();
		
		//Create and set menu adapter
		ma = new MenuAdapter(this, milkOptions);
		list.setAdapter(ma);
	}

	//Functions that populates order field with drink status selected so far by the user
	private void setOrderField() 
	{
		String size = currentOrder.get("size");
		String temperature = currentOrder.get("drinkTemperature");
		String drinkName = currentOrder.get("name");
		
		orderField.setText("Order Status: " + size + " " + temperature + " " + drinkName);
	}
	
}
