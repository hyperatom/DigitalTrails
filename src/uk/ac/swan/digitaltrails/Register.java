package uk.ac.swan.digitaltrails;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Register extends Activity{

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
	}
	
	public void cancelRButton(View view){
        Intent intent = new Intent(this, Launch_View.class);
        startActivity(intent);
    }
	
	public void registerButton(View view){
        Intent intent = new Intent(this, Home_View.class);
        startActivity(intent);
    }
}
