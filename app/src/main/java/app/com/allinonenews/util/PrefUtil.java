package app.com.allinonenews.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import app.com.allinonenews.model.SelectedFilter;
import app.com.allinonenews.model.SelectedSource;

import static app.com.allinonenews.data.Constants.CATEGORY_ALL;

/**
 * Created by mukesh on 5/4/17.
 */

public class PrefUtil {
    private static final String PREF_NAME="All_in_one_news_pref";
    private static PrefUtil INSTANCE;
    private SharedPreferences preferences;
    private static final long TIME_TO_UPDATE_SOURCE=24*60*60*1000;
    private static final String DEFAULT_SOURCE_NAME="Google News";
    private static final String DEFAULT_SOURCE_ID="google-news";



    public static class Key{
        public static final String SOURCE_SAVED_LAST_AT="source_saved_last_at";
        public static final String SELECTED_SOURCE_NAME = "selected_source_name";
        public static final String SELECTED_SOURCE_ID = "selected_source_id";
        public static final String FILTER_COUNTRY = "filter_country";
        public static final String FILTER_LANGUAGE = "filter_language";
        public static final String FILTER_CATEGORY = "filter_category";
        public static final String LIST_LAST_POSITION = "list_last_position";
        public static final String LIST_OFFSET = "list_offset";
    }

    private PrefUtil(Context context){
        this.preferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }

    public static PrefUtil getInstance(Context context){
        if (INSTANCE==null){
            INSTANCE=new PrefUtil(context);
        }
        return INSTANCE;
    }


    public boolean needToUpdateSource() {
        long sourceSavedAtLast = preferences.getLong(Key.SOURCE_SAVED_LAST_AT, 0);
        return sourceSavedAtLast == 0 || ((System.currentTimeMillis()-sourceSavedAtLast) > TIME_TO_UPDATE_SOURCE);
    }

    public void saveSourceUpdateTime(){
        SharedPreferences.Editor editor= preferences.edit();
        editor.putLong(Key.SOURCE_SAVED_LAST_AT,System.currentTimeMillis());
        editor.apply();
    }

    public void saveSelectedSource(@Nullable SelectedSource selectedSource){

        SharedPreferences.Editor editor= preferences.edit();

        if (selectedSource==null || Strings.isNullOrEmpty(selectedSource.getSourceName()) || Strings.isNullOrEmpty(selectedSource.getSourceName())){
            editor.putString(Key.SELECTED_SOURCE_NAME,DEFAULT_SOURCE_NAME);
            editor.putString(Key.SELECTED_SOURCE_ID,DEFAULT_SOURCE_ID);
        }
        else {
            editor.putString(Key.SELECTED_SOURCE_NAME,selectedSource.getSourceName());
            editor.putString(Key.SELECTED_SOURCE_ID,selectedSource.getSourceId());
        }
        editor.apply();
    }

    public SelectedSource getSelectedSource(){
        SelectedSource s= new SelectedSource();
        String name=preferences.getString(Key.SELECTED_SOURCE_NAME,DEFAULT_SOURCE_NAME);
        String id=preferences.getString(Key.SELECTED_SOURCE_ID,DEFAULT_SOURCE_ID);
        s.setSourceId(id);
        s.setSourceName(name);
        return s;
    }


    public void saveFilter(@Nullable SelectedFilter filter){
        SharedPreferences.Editor editor= preferences.edit();
        if (filter==null ){
            editor.putString(Key.FILTER_COUNTRY,"");
            editor.putString(Key.FILTER_LANGUAGE,"");
            editor.putString(Key.FILTER_CATEGORY,CATEGORY_ALL);

        }
        else {
            String cou=filter.getCountry();
            if (Strings.isNullOrEmpty(cou))
                cou="";
            String lan=filter.getLanguage();
            if (Strings.isNullOrEmpty(lan))
                lan="";

            String cat=filter.getCategory();
            if (Strings.isNullOrEmpty(cat))
                cat="";

            editor.putString(Key.FILTER_COUNTRY,cou);
            editor.putString(Key.FILTER_LANGUAGE,lan);
            editor.putString(Key.FILTER_CATEGORY,cat);
        }
        editor.apply();
    }

    public SelectedFilter getFilter(){
        SelectedFilter f= new SelectedFilter();
        String cou=preferences.getString(Key.FILTER_CATEGORY,"");
        String lan=preferences.getString(Key.FILTER_LANGUAGE,"");
        String cat=preferences.getString(Key.FILTER_CATEGORY,CATEGORY_ALL);
        f.setCountry(cou);
        f.setLanguage(lan);
        f.setCategory(cat);
        return f;
    }


    public void saveListPosition(int position, int offset) {
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt(Key.LIST_LAST_POSITION,position);
        editor.putInt(Key.LIST_OFFSET,offset);
        editor.apply();
    }

    public int getListLastPosition(){
        return preferences.getInt(Key.LIST_LAST_POSITION,0);
    }

    public int getListOffset(){
        return preferences.getInt(Key.LIST_OFFSET,0);
    }

}
