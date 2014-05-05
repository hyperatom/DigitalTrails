package uk.ac.swan.digitaltrails.fragments;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Audio;
import uk.ac.swan.digitaltrails.components.Video;
import uk.ac.swan.digitaltrails.components.Waypoint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.model.people.Person.Image;

/**
 * 
 * @author Lewis Hancock
 *
 */
/**
 * @author Lewis Hancock
 *
 */
public class InfoViewDialogFragment extends DialogFragment {
	
	/**
	 * 
	 */
	public static final String ARG_TITLE = "title";
	/**
	 * 
	 */
	public static final String ARG_SNIPPET = "snippet";
	/**
	 * 
	 */
	public static final String ARG_DESCRIPTION = "description";

	/**
	 * 
	 */
	public InfoViewDialogFragment() {
		super();
	}

	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final View view = getActivity().getLayoutInflater().inflate(R.layout.info_view_dialog, null);
		builder.setView(view);
		setupUi(view);
		builder.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// user cancelled
			}
		});
		return builder.create();
	}
	
	/**
	 * Initialise EditText fields with the correct values
	 * @param view The view to update
	 */
	/**
	 * @param view
	 */
	private void setupUi(View view) {
		Bundle args = getArguments();
		if (args.getString(ARG_TITLE) != null) {
			((TextView) view.findViewById(R.id.name_label)).setText(args.getString(ARG_TITLE));
		} else {
			((TextView) view.findViewById(R.id.name_label)).setText("Waypoint");
		}
		if (args.getString(ARG_DESCRIPTION) != null) {
			((TextView) view.findViewById(R.id.description_label)).setText(args.getString(ARG_DESCRIPTION));		
		} else {
			((TextView) view.findViewById(R.id.description_label)).setText("Waypoint");			
		}
		// TODO:  Display media somehow.
	}

}
