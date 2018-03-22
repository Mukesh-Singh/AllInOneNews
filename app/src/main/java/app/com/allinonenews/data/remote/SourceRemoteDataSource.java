package app.com.allinonenews.data.remote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.allinonenews.data.SourceDataSource;
import app.com.allinonenews.data.remote.api.RestHelper;
import app.com.allinonenews.model.Source;
import app.com.allinonenews.model.SourceResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mukesh on 31/3/17.
 */

public class SourceRemoteDataSource implements SourceDataSource{

    private static final String TAG = SourceRemoteDataSource.class.getSimpleName();
    private static SourceRemoteDataSource INSTANCE;


    //private SourceRemoteDataSource(Context context){

    //}
    public static SourceRemoteDataSource getInstance(/*@NonNull Context context*/) {
        if (INSTANCE == null) {
            INSTANCE = new SourceRemoteDataSource(/*context*/);
        }
        return INSTANCE;
    }
    private static Map<String,String> getSourceQueryMap(String country, String language, String category) {
        Map<String,String> queryMap=new HashMap<>();
        if (country!=null && !country.isEmpty())
            queryMap.put("country",country);
        if (language!=null && !language.isEmpty())
            queryMap.put("language",language);
        if (category!=null && !category.isEmpty())
            queryMap.put("category",category);

        return queryMap;
    }

    @Override
    public void getSources(final GetSourcesCallBack callBack) {
        getSources(null,null,null,callBack);
    }

    @Override
    public void getSources(String country, GetSourcesCallBack callBack) {
        getSources(country,null,null,callBack);
    }

    @Override
    public void getSources(String country, String language, GetSourcesCallBack callBack) {
        getSources(country,language,null,callBack);
    }

    @Override
    public void getSources(String country, String language, String category, final GetSourcesCallBack callBack) {
        Call<SourceResponseModel> call = RestHelper.getInstance().getSources(getSourceQueryMap(country,language,category));
        call.enqueue(new Callback<SourceResponseModel>() {
            @Override
            public void onResponse(Call<SourceResponseModel> call, Response<SourceResponseModel> response) {
                if (response.body()==null || response.body().getSources()==null || response.body().getSources().isEmpty()){
                    callBack.onDataNotAvailable();
                }
                else {
                    callBack.onLoadSuccess(response.body().getSources());
                    //Log.e(TAG,response.body().toString());
                }

            }

            @Override
            public void onFailure(Call<SourceResponseModel> call, Throwable t) {
                callBack.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getSource(String sourceId, GetSourceCallBack callBack) {

    }

    @Override
    public void saveSources(List<Source> list) {

    }

    @Override
    public void saveSource(Source source) {

    }

    @Override
    public void getCategories(GetCategoryCallback categoryCallback) {

    }


}
