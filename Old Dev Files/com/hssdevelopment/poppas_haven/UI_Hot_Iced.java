package com.hssdevelopment.poppas_haven;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UI_Hot_Iced extends ListActivity
{
	//Purpose: Get Hot/Iced Options from User
	
	//List for hotIced
	ArrayList<String> hotIced;
	HashMap<String, String> description;
	
	//Unique id of drink
	String id;
	
	//UI Controls
	private ListView list;
	private TextView orderField;
	private MenuAdapter ma;
	private View footer;
	
	//List View Child Click Handler
	private OnItemClickListener listClickHandler;
	
	//Order Field String
	private String order_status = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_milk_options);
		
		hotIced = new ArrayList<String>();
		hotIced.add("Hot");
		hotIced.add("Iced");
		
		description = new HashMap<String, String>();
		description.put("Hot", "It's Freezing outside, get me something warm!");
		description.put("Iced", "Yeah right, I need something refreshing");

		list = (ListView)getListView();
		
		Bundle extras = getIntent().getExtras();
		
		//Grab extras if they are available
		if (extras != null)
		{
			String drink = extras.getString("order_status");
			if(drink != null)
				order_status = drink;
		}
		
		listClickHandler = new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View child, int position, long pos) 
			{
				
				TextView category = (TextView)child.findViewById(R.id.drink_name);
				id = category.getText().toString();
				Toast.makeText(child.getContext(), id, Toast.LENGTH_SHORT).show();
				
				//Create New Intent
				//Intent i = new Intent(child.getContext(), UI_Milk_Options.class);
				//i.putExtra("drink_name", drink_name);
				//startActivity(i);
			}
		};
		
		list.setOnItemClickListener(listClickHandler);
		
		footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
		list.addFooterView(footer);
		
		orderField = (TextView)findViewById(R.id.order_status);
		orderField.append(" " + order_status);
		
		ma = new MenuAdapter(this, description, hotIced);
		list.setAdapter(ma);
	}
	
}