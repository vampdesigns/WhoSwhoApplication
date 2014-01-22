package com.test.whoswhoapp.ui.fragments;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.test.whoswhoapp.Constants;
import com.test.whoswhoapp.R;
import com.test.whoswhoapp.ui.activity.MainActivity;
import com.test.whoswhoapp.utils.ConnectivityUtils;
import com.test.whoswhoapp.utils.DialogUtils;

public class HomeFragment extends Fragment implements Constants{

    private WebView mWebView;

    private boolean mLoadingFinished = true;
    private boolean mRedirect = false;
    private String mUrl;

    private MainActivity mParent;

    // ---------------------------------------------------------------------------------------------
    // Fragment life cycle methods
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpWebView(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mParent = ((MainActivity) getActivity());
        // load URL
        if (getArguments().containsKey(EXTRA_URL)) {
            mUrl = getArguments().getString(EXTRA_URL);
            loadUrl();
        } else {
            throw new RuntimeException("WebViewFragment should has EXTRA_URL in params.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mWebView.stopLoading();
        progressShow(false);
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
                loadUrl();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // ---------------------------------------------------------------------------------------------
    // Private help methods
    // ---------------------------------------------------------------------------------------------
    private void loadUrl() {
        progressShow(true);
        mWebView.requestFocus();
        if (ConnectivityUtils.isNetworkAvailable(getActivity())) {
            mWebView.loadUrl(mUrl);
        } else {
            DialogUtils.showNoConnectionDialog(getActivity());
        }
    }

    private void setUpWebView(View v) {
        mWebView = (WebView) v.findViewById(R.id.web_view);

        WebSettings webSettings = mWebView.getSettings();

        /* settings to allow the web page to load properly */
        webSettings.setJavaScriptEnabled(true);

        /*
         * App cache settings. If it's needed bigger Max Size, it can be settled
         * with the method onReachedMaxAppCacheSize
         */
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getActivity().getCacheDir().getAbsolutePath());

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // used only with HTML5. Storing data in the web browser.
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setBuiltInZoomControls(true);

        // Fix for devices on 2.3.0. This removes the white line in the right.
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        // Ignore SSL certificate errors
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                super.shouldOverrideUrlLoading(view, url);
                if (!mLoadingFinished) {
                    mRedirect = true;
                }
                mLoadingFinished = false;
                mWebView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mLoadingFinished = false;
                // SHOW LOADING IF IT ISNT ALREADY VISIBLE
                progressShow(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!mRedirect) {
                    mLoadingFinished = true;
                }

                if (mLoadingFinished && !mRedirect) {
                    // HIDE LOADING IT HAS FINISHED
                    progressShow(false);
                } else {
                    mRedirect = false;
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                    String failingUrl) {
                Log.d("HomeFragment", "failingUrl = " + failingUrl);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        mWebView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (mWebView.canGoBack()) {
                                mWebView.goBack();
                                return true;
                            }
                            break;
                    }
                }
                return false;
            }
        });
    }

    private void progressShow(boolean isNeeded) {
        if (mParent != null) {
            mParent.setSupportProgressBarIndeterminateVisibility(isNeeded);
            mParent.setProgressBarIndeterminate(isNeeded);
            mParent.setProgressBarVisibility(isNeeded);
        }
    }

}
