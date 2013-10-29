package uk.ac.swan.digitaltrails;

import java.io.IOException;
import uk.ac.swan.digitaltrails.R;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		Intent i = getIntent();
		int locationID = i.getIntExtra("locationID", 0);
		int walkID = i.getIntExtra("walkID", 0);

		TextView titleText = (TextView) findViewById(R.id.textView1);

		TextView infoText = (TextView) findViewById(R.id.textView2);
		ImageView imgView= (ImageView) findViewById(R.id.imageView1);
		imgView.setImageDrawable(this.getResources().getDrawable(R.drawable.abershore));
		infoText.setMovementMethod(new ScrollingMovementMethod());
		
		DatabaseHandler dbHandle=new DatabaseHandler(this);
		  try {
	         	dbHandle.createDataBase();
	        } catch (IOException ioe) {
	         	throw new Error("Unable to create database");
	        }
	        
			try {
				dbHandle.openDataBase();
	 		}catch(SQLException sqle){
	 				throw sqle;
	 		}
		
		
		WalkPoint thisPoint=dbHandle.getWalkPoint(locationID,walkID);
		dbHandle.close();
		
		if(thisPoint.getImgList()!=null|| thisPoint.getImgList().size()>0) {
			 
		        String imgName=thisPoint.getImgList().get(0);
			int path = getResources().getIdentifier(imgName, "drawable", "uk.ac.aber.townwalks");
			imgView.setImageResource(path);
		}
		infoText.setText(thisPoint.getInfoText());
		titleText.setText(thisPoint.getTitle());
	}
}
