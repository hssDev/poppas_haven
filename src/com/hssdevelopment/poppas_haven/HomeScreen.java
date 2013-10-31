package com.hssdevelopment.poppas_haven;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/*This is the title screen for Poppa's Haven Mobile Ordering application
 * Special Thanks to Vogella.com, mkyong.com, android hive, and of course,
 * StackOverflow.com for all the help, tutorials, and general tips and tricks
 * for building this application
 * Copyright (c) 2013 Michael Hensley
 */

public class HomeScreen extends Activity implements OnClickListener
{
	//UI Controls
	Button getDirections;
	Button startOrder;
	Map coffeeMap;
	List categoryList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		
		//Get references to Buttons
		getDirections = (Button)findViewById(R.id.get_directions_button);
		startOrder = (Button)findViewById(R.id.start_order);
		
		//Set Button on click listener
		getDirections.setOnClickListener(this);
		startOrder.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) 
	{
		if (v == getDirections)
		{
			//Pass Request to Google Maps API
			/*
			String uri = "45.5252201, -122.814744";
			Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
			i.setClassName("com.google.android.apps.maps", "com.goog.android.maps.MapsActivity"); 
			startActivity(i);	
			*/
		}
		
		else if (v == startOrder)
		{
			Intent i = new Intent(v.getContext(), UI_Category_Menu.class);
			startActivity(i);
		}
		
	}
	
}

