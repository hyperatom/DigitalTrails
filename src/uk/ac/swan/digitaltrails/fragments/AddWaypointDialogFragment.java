package uk.ac.swan.digitaltrails.fragments;

import com.google.android.gms.maps.model.LatLng;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.fragments.EditWaypointDialogFragment.EditWaypointDialogListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * @author Lewis Hancock
 *
 */
public class AddWaypointDialogFragment extends DialogFragment {

	/**
	 * @author Lewis Hancock
	 *
	 */
	public interface AddWaypointDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog, View view);
		public void onDialogNegativeClick(DialogFragment dialog, View view);
	}

	/**
	 * 
	 */
	public static final String ARG_POSITION = "position";
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
	public static final String ARG_DESCRIPTION = "descripiton";
	/**
	 * 
	 */
	public static final String ARG_INDEX = "index";
	
	/**
	 * 
	 */
	public static final String TAG = "AddWaypointDialogListener";
	
	/**
	 * 
	 */
	private AddWaypointDialogListener mListener;
	/**
	 * 
	 */
	private LatLng mPosition;
	
	/**
	 * 
	 */
	public AddWaypointDialogFragment() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onAttach(android.app.Activity)
	 */
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Check we have implemented listener.
		try {
			mListener = (AddWaypointDialogListener) getTargetFragment();
			Log.d(TAG, "Attached Listener to: " + getTargetFragment().toString());
		} catch (ClassCastException e) {
			throw new ClassCastException(getTargetFragment().toString() + " must implemented EditWaypointDialogListener");
		}
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final View view = getActivity().getLayoutInflater().inflate(R.layout.add_waypoint_info_dialog, null);
		builder.setView(view);
		setupUi(view);
		builder.setTitle(R.string.add_waypoint_dialog_title);
		// Add the positive button
		builder.setPositiveButton(R.string.confirm_waypoint, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogPositiveClick(AddWaypointDialogFragment.this, view);
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogNegativeClick(AddWaypointDialogFragment.this, view);
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
	protected void setupUi(View view) {
		String lat;
		String longitude;
		Bundle args = getArguments();
		if (args.getParcelable(ARG_POSITION) != null) {
			Log.d(TAG, "got position");
			mPosition = (LatLng) args.getParcelable(ARG_POSITION);
			lat = String.valueOf(mPosition.latitude);
			longitude = String.valueOf(mPosition.longitude);
		} else {
			Log.d(TAG, "no position");
			lat = "";
			longitude = "";
		}
		if (args.getString(ARG_TITLE) != null) {
			((EditText) view.findViewById(R.id.name_edit)).setText(args.getString(ARG_TITLE));
		} else {
			((EditText) view.findViewById(R.id.name_edit)).setText("None Found");
		}
		if (args.getString(ARG_DESCRIPTION) != null) {
			((EditText) view.findViewById(R.id.description_edit)).setText(args.getString(ARG_DESCRIPTION));		
		} else {
			((EditText) view.findViewById(R.id.description_edit)).setText("None Found");			
		}

		((EditText) view.findViewById(R.id.latitude_edit)).setText(lat);
		((EditText) view.findViewById(R.id.longitude_edit)).setText(longitude);
	}
}
