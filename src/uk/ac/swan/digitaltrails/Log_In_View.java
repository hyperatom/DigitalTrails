package uk.ac.swan.digitaltrails;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Log_In_View extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_in_view);
	}
	
	public void cancelButton(View view){
        Intent intent = new Intent(this, Launch_View.class);
        startActivity(intent);
    }
	
	public void signInButton(View view){
        Intent intent = new Intent(this, Home_View.class);
        startActivity(intent);
    }
}
