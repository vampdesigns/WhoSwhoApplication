package com.test.whoswhoapp.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

import com.test.whoswhoapp.Constants;
import com.test.whoswhoapp.utils.VolleyInstance;

/**
 * An Activity that provides ActionBarCompat support. Use it for some common
 * data or functions.
 */
public abstract class BaseActivity extends ActionBarActivity implements
		Constants {

	/**
	 * use it for easier access of Context or Activity in inner classes or
	 * interface implementations
	 */
	protected BaseActivity mActivity;

	/** field for access default ShraredPreferences */
	protected SharedPreferences mPrefs;

	// ---------------------------------------------------------------------------------------------
	// Activity life cycle methods
	// ---------------------------------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		super.onCreate(savedInstanceState);
		VolleyInstance.init(this);
		mActivity = this;
	}

	@Override
	protected void onStop() {
		super.onStop();
		VolleyInstance.getRequestQueue().cancelAll(this);
	}

	// ---------------------------------------------------------------------------------------------
	// Public help methods
	// ---------------------------------------------------------------------------------------------
	/**
	 * Gets default {@link SharedPreferences} for the Application.
	 * 
	 * @return {@link SharedPreferences}
	 */
	public SharedPreferences getPrefs() {
		return mPrefs;
	}
}
