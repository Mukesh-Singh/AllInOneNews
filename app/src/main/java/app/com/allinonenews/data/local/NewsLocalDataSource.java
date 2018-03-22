package app.com.allinonenews.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

import app.com.allinonenews.data.Constants;
import app.com.allinonenews.data.NewsDataSource;
import app.com.allinonenews.model.NewsModel;
import app.com.allinonenews.model.NewsResponseModel;
import app.com.allinonenews.model.Source;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mukesh on 31/3/17.
 */

public class NewsLocalDataSource implements NewsDataSource {

    private static NewsLocalDataSource INSTANCE;
    private NewsDbHelper dbHelper;
    private SourceLocalDataSource sourceLocalDataSource;
    private static final String LIMIT="40";

    private NewsLocalDataSource(Context context) {
        checkNotNull(context);
        dbHelper = new NewsDbHelper(context);
        sourceLocalDataSource = SourceLocalDataSource.getInstance(context);
    }

    public static NewsLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NewsLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getNews(String sourceId,GetNewsCallback callback) {
            String[] projection = getProjectionOfAllField();
            String selection = NewsPersistence.NewsEntity.COLUMN_SOURCE_ID + " LIKE ?";
            String[] selectionArgument = {sourceId};
            String orderby=NewsPersistence.NewsEntity._ID+" DESC";
            SQLiteDatabase db=dbHelper.getReadableDatabase();
            Cursor cursor = db.query(NewsPersistence.NewsEntity.TABLE_NAME, projection, selection, selectionArgument, null, null, orderby,LIMIT);
            sendCallBack(getRespectedNewsResponseModel(sourceId,getNewsList(cursor)),callback);
    }

    @Override
    public void getNews(String sourceId, String sortBy,GetNewsCallback callback) {
        getNews(sourceId,callback);
    }


    private void sendCallBack(NewsResponseModel newsResponseModel,NewsDataSource.GetNewsCallback callBack){
        if (callBack!=null){
            if (newsResponseModel==null ){
                callBack.onDataNotAvailable();
            }
            else {
                callBack.onLoadSuccess(newsResponseModel);
            }
        }
    }

    @Override
    public void saveNews(NewsResponseModel newsResponseModel) {
        checkNotNull(newsResponseModel);
        checkNotNull(newsResponseModel.getArticles());
        if (!newsResponseModel.getArticles().isEmpty()) {
            for (NewsModel news : newsResponseModel.getArticles()) {
                saveNews(news, newsResponseModel.getSource());
            }


        }
    }

    @Override
    public void saveNews(NewsModel news, String sourceId) {
        checkNotNull(news);
        if (!Strings.isNullOrEmpty(sourceId)) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NewsPersistence.NewsEntity.COLUMN_TITLE, news.getTitle());
            values.put(NewsPersistence.NewsEntity.COLUMN_AUTHOR, news.getAuthor());
            values.put(NewsPersistence.NewsEntity.COLUMN_DESCRIPTION, news.getDescription());
            values.put(NewsPersistence.NewsEntity.COLUMN_URL, news.getUrl());
            values.put(NewsPersistence.NewsEntity.COLUMN_URL_TO_IMAGE, news.getUrlToImage());
            values.put(NewsPersistence.NewsEntity.COLUMN_SOURCE_ID, sourceId);
            values.put(NewsPersistence.NewsEntity.COLUMN_PUBLISHED_AT, news.getPublishedAt());
            //check if already exist


            String id="";
            try {
                String[] projection = {
                        NewsPersistence.NewsEntity._ID
                };

                String selection = NewsPersistence.NewsEntity.COLUMN_TITLE + " LIKE ? AND " /*+ NewsPersistence.NewsEntity.COLUMN_AUTHOR + " LIKE ? AND "*/ + NewsPersistence.NewsEntity.COLUMN_URL + " LIKE ?";
                String[] selectionArgument = {news.getTitle(), news.getUrl()};


                Cursor cursor = db.query(NewsPersistence.NewsEntity.TABLE_NAME, projection, selection, selectionArgument, null, null, null);

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    id = cursor.getString(cursor.getColumnIndexOrThrow(NewsPersistence.NewsEntity._ID));
                }
                if(cursor!=null){
                    cursor.close();
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
            if (id.isEmpty()) {
                db.insert(NewsPersistence.NewsEntity.TABLE_NAME, null, values);
            }
            else {
                String condition=NewsPersistence.NewsEntity._ID+" LIKE ?";
                String []arg={id};
                db.update(NewsPersistence.NewsEntity.TABLE_NAME,values,condition,arg);
            }
            db.close();

        }
    }

    @Override
    public void loadMore(String sourceId,int lastNewsId,GetNewsCallback callback) {
        String[] projection = getProjectionOfAllField();
        String selection = NewsPersistence.NewsEntity.COLUMN_SOURCE_ID + " LIKE ? AND "+ NewsPersistence.NewsEntity._ID+"< ?";
        String[] selectionArgument = {sourceId,String.valueOf(lastNewsId)};
        String orderby=NewsPersistence.NewsEntity._ID+" DESC";
        SQLiteDatabase database=dbHelper.getReadableDatabase();
        Cursor cursor = database.query(NewsPersistence.NewsEntity.TABLE_NAME, projection, selection, selectionArgument, null, null, orderby,LIMIT);
        sendCallBack(getRespectedNewsResponseModel(sourceId,getNewsList(cursor)),callback);
    }


    private String[] getProjectionOfAllField(){
        return  new String []{
                NewsPersistence.NewsEntity._ID,
                NewsPersistence.NewsEntity.COLUMN_TITLE,
                NewsPersistence.NewsEntity.COLUMN_AUTHOR,
                NewsPersistence.NewsEntity.COLUMN_DESCRIPTION,
                NewsPersistence.NewsEntity.COLUMN_PUBLISHED_AT,
                NewsPersistence.NewsEntity.COLUMN_URL,
                NewsPersistence.NewsEntity.COLUMN_URL_TO_IMAGE,
                NewsPersistence.NewsEntity.COLUMN_SOURCE_ID
        };
    }

    private List<NewsModel> getNewsList(Cursor cursor){
        List<NewsModel> tempList=new ArrayList<>();
        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            for (int i=0;i<cursor.getCount();i++){
                NewsModel newsModel = new NewsModel();
                int _id=cursor.getInt(cursor.getColumnIndexOrThrow(NewsPersistence.NewsEntity._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(NewsPersistence.NewsEntity.COLUMN_TITLE));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(NewsPersistence.NewsEntity.COLUMN_AUTHOR));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(NewsPersistence.NewsEntity.COLUMN_DESCRIPTION));
                String publish = cursor.getString(cursor.getColumnIndexOrThrow(NewsPersistence.NewsEntity.COLUMN_PUBLISHED_AT));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(NewsPersistence.NewsEntity.COLUMN_URL));
                String urlImage = cursor.getString(cursor.getColumnIndexOrThrow(NewsPersistence.NewsEntity.COLUMN_URL_TO_IMAGE));
                //String source=cursor.getString(cursor.getColumnIndexOrThrow(NewsPersistence.NewsEntity.COLUMN_SOURCE_ID));

                newsModel.set_id(_id);
                newsModel.setTitle(title);
                newsModel.setAuthor(author);
                newsModel.setDescription(desc);
                newsModel.setPublishedAt(publish);
                newsModel.setUrl(url);
                newsModel.setUrlToImage(urlImage);
                tempList.add(newsModel);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return tempList;
    }

    private NewsResponseModel getRespectedNewsResponseModel(String sourceId,List<NewsModel> list){
        NewsResponseModel newsResponseModel = new NewsResponseModel();
        Source source = sourceLocalDataSource.getSourceFromId(sourceId);

        newsResponseModel.setArticles(list);
        newsResponseModel.setSortBy(Constants.SORT_BY);
        newsResponseModel.setSource(sourceId);
        newsResponseModel.setSourceName("Google News");
        return newsResponseModel;
    }

}
