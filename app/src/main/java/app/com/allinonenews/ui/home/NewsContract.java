package app.com.allinonenews.ui.home;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import app.com.allinonenews.base.BasePresenter;
import app.com.allinonenews.base.BaseView;
import app.com.allinonenews.model.NewsModel;
import app.com.allinonenews.model.NewsResponseModel;
import app.com.allinonenews.model.SelectedSource;

/**
 * Created by mukesh on 30/3/17.
 */

public interface NewsContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showNews(NewsResponseModel responseModel);

        void updateFromNetwork(NewsResponseModel newsModels);

        void hideNewsView();

        void showNoNewsView();

        void hideNoNewsView();

        void showNewsDetails(NewsModel newsModel);

        void openAuthorDetail(NewsModel newsModel);

        void showLoadMore();

        void hideLoadMore(List<NewsModel> newsModels);

        void clearList();

        void updateTitle(String string);

        void updateNotificationView(int count);

        void updateSeen();

        void scrollToTop();

        void showGotToTopButton(boolean visible);

        void scrollToPosition(int position, int offset);
    }

    interface Presenter extends BasePresenter {

        void loadNews();

        void forceFullyLoadFromNetwork();

        void newsDetails(NewsModel newsModel);

        void onAuthorNameClicked(NewsModel newsModel);

        void loadMore(RecyclerView.LayoutManager layoutManager);

        void updateNews(SelectedSource source);

        void updateNotification(int count);

        void updateSeen();

        void goToTop();

        void setVisibilityOfGoToToButton(boolean visible);

        void saveFirstVisibleItemItemPosition(int position, int offset);

        void scrollListToLastSavedPosition();
    }

}
