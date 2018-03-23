package app.com.allinonenews.ui.home;

import android.app.SearchManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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

import java.util.List;

import app.com.allinonenews.R;
import app.com.allinonenews.databinding.FragmentNewsBinding;
import app.com.allinonenews.model.NewsModel;
import app.com.allinonenews.model.NewsResponseModel;
import app.com.allinonenews.ui.details.DetailActivity;
import app.com.allinonenews.util.PrefUtil;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mukesh on 30/3/17.
 */

public class NewsFragment extends Fragment implements NewsContract.View{

    private static final String TAG = NewsFragment.class.getSimpleName();
    private NewsContract.Presenter newsPresenter;
    private LinearLayoutManager layoutManager;
    private NewsAdapter adapter;
    private FragmentNewsBinding binding;

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
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_news,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter=new NewsAdapter(getContext(),getNewsItemClickListener());
        layoutManager=new LinearLayoutManager(getContext());
        binding.newsRecyclerView.setLayoutManager(layoutManager);
        binding.newsRecyclerView.setAdapter(adapter);
        newsPresenter.loadNews();
        binding.refreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorAccent,R.color.hyper_link_color);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsPresenter.forceFullyLoadFromNetwork();
            }
        });
        binding.newsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                newsPresenter.loadMore();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                //Log.e(TAG, "First Visible item: "+firstVisibleItem);
                if (firstVisibleItem<=3){
                    setVisibilityOfGoToToButton(false);
                }
                else {
                    if (dy<=0)
                        setVisibilityOfGoToToButton(true);
                    else
                        setVisibilityOfGoToToButton(false);
                }

            }

        });
        binding.notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToTop();
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

        binding.goToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //newsPresenter.setVisibilityOfGoToToButton(false);
                scrollToTop();

            }
        });

    }

    public void setVisibilityOfGoToToButton(boolean visibility) {
        if (!newsPresenter.isUpdate()){
             setVisibilityGoToTopButton(visibility);
        }
    }

    @Override
    public void scrollToTop() {
        binding.newsRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void setVisibilityGoToTopButton(boolean visible) {
        if (visible)
        binding.goToTop.setVisibility(View.VISIBLE);
        else binding.goToTop.setVisibility(View.GONE);
    }

    @Override
    public void scrollToPosition(int position,int offset) {
        layoutManager.scrollToPositionWithOffset(position, offset);

    }

    @Override
    public LinearLayoutManager getRecyclerViewLayoutManager() {
        return layoutManager;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
            binding.refreshLayout.setRefreshing(active);


    }

    @Override
    public void showNews(NewsResponseModel newsResponseModel) {
        binding.newsRecyclerView.setVisibility(View.VISIBLE);
        adapter.updateFromLocal(newsResponseModel);
        scrollListToLastSavedPosition();
    }
    @Override
    public void updateFromNetwork(NewsResponseModel newsResponseModel){
        adapter.updateFromNetwork(newsResponseModel);
    }

    @Override
    public void hideNewsView() {
        binding.newsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showNoNewsView() {
        binding.noNewsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoNewsView() {
        binding.noNewsLayout.setVisibility(View.GONE);
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
        binding.notify.setText(count+" "+getString(R.string.update));
        binding.notify.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateSeen() {
        binding.notify.setText("Top");
        binding.notify.setVisibility(View.GONE);
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
        View v = binding.newsRecyclerView.getChildAt(0);
        int offset = (v == null) ? 0 : (v.getTop() - binding.newsRecyclerView.getPaddingTop());
        newsPresenter.saveFirstVisibleItemItemPosition(firstVisibleItem, offset);
        super.onStop();
    }

    public void scrollListToLastSavedPosition() {
        PrefUtil prefUtil=PrefUtil.getInstance(getContext());
        int pos=prefUtil.getListLastPosition();
        int offset=prefUtil.getListOffset();
        scrollToPosition(pos,offset);
    }
}
