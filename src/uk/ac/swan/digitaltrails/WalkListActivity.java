
package uk.ac.swan.digitaltrails;

import java.io.IOException;
import java.util.ArrayList;

import uk.ac.swan.digitaltrails.R;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WalkListActivity extends Activity {
	DatabaseHandler dbHandle;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab2);
		ListView listView = (ListView) findViewById(R.id.listView1);
		
		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data
		dbHandle=new DatabaseHandler(this);

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
		
		//DataSource dataSource=DataSource.getInstance();
		ArrayList<String> walkTitles=new ArrayList<String>();
		//for (Walk walk : dataSource.getWalks()) 
		for (Walk walk: dbHandle.getAllWalks())
		{
			walkTitles.add(walk.getTitle());
			
		}
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		  android.R.layout.simple_list_item_1, android.R.id.text1, walkTitles);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// Assign adapter to ListView
		listView.setAdapter(adapter); 
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			 
			@Override
			public void onItemClick(AdapterView arg0, View arg1, int position,
								long arg3) {
				Intent i = new Intent(WalkListActivity.this, WalkMapActivity.class);
				i.putExtra("position", position);
				
				Walk selectedWalk=dbHandle.getAllWalks().get(position);
				
				i.putExtra("walkId",selectedWalk.getID());
				dbHandle.close();

				startActivity(i);
				}
			});

	}
	
	

}
