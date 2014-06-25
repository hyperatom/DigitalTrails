package uk.ac.swan.digitaltrails.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpStatus;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

@SuppressWarnings("deprecation")
public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

	public static final String TAG = "ImageDownloader";
	
	private static final String JPG_REGEX = "/\\w+\\.jpg$";
	
	private String mFileName;
	public ImageDownloader() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		String tmpName = params[0];
		String[] matches = tmpName.split(JPG_REGEX);
		if (matches.length > 1) {
			// uh oh
			Log.w(TAG, "We got a mismatch, don't save.");
		} else {
			mFileName = matches[0];
		}
		return downloadBitmap(params[0]);
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		saveImageToSd(result);
	}
	
	/**
	 * Download the bitmap
	 * @param url The url to download image from
	 * @return The Bitmap object.
	 */
	private Bitmap downloadBitmap(String url) {
		// Init default HTTP client.
		@SuppressWarnings("resource")
		final DefaultHttpClient client = new DefaultHttpClient();
		
		// Form HttpGet request
		final HttpGet getRequest = new HttpGet(url);
		
		try {
			HttpResponse response = client.execute(getRequest);
			
			// Check 200 OK for success
			final int statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode != HttpStatus.SC_OK){
				Log.w(TAG, "Error " + statusCode + " while retrieving bitmap from " + url);
				return null;
			}
			
			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					// get contents from stream
					inputStream = entity.getContent();
					
					// decode data back into Android Bitmap
					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			getRequest.abort();
			Log.e(TAG, "Something went wrong while downloading bitmap from " + url + " " + e.toString());
		}
		return null;
	}
	
	private void saveImageToSd(Bitmap bmp) {
		Log.i(TAG, "Saving file: " + mFileName);
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/DigitalTrailsImages");
		myDir.mkdirs();
		File file = new File(myDir, mFileName);
		int numTries = 0;
		while (file.exists()) {
			mFileName += numTries;
			numTries++;
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			Log.e(TAG, "Something went wrong saving " + mFileName + " to storage");
			e.printStackTrace();
		}
	}


}
