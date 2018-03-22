package app.com.allinonenews.ui.home;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import app.com.allinonenews.R;
import app.com.allinonenews.model.NewsModel;
import app.com.allinonenews.model.NewsResponseModel;
import app.com.allinonenews.ui.details.DetailActivity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mukesh on 30/3/17.
 */

public class NewsFragment extends Fragment implements NewsContract.View{

    private static final String TAG = NewsFragment.class.getSimpleName();
    private NewsContract.Presenter newsPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mNewsRecyclerView;
    private LinearLayout mNoNewsLayout;
    private LinearLayoutManager layoutManager;
    private ImageView mNoNewsImage;
    private TextView mNoNewsText;
    private TextView mRetryText;
    private NewsAdapter adapter;
    private TextView mNotification;
    private ImageView mGoToTop;

    public NewsFragment() {

    }

    public static NewsFragment newInstance(Bundle bundle) {
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.refreshLayout);
        mNewsRecyclerView=(RecyclerView)view.findViewById(R.id.newsRecyclerView);
        mNoNewsLayout=(LinearLayout) view.findViewById(R.id.noNewsLayout);
        mNoNewsImage=(ImageView)view.findViewById(R.id.noNewsIcon);
        mNoNewsText=(TextView)view.findViewById(R.id.noNoNewsText);
        mRetryText=(TextView)view.findViewById(R.id.noNewsRetry);
        mNotification=(TextView)view.findViewById(R.id.notify);
        mGoToTop=(ImageView)view.findViewById(R.id.goToTop);
        adapter=new NewsAdapter(getContext(),getNewsItemClickListener());
        layoutManager=new LinearLayoutManager(getContext());
        mNewsRecyclerView.setLayoutManager(layoutManager);
        mNewsRecyclerView.setAdapter(adapter);
        newsPresenter.loadNews();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorAccent,R.color.hyper_link_color);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsPresenter.forceFullyLoadFromNetwork();
            }
        });
        mNewsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                newsPresenter.loadMore(layoutManager);
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                //Log.e(TAG, "First Visible item: "+firstVisibleItem);
                if (firstVisibleItem<=3){
                    newsPresenter.setVisibilityOfGoToToButton(false);
                }
                else {
                    if (dy<=0)
                        newsPresenter.setVisibilityOfGoToToButton(true);
                    else
                        newsPresenter.setVisibilityOfGoToToButton(false);
                }

                //Log.e(TAG, "Dy : "+dy);
            }

        });
        mNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Notification", Toast.LENGTH_SHORT).show();
                //LinearLayoutManager layoutManager = (LinearLayoutManager) mNewsRecyclerView.getLayoutManager();
                //layoutManager.scrollToPositionWithOffset(0, 5);
               newsPresenter.goToTop();
            }
        });

        adapter.setUpdateNotificationListener(new NewsAdapter.UpdateNotificationListener() {
            @Override
            public void showNoti(int count) {
               newsPresenter.updateNotification(count);
            }

            @Override
            public void notificationSeen() {
                newsPresenter.updateSeen();
            }


        });

        mGoToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //newsPresenter.setVisibilityOfGoToToButton(false);
                newsPresenter.goToTop();

            }
        });

    }

    @Override
    public void scrollToTop() {
        mNewsRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void showGotToTopButton(boolean visible) {
        if (visible)
        mGoToTop.setVisibility(View.VISIBLE);
        else mGoToTop.setVisibility(View.GONE);
    }

    @Override
    public void scrollToPosition(int position,int offset) {
        layoutManager.scrollToPositionWithOffset(position, offset);

    }

    @Override
    public void setLoadingIndicator(boolean active) {
            mSwipeRefreshLayout.setRefreshing(active);


    }

    @Override
    public void showNews(NewsResponseModel newsResponseModel) {
        mNewsRecyclerView.setVisibility(View.VISIBLE);
        adapter.updateFromLocal(newsResponseModel);
        newsPresenter.scrollListToLastSavedPosition();
    }
    @Override
    public void updateFromNetwork(NewsResponseModel newsResponseModel){
        adapter.updateFromNetwork(newsResponseModel);
    }

    @Override
    public void hideNewsView() {
        mNewsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showNoNewsView() {
        mNoNewsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoNewsView() {
        mNoNewsLayout.setVisibility(View.GONE);
    }

    @Override
    public void showNewsDetails(NewsModel newsModel) {
        Intent intent=new Intent(getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_NEWS_MODEL,newsModel);
        getActivity().startActivity(intent);
    }

    @Override
    public void openAuthorDetail(NewsModel newsModel) {
        String authorText=newsModel.getAuthor();
        if (authorText==null||authorText.isEmpty())
            return;
        try {

            if (authorText.contains("http")){
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(authorText));
                getContext().startActivity(i);
            }
            else {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, authorText);
                getContext().startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoadMore() {
        adapter.loadMoreStart();
    }

    @Override
    public void hideLoadMore(List<NewsModel> newsModels) {
        adapter.addLoadMoreResult(newsModels);
    }

    @Override
    public void clearList() {
        adapter.clearList();
    }

    @Override
    public void updateTitle(String string) {
        getActivity().setTitle(string);
    }

    @Override
    public void updateNotificationView(int count) {
        mNotification.setText(count+" "+getString(R.string.update));
        mNotification.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateSeen() {
        mNotification.setText("Top");
        mNotification.setVisibility(View.GONE);
    }


    @Override
    public void setPresenter(NewsContract.Presenter presenter) {
        newsPresenter=checkNotNull(presenter);

    }

    public NewsAdapter.NewsItemClickListener getNewsItemClickListener() {
        return new NewsAdapter.NewsItemClickListener() {
            @Override
            public void onItemClick(NewsModel newsModel) {
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(newsModel.getUrl()));
//                getContext().startActivity(i);
                newsPresenter.newsDetails(newsModel);
            }

            @Override
            public void onAuthorNameClick(NewsModel newsModel) {
                newsPresenter.onAuthorNameClicked(newsModel);
            }
        };
    }

    @Override
    public void onStop() {
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        View v = mNewsRecyclerView.getChildAt(0);
        int offset = (v == null) ? 0 : (v.getTop() - mNewsRecyclerView.getPaddingTop());
        newsPresenter.saveFirstVisibleItemItemPosition(firstVisibleItem, offset);
        super.onStop();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
