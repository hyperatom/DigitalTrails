package uk.ac.swan.digitaltrails;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.R.layout;
import uk.co.tmilner.whiterock.Register;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Launch_View extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_view);
		
	}
	
	public void logInButton(View view){
        Intent intent = new Intent(this, Log_In_View.class);
        startActivity(intent);
    }
	
	public void registerButton(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
