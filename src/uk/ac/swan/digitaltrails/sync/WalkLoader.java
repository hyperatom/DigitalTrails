package uk.ac.swan.digitaltrails.sync;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.swan.digitaltrails.components.Walk;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class WalkLoader extends AsyncTaskLoader<List<Walk>> {

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
		
		mWalks = null;
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
		return Collections.unmodifiableList(result);
	}

}
