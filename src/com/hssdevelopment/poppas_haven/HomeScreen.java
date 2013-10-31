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
	private Button getDirections;
	private Button startOrder;
	private Map coffeeMap;
	private List categoryList;
	
	//Address of Poppa's Haven
	public static final String COFFEE_ADDRESS = "800 NW Murray Blvd Portland, OR 97229";
	
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
		//msatpathy.wordpress.com/android/search-google-map-using-intent/
		if (v == getDirections)
		{
			//Pass Request to Google Maps API
			Intent geoIntent = new Intent(android.content.Intent.ACTION_VIEW,
										   Uri.parse("geo:o,o?q=" + COFFEE_ADDRESS));
			startActivity(geoIntent);
		}
		
		else if (v == startOrder)
		{
			Intent i = new Intent(v.getContext(), UI_Category_Menu.class);
			startActivity(i);
		}
		
	}
	
}

