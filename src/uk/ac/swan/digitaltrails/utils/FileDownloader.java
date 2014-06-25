package uk.ac.swan.digitaltrails.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import uk.ac.swan.digitaltrails.components.Audio;
import uk.ac.swan.digitaltrails.components.Media;
import uk.ac.swan.digitaltrails.components.Photo;
import uk.ac.swan.digitaltrails.components.Video;
import uk.ac.swan.digitaltrails.database.AudioDataSource;
import uk.ac.swan.digitaltrails.database.MediaDataSource;
import uk.ac.swan.digitaltrails.database.PhotoDataSource;
import uk.ac.swan.digitaltrails.database.VideoDataSource;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

/**
 * @author Lewis H
 * AsyncTask to download (currently only new) files from the server and add to the database.
 */
public class FileDownloader extends AsyncTask<Media, Void, Media> {

	private final int TIMEOUT_CONNECTION = 5000; // 5 sec
	private final int TIMEOUT_SOCKET = 30000; // 30 sec
	private static final String TAG = "FileDownloader";
	private static final String SERVER_ROOT = "http://s523031181.websitehome.co.uk/media/image/";
	
	private Uri mUri;
	private String mType;

	public FileDownloader(Media m) {
		// This is probably terrible, but it's very late and I'm on a tight schedule.
		if (m instanceof Audio) {
			mUri = WhiteRockContract.WaypointAudio.CONTENT_URI;
			mType = "audio";
		} else if (m instanceof Photo) {
			mUri = WhiteRockContract.WaypointImage.CONTENT_URI;
			mType = "photo";
		} else if (m instanceof Video) {
			mUri = WhiteRockContract.WaypointVideo.CONTENT_URI;
			mType = "video";
		}
	}
	
	@Override
	protected Media doInBackground(Media... params) {
		return downloadFile(params[0]);
	}

	@Override
	protected void onPostExecute(Media result) {
		MediaDataSource dataSource;
		if (mType.equals("audio")) {
			dataSource = new AudioDataSource(WhiteRockApp.getContext());
		} else if (mType.equals("photo")) {
			dataSource = new PhotoDataSource(WhiteRockApp.getContext());
		} else if (mType.equals("video")) {
			dataSource = new VideoDataSource(WhiteRockApp.getContext());
		} else {
			Log.e(TAG, "Somehow our class is not of any media types");
			return;
		}
		
		ContentValues values = dataSource.getContentValues(result);
		values.remove("_id");
		Uri mediaUri = WhiteRockApp.getContext().getContentResolver().insert(mUri, values);
		Log.d(TAG, "Media inserted at: " + mediaUri);
	}
	
	private Media downloadFile(Media media) {
		//TODO: Check with Tom if we can do downloading from a certain byte offset.

		Log.d(TAG, "Inside downloadFile");
		// Check we have the file.
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Log.e(TAG, "Unable to access externalstorage");
			return null;
		}
		try {
			Log.i(TAG, "Creating file directory");
			String fileUrl = SERVER_ROOT+media.getFileLocation();
			URL url = new URL(fileUrl);
			String root = WhiteRockApp.getContext().getExternalFilesDir(null).toString();
			Log.d(TAG, "Root is: " + root);
			File myDir = new File(root + "/DigitalTrailsMedia");
			myDir.mkdirs();
			File file = new File(myDir, media.getFileLocation());
			Log.i(TAG, "File Download Beginning..." + fileUrl);
			
			// open URL connection
			URLConnection ucon = url.openConnection();
			
			// Timeouts affecting how long it takes for app to realise it cannot connect.
			ucon.setReadTimeout(TIMEOUT_CONNECTION);
			ucon.setConnectTimeout(TIMEOUT_SOCKET);
			
			InputStream is = ucon.getInputStream();
			BufferedInputStream inStream = new BufferedInputStream(is, 5 * 1024);
			FileOutputStream outStream = new FileOutputStream(file);
			byte[] buff = new byte[5 * 1024];
			
			// Read and store bytes until none left.
			int len = 0;
			while ((len = inStream.read(buff)) != -1) {
				outStream.write(buff, 0, len);
			}
			
			// clean up after ourselves.
			outStream.flush();
			outStream.close();
			inStream.close();
			Log.i(TAG, "Download Completed");
			media.setFileLocation(file.getAbsolutePath());
			Log.i(TAG, "Download complete. File found at: " + media.getFileLocation());
					
			return media;
		} catch (MalformedURLException e) {
			Log.e(TAG, "URL not valid: " + e.toString());
		} catch (IOException e) {
			Log.e(TAG, "IOException downloading file: " + e.toString());
		}
		return null;
	}

}
