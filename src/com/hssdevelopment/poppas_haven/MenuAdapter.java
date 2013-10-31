package com.hssdevelopment.poppas_haven;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {
	
	private Activity activity;
	private HashMap<String, String> description;
	private ArrayList<String> menu;
	private static LayoutInflater inflater = null;
	
	public MenuAdapter(Activity a, HashMap<String, String> description, ArrayList<String> menu)
	{
		this.description = description;
		this.menu = menu;
		this.activity = a;
		inflater = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public MenuAdapter(Activity a, ArrayList<String> milkOptions) 
	{
		this.description = new HashMap<String, String>();
		this.menu = milkOptions;
		this.activity = a;
		inflater = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() 
	{
		return menu.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return position;
	}

	@Override
	public long getItemId(int position	) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View vi = convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.list_row, null);
		
		TextView drinkName = (TextView)vi.findViewById(R.id.drink_name);
		TextView drinkDescription = (TextView)vi.findViewById(R.id.description);
		
		String name = menu.get(position);
		drinkName.setText(name);
		drinkDescription.setText(description.get(name));
		
		return vi;
	}
	
	public void setList(ArrayList<String> menu)
	{
		this.menu = menu;
	}

}
