package uk.ac.swan.digitaltrails.database;

import java.io.IOException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final String DB_NAME = DbSchema.DB_NAME;
	private static final int DB_VERSION = 1;
	
	private static final int WALK_TABLE = 0;
	private static final int WALK_ID = 1;
	
	private final Context mContext;

	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO: create from DbSchema stuff.
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO: Drop all tables first.
		onCreate(db);
	}
}
