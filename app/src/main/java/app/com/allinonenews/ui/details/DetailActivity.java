package app.com.allinonenews.ui.details;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.common.base.Strings;

import app.com.allinonenews.R;
import app.com.allinonenews.model.NewsModel;

public class DetailActivity extends AppCompatActivity implements DetailContract.View{
    public static final String EXTRA_NEWS_MODEL = "extra_news_mode";
    private static final String TAG = DetailActivity.class.getSimpleName();
    private WebView mWebView;
    private SwipeRefreshLayout mRefreshLayout;
    private DetailContract.Presenter presenter;
    private NewsModel newsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.from_bottom, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        presenter=new DetailsPresenter(this);


        mWebView=(WebView)findViewById(R.id.webView);
        mRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.webViewRefresh);
        mWebView.setWebViewClient(getWebViewClient());
        WebSettings settings=mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setLoadsImagesAutomatically(true);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });

        try {
            newsModel = getIntent().getParcelableExtra(EXTRA_NEWS_MODEL);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        setTitle("");
        if (newsModel!=null){
            if (!Strings.isNullOrEmpty(newsModel.getTitle())) {
                setTitle(newsModel.getTitle());
                toolbar.setSubtitle(newsModel.getUrl());
            } else if (!Strings.isNullOrEmpty(newsModel.getUrl())){
                setTitle(newsModel.getUrl());
            }
            else
                setTitle(getString(R.string.news_details));
        }



        if (!Strings.isNullOrEmpty(newsModel.getUrl())){
            presenter.loadUrl(newsModel.getUrl());
        }
        else {
            onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return false;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.to_bottom);
    }



    public WebViewClient getWebViewClient() {
        return new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e(TAG,"URL: "+url);
                showLoadingIndicator(true);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                showLoadingIndicator(false);
            }
        };
    }

    @Override
    public void loadUrlInWebView(String url) {
        if (!Strings.isNullOrEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }

    @Override
    public void showLoadingIndicator(boolean isActive) {
        mRefreshLayout.setRefreshing(isActive);
    }

    @Override
    public void refreshWebView() {
        mWebView.reload();
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        this.presenter=presenter;
    }
}
