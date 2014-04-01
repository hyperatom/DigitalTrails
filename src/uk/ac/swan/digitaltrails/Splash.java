package uk.ac.swan.digitaltrails;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.R.layout;
import uk.ac.swan.digitaltrails.R.menu;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class Splash extends Activity{
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	
    public void splashButton(View view){
        Intent intent = new Intent(this, Launch_View.class);
        startActivity(intent);
    	//Log.i("SPLASH", "BUton pshed");
    }
    
}
