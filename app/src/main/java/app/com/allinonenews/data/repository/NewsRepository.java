package app.com.allinonenews.data.repository;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.allinonenews.data.NewsDataSource;
import app.com.allinonenews.data.local.NewsLocalDataSource;
import app.com.allinonenews.data.remote.NewsRemoteDataSource;
import app.com.allinonenews.model.NewsModel;
import app.com.allinonenews.model.NewsResponseModel;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mukesh on 31/3/17.
 */

public class NewsRepository implements NewsDataSource {

    private static final String TAG = NewsRepository.class.getSimpleName();
    private NewsDataSource localDataSource;
    private NewsDataSource remoteDataSource;
    private Map<String, NewsResponseModel> map = new HashMap<>();


    public NewsRepository(Context context) {
        localDataSource = NewsLocalDataSource.getInstance(context);
        remoteDataSource = NewsRemoteDataSource.getInstance(context);

    }


    public void getNews(final String sourceName, final GetNewsCallback localSourceCallBack, final GetNewsCallback remoteSourceCallback) {
        getNewsFromLocalSource(sourceName, localSourceCallBack);
        getNewsFromRemoteSource(sourceName, remoteSourceCallback);

    }


    private void getNewsFromLocalSource(final String sourceName, final GetNewsCallback callback) {

        localDataSource.getNews(sourceName, new GetNewsCallback() {
            @Override
            public void onLoadSuccess(NewsResponseModel newsResponseModel) {
                map.put(sourceName, newsResponseModel);
                callback.onLoadSuccess(newsResponseModel);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    public void getNewsFromRemoteSource(final String sourceName, final GetNewsCallback callback) {
        remoteDataSource.getNews(sourceName, new GetNewsCallback() {
            @Override
            public void onLoadSuccess(NewsResponseModel newsResponseModel) {
                localDataSource.saveNews(newsResponseModel);
                NewsResponseModel t=saveInCache(sourceName, newsResponseModel);
                callback.onLoadSuccess(t);
            }

            @Override
            public void onDataNotAvailable() {
                if (map.get(sourceName) == null || map.get(sourceName).getArticles() == null || map.get(sourceName).getArticles().isEmpty()) {
                    callback.onDataNotAvailable();
                } else {
                    callback.onLoadSuccess(map.get(sourceName));
                }
            }
        });
    }

    @Override
    public void getNews(final String sourceName, final GetNewsCallback callback) {

        //getNewsFromLocalSource(sourceName,callback);
        //getNewsFromRemoteSource(sourceName,callback);
    }


    private void getNewsFromLocalSource(final String sourceName, String sortBy, final GetNewsCallback callback) {

        localDataSource.getNews(sourceName, sortBy, new GetNewsCallback() {
            @Override
            public void onLoadSuccess(NewsResponseModel newsResponseModel) {
                callback.onLoadSuccess(newsResponseModel);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getNewsFromRemoteSource(final String sourceName, String sortBy, final GetNewsCallback callback) {
        remoteDataSource.getNews(sourceName, sortBy, new GetNewsCallback() {
            @Override
            public void onLoadSuccess(NewsResponseModel newsResponseModel) {
                localDataSource.saveNews(newsResponseModel);
                callback.onLoadSuccess(saveInCache(sourceName, newsResponseModel));
            }

            @Override
            public void onDataNotAvailable() {
                if (map.get(sourceName) == null || map.get(sourceName).getArticles() == null || map.get(sourceName).getArticles().isEmpty()) {
                    callback.onDataNotAvailable();
                }
            }
        });
    }


//    public void getNews(String sourceName, String sortBy, GetNewsCallback localSourceCallback, GetNewsCallback remoteCallback) {
//        getNewsFromLocalSource(sourceName,sortBy,localSourceCallback);
//        getNewsFromRemoteSource(sourceName,sortBy,remoteCallback);
//    }


    @Override
    public void getNews(String sourceName, String sortBy, GetNewsCallback callback) {
        //getNewsFromLocalSource(sourceName, sortBy, callback);
        //getNewsFromRemoteSource(sourceName, sortBy, callback);
    }

    @Override
    public void saveNews(NewsResponseModel newsResponseModel) {
        localDataSource.saveNews(newsResponseModel);

    }

    @Override
    public void saveNews(NewsModel news, String sourceId) {
        localDataSource.saveNews(news, sourceId);
    }

    @Override
    public void loadMore(String sourceId, int lastNewsId, GetNewsCallback callback) {
        loadMore(sourceId, callback);
    }

    public void loadMore(final String sourceId, final GetNewsCallback callback) {
        checkNotNull(sourceId);
        NewsResponseModel responseModel = map.get(sourceId);
        if (responseModel == null) {
            if (callback != null)
                callback.onDataNotAvailable();
        } else {
            localDataSource.loadMore(sourceId, responseModel.getArticles().get(responseModel.getArticles().size() - 1).get_id(), new GetNewsCallback() {
                @Override
                public void onLoadSuccess(NewsResponseModel newsResponseModel) {
                    addInCacheInLoadMore(sourceId, newsResponseModel.getArticles());
                    callback.onLoadSuccess(newsResponseModel);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }


    private void addInCacheInLoadMore(String sourceId, List<NewsModel> newsModels) {
        NewsResponseModel saved = map.get(sourceId);
        if (saved != null && newsModels != null) {
            saved.getArticles().addAll(newsModels);
        }
    }

    private NewsResponseModel saveInCache(String sourceName, NewsResponseModel newsResponseModel) {
        NewsResponseModel saved = map.get(sourceName);
        if (saved == null) {
            map.put(sourceName, newsResponseModel);
            return newsResponseModel;
        } else {
            List<NewsModel> exitingItem=new ArrayList<>();
            if (saved.getArticles() != null && newsResponseModel.getArticles() != null && newsResponseModel.getArticles().size() > 0) {
                for (int i = 0; i < newsResponseModel.getArticles().size(); i++) {
                    if (!saved.getArticles().contains(newsResponseModel.getArticles().get(i))) {
                        saved.getArticles().add(0, newsResponseModel.getArticles().get(i));
                    }
                    else
                        exitingItem.add(newsResponseModel.getArticles().get(i));
                }
            } else {
                saved.setArticles(newsResponseModel.getArticles());
            }

            map.put(sourceName, saved);
            Log.e(TAG,newsResponseModel.getArticles().size()+" before");
            if (!exitingItem.isEmpty()){
                for (int i=0;i<exitingItem.size();i++) {
                    newsResponseModel.getArticles().remove(exitingItem.get(i));
                }
            }
            Log.e(TAG,newsResponseModel.getArticles().size()+" after");

            return newsResponseModel;

        }

    }

}
