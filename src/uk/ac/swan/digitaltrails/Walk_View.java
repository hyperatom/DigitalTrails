package uk.ac.swan.digitaltrails;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Walk_View extends Activity{

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walk_view);
	}
	
	public void walk1Button(View view){
        Intent intent = new Intent(this, Choose_Walk.class);
        startActivity(intent);
    }
}
