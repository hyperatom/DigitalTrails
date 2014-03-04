package uk.ac.swan.digitaltrails;

import uk.ac.swan.digitaltrails.R;

import android.app.Activity;
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

		titleText.setText("Database branch blah");
	}

	public void onClick(View src) {

	}
}