package uk.ac.swan.digitaltrails.fragments;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.utils.WhiteRockApp;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Please, just trust this class cause I don't have time to document it. It is modified 
 * from the official Android documentation - see all Lessons in this link http://developer.android.com/training/displaying-bitmaps/index.html
 * @author Lewis H
 *
 */
public class ImageGridFragment extends DialogFragment implements AdapterView.OnItemClickListener {
	public static final String IMAGE_PATH_EXTRA = "paths";
	public static final String ARG_AUDIO_PATH = "audio";
	public static final String ARG_VIDEO_PATH = "video";
	public static final String ARG_TITLE = "title";
	public static final String ARG_DESCRIPTION = "description";
	
	private static final String TAG = "ImageGridFramgent";
	private String[] mImagePaths;
	private String[] mAudioPaths;

	private SparseArray<String> mAudioSparseArray ;
	private ImageAdapter mAdapter;
	private String mTitle;
	private String mDescription;
	private Bitmap mPlaceHolderBitmap;
	
	public ImageGridFragment() { }

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		mImagePaths = args.getStringArray(IMAGE_PATH_EXTRA);
		mAudioPaths = args.getStringArray(ARG_AUDIO_PATH);
		
		mAudioSparseArray = new SparseArray<String>();
		
		if (mAudioPaths.length > 0) {
			for (int i = 0; i < mAudioPaths.length; i++) {
				mAudioSparseArray.append(i, mAudioPaths[i]);
			}
		}
		
		mTitle = args.getString(ARG_TITLE);
		mDescription = args.getString(ARG_DESCRIPTION);
		mAdapter = new ImageAdapter(getActivity());
		mPlaceHolderBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_grid_fragment, container, false);
		final GridView mGridView = (GridView) v.findViewById(R.id.gridView);
		final TextView descrLabel = (TextView) v.findViewById(R.id.description_label);
		descrLabel.setText(mDescription+"\n");
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
		getDialog().setTitle(mTitle);
		return v;
	}

	
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	
    	if (mImagePaths[position] == "android.resource://uk.ac.swan.digitaltrails/" + R.drawable.ic_action_volume_on + ".png") {
    		Log.d(TAG, "Staritng intent to open: " + Uri.fromFile(new File(mAudioSparseArray.get(position)))); 
    		intent.setDataAndType(Uri.fromFile(new File(mAudioSparseArray.get(position))), "audio/*");
    	} else if (mImagePaths[position] == "videoClip") {
    		//TODO: Update to be like audio when video is required
    		//intent.setDataAndType(Uri.fromFile(new File(mImagePaths[position])), "video/*");
    	} else {
	    	intent.setDataAndType(Uri.fromFile(new File(mImagePaths[position])), "image/*");
    	}
    	startActivity(intent);
    }

    private class ImageAdapter extends BaseAdapter {
        private final Context mContext;
        public ImageAdapter(Context context) {
            super();
            mContext = context;
        }

        @Override
        public int getCount() {
            return mImagePaths.length;
        }

        @Override
        public Object getItem(int position) {
            return mImagePaths[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            ImageView imageView;
            if (convertView == null) { // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new GridView.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            } else {
                imageView = (ImageView) convertView;
            }
            loadBitmap(mImagePaths[position], imageView);
            return imageView;
        }
    }

    public void loadBitmap(String path, ImageView imageView) {
    	if (cancelPotentialWork(path, imageView)) {
    		final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
    		final AsyncDrawable asyncDrawable = new AsyncDrawable(getResources(), mPlaceHolderBitmap, task);
    		imageView.setImageDrawable(asyncDrawable);
    		task.execute(path);
    	}
    }
    
    static class AsyncDrawable extends BitmapDrawable {
    	private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
    	
    	public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
    		super(res, bitmap);
    		bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
    	}
    	
    	public BitmapWorkerTask getBitmapWorkerTask() {
    		return bitmapWorkerTaskReference.get();
    	}
    }
    
    public static boolean cancelPotentialWork(String path, ImageView imageView) {
    	final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

    	if (bitmapWorkerTask != null) {
    		final String bitmapPath = bitmapWorkerTask.path;
    		if (bitmapPath != path) {
    			bitmapWorkerTask.cancel(true);
    		} else {
    			return false;
    		}
    	}
    	return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
    	if (imageView != null) {
    		final Drawable drawable = imageView.getDrawable();
    		if (drawable instanceof AsyncDrawable) {
    			final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
    			return asyncDrawable.getBitmapWorkerTask();
    		}
    	}
    	return null;
    }


    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    	private static final String TAG = "BitmapWorkerTask";
    	private final WeakReference<ImageView> imageViewReference;
    	private String path;
    	    	
    	public BitmapWorkerTask(ImageView imageView) {
    		imageViewReference = new WeakReference<ImageView>(imageView);
    	}
    	    	
		@Override
		protected Bitmap doInBackground(String... params) {
			path = params[0];
			return decodeSampledBitmapFromFile(path, 100, 100);
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			Log.d(TAG, "In onPostExecute");
			if (isCancelled()) {
				Log.d(TAG, "cancelled");
				bitmap = null;
			}
			if (imageViewReference != null && bitmap != null) {
				Log.d(TAG, "imageViewReference is fine...");
				final ImageView imageView = imageViewReference.get();
				final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
				if (this == bitmapWorkerTask && imageView != null) {
					Log.d(TAG, "Valid - displaying");
					imageView.setImageBitmap(bitmap);
				}
			}
		}
		
		public Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

		    // First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(path, options);

		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		    Log.d(TAG, "Got inSampleSize");
		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    return BitmapFactory.decodeFile(path, options);
		}
		
		public int calculateInSampleSize(
				BitmapFactory.Options options, int reqWidth, int reqHeight) {
			// Raw height and width of image
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {

				final int halfHeight = height / 2;
				final int halfWidth = width / 2;

				// Calculate the largest inSampleSize value that is a power of 2 and keeps both
				// height and width larger than the requested height and width.
				while ((halfHeight / inSampleSize) > reqHeight
						&& (halfWidth / inSampleSize) > reqWidth) {
					inSampleSize *= 2;
				}
			}

			return inSampleSize;
		}


    	
    }
    
}
