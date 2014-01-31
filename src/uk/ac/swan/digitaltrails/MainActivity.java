package uk.ac.swan.digitaltrails;

import uk.ac.swan.digitaltrails.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab1);
		ImageView imgView = (ImageView) findViewById(R.id.imageView1);
		TextView titleText = (TextView) findViewById(R.id.textView1);
		TextView introText = (TextView) findViewById(R.id.textView2);

		// set app customization here
		imgView.setImageDrawable(this.getResources().getDrawable(
				R.drawable.abershore));

		titleText.setText("White Rock Digital Trails");
		introText
				.setText("Welcome to White Rock Digitial Trails for Android. Please select a walk of your choice from the 'Walks' tab to start exploring some of the places of interest.");

	}

	public void onClick(View src) {

	}
	
	public void walks(){
		Intent intent = new Intent(this, WalkListActivity.class);
		startActivity(intent);
	}
}