package com.test.whoswhoapp.ui.fragments;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.test.whoswhoapp.Constants;
import com.test.whoswhoapp.R;
import com.test.whoswhoapp.adapter.EmployeeAdapter;
import com.test.whoswhoapp.common.EmployeeObject;
import com.test.whoswhoapp.utils.ConnectivityUtils;
import com.test.whoswhoapp.utils.DialogUtils;
import com.test.whoswhoapp.utils.HTMLPageDownloader;
import com.test.whoswhoapp.utils.HTMLPageDownloader.HTMLPageDownloaderListener;

public class EmployeeFragment extends ListFragment implements OnCancelListener, Constants, HTMLPageDownloaderListener {

    private EmployeeAdapter mAdapter;

	private AsyncTask<Void, Void, String> mAsyncDownload;

	private ArrayList<EmployeeObject> mListEmployee;
    
	// ---------------------------------------------------------------------------------------------
	// Activity life cycle methods
	// ---------------------------------------------------------------------------------------------
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (loadItems()) {
            DialogUtils.showLoadingDialog(getActivity(), this);
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employee, container, false);
    }
	
    @Override
    public void onDetach() {
        super.onDetach();
        if (mAsyncDownload != null) {
        	mAsyncDownload.cancel(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_web_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reload:
            	if (loadItems()) {
                    DialogUtils.showLoadingDialog(getActivity(), this);
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
	// ---------------------------------------------------------------------------------------------
	// Private help methods
	// ---------------------------------------------------------------------------------------------

    /**
     * Re-init adapter with given items
     * @param playlistObject
     */
    private void reloadAdapter() {
        if (isAdded()) {
            if (mAdapter == null) {
                mAdapter = new EmployeeAdapter(getActivity(), R.layout.list_item_employee,
                		mListEmployee);
                setListAdapter(mAdapter);
            }
        }
    }
    private boolean loadItems() {
        if (ConnectivityUtils.isNetworkAvailable(getActivity())) {
        	mAsyncDownload = new HTMLPageDownloader(URL_WEB_SITE, this).execute();
            return true;
        } else {
        	DialogUtils.showDialog(getActivity(), R.string.error, R.string.no_internet_connection_);
            return false;
        }
    }

	@Override
	public void onCancel(DialogInterface dialog) {
        if (mAsyncDownload != null) {
        	mAsyncDownload.cancel(true);
        }
	}

	@Override
	public void completionCallBack(String html) {
		if (DialogUtils.isProgressShowing()) {
            DialogUtils.hideLoadingDialog();
        }
		
		Document doc = Jsoup.parse(html.toString());
		mListEmployee = new ArrayList<EmployeeObject>();
		
		for (Element element : doc.select("div[class=col col2]")) {
			EmployeeObject employee = new EmployeeObject();
			employee.setName(element.getElementsByTag("h3").text());
			employee.setTitle(element.getElementsByTag("p").get(0).text());
			employee.setBio(element.getElementsByClass("user-description").text());
			String imgSrc = element.select("div[class=title]").get(0).childNode(0).attr("src");
			employee.setImgLink(imgSrc);
			mListEmployee.add(employee);
		}
		
		if (mListEmployee.size() > 0) {
            reloadAdapter();
        } else {
        	Log.d("EmployeeFragment", "Employees were not loaded");
        }   
		
	}

}
