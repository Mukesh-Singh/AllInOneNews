package app.com.allinonenews.data.remote.api;

import java.util.Map;

import app.com.allinonenews.model.NewsResponseModel;
import app.com.allinonenews.model.SourceResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by mukesh on 27/3/17.
 */

public interface ApiMethods {
    String API_KEY="2890fe75234046e0bf976a2b8014aeae";
    String API_URL="https://newsapi.org/v1/";
    @GET("articles")
    Call<NewsResponseModel> getNewsList(@QueryMap Map<String, String> options);

    @GET("sources")
    Call<SourceResponseModel> getSources(@QueryMap Map<String, String> options);

}
