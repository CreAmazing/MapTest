package com.panda.maptest;

import java.io.FileOutputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.SensorsComponentAndroid;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.IRadar;
import com.metaio.sdk.jni.LLACoordinate;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;
import com.panda.maptest.bean.Place;

public class ARMap extends ARViewActivity implements
		SensorsComponentAndroid.Callback {

	/**
	 * Geometries
	 */
	// private IGeometry mGeometrySouth;
	// private IGeometry mGeometryWest;
	// private IGeometry mGeometryNorth;

	private LLACoordinate p1;
	private LLACoordinate p2;
	private LLACoordinate p3;

	private ArrayList<Place> places = new ArrayList<Place>();
	private ArrayList<IGeometry> geomtrys = new ArrayList<IGeometry>();

	Place place1 = new Place("东南苑", p1);
	Place place2 = new Place("万达电影", p2);
	Place place3 = new Place("望江楼", p3);

	private IRadar mRadar;

	/**
	 * Offset from current location
	 */
	private static final double OFFSET = 0.00002;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set GPS tracking configuration
		// The GPS tracking configuration must be set on user-interface thread
		boolean result = metaioSDK.setTrackingConfiguration("GPS");
		MetaioDebug.log("Tracking data loaded: " + result);

		p1 = new LLACoordinate(30.620946, 104.094270, mSensors.getLocation()
				.getAltitude(), mSensors.getLocation().getAccuracy());
		p2 = new LLACoordinate(30.620143, 104.096883, mSensors.getLocation()
				.getAltitude(), mSensors.getLocation().getAccuracy());
		p3 = new LLACoordinate(30.63032, 104.09371, mSensors.getLocation()
				.getAltitude(), mSensors.getLocation().getAccuracy());

		Place place1 = new Place("东南苑", p1);
		Place place2 = new Place("万达电影", p2);
		Place place3 = new Place("望江楼", p3);

		places.add(place1);
		places.add(place2);
		places.add(place3);

		Log.d("初始化", "完成");
	}

	@Override
	protected void onPause() {
		super.onPause();

		// remove callback
		if (mSensors != null) {
			mSensors.registerCallback(null);
			// mSensorsManager.pause();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		// Register callback to receive sensor updates
		if (mSensors != null) {
			mSensors.registerCallback(this);
			// mSensorsManager.resume();
		}

	}

	@Override
	public void onLocationSensorChanged(LLACoordinate location) {
		MetaioDebug.log("Location changed: " + location);
		updateGeometriesLocation(location);
	}

	public void onButtonClick(View v) {
		finish();
	}

	@Override
	protected int getGUILayout() {
		return R.layout.tutorial5;
	}

	@Override
	protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
		return null;
	}

	@Override
	protected void loadContents() {

		try {

			mRadar = metaioSDK.createRadar();
			mRadar.setBackgroundTexture(AssetsManager
					.getAssetPath("Tutorial5/Assets5/radar.png"));
			mRadar.setObjectsDefaultTexture(AssetsManager
					.getAssetPath("Tutorial5/Assets5/yellow.png"));
			mRadar.setRelativeToScreen(IGeometry.ANCHOR_TL);
			Log.d("Rader", "OK");
			metaioSDK.setLLAObjectRenderingLimits(10, 1000);

			String filepath = AssetsManager
					.getAssetPath("Tutorial5/Assets5/POI_bg.png");
			if (filepath != null) {
				for (int i = 0; i < places.size(); i++) {
					IGeometry g = metaioSDK.createGeometryFromImage(
							createBillboardTexture(places.get(i).getName()),
							true);
					Log.d("names", places.get(i).getName());
					geomtrys.add(g);
					mRadar.add(g);

				}
				// mGeometrySouth =
				// metaioSDK.createGeometryFromImage(createBillboardTexture("东南苑"),true);
				// mGeometryNorth =
				// metaioSDK.createGeometryFromImage(createBillboardTexture("万达电影"),true);
				// mGeometryWest =
				// metaioSDK.createGeometryFromImage(createBillboardTexture("望江楼"),true);

			}

			updateGeometriesLocation(mSensors.getLocation());

			// create radar

			// add geometries to the radar
			// mRadar.add(mGeometryNorth);
			// mRadar.add(mGeometrySouth);
			// mRadar.add(mGeometryWest);

		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String createBillboardTexture(String billBoardTitle) {
		try {
			final String texturepath = getCacheDir() + "/" + billBoardTitle
					+ ".png";
			Paint mPaint = new Paint();

			// Load background image (256x128), and make a mutable copy
			Bitmap billboard = null;

			// reading billboard background
			String filepath = AssetsManager
					.getAssetPath("Tutorial5/Assets5/POI_bg.png");
			Bitmap mBackgroundImage = BitmapFactory.decodeFile(filepath);

			billboard = mBackgroundImage.copy(Bitmap.Config.ARGB_8888, true);

			Canvas c = new Canvas(billboard);

			mPaint.setColor(Color.WHITE);
			mPaint.setTextSize(24);
			mPaint.setTypeface(Typeface.DEFAULT);

			float y = 40;
			float x = 30;

			// Draw POI name
			if (billBoardTitle.length() > 0) {
				String n = billBoardTitle.trim();

				final int maxWidth = 160;

				int i = mPaint.breakText(n, true, maxWidth, null);
				c.drawText(n.substring(0, i), x, y, mPaint);

				// Draw second line if valid
				if (i < n.length()) {
					n = n.substring(i);
					y += 20;
					i = mPaint.breakText(n, true, maxWidth, null);

					if (i < n.length()) {
						i = mPaint.breakText(n, true, maxWidth - 20, null);
						c.drawText(n.substring(0, i) + "...", x, y, mPaint);
					} else {
						c.drawText(n.substring(0, i), x, y, mPaint);
					}
				}

			}

			// writing file
			try {
				FileOutputStream out = new FileOutputStream(texturepath);
				billboard.compress(Bitmap.CompressFormat.PNG, 90, out);
				MetaioDebug.log("Texture file is saved to " + texturepath);
				return texturepath;
			} catch (Exception e) {
				MetaioDebug.log("Failed to save texture file");
				e.printStackTrace();
			}

			billboard.recycle();
			billboard = null;

		} catch (Exception e) {
			MetaioDebug.log("Error creating billboard texture: "
					+ e.getMessage());
			MetaioDebug.printStackTrace(Log.DEBUG, e);
			return null;
		}
		return null;
	}

	private void updateGeometriesLocation(LLACoordinate location) {

		Log.d("Location", location.toString());

		// p1 = new LLACoordinate( 30.620946, 104.094270,
		// location.getAltitude(), location.getAccuracy());
		// p2 = new LLACoordinate( 30.620143, 104.096883,
		// location.getAltitude(), location.getAccuracy());
		// p3 = new LLACoordinate( 30.63032, 104.09371, location.getAltitude(),
		// location.getAccuracy());

		/*
		 * if (mGeometrySouth != null) {
		 * location.setLatitude(location.getLatitude()-OFFSET);
		 * MetaioDebug.log("geometrySouth.setTranslationLLA: "+location);
		 * mGeometrySouth.setTranslationLLA(location);
		 * location.setLatitude(location.getLatitude()+OFFSET);
		 * 
		 * }
		 * 
		 * if (mGeometryNorth != null) {
		 * location.setLatitude(location.getLatitude()+OFFSET);
		 * MetaioDebug.log("geometryNorth.setTranslationLLA: "+location);
		 * mGeometryNorth.setTranslationLLA(location);
		 * location.setLatitude(location.getLatitude()-OFFSET); }
		 * 
		 * if (mGeometryWest != null) {
		 * location.setLongitude(location.getLongitude()-OFFSET);
		 * MetaioDebug.log("geometryWest.setTranslationLLA: "+location);
		 * mGeometryWest.setTranslationLLA(location);
		 * location.setLongitude(location.getLongitude()+OFFSET); }
		 */

		/*
		 * if (mGeometrySouth != null) {
		 * 
		 * mGeometrySouth.setTranslationLLA(p1);
		 * mGeometrySouth.setLLALimitsEnabled(true);
		 * 
		 * } if (mGeometryNorth != null) {
		 * 
		 * mGeometryNorth.setTranslationLLA(p2);
		 * mGeometryNorth.setLLALimitsEnabled(true);
		 * 
		 * } if (mGeometryWest != null) {
		 * 
		 * mGeometryWest.setTranslationLLA(p3);
		 * mGeometryWest.setLLALimitsEnabled(true);
		 * 
		 * }
		 */
		if (null != places) {
			for (int i = 0; i < places.size(); i++) {
				geomtrys.get(i)
						.setTranslationLLA(places.get(i).getCoordinate());
				geomtrys.get(i).setLLALimitsEnabled(true);
			}
		}

	}

	@Override
	protected void onGeometryTouched(final IGeometry geometry) {
		MetaioDebug.log("Geometry selected: " + geometry);

		mSurfaceView.queueEvent(new Runnable() {

			@Override
			public void run() {
				mRadar.setObjectsDefaultTexture(AssetsManager
						.getAssetPath("Tutorial5/Assets5/yellow.png"));
				mRadar.setObjectTexture(geometry,
						AssetsManager.getAssetPath("Tutorial5/Assets5/red.png"));

				Log.d("distance", "" + p1.distanceTo(mSensors.getLocation()));
				// mGeometrySouth =
				// metaioSDK.createGeometryFromImage(createBillboardTexture("东南苑\n"+p1.distanceTo(mSensors.getLocation())));
			}

		});
	}

	@Override
	public void onGravitySensorChanged(float[] gravity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHeadingSensorChanged(float[] orientation) {
		// TODO Auto-generated method stub

	}

}
