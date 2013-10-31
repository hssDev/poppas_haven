package com.hssdevelopment.poppas_haven;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class MilkOptions extends Activity implements OnClickListener
{
	TextView regular, nonfat, whole, breve, soy, hemp;
	CheckBox regularBox, nonfatBox, wholeBox, breveBox, soyBox, hempBox;
	CheckBox currentCheckBox;
	Button sendMilkDetails;
	
	//Extras
	String drinkName; 
	String milk = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.milk_options);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			drinkName = extras.getString("drink_name");
		}
		
		
		regular = (TextView)findViewById(R.id.two_percent_textview);
		nonfat = (TextView)findViewById(R.id.nonfat_textview);
		whole = (TextView)findViewById(R.id.whole_milk_textview);
		breve = (TextView)findViewById(R.id.breve_textview);
		soy = (TextView)findViewById(R.id.soy_textview);
		hemp = (TextView)findViewById(R.id.hemp_textview);
		
		regularBox = (CheckBox)findViewById(R.id.two_percent_checkbox);
		nonfatBox = (CheckBox)findViewById(R.id.nonfat_checkbox);
		wholeBox = (CheckBox)findViewById(R.id.whole_milk_checkbox);
		breveBox = (CheckBox)findViewById(R.id.breve_checkbox);
		soyBox = (CheckBox)findViewById(R.id.soy_checkbox);
		hempBox = (CheckBox)findViewById(R.id.hemp_checkbox);
		
		sendMilkDetails = (Button)findViewById(R.id.send_milk_details);
		
		regular.setOnClickListener(this); 
		nonfat.setOnClickListener(this);
		whole.setOnClickListener(this); 
		breve.setOnClickListener(this);
		soy.setOnClickListener(this);
		hemp.setOnClickListener(this);
		regularBox.setOnClickListener(this);
		nonfatBox.setOnClickListener(this); 
		wholeBox.setOnClickListener(this);
		breveBox.setOnClickListener(this); 
		soyBox.setOnClickListener(this);
		hempBox.setOnClickListener(this);
		sendMilkDetails.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) 
	{
		if(v == regular || v == regularBox)
		{
			if(currentCheckBox == null)
			{
				currentCheckBox = regularBox;
				currentCheckBox.setChecked(true);
			}
			
			else
			{
				currentCheckBox.setChecked(false);
				currentCheckBox = regularBox;
				currentCheckBox.setChecked(true);
			}
			
			milk = "";
		}
			
		else if(v == nonfat || v == nonfatBox)
		{
			if(currentCheckBox == null)
			{
				currentCheckBox = nonfatBox;
				currentCheckBox.setChecked(true);
			}
			
			else
			{
				currentCheckBox.setChecked(false);
				currentCheckBox = nonfatBox;
				currentCheckBox.setChecked(true);
			}
			
			milk = "Non-Fat";
		}
			
		else if (v == whole || v == wholeBox)
		{
			if(currentCheckBox == null)
			{
				currentCheckBox = wholeBox;
				currentCheckBox.setChecked(true);
			}
			
			else
			{
				currentCheckBox.setChecked(false);
				currentCheckBox = wholeBox;
				currentCheckBox.setChecked(true);
			}
			
			milk = "Whole Milk";
		}
		
		else if (v == breve || v == breveBox)
		{
			if(currentCheckBox == null)
			{
				currentCheckBox = breveBox;
				currentCheckBox.setChecked(true);
			}
			
			else
			{
				currentCheckBox.setChecked(false);
				currentCheckBox = breveBox;
				currentCheckBox.setChecked(true);
			}
			
			milk = "Breve";
		}
		
		else if (v == soy || v == soyBox)
		{
			if(currentCheckBox == null)
			{
				currentCheckBox = soyBox;
				currentCheckBox.setChecked(true);
			}
			
			else
			{
				currentCheckBox.setChecked(false);
				currentCheckBox = soyBox;
				currentCheckBox.setChecked(true);
			}
			
			milk = "Soy";
		}
		
		else if (v == hemp || v == hempBox)
		{
			if(currentCheckBox == null)
			{
				currentCheckBox = hempBox;
				currentCheckBox.setChecked(true);
			}
			
			else
			{
				currentCheckBox.setChecked(false);
				currentCheckBox = hempBox;
				currentCheckBox.setChecked(true);
			}
			
			milk = "Hemp";
		}
			
		else if (v == sendMilkDetails)
		{
			//Intent i = new Intent(v.getContext(), OrderOptions.class);
			//i.putExtra("drink_name", drinkName);
			//i.putExtra ("milk", milk);
			//startActivity(i);
		}
		
	}
	
}
