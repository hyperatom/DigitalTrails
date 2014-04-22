package uk.ac.swan.digitaltrails.fragments;

import uk.ac.swan.digitaltrails.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

public class EditWaypointDialogFragment extends DialogFragment {

	public interface EditWaypointDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog, View view);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	public static final String ARG_POSITION = "position";
	public static final String ARG_TITLE = "title";
	public static final String ARG_SNIPPET = "snippet";
	
	private static final String TAG = "EditWaypointDialogFragment";
	private EditWaypointDialogListener mListener;
	private LatLng mPosition;


	public LatLng getPosition() {
		return mPosition;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Check we have implemented listener.
		try {
			mListener = (EditWaypointDialogListener) getTargetFragment();
		} catch (ClassCastException e) {
			throw new ClassCastException(getTargetFragment().toString() + " must implemented EditWaypointDialogListener");
		}
	}

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
				mListener.onDialogPositiveClick(EditWaypointDialogFragment.this, view);
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogNegativeClick(EditWaypointDialogFragment.this);
			}
		});
		return builder.create();
	}

	/**
	 * Initialise EditText fields with the correct values
	 * @param view The view to update
	 */
	private void setupUi(View view) {
		String lat;
		String longitude;
		Bundle args = getArguments();
		if (args.get(ARG_POSITION) != null) {
			mPosition = (LatLng) args.get(ARG_POSITION);
			lat = String.valueOf(mPosition.latitude);
			longitude = String.valueOf(mPosition.longitude);
		} else {
			lat = "";
			longitude = "";
		}
		if (args.getString(ARG_TITLE) != null) {
			((EditText) view.findViewById(R.id.name_edit)).setText(args.getString(ARG_TITLE));
		} else {
			((EditText) view.findViewById(R.id.name_edit)).setText("Waypoint");
		}
		if (args.getString(ARG_SNIPPET) != null) {
			((EditText) view.findViewById(R.id.description_edit)).setText(args.getString(ARG_SNIPPET));		
		} else {
			((EditText) view.findViewById(R.id.description_edit)).setText("Waypoint");			
		}

		((EditText) view.findViewById(R.id.latitude_edit)).setText(lat);
		((EditText) view.findViewById(R.id.longitude_edit)).setText(longitude);
	}


}
