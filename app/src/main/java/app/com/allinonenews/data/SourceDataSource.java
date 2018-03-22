package app.com.allinonenews.data;

import java.util.List;

import app.com.allinonenews.model.Source;

/**
 * Created by mukesh on 31/3/17.
 */

public interface SourceDataSource {
    interface GetSourcesCallBack {
        void onLoadSuccess(List<Source> list);
        void onDataNotAvailable();
    }
    interface GetSourceCallBack{
        void onLoadSuccess(Source source);
        void onDataNotAvailable();
    }

    interface GetCategoryCallback{
        void onLoadSuccess(List<String> list);
        void onDataNotAvailable();
    }

    void getSources(GetSourcesCallBack callBack);
    void getSources(String country,GetSourcesCallBack callBack);
    void getSources(String country, String language, GetSourcesCallBack callBack);
    void getSources(String country, String language, String category, GetSourcesCallBack callBack);
    void getSource(String sourceId,GetSourceCallBack callBack);
    void saveSources(List<Source> list);
    void saveSource(Source source);
    void getCategories(GetCategoryCallback categoryCallback);
}
