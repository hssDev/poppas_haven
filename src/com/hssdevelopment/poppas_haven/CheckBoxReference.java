package com.hssdevelopment.poppas_haven;

import android.widget.CheckBox;

public class CheckBoxReference 
{
	private int parentPosition;
	private int childPosition;
	private CheckBox currentCheckbox;
	
	public int getParentPosition()
	{
		return this.parentPosition;
	}

	public int getChildPosition() {
		return childPosition;
	}
	
	public void setParentPosition(int parentPosition)
	{
		this.parentPosition = parentPosition;
	}
	
	public void setChildPosition(int childPosition) {
		this.childPosition = childPosition;
	}

	public CheckBox getCurrentCheckbox() {
		return currentCheckbox;
	}

	public void setCurrentCheckbox(CheckBox currentCheckbox) {
		this.currentCheckbox = currentCheckbox;
	}
	
	public boolean isCurrentReference(int parentPosition, int childPosition)
	{
		if(parentPosition == this.parentPosition && childPosition == this.childPosition)
			return true;
		else 
			return false;
	}
}
