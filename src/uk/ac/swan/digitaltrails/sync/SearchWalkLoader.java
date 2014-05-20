package uk.ac.swan.digitaltrails.sync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.swan.digitaltrails.components.Walk;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * @author Lewis Hancock
 *
 */
public class SearchWalkLoader extends AsyncTaskLoader<List<Walk>> {

	/**
	 * 
	 */
	private static final String TAG = "WalkLoader";
	
	/**
	 * 
	 */
	private Bundle mBundle;
	
	/**
	 * 
	 */
	private List<Walk> mWalks;
	
	/**
	 * @param context
	 */
	public SearchWalkLoader(Context context, Bundle bundle) {
		super(context);
		mBundle = bundle;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.content.Loader#onStartLoading()
	 */
	@Override
	protected void onStartLoading() {
		if (mWalks != null) {
			deliverResult(mWalks);
		}
		
		if (takeContentChanged() || mWalks == null) {
			forceLoad();
		}
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.content.Loader#onStopLoading()
	 */
	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.content.Loader#onReset()
	 */
	@Override
	protected void onReset() {
		super.onReset();
		
		onStopLoading();
		
		if (mWalks != null) {
			onReleaseResources(mWalks);
			mWalks = null;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.content.AsyncTaskLoader#loadInBackground()
	 */
	@Override
	public List<Walk> loadInBackground() {
		List<Walk> result = new ArrayList<Walk>();
		WhiteRockServerAccessor serverAccessor = new WhiteRockServerAccessor();
		try {
			String query = mBundle.getString("query");
			result = serverAccessor.searchForWalk(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param walks
	 */
	protected void onReleaseResources(List<Walk> walks) {
		// do nothing 'cause we're a list.
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.content.Loader#deliverResult(java.lang.Object)
	 */
	@Override
	public void deliverResult(List<Walk> walks) {
		if (isReset()) {
			if (walks != null) {
				onReleaseResources(walks);
			}
		}
		
		List<Walk> oldWalks = mWalks;
		mWalks = walks;
		
		if (isStarted()) {
			super.deliverResult(walks);
		}
		
		if (oldWalks != null) {
			onReleaseResources(oldWalks);
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.content.AsyncTaskLoader#onCanceled(java.lang.Object)
	 */
	@Override
	public void onCanceled(List<Walk> walks) {
		super.onCanceled(walks);
		onReleaseResources(walks);
	}
	
}
