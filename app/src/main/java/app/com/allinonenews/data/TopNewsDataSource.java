package app.com.allinonenews.data;

import app.com.allinonenews.data.model.TopNewsRequestParams;
import app.com.allinonenews.model.NewsResponseModel;

/**
 * Created by mukesh on 23/3/18.
 */

public interface TopNewsDataSource {
    interface GetTopNewsCallBack{
        void onLoadSuccess(NewsResponseModel newsResponseModel);
        void onDataNotAvailable();
    }

    void getTopNewsList(TopNewsRequestParams requestParams,GetTopNewsCallBack callBack);
}
