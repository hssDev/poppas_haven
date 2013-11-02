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


/*
 * Home Screen Class for Poppas Haven Application
 * Purpose: Direct users to either a mobile order system implemented
 * in the application or to send address coordinates to the Google Map
 * API in order to give customer directions to the store. 
 */
public class HomeScreen extends Activity implements OnClickListener
{
	//UI Controls
	private Button getDirections;
	private Button startOrder;
	
	//Address of Poppa's Haven
	private static final String POPPAS_HAVEN_ADDRESS = "800 NW Murray Blvd Portland, OR 97229";
	//Coordinate prefix for Google Maps API
	private static final String COORDINATE_PREFIX = "geo:o,o?q=";
	
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

	/*onClick Purpose: Provide Button functionality for main screen buttons
	 * getDirections and startOrder. 
	 */
	@Override
	public void onClick(View v) 
	{
		//If User clicks on directions button, launch Google Maps Activity to give directions
		//msatpathy.wordpress.com/android/search-google-map-using-intent/
		if (v == getDirections)
		{
			
			//Pass Request to Google Maps API
			Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW,
							   Uri.parse(COORDINATE_PREFIX + POPPAS_HAVEN_ADDRESS));
			startActivity(mapIntent);
		}
		
		//Else launch ordering system's first activity, UI_Category_Menu.class
		else if (v == startOrder)
		{
			Intent new_order = new Intent(v.getContext(), UI_Category_Menu.class);
			startActivity(new_order);
		}
		
	}
	
}

