package com.test.whoswhoapp.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.test.whoswhoapp.R;
import com.test.whoswhoapp.adapter.DrawerMenuAdapter;
import com.test.whoswhoapp.ui.fragments.HomeFragment;
import com.test.whoswhoapp.ui.fragments.NoneFragment;
import com.test.whoswhoapp.ui.fragments.EmployeeFragment;

public class MainActivity extends BaseActivity {
    
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerMenuListView;
    private LinearLayout mDrawerMenuContainer;

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;    

    private DrawerMenuAdapter mDrawerMenuAdapter;
    private String[] mDrawerMenuTitles;

    private ActionBarDrawerToggle mDrawerToggle;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();
        mDrawerMenuTitles = getResources().getStringArray(R.array.menu_array);
		initViews();
		setupViews();
        setupActionBar();
        selectItem(NONE);
	}

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(mDrawerMenuContainer)) {
                    mDrawerLayout.closeDrawer(mDrawerMenuContainer);
                } else {
                    mDrawerLayout.openDrawer(mDrawerMenuContainer);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
    
    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Public help methods
    // ---------------------------------------------------------------------------------------------
    /**
     * Selects an item from Navigation Drawer's menu.
     * 
     * @param position an integer for index that points to desired menu item
     */
    public void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        Bundle args = new Bundle();

        switch (position) {
            case HOME:
            	args.putString(EXTRA_URL, URL_WEB_SITE);
                fragment = new HomeFragment();
                fragment.setArguments(args);
                break;
            case SHOW_PLAYLISTS:
                fragment = new EmployeeFragment();
                break;            
            default:
                fragment = new NoneFragment();
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title
        mDrawerMenuListView.setItemChecked(position, true);
        if (position < mDrawerMenuTitles.length && position >= 0) {
            CharSequence c = mDrawerMenuTitles[position];
            setTitle(c); 
        } else {
        	setTitle(getString(R.string.app_name));
        }

        // close the drawer
        mDrawerLayout.closeDrawer(mDrawerMenuContainer);
    }
    
    // ---------------------------------------------------------------------------------------------
    // Private help methods
    // ---------------------------------------------------------------------------------------------
    private void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
	
    private void initViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerMenuListView = (ListView) findViewById(R.id.drawer_menu_list);
        mDrawerMenuContainer = (LinearLayout) findViewById(R.id.drawer_menu_container);
    }

    private void setupViews() {
        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerMenuAdapter = new DrawerMenuAdapter(this, mDrawerMenuTitles);
        mDrawerMenuListView.setAdapter(mDrawerMenuAdapter);
        mDrawerMenuListView.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open, /*
                                       * "open drawer" description for
                                       * accessibility
                                       */
                R.string.drawer_close /*
                                       * "close drawer" description for
                                       * accessibility
                                       */
                ) {
                    @Override
                    public void onDrawerClosed(View view) {
                        getSupportActionBar().setTitle(mTitle);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        getSupportActionBar().setTitle(mDrawerTitle);
                    }
                };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

}
