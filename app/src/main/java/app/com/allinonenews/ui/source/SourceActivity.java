package app.com.allinonenews.ui.source;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.com.allinonenews.R;
import app.com.allinonenews.data.RepositoryFactory;
import app.com.allinonenews.model.SelectedFilter;
import app.com.allinonenews.model.SelectedSource;
import app.com.allinonenews.model.Source;
import app.com.allinonenews.util.DialogUtil;
import app.com.allinonenews.util.PrefUtil;

import static app.com.allinonenews.data.Constants.CATEGORY_ALL;

public class SourceActivity extends AppCompatActivity implements View.OnClickListener,SourceContract.View {
    private static final String TAG = SourceActivity.class.getSimpleName();
    public static final String EXTRA_DATA = "extra_data";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noSourceText;
    private SourceAdapter adapter;
    private SourceContract.Presenter presenter;
    //private TextView categoryFilter;

    private SelectedSource selectedSource;
    private SelectedFilter selectedFilter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        presenter = new SourcePresenter(RepositoryFactory.getSourceRepository(this), this, PrefUtil.getInstance(this));
        selectedSource = PrefUtil.getInstance(this).getSelectedSource();
        selectedFilter = PrefUtil.getInstance(this).getFilter();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        setTitle(selectedSource.getSourceName());
        toolbar.setSubtitle(selectedFilter.getCategory());
        //toolbar.setSubtitle("Category:All \nLanguage: All \nCountry: All");
        recyclerView = (RecyclerView) findViewById(R.id.sourceRecyclerView);
        progressBar = (ProgressBar) findViewById(R.id.sourceProgress);
        noSourceText = (TextView) findViewById(R.id.noSourceAvailable);
        //categoryFilter=(TextView)findViewById(R.id.categoryFilter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.sourceFab);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SourceAdapter(this,getItemClickListener());
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        presenter.loadSource();

        fab.setOnClickListener(this);
        //categoryFilter.setOnClickListener(this);

    }
    public SourceAdapter.UpdateTitleListener getItemClickListener() {
        return new SourceAdapter.UpdateTitleListener() {
            @Override
            public void updateTitle(String title) {
                setTitle(title);
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sourceFab:
                if (!getTitle().toString().equalsIgnoreCase(getString(R.string.select_source))) {
                    saveFilter();
                    SelectedSource source = adapter.getSelectedSource();
                    Log.e(TAG, source.getSourceId() + source.getSourceName());
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_DATA, source);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    Snackbar.make(findViewById(R.id.sourceFab),getString(R.string.select_source),Snackbar.LENGTH_SHORT).show();
                }
                break;



//            case R.id.categoryFilter:
//                presenter.clickOnCategoryFilter();
//                break;
        }
    }

    @Override
    public void showSourceList(List<Source> sources) {
        noSourceText.setVisibility(View.GONE);
        adapter.showMainList(sources);
        String filter=selectedFilter.getCategory();
        if (filter.equalsIgnoreCase(CATEGORY_ALL))
            filter=null;
        adapter.applyFilter(null,null,filter);
    }

    @Override
    public void showNoSourceAvailable() {
        noSourceText.setVisibility(View.VISIBLE);
    }



    @Override
    public void showCategoryPopUp(final List<String> categories) {
        categories.add(0,CATEGORY_ALL);
        DialogUtil.showCountryList(this, categories,toolbar.getSubtitle().toString(), new DialogUtil.CountryListSelectCallback() {
            @Override
            public void onSelected(String selectedCategory) {
                Log.e(TAG,"Selected: "+selectedCategory);
                //categoryFilter.setText(getString(R.string.category_filter,selectedCategory));
                toolbar.setSubtitle(selectedCategory.toUpperCase());
                if (selectedCategory.equalsIgnoreCase(CATEGORY_ALL))
                    selectedCategory=null;
                adapter.applyFilter(null,null,selectedCategory);
                //setTitle(getString(R.string.select_source));
            }

            @Override
            public void cancel() {
                Log.e(TAG,"Cancel ");
            }
        });
    }

    @Override
    public void showProgress(boolean isActive) {
        if (isActive)
            progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void scrollListTo(final int position) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(position, 5);
        //recyclerView.scrollToPosition(position);

    }

    @Override
    public void countryListNotAvailable() {
        Toast.makeText(this,getString(R.string.country_not_available),Toast.LENGTH_SHORT).show();
    }



    @Override
    public void setPresenter(SourceContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_filter:
                presenter.clickOnCategoryFilter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveFilter() {
        SelectedFilter filter=new SelectedFilter();
        filter.setCategory(toolbar.getSubtitle().toString());
        PrefUtil.getInstance(this).saveFilter(filter);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.source_menu,menu);
        return true;
    }


}
