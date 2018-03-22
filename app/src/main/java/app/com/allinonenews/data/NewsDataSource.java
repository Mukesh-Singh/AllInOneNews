package app.com.allinonenews.data;

import app.com.allinonenews.model.NewsModel;
import app.com.allinonenews.model.NewsResponseModel;

/**
 * Created by mukesh on 31/3/17.
 */

public interface NewsDataSource {
    interface GetNewsCallback{
        void onLoadSuccess(NewsResponseModel newsResponseModel);
        void onDataNotAvailable();
    }

    void getNews(String sourceName,GetNewsCallback callback);
    void getNews(String sourceName,String sortBy,GetNewsCallback callback);
    void saveNews(NewsResponseModel newsResponseModel);
    void saveNews(NewsModel news,String sourceId);
    void loadMore(String sourceId,int lastNewsId,GetNewsCallback callback);
}
