package app.com.allinonenews.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import app.com.allinonenews.R;
import app.com.allinonenews.data.RepositoryFactory;
import app.com.allinonenews.databinding.ActivityHomeBinding;
import app.com.allinonenews.model.SelectedSource;
import app.com.allinonenews.sync.SyncService;
import app.com.allinonenews.ui.source.SourceActivity;
import app.com.allinonenews.util.ActivityUtils;
import app.com.allinonenews.util.PrefUtil;

public class NewsActivity extends AppCompatActivity {

    private static final int REQUEST_SOURCE = 100;
    private NewsContract.Presenter presenter;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_home);
        setSupportActionBar(binding.toolBarLayout.toolbar);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(NewsActivity.this, SourceActivity.class),REQUEST_SOURCE);
            }
        });

        NewsFragment newsFragment =(NewsFragment) getSupportFragmentManager().findFragmentById(R.id.homeContent);
        if (newsFragment == null) {
            newsFragment = NewsFragment.newInstance(null);
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), newsFragment, R.id.homeContent);

        presenter=new NewsPresenter(RepositoryFactory.getNewsRepository(this),newsFragment,PrefUtil.getInstance(this));

        receiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase(SyncService.ACTION)){
                    //presenter.
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(SyncService.ACTION));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_SOURCE){
            if (resultCode==RESULT_OK){
                SelectedSource source=data.getParcelableExtra(SourceActivity.EXTRA_DATA);
                PrefUtil.getInstance(NewsActivity.this).saveSelectedSource(source);
                PrefUtil.getInstance(this).saveListPosition(0,0);
                presenter.updateNews(source);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_home,menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver!=null){
            unregisterReceiver(receiver);
        }
    }
}
