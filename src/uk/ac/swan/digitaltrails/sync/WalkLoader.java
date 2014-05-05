package uk.ac.swan.digitaltrails.sync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.swan.digitaltrails.components.Walk;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class WalkLoader extends AsyncTaskLoader<List<Walk>> {

	private static final String TAG = "WalkLoader";
	
	private List<Walk> mWalks;
	
	public WalkLoader(Context context) {
		super(context);
	}
	
	@Override
	protected void onStartLoading() {
		if (mWalks != null) {
			deliverResult(mWalks);
		}
		
		if (takeContentChanged() || mWalks == null) {
			forceLoad();
		}
	}
	
	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	protected void onReset() {
		super.onReset();
		
		onStopLoading();
		
		if (mWalks != null) {
			onReleaseResources(mWalks);
			mWalks = null;
		}
		
	}
	
	@Override
	public List<Walk> loadInBackground() {
		List<Walk> result = new ArrayList<Walk>();
		WhiteRockServerAccessor serverAccessor = new WhiteRockServerAccessor();
		try {
			result = serverAccessor.getWalks();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	protected void onReleaseResources(List<Walk> walks) {
		// do nothing 'cause we're a list.
	}
	
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

	@Override
	public void onCanceled(List<Walk> walks) {
		super.onCanceled(walks);
		onReleaseResources(walks);
	}
	
}
