package com.hssdevelopment.poppas_haven;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	 	private Activity context;
	 	//Key will be the name of the parent, List will be the 
	 	//Collection of children mapped to that parent
	    private Map<String, List<String>> childMap;
	    private List<String> parentList;
	    private CheckBoxChecker currentCheckedList;
	 
	    public ExpandableListAdapter(Activity context, List<String> parentList,
	            Map<String, List<String>> childMap) 
	    {
	        this.context = context;
	        this.parentList = parentList;
	        this.childMap = childMap;
	        this.currentCheckedList = new CheckBoxChecker(parentList.size());
	    }
	 
	    public Object getChild(int groupPosition, int childPosition) 
	    {
	        return childMap.get(parentList.get(groupPosition)).get(childPosition);
	    }
	 
	    public long getChildId(int groupPosition, int childPosition) {
	        return childPosition;
	    }
	 
	    public View getChildView(final int groupPosition, final int childPosition,
	            				 boolean isLastChild, View convertView, ViewGroup parent) 
	    {
	        final String childItem = (String) getChild(groupPosition, childPosition);
	        LayoutInflater inflater = context.getLayoutInflater();
	 
	        if (convertView == null) 
	        {
	            convertView = inflater.inflate(R.layout.child_item, null);
	        }
	 
	        final TextView item = (TextView) convertView.findViewById(R.id.child_textview);
	        final CheckBox select = (CheckBox) convertView.findViewById(R.id.user_selection);
	        
	        //Is check box checked?
	        if(select.isChecked())
	        {
	        	//Check current checked list to see if child is supposed to be checked
	        	//If view is recycled, uncheck the box
	        	if(!currentCheckedList.isChecked(groupPosition, childPosition))
	        	{
	        		select.setChecked(false);
	        	}
	        }
	        
	        //check box is not checked
	        if(!select.isChecked())
	        {
	        	if(currentCheckedList.isChecked(groupPosition, childPosition))
	        	{
	        		select.setChecked(true);
	        	}
	        }
	        
	        item.setText(childItem);
	        return convertView;
	    }
	 
	    public int getChildrenCount(int groupPosition) 
	    {
	        return childMap.get(parentList.get(groupPosition)).size();
	    }
	 
	    public Object getGroup(int groupPosition) 
	    {
	        return parentList.get(groupPosition);
	    }
	 
	    public int getGroupCount() 
	    {
	        return parentList.size();
	    }
	 
	    public long getGroupId(int groupPosition) {
	        return groupPosition;
	    }
	 
	    public View getGroupView(int groupPosition, boolean isExpanded,
	            				 View convertView, ViewGroup parent) 
	    {
	        String parentName = (String) getGroup(groupPosition);
	        if (convertView == null) 
	        {
	            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.group_item, null);
	        }
	        
	        TextView item = (TextView) convertView.findViewById(R.id.category);
	        item.setTypeface(null, Typeface.BOLD);
	        item.setText(parentName);
	        
	        return convertView;
	    }
	 
	    public boolean hasStableIds() {
	        return true;
	    }
	 
	    public boolean isChildSelectable(int groupPosition, int childPosition) 
	    {
	        return true;
	    }
	    
		public void setActiveCheckBox(int groupPosition, int childPosition, String data) 
		{
			currentCheckedList.setActiveCheckBox(groupPosition, childPosition);
			currentCheckedList.addCheckBoxData(data);
		}
		
		public void removeActiveCheckBox(int groupPosition, int childPosition, String data)
		{
			currentCheckedList.disableActiveCheckBox(groupPosition, childPosition);
			currentCheckedList.removeCheckBoxData(data);
		}
		
		public List<String> getParentList()
		{
			return this.parentList;
		}
		
		public Map<String, List<String>> getChildMap()
		{
			return this.childMap;
		}
		
		public void setParentList(List<String> parentList)
		{
			this.parentList = parentList;
		}
		
		public void setChildMap(Map<String, List<String>> childMap)
		{
			this.childMap = childMap;
		}

		//This method takes the array list in helper class CheckBoxChecker,
		//Concatenates the list into a single xml string to be sent to the 
		//next activity
		public String getUserChoices() 
		{
			String xmlData = "";
			ArrayList<String> dataList = currentCheckedList.getCheckBoxData();
			for(String s : dataList)
			{
				xmlData += (s + " ");
			}
			
			return xmlData;
		}	
}
