package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class LaunchActivity extends ActionBarActivity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_view);
		getSupportActionBar().hide();
	}
	
	public void logInButton(View view){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
	
	public void registerButton(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
	
	public void skipButton(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
