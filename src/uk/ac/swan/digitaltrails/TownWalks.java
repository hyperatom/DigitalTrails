/*
 * Author : ErVaLt / techwavedev.com
 * Description : TabLayout Andorid App
 */
package uk.ac.swan.digitaltrails;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import uk.ac.swan.digitaltrails.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

public class TownWalks extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		 AssetManager am = getAssets();
		    try {
		    	// create a File object for the parent directory
		    	File mapDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/osmdroid/");
		    	// have the object build the directory structure, if needed.
		    	mapDirectory.mkdirs();
		        //String fileName = "AberaeronTiles.zip";
		    	String fileName = "Aberystwyth.zip";
		        File destinationFile = new File(Environment.getExternalStorageDirectory()+"/osmdroid/" + fileName);    
		       // InputStream in = am.open("AberaeronTiles.zip");
		        InputStream in = am.open("Aberystwyth.zip");
		        FileOutputStream f = new FileOutputStream(destinationFile); 
		        byte[] buffer = new byte[1024];
		        int len1 = 0;
		        while ((len1 = in.read(buffer)) > 0) {
		            f.write(buffer, 0, len1);
		        }
		        f.close();
		        Log.d("CopyFileFromAssetsToSD", "Tiles copied to SD.");
		    } catch (Exception e) {
		    	if(e.getMessage().length()>0) {
		        Log.d("CopyFileFromAssetsToSD", e.getMessage());
		    	}
		        Toast.makeText(getApplicationContext(), "Error copying map tiles to SD card. Offline maps unavailable.",Toast.LENGTH_LONG).show();
		    }
		
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost

		Intent intent = new Intent(this, MainActivity.class);
		tabHost.addTab(tabHost.newTabSpec("Welcome")
				.setIndicator("Welcome", res.getDrawable(R.drawable.ic_tab_main))
				.setContent(intent));

		Intent intent2 = new Intent(this, WalkListActivity.class);
		tabHost.addTab(tabHost
				.newTabSpec("Walks")
				.setIndicator("Walks", res.getDrawable(R.drawable.ic_tab_setup))
				.setContent(intent2));
		tabHost.setCurrentTab(0);

		// Set tabs Colors
		tabHost.setBackgroundColor(Color.BLACK);
		tabHost.getTabWidget().setBackgroundColor(Color.BLACK);

	}
}