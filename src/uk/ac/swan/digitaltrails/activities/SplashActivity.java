package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * @author Chris Lewis
 *
 */
public class SplashActivity extends ActionBarActivity{
    
	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		getSupportActionBar().hide();
		new Handler().postDelayed(new Runnable() {
	        
	        // Using handler with postDelayed called runnable run method

	        @Override
	        public void run() {
	            Intent i = new Intent(SplashActivity.this, LaunchActivity.class);
	            startActivity(i);

	            // close this activity
	            finish();
	        }
	    }, (long) (1.5*1000)); // wait for 5 seconds
	}
	
	/**
	 * @param view
	 */
	public void splashButton(View view){
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
    	//Log.i("SPLASH", "BUton pshed");
    }
    
   

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
	     
	    super.onDestroy();
	     
	}
}
    

