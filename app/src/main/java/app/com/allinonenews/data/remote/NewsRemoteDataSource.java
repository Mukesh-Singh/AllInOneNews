package app.com.allinonenews.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import app.com.allinonenews.data.NewsDataSource;
import app.com.allinonenews.data.remote.api.ApiMethods;
import app.com.allinonenews.data.remote.api.RestHelper;
import app.com.allinonenews.model.NewsModel;
import app.com.allinonenews.model.NewsResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mukesh on 31/3/17.
 */

public class NewsRemoteDataSource implements NewsDataSource {
    private static final String TAG = NewsRemoteDataSource.class.getSimpleName();
    private static NewsRemoteDataSource INSTANCE;


    private NewsRemoteDataSource(Context context){

    }
    public static NewsRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NewsRemoteDataSource(context);
        }
        return INSTANCE;
    }
    private static Map<String,String> getQueryMap(String source, String sortby) {
        Map<String,String> queryMap=new HashMap<>();
        if (source!=null && !source.isEmpty())
            queryMap.put("source",source);
        if (sortby!=null && !sortby.isEmpty())
            queryMap.put("sortBy",sortby);
        if (!ApiMethods.API_KEY.isEmpty())
            queryMap.put("apiKey",ApiMethods.API_KEY);
        return queryMap;
    }

    @Override
    public void getNews(String sourceName, final GetNewsCallback callback) {

        Call<NewsResponseModel> call = RestHelper.getInstance().getNewsList(getQueryMap(sourceName,null));
        call.enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, Response<NewsResponseModel> response) {
                callback.onLoadSuccess(response.body());
                //Log.e(TAG,response.body().toString());

            }

            @Override
            public void onFailure(Call<NewsResponseModel> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
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
