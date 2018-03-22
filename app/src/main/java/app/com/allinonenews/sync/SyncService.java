package app.com.allinonenews.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;

import app.com.allinonenews.data.NewsDataSource;
import app.com.allinonenews.model.NewsResponseModel;
import app.com.allinonenews.model.SelectedSource;
import app.com.allinonenews.util.PrefUtil;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SyncService extends JobService {

    public static final String ACTION="app.com.allinonenews.sync.SyncService";
    public static final String EXTRA_DATA="data";

    private static final String TAG = SyncService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return START_STICKY;
    }


   

    @Override
    public boolean onStartJob(JobParameters params) {
        SyncRepository syncRepository=new SyncRepository(this);
        PrefUtil prefUtil=PrefUtil.getInstance(this);
        SelectedSource selectedSource=prefUtil.getSelectedSource();
        syncRepository.getNews(selectedSource.getSourceId(), new NewsDataSource.GetNewsCallback() {
            @Override
            public void onLoadSuccess(NewsResponseModel newsResponseModel) {
                broadcast(newsResponseModel);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
        return true;
    }

    private void broadcast(NewsResponseModel newsResponseModel) {
        ArrayList<Parcelable> list=new ArrayList<>();
        if (newsResponseModel!=null && newsResponseModel.getArticles()!=null && !newsResponseModel.getArticles().isEmpty())
            list.addAll(newsResponseModel.getArticles());
        Intent intent=new Intent();
        intent.setAction(ACTION);
        intent.putParcelableArrayListExtra(EXTRA_DATA,list);
        sendBroadcast(intent);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
