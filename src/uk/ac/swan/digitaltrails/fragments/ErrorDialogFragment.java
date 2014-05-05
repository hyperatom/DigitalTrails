package uk.ac.swan.digitaltrails.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author Lewis Hancock
 *
 */
public class ErrorDialogFragment extends DialogFragment {
	/**
	 * 
	 */
	private Dialog mDialog;
	
	/**
	 * 
	 */
	public ErrorDialogFragment() {
		super();
		mDialog = null;
	}
	
	/**
	 * @param dialog
	 */
	public void setDialog(Dialog dialog) {
		mDialog = dialog;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return mDialog;
	}
}
