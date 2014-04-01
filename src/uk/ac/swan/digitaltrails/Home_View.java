package uk.ac.swan.digitaltrails;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.R.layout;
import uk.co.tmilner.whiterock.Walk_View;
import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Home_View extends ActionBarActivity{

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_view);
		
	}
	
	public void viewWalkButton(View view){
        Intent intent = new Intent(this, Walk_View.class);
        startActivity(intent);
    }
	
	public void logOutButton(View view){
        Intent intent = new Intent(this, Launch_View.class);
        startActivity(intent);
    }
	
	
	public void accountButton(View view){
        Intent intent = new Intent(this, Edit_Account_View.class);
        startActivity(intent);
    }
}
