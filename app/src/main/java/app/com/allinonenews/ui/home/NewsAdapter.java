package app.com.allinonenews.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import app.com.allinonenews.R;
import app.com.allinonenews.model.NewsModel;
import app.com.allinonenews.model.NewsResponseModel;

/**
 * Created by mukesh on 3/4/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private final NewsItemClickListener clickListener;
    private final Context context;
    private UpdateNotificationListener updateNotificationListener;
    private NewsResponseModel newsResponseModel;

    interface NewsItemClickListener{
        void onItemClick(NewsModel newsModel);
        void onAuthorNameClick(NewsModel newsModel);
    }

    interface UpdateNotificationListener{
        void showNoti(int count);
        void notificationSeen();
    }



    public NewsAdapter(Context context,NewsItemClickListener clickListener){
        this.context=context;
        this.clickListener=clickListener;
        newsResponseModel=new NewsResponseModel();
        newsResponseModel.setArticles(new ArrayList<NewsModel>());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            return new NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false));
        }
        else if (viewType == VIEW_TYPE_LOADING) {
            return new LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.load_more_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsViewHolder) {
            NewsViewHolder nH=(NewsViewHolder)holder;
            nH.bindData(newsResponseModel.getArticles().get(position));
            Log.e("onBindViewHolder", "position: " + position + "_id " + newsResponseModel.getArticles().get(position).get_id());
        }
        else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (newsResponseModel!=null && newsResponseModel.getArticles()!=null) {
            return newsResponseModel.getArticles().get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }
        return VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        if (newsResponseModel!=null && newsResponseModel.getArticles()!=null)
            return newsResponseModel.getArticles().size();
        return 0;
    }

    public void setUpdateNotificationListener(UpdateNotificationListener updateNotificationListener) {
        this.updateNotificationListener = updateNotificationListener;
    }

    private void setModel(NewsResponseModel responseModel){
        if (newsResponseModel==null) {
            newsResponseModel = new NewsResponseModel();
            newsResponseModel.setArticles(new ArrayList<NewsModel>());
        }
        newsResponseModel.setSource(responseModel.getSource());
        newsResponseModel.setSourceName(responseModel.getSourceName());
        newsResponseModel.setSortBy(responseModel.getSortBy());
        newsResponseModel.setStatus(responseModel.getStatus());

    }

    public void updateFromLocal(NewsResponseModel responseModel){
            setModel(responseModel);
            newsResponseModel.getArticles().clear();
            newsResponseModel.getArticles().addAll(0,responseModel.getArticles());
            notifyDataSetChanged();
    }

    public void updateFromNetwork(NewsResponseModel newsModels){
        setModel(newsModels);
        if (newsModels.getArticles()!=null) {
//            newsResponseModel.getArticles().addAll(0, newsModels.getArticles());
//            notifyDataSetChanged();
            appendNewsList(newsModels.getArticles());
        }
    }

    private void appendNewsList(List<NewsModel> list){
        if (list.size()>0){
            newsResponseModel.getArticles().addAll(0, list);
            notifyItemRangeInserted(0,list.size());
            updateNotificationListener.showNoti(list.size());

        }
    }




    public void loadMoreStart() {
            newsResponseModel.getArticles().add(null);
            notifyItemInserted(newsResponseModel.getArticles().size() - 1);
            //notifyDataSetChanged();
    }

    public void addLoadMoreResult(List<NewsModel> newsModels) {
        newsResponseModel.getArticles().remove(newsResponseModel.getArticles().size() - 1);
        notifyItemRemoved(newsResponseModel.getArticles().size());
        if (newsModels!=null && newsModels.size()>0){
            newsResponseModel.getArticles().addAll(newsModels);
            notifyDataSetChanged();
        }
    }


    public void clearList() {
        newsResponseModel=null;
        notifyDataSetChanged();
    }



     class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loadMoreProgress);
        }
    }


    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView title;
        private  final TextView author;
        private final TextView publishedAt;
        private final TextView description;
        private final ImageView image;

        private Context context;
        public NewsViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            title=(TextView)itemView.findViewById(R.id.newsItemTitle);
            author=(TextView)itemView.findViewById(R.id.newsItemAuthor);
            publishedAt=(TextView)itemView.findViewById(R.id.newsItemPublishedAt);
            description=(TextView)itemView.findViewById(R.id.newsItemDescription);
            image=(ImageView)itemView.findViewById(R.id.newsItemsImageView);
            itemView.setOnClickListener(this);
            author.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener!=null){
                switch (v.getId()){
                    case R.id.newsItemAuthor:
                        clickListener.onAuthorNameClick(newsResponseModel.getArticles().get(getAdapterPosition()));
                        break;
                    default:
                        clickListener.onItemClick(newsResponseModel.getArticles().get(getAdapterPosition()));
                        break;

                }

            }
        }

        void bindData(NewsModel m){

            String auth=m.getAuthor();

            //set author
            if (auth==null)
                auth="";
            auth=auth.replace("\t","").replace("\n","").trim();
            this.author.setText(context.getString(R.string.author,auth));

            //set title
            this.title.setText(m.getTitle());
            //set description
            this.description.setText(/*m.get_id()+"->"+"Source: "+newsResponseModel.getSource()+"\n"+*/m.getDescription());
            //set publish
            String pub=m.getPublishedAt();
            if (pub==null)
                pub="";
            pub=pub.replace("\t","").replace("\n","").trim();
            this.publishedAt.setText(context.getString(R.string.published, /*getTimeInLocal(pub)*/pub));

            Glide.with(this.image.getContext()).load(m.getUrlToImage())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            image.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            image.setVisibility(View.VISIBLE);
                            image.setImageDrawable(resource);
                            return false;
                        }
                    })
                    .into(this.image)

            ;

            if (getAdapterPosition()==0)
                updateNotificationListener.notificationSeen();
        }
    }
}
