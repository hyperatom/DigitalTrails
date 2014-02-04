package uk.ac.swan.digitaltrails;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//TODO: Databases are placed onto the SD card, no other strategy if SD card missing.
//TODO: Class needs a total rewrite to match our new DB structure.
public class DatabaseHandler extends SQLiteOpenHelper {

	private static final String TABLE_PLACES = "places";

	private static final String TABLE_LOCATIONS = "locations";

	private static final String TABLE_WALKS = "walks";

	private static String DB_PATH = "/data/data/uk.ac.swan.digitaltrails/databases/";

	private static String DB_NAME = "aberystwyth.sqlite3"; //TODO: change
	// private static String DB_NAME = "aberaeron.sqlite3";

	private SQLiteDatabase myDataBase;

	private final Context myContext;

	public DatabaseHandler(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
			Log.i("dbhandle", "db exists");
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {
		Log.i("dbhandle", "copy db");

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;

		// SQLiteDatabase.NO_LOCALIZED_COLLATORS
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.NO_LOCALIZED_COLLATORS);

	}

	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	//TODO: delete this when we have the new database. This is a tragic mess.
	private ArrayList<Integer> getLocationIDsForWalkID(int walkID) {
		ArrayList<Integer> idList = new ArrayList<Integer>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PLACES
				+ " WHERE walkId=" + walkID;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Integer locationID = Integer.parseInt(cursor.getString(2));
				idList.add(locationID);
			} while (cursor.moveToNext());
		}

		cursor.close();
		// return locationID list
		return idList;
	}

	//Why would we ever want to query for one point? It's nice to have, but in practice shouldn't be used.
	//TODO: One query and an array solves this crap.
	public WalkPoint getWalkPoint(int walkPointID, int walkID) { // this is the
																	// locationID
																	// (unique&global)

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS + " WHERE id="
				+ walkPointID;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null)
			cursor.moveToFirst();
		// new WalkPoint(0, 52.41630256175995,-4.065622687339783,
		// "Astute Office", "This is the Astute office...")
		WalkPoint walkPoint = new WalkPoint(Integer.parseInt(cursor
				.getString(0)), Double.parseDouble(cursor.getString(3)),
				Double.parseDouble(cursor.getString(4)), cursor.getString(1),
				cursor.getString(5));
		walkPoint.setImgList(getImageNamesForPoint(walkPoint.getID(), walkID));

		cursor.close();
		return walkPoint;
	}

	
	// WHy have they not taken the whole walk? Just taking english is silly.
	public Walk getWalkID(int walkID) {
		String selectQuery = "SELECT  * FROM " + TABLE_WALKS + " where id ="
				+ walkID;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor != null)
			cursor.moveToFirst();

		// THIS ALL NEEDS CHANGING BASED ON DATABASE.
		// 0=id, 1=titleEng, 2=titleWelsh, 3=shortWelshDesc, 4=shortEngDesc,
		// 5=engDesc, 6=welshDesc
		Walk walk = new Walk();
		walk.setID(Integer.parseInt(cursor.getString(0)));
		walk.setTitle(cursor.getString(1));
		walk.setShortDescription(cursor.getString(4));
		walk.setLongDescription(cursor.getString(5));
		walk.setPoints(getLocationIDsForWalkID(walk.getID()));

		cursor.close();
		// return list
		return walk;
	}

	// much more like it.
	public ArrayList<WalkPoint> getWalkPointsForWalkID(int walkID) {
		ArrayList<WalkPoint> walkPoints = new ArrayList<WalkPoint>();
		for (Integer walkPointID : getLocationIDsForWalkID(walkID)) {
			walkPoints.add(getWalkPoint(walkPointID.intValue(), walkID));
		}

		return walkPoints;
	}

	
	public ArrayList<Walk> getAllWalks() {
		ArrayList<Walk> walkList = new ArrayList<Walk>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_WALKS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				// 0=id, 1=titleEng, 2=titleWelsh, 3=shortWelshDesc,
				// 4=shortEngDesc, 5=engDesc, 6=welshDesc
				Walk walk = new Walk();
				walk.setID(Integer.parseInt(cursor.getString(0)));
				walk.setTitle(cursor.getString(1));
				walk.setShortDescription(cursor.getString(4));
				walk.setLongDescription(cursor.getString(5));
				walk.setPoints(getLocationIDsForWalkID(walk.getID()));
				walkList.add(walk);
			} while (cursor.moveToNext());
		}
		cursor.close();

		// return list
		return walkList;
	}

	/*
	 * public ArrayList<WalkPoint> getAllPoints() { ArrayList<WalkPoint>
	 * walkPointList = new ArrayList<WalkPoint>(); // Select All Query String
	 * selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS;
	 * 
	 * SQLiteDatabase db = this.getWritableDatabase(); Cursor cursor =
	 * db.rawQuery(selectQuery, null);
	 * 
	 * // looping through all rows and adding to list if (cursor.moveToFirst())
	 * { do { Log.i("", cursor.getString(1)); WalkPoint walkPoint = new
	 * WalkPoint
	 * (Integer.parseInt(cursor.getString(0)),Double.parseDouble(cursor.
	 * getString
	 * (3)),Double.parseDouble(cursor.getString(4)),cursor.getString(1),
	 * cursor.getString(5));
	 * walkPoint.setImgList(getImageNamesForPoint(walkPoint.getID()));
	 * walkPointList.add(walkPoint); } while (cursor.moveToNext()); }
	 * 
	 * // return contact list cursor.close(); return walkPointList; }
	 */

	private int getPlaceIdForPoint(int pointID, int walkID) {
		String selectQuery = "SELECT id FROM places WHERE locationId ="
				+ pointID + " and walkId= " + walkID;

		int placeId = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null)
			cursor.moveToFirst();

		placeId = Integer.parseInt(cursor.getString(0));

		cursor.close();

		return placeId;
	}

	private ArrayList<String> getImageNamesForPoint(int pointID, int walkID) {

		int placeID = getPlaceIdForPoint(pointID, walkID);

		ArrayList<String> imageList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT  * FROM photos WHERE placeId = " + placeID;
		Log.i("", selectQuery);
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				String imgName = cursor.getString(2);
				Log.i("", imgName);
				imageList.add(imgName);
			} while (cursor.moveToNext());
		}

		cursor.close();

		// return contact list
		return imageList;
	}

}
