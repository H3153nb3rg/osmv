/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.the.gogo.panoramio.panoviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import at.the.gogo.panoramio.panoviewer.activities.PanoViewerActivity;

/**
 * This class is responsible for downloading and parsing the search results for
 * a particular area. All of the work is done on a separate thread, and progress
 * is reported back through the DataSetObserver set in
 * {@link #addObserver(DataSetObserver). State is held in memory by in memory
 * maintained by a single instance of the ImageManager class.
 */
public class ImageManager {
	private static final String TAG = "Panoramio";

	/**
	 * Base URL for Panoramio's web API
	 */
//	private static final String THUMBNAIL_URL = "//www.panoramio.com/map/get_panoramas.php?order=popularity&set=public&from=0&to=20&miny=%.6f&minx=%.6f&maxy=%.6f&maxx=%.6f&size=thumbnail&mapfilter=true";
	private static final String THUMBNAIL_URL = "//www.panoramio.com/map/get_panoramas.php?order=popularity&set=public&from=0&to=25&miny=%.6f&minx=%.6f&maxy=%.6f&maxx=%.6f&size=medium&mapfilter=true";

	/**
	 * Used to post results back to the UI thread
	 */
	private final Handler mHandler = new Handler();

	/**
	 * Holds the single instance of a ImageManager that is shared by the
	 * process.
	 */
	private static ImageManager sInstance;

	/**
	 * Holds the images and related data that have been downloaded
	 */
	private final ArrayList<PanoramioItem> mImages = new ArrayList<PanoramioItem>();

	/**
	 * Observers interested in changes to the current search results
	 */
	private final ArrayList<WeakReference<DataSetObserver>> mObservers = new ArrayList<WeakReference<DataSetObserver>>();

	/**
	 * True if we are in the process of loading
	 */
	private boolean mLoading;

	private final Context mContext;

	/**
	 * Key for an Intent extra. The value is the zoom level selected by the
	 * user.
	 */
	public static final String ZOOM_EXTRA = "zoom";

	/**
	 * Key for an Intent extra. The value is the latitude of the center of the
	 * search area chosen by the user.
	 */
	public static final String LATITUDE_E6_EXTRA = "latitudeE6";

	/**
	 * Key for an Intent extra. The value is the latitude of the center of the
	 * search area chosen by the user.
	 */
	public static final String LONGITUDE_E6_EXTRA = "longitudeE6";

	// keys for bounding area
	public static final String LATITUDE_E6_MIN = "latMin";
	public static final String LATITUDE_E6_MAX = "latMax";
	public static final String LONGITUDE_E6_MIN = "lonMin";
	public static final String LONGITUDE_E6_MAX = "lonMax";

	/**
	 * Key for an Intent extra. The value is an item to display
	 */
	public static final String PANORAMIO_ITEM_EXTRA = "item";

	public static ImageManager getInstance(final Context c) {
		if (sInstance == null) {
			sInstance = new ImageManager(c.getApplicationContext());
		}
		return sInstance;
	}

	private ImageManager(final Context c) {
		mContext = c;
	}

	/**
	 * @return True if we are still loading content
	 */
	public boolean isLoading() {
		return mLoading;
	}

	/**
	 * Clear all downloaded content
	 */
	public void clear() {
		mImages.clear();
		notifyObservers();
	}

	/**
	 * Add an item to and notify observers of the change.
	 * 
	 * @param item
	 *            The item to add
	 */
	private void add(final PanoramioItem item) {
		mImages.add(item);
		notifyObservers();
	}

	/**
	 * @return The number of items displayed so far
	 */
	public int size() {
		return mImages.size();
	}

	/**
	 * Gets the item at the specified position
	 */
	public PanoramioItem get(final int position) {
		return mImages.get(position);
	}

	/**
	 * Adds an observer to be notified when the set of items held by this
	 * ImageManager changes.
	 */
	public void addObserver(final DataSetObserver observer) {
		final WeakReference<DataSetObserver> obs = new WeakReference<DataSetObserver>(
				observer);
		mObservers.add(obs);
	}

	/**
	 * Load a new set of search results for the specified area.
	 * 
	 * @param minLong
	 *            The minimum longitude for the search area
	 * @param maxLong
	 *            The maximum longitude for the search area
	 * @param minLat
	 *            The minimum latitude for the search area
	 * @param maxLat
	 *            The minimum latitude for the search area
	 */
	public void load(final float minLong, final float maxLong,
			final float minLat, final float maxLat) {
		mLoading = true;
		new Thread(new ImageGatherer(minLong, maxLong, minLat, maxLat)).start();
	}

	/**
	 * Called when something changes in our data set. Cleans up any weak
	 * references that are no longer valid along the way.
	 */
	private void notifyObservers() {
		final ArrayList<WeakReference<DataSetObserver>> observers = mObservers;
		final int count = observers.size();
		for (int i = count - 1; i >= 0; i--) {
			final WeakReference<DataSetObserver> weak = observers.get(i);
			final DataSetObserver obs = weak.get();
			if (obs != null) {
				obs.onChanged();
			} else {
				observers.remove(i);
			}
		}

	}

	/**
	 * This thread does the actual work of downloading and parsing data.
	 * 
	 */
	private class ImageGatherer implements Runnable {

		private final float mMinLong;
		private final float mMaxLong;
		private final float mMinLat;
		private final float mMaxLat;

		public ImageGatherer(final float minLong, final float maxLong,
				final float minLat, final float maxLat) {
			mMinLong = minLong;
			mMaxLong = maxLong;
			mMinLat = minLat;
			mMaxLat = maxLat;
		}

		@Override
		public void run() {

			String url = THUMBNAIL_URL;
			url = String.format(Locale.US, url, mMinLat, mMinLong, mMaxLat,
					mMaxLong);
			Log.i(TAG, "Pano request URL: " + url);
			try {
				final URI uri = new URI("http", url, null);
				final HttpGet get = new HttpGet(uri);

				final HttpClient client = new DefaultHttpClient();
				final HttpResponse response = client.execute(get);
				final HttpEntity entity = response.getEntity();
				final String str = convertStreamToString(entity.getContent());
				final JSONObject json = new JSONObject(str);
				parse(json);
			} catch (final Exception e) {
				Log.e(TAG, e.toString());
			}
		}

		private void parse(final JSONObject json) {
			try {
				final JSONArray array = json.getJSONArray("photos");
				final int count = array.length();
				for (int i = 0; i < count; i++) {
					final JSONObject obj = array.getJSONObject(i);

					final long id = obj.getLong("photo_id");
					String title = obj.getString("photo_title");
					final String owner = obj.getString("owner_name");
					final String thumb = obj.getString("photo_file_url");
					final String ownerUrl = obj.getString("owner_url");
					final String photoUrl = obj.getString("photo_url");
					
					final double latitude = obj.getDouble("latitude");
					final double longitude = obj.getDouble("longitude");
					final Bitmap b = BitmapUtils.loadBitmap(thumb);
					if (title == null) {
						title = mContext.getString(R.string.untitled);
					}

					final PanoramioItem item = new PanoramioItem(id, thumb, b,
							(int) (latitude * PanoViewerActivity.MILLION),
							(int) (longitude * PanoViewerActivity.MILLION),
							title, owner, ownerUrl, photoUrl);
					final boolean done = i == (count - 1);
					mHandler.post(new Runnable() {
						@Override
						public void run() {

							sInstance.mLoading = !done;
							sInstance.add(item);
						}
					});
				}
			} catch (final JSONException e) {
				Log.e(TAG, e.toString());
			}
		}

		private String convertStreamToString(final InputStream is) {
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(is), 8 * 1024);
			final StringBuilder sb = new StringBuilder();

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

			return sb.toString();
		}

	}

}
