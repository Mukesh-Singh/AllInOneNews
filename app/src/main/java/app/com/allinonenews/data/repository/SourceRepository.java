package app.com.allinonenews.data.repository;

import android.content.Context;

import java.util.List;

import app.com.allinonenews.data.SourceDataSource;
import app.com.allinonenews.data.local.SourceLocalDataSource;
import app.com.allinonenews.data.remote.SourceRemoteDataSource;
import app.com.allinonenews.model.Source;
import app.com.allinonenews.util.PrefUtil;

/**
 * Created by mukesh on 5/4/17.
 */

public class SourceRepository implements SourceDataSource{
    private SourceDataSource localDataSource;
    private SourceDataSource remoteDataSource;
    private Context context;


    public SourceRepository(Context context){
        this.context=context;
        localDataSource= SourceLocalDataSource.getInstance(context);
        remoteDataSource= SourceRemoteDataSource.getInstance();
    }


    @Override
    public void getSources(final GetSourcesCallBack callBack) {
        localDataSource.getSources(new GetSourcesCallBack() {
            @Override
            public void onLoadSuccess(List<Source> list) {
                callBack.onLoadSuccess(list);
            }

            @Override
            public void onDataNotAvailable() {
                if (PrefUtil.getInstance(context).needToUpdateSource()){
                    getFromRemote(null,null,null,callBack);
                }
            }
        });
    }

    @Override
    public void getSources(String country, final GetSourcesCallBack callBack) {
        localDataSource.getSources(country,new GetSourcesCallBack() {
            @Override
            public void onLoadSuccess(List<Source> list) {
                callBack.onLoadSuccess(list);
            }

            @Override
            public void onDataNotAvailable() {
                callBack.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getSources(String country, String language, final GetSourcesCallBack callBack) {
        localDataSource.getSources(country,language,new GetSourcesCallBack() {
            @Override
            public void onLoadSuccess(List<Source> list) {
                callBack.onLoadSuccess(list);
            }

            @Override
            public void onDataNotAvailable() {
                callBack.onDataNotAvailable();
            }
        });
    }





    @Override
    public void getSources(final String country, final String language, final String category, final GetSourcesCallBack callBack) {
        localDataSource.getSources(country,language,category,new GetSourcesCallBack() {
            @Override
            public void onLoadSuccess(List<Source> list) {
                callBack.onLoadSuccess(list);
            }

            @Override
            public void onDataNotAvailable() {
                callBack.onDataNotAvailable();
            }
        });

    }

    private void getFromRemote(String country, String language, String category, final GetSourcesCallBack callBack) {
        remoteDataSource.getSources(country, language, category, new GetSourcesCallBack() {
            @Override
            public void onLoadSuccess(List<Source> list) {
                localDataSource.saveSources(list);
                PrefUtil.getInstance(context).saveSourceUpdateTime();
                callBack.onLoadSuccess(list);
            }

            @Override
            public void onDataNotAvailable() {
                callBack.onDataNotAvailable();
            }
        });

    }

    @Override
    public void getSource(String sourceId, GetSourceCallBack callBack) {
            localDataSource.getSource(sourceId,callBack);
    }

    @Override
    public void saveSources(List<Source> list) {
            localDataSource.saveSources(list);
    }

    @Override
    public void saveSource(Source source) {
            localDataSource.saveSource(source);
    }

    @Override
    public void getCategories(GetCategoryCallback categoryCallback) {
        localDataSource.getCategories(categoryCallback);
    }
}
