package com.hssdevelopment.poppas_haven;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/*Purpose: Error Message can be invoked in an activity to display 
 * an error message in a dialog box to the user by passing to this class the
 * message string and the current activity context, and calling the showErrorMessage function.
 */
public class Error_Message 
{
	private Activity UI_Context;
	private AlertDialog.Builder messageBuilder;
	private String errorMessage;
	
	//Error message to show at the top of the dialog box
	private static final String ERROR_MESSAGE_TITLE = "An Error Has Occured";
	
	Error_Message(Activity UI_Context, String errorMessage)
	{
		this.UI_Context = UI_Context;
		this.errorMessage = errorMessage;
	}
	
	/*showErrorMessage()
	 * Builds an Alert Dialog Box that shows an error message to the user with 
	 * an ok button. Immediately after calling this function, the calling activity
	 * should call finish()
	 */
	
	public void showErrorMessage()
	{
		//Create new Dialog builder
		messageBuilder = new AlertDialog.Builder(UI_Context);
		
		//Set positive button to okay and set okay button click to close
		//current activity
		messageBuilder.setPositiveButton("Okay", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				UI_Context.finish();
			}
		});
		
		//Set Error Message Title
		messageBuilder.setTitle(ERROR_MESSAGE_TITLE);
		
		//Set Error Message
		messageBuilder.setMessage(errorMessage);
		
		messageBuilder.show();
	}
}
