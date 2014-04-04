package uk.ac.swan.digitaltrails.fragments;

import uk.ac.swan.digitaltrails.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("NewApi")
public class SearchListFragment extends Fragment{
	
	private OnItemSelectedListener listener;
	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_search_list,
	        container, false);
	    TextView view1 = (TextView) view.findViewById(R.id.textView1);
	    view1.setOnClickListener(new View.OnClickListener() {
	      @Override
	      public void onClick(View v) {
	        updateDetail();
	      }
	    });
	    TextView view2 = (TextView) view.findViewById(R.id.textView2);
	    view2.setOnClickListener(new View.OnClickListener() {
	      @Override
	      public void onClick(View v) {
	        updateDetail2();
	      }
	    });
	    TextView view3 = (TextView) view.findViewById(R.id.textView3);
	    view3.setOnClickListener(new View.OnClickListener() {
	      @Override
	      public void onClick(View v) {
	        updateDetail3();
	      }
	    });
	    TextView view4 = (TextView) view.findViewById(R.id.textView4);
	    view4.setOnClickListener(new View.OnClickListener() {
	      @Override
	      public void onClick(View v) {
	        updateDetail4();
	      }
	    });
	    return view;
	  }

	  public interface OnItemSelectedListener {
	      public void onSearchItemSelected(String link);
	    }
	  
	  @Override
	    public void onAttach(Activity activity) {
	      super.onAttach(activity);
	      if (activity instanceof OnItemSelectedListener) {
	        listener = (OnItemSelectedListener) activity;
	      } else {
	        throw new ClassCastException(activity.toString()
	            + " must implemenet MyListFragment.OnItemSelectedListener");
	      }
	    }
	  
	  
	  // May also be triggered from the Activity
	  public void updateDetail() {
	    // create fake data
	    String date1 = "TOO";
	    // Send data to Activity
	    listener.onSearchItemSelected(date1);
	 
	  }
	  
	  // May also be triggered from the Activity
	  public void updateDetail2() {
		  // create fake data
		  String date2 = "TOOOO";
		  // Send data to Activity
		  listener.onSearchItemSelected(date2);
	  }
	  
	  // May also be triggered from the Activity
	  public void updateDetail3() {
		  // create fake data
		  String date3 = "TOOOOOMMMM";
		  // Send data to Activity
		  listener.onSearchItemSelected(date3);
	  }

	  // May also be triggered from the Activity
	  public void updateDetail4() {
		  // create fake data
		  String date4 = "TOOOOOMMM MILNAAAA";
		  // Send data to Activity
		  listener.onSearchItemSelected(date4);
	  }
	  
} 