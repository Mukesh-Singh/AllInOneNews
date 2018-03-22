package app.com.allinonenews.sync;

import android.content.Context;

import app.com.allinonenews.data.NewsDataSource;
import app.com.allinonenews.data.local.NewsLocalDataSource;
import app.com.allinonenews.data.remote.NewsRemoteDataSource;
import app.com.allinonenews.model.NewsModel;
import app.com.allinonenews.model.NewsResponseModel;

/**
 * Created by mukesh on 11/4/17.
 */

public class SyncRepository implements NewsDataSource {

    private static final String TAG = SyncRepository.class.getSimpleName();
    private NewsDataSource localDataSource;
    private NewsDataSource remoteDataSource;


    public SyncRepository(Context context) {
        localDataSource = NewsLocalDataSource.getInstance(context);
        remoteDataSource = NewsRemoteDataSource.getInstance(context);

    }


    private void getNewsFromRemoteSource(final String sourceName, final GetNewsCallback callback) {
        remoteDataSource.getNews(sourceName, new GetNewsCallback() {
            @Override
            public void onLoadSuccess(NewsResponseModel newsResponseModel) {
                localDataSource.saveNews(newsResponseModel);
                if (callback != null)
                    callback.onLoadSuccess(newsResponseModel);
            }

            @Override
            public void onDataNotAvailable() {
                if (callback != null)
                    callback.onDataNotAvailable();
            }
        });
    }


    @Override
    public void getNews(String sourceName, GetNewsCallback callback) {
        getNewsFromRemoteSource(sourceName, callback);
    }

    @Override
    public void getNews(String sourceName, String sortBy, GetNewsCallback callback) {

    }

    @Override
    public void saveNews(NewsResponseModel newsResponseModel) {

    }

    @Override
    public void saveNews(NewsModel news, String sourceId) {

    }

    @Override
    public void loadMore(String sourceId, int lastNewsId, GetNewsCallback callback) {

    }
}
