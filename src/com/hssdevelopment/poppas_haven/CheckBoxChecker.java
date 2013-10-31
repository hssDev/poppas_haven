package com.hssdevelopment.poppas_haven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CheckBoxChecker 
{
	//Purpose: Defines an object that keeps an active list of all CheckBoxes
	//checked in an expandable list view. Since expandable list views recycle views
	//function Boolean isChecked(int parent, int child) can be called to verify
	//that the checkbox should or should not be checked. 
	
	//Used to track checked child textboxes. Key is the parent, HashSet contains the 
	//positions of all checked children per parent.
	Map<Integer, HashSet<Integer>> parentMap;
	//Keeps a running list of the text associated with checking a checkbox
	//Ex: Someone checks the option for carmel sauce, add "carmel sauce" 
	//into checkboxData
	ArrayList<String> checkboxData;
	int parentSize;
	
	CheckBoxChecker(int parentSize)
	{
		this.parentSize = parentSize;
		parentMap = new HashMap<Integer, HashSet<Integer>>();
		checkboxData = new ArrayList<String>();
		
	}
	
	//Pre-Condition: Must be used by the same expandable list that
	//created this object. 
	//Post-Condition: Will return true if child CheckBox is actively 
	//selected. To set an active CheckBox call setActiveCheckBox(int parentPosition, int childPosition);
	public boolean isChecked(int parentPosition, int childPosition)
	{
		//Search the child set for given parent for an active CheckBox at that location
		if(parentMap.containsKey(parentPosition))
			return parentMap.get(parentPosition).contains(childPosition);
		else
			return false;
	}
	//Adds active CheckBox to appropriate child list
	public void setActiveCheckBox(int parentPosition, int childPosition)
	{
		if(parentMap.containsKey(parentPosition))
			parentMap.get(parentPosition).add(childPosition);
		else
		{
			HashSet<Integer> hs = new HashSet<Integer>();
			hs.add(childPosition);
			parentMap.put(parentPosition, hs);
		}
	}
	
	//Removes active CheckBox from appropriate child list
	public void disableActiveCheckBox(int parentPosition, int childPosition)
	{
		if(parentMap.containsKey(parentPosition))
			parentMap.get(parentPosition).remove(childPosition);
	}
	
	//Returns reference to CheckBox List Data:
	public ArrayList<String>getCheckBoxData()
	{
		return this.checkboxData;
	}
	
	//Add new data to checkboxData
	public void addCheckBoxData(String data)
	{
		checkboxData.add(data);
	}
	
	//Remove data from list
	public void removeCheckBoxData(String data)
	{
		checkboxData.remove(data);
	}
	
}
