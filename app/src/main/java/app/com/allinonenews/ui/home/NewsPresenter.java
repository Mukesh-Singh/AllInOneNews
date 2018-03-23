package app.com.allinonenews.ui.home;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;

import app.com.allinonenews.data.NewsDataSource;
import app.com.allinonenews.data.repository.NewsRepository;
import app.com.allinonenews.model.NewsModel;
import app.com.allinonenews.model.NewsResponseModel;
import app.com.allinonenews.model.SelectedSource;
import app.com.allinonenews.util.PrefUtil;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mukesh on 30/3/17.
 */

public class NewsPresenter implements NewsContract.Presenter{

    private static final String TAG = NewsPresenter.class.getSimpleName();
    //private static final String NEWS_ID="google-news";
    private NewsContract.View mView;
    private NewsRepository newsRepository;
    private boolean isLoading;
    private final int VISIBLE_THRESHOLD=1;
    private PrefUtil pref;
    private SelectedSource source;
    private boolean isUpdate;
    private int recyDy;


    public NewsPresenter (NewsRepository newsRepository, NewsContract.View view, PrefUtil pref){
        checkNotNull(newsRepository);
        checkNotNull(view);
        checkNotNull(pref);
        this.newsRepository=newsRepository;
        this.mView=view;
        this.mView.setPresenter(this);
        this.pref=pref;

    }

    @Override
    public void loadNews() {
        source=pref.getSelectedSource();
        mView.updateTitle(source.getSourceName());
        mView.setLoadingIndicator(true);
        newsRepository.getNews(source.getSourceId(), getLocalDataSourceCallBack(), getRemoteDataSourceCallBack());
    }

    private NewsDataSource.GetNewsCallback getLocalDataSourceCallBack(){
        return new NewsDataSource.GetNewsCallback() {
            @Override
            public void onLoadSuccess(NewsResponseModel newsResponseModel) {
                mView.hideNoNewsView();
                mView.showNews(newsResponseModel);
            }

            @Override
            public void onDataNotAvailable() {

            }
        };
    }

    private NewsDataSource.GetNewsCallback getRemoteDataSourceCallBack() {
        return new NewsDataSource.GetNewsCallback() {
            @Override
            public void onLoadSuccess(NewsResponseModel newsResponseModel) {
                mView.setLoadingIndicator(false);
                mView.hideNoNewsView();
                mView.updateFromNetwork(newsResponseModel);
            }

            @Override
            public void onDataNotAvailable() {
                mView.setLoadingIndicator(false);
                mView.hideNewsView();
                mView.showNoNewsView();
            }
        };
    }



    @Override
    public void forceFullyLoadFromNetwork() {
        mView.setLoadingIndicator(true);
        newsRepository.getNewsFromRemoteSource(source.getSourceId(), getRemoteDataSourceCallBack());
    }

    @Override
    public void newsDetails(NewsModel newsModel) {
        mView.showNewsDetails(newsModel);
    }

    @Override
    public void onAuthorNameClicked(NewsModel newsModel) {
        mView.openAuthorDetail(newsModel);
    }

    @Override
    public void loadMore() {
        try {
            LinearLayoutManager lm = mView.getRecyclerViewLayoutManager();
            int totalItemCount = lm.getItemCount();
            if(totalItemCount<=0) {
                mView.setVisibilityGoToTopButton(false);
                return;
            }
            int lastVisibleItem = lm.findLastVisibleItemPosition();
            if (!isLoading && totalItemCount <=(lastVisibleItem+VISIBLE_THRESHOLD) ){
                mView.showLoadMore();
                isLoading=true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        newsRepository.loadMore(source.getSourceId(), new NewsDataSource.GetNewsCallback() {
                            @Override
                            public void onLoadSuccess(NewsResponseModel newsResponseModel) {
                                mView.hideLoadMore(newsResponseModel.getArticles());
                                isLoading = false;
                            }

                            @Override
                            public void onDataNotAvailable() {
                                mView.hideLoadMore(null);
                                isLoading = false;
                            }
                        });
                    }
                },1000);



            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateNews(SelectedSource source) {
        mView.clearList();
        loadNews();
    }

    @Override
    public void updateNotification(int count) {
        isUpdate=true;
        mView.updateNotificationView(count);
    }

    @Override
    public void updateSeen() {
        isUpdate=false;
        mView.updateSeen();
    }

    @Override
    public boolean isUpdate(){
        return isUpdate;
    }





    @Override
    public void saveFirstVisibleItemItemPosition(int position,int offset) {
        pref.saveListPosition(position,offset);
    }

    @Override
    public void start() {

    }


}
