package app.com.allinonenews.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.com.allinonenews.data.SourceDataSource;
import app.com.allinonenews.model.Source;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mukesh on 31/3/17.
 */

public class SourceLocalDataSource implements SourceDataSource {

    private static SourceLocalDataSource INSTANCE;
    private NewsDbHelper dbHelper;

    private SourceLocalDataSource(Context context) {
        dbHelper = new NewsDbHelper(context);

    }

    public static SourceLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SourceLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getSources(GetSourcesCallBack callBack) {


        String[] projection = getProjectionForAllField();
        List<Source> list = query(projection, null, null);
        sendCallBack(list, callBack);
    }

    @Override
    public void getSources(String country, GetSourcesCallBack callBack) {
        String[] projection = getProjectionForAllField();
        String selection = SourcePersistence.SourceEntity.COLUMN_COUNTRY + " LIKE ?";
        String[] selectionArg = {country};

        List<Source> list = query(projection, selection, selectionArg);
        ;
        sendCallBack(list, callBack);
    }

    @Override
    public void getSources(String country, String language, GetSourcesCallBack callBack) {
        String[] projection = getProjectionForAllField();
        String selection = SourcePersistence.SourceEntity.COLUMN_COUNTRY + " LIKE ? AND " + SourcePersistence.SourceEntity.COLUMN_LANGUAGE + " LIKE ?";
        String[] selectionArg = {country, language};

        List<Source> list = query(projection, selection, selectionArg);
        sendCallBack(list, callBack);
    }

    @Override
    public void getSources(String country, String language, String category, GetSourcesCallBack callBack) {
        String[] projection = getProjectionForAllField();
        String selection = SourcePersistence.SourceEntity.COLUMN_COUNTRY + " LIKE ? AND " + SourcePersistence.SourceEntity.COLUMN_LANGUAGE + " LIKE ? AND " + SourcePersistence.SourceEntity.COLUMN_LANGUAGE + " LIKE ?";
        String[] selectionArg = {country, language, category};

        List<Source> list = query(projection, selection, selectionArg);
        sendCallBack(list, callBack);

    }

    public Source getSourceFromId(String sourceId) {
        List<Source> list = getSources(sourceId);
        if (list == null || list.isEmpty())
            return null;
        return list.get(0);
    }

    private List<Source> getSources(String sourceId) {
        String[] projection = getProjectionForAllField();
        String selection = SourcePersistence.SourceEntity.COLUMN_SOURCE_ID + " LIKE ?";
        String[] selectionArg = {sourceId};

        return query(projection, selection, selectionArg);
    }

    @Override
    public void getSource(String sourceId, GetSourceCallBack callBack) {
        List<Source> list = getSources(sourceId);
        if (callBack != null) {
            if (list == null || list.isEmpty()) {
                callBack.onDataNotAvailable();
            } else {
                callBack.onLoadSuccess(list.get(0));
            }
        }
    }

    private void deleteAllSource() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(SourcePersistence.SourceEntity.TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void saveSources(List<Source> list) {
        if (list != null && !list.isEmpty()) {
            deleteAllSource();
            for (Source source : list) {
                saveSource(source);
            }
        }
    }

    private void sendCallBack(List<Source> list, GetSourcesCallBack callBack) {
        if (callBack != null) {
            if (list == null || list.isEmpty()) {
                callBack.onDataNotAvailable();
            } else {
                callBack.onLoadSuccess(list);
            }
        }
    }

    @Override
    public void saveSource(Source source) {
        checkNotNull(source);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SourcePersistence.SourceEntity.COLUMN_CATEGORY, source.getCategory());
        values.put(SourcePersistence.SourceEntity.COLUMN_COUNTRY, source.getCountry());
        values.put(SourcePersistence.SourceEntity.COLUMN_DESCRIPTION, source.getDescription());
        values.put(SourcePersistence.SourceEntity.COLUMN_LANGUAGE, source.getLanguage());
        values.put(SourcePersistence.SourceEntity.COLUMN_NAME, source.getName());
        String sortyAva = "";
        if (source.getSortBysAvailable() != null && !source.getSortBysAvailable().isEmpty()) {
            for (String string : source.getSortBysAvailable()) {
                sortyAva = sortyAva + "," + string;
            }
            if (!sortyAva.isEmpty()) {
                sortyAva = sortyAva.substring(1, sortyAva.length());
            }
        }
        values.put(SourcePersistence.SourceEntity.COLUMN_SORT_BY_AVAILABLE, sortyAva);
        values.put(SourcePersistence.SourceEntity.COLUMN_SOURCE_ID, source.getId());
        values.put(SourcePersistence.SourceEntity.COLUMN_URL, source.getUrl());
        if (source.getUrlsToLogos() != null) {
            values.put(SourcePersistence.SourceEntity.COLUMN_URL_TO_LOGOS_LARGE, source.getUrlsToLogos().getLarge());
            values.put(SourcePersistence.SourceEntity.COLUMN_URL_TO_LOGOS_MEDIUM, source.getUrlsToLogos().getMedium());
            values.put(SourcePersistence.SourceEntity.COLUMN_URL_TO_LOGOS_SMALL, source.getUrlsToLogos().getSmall());
        }

        db.insert(SourcePersistence.SourceEntity.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void getCategories(GetCategoryCallback categoryCallback) {
        List<String> list = new ArrayList<>();
        String[] projection = {SourcePersistence.SourceEntity.COLUMN_CATEGORY};
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(true,SourcePersistence.SourceEntity.TABLE_NAME, projection, null,null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    list.add(cursor.getString(cursor.getColumnIndexOrThrow(SourcePersistence.SourceEntity.COLUMN_CATEGORY)));
                    cursor.moveToNext();
                }
            }

            cursor.close();
        }
        db.close();
        if (list.isEmpty())
            categoryCallback.onDataNotAvailable();
        else categoryCallback.onLoadSuccess(list);

    }


    private List<Source> query(String[] projection, String selection, String[] selectionArg) {
        List<Source> list = new ArrayList<>();
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(SourcePersistence.SourceEntity.TABLE_NAME, projection, selection, selectionArg, null, null, null);
            list = getNewsListFromCursor(cursor);

            if (cursor != null) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }


    private String[] getProjectionForAllField() {
        return new String[]{
                SourcePersistence.SourceEntity.COLUMN_CATEGORY,
                SourcePersistence.SourceEntity.COLUMN_COUNTRY,
                SourcePersistence.SourceEntity.COLUMN_DESCRIPTION,
                SourcePersistence.SourceEntity.COLUMN_LANGUAGE,
                SourcePersistence.SourceEntity.COLUMN_NAME,
                SourcePersistence.SourceEntity.COLUMN_SORT_BY_AVAILABLE,
                SourcePersistence.SourceEntity.COLUMN_SOURCE_ID,
                SourcePersistence.SourceEntity.COLUMN_URL,
                SourcePersistence.SourceEntity.COLUMN_URL_TO_LOGOS_LARGE,
                SourcePersistence.SourceEntity.COLUMN_URL_TO_LOGOS_MEDIUM,
                SourcePersistence.SourceEntity.COLUMN_URL_TO_LOGOS_SMALL,


        };
    }

    private List<Source> getNewsListFromCursor(Cursor cursor) {
        checkNotNull(cursor);
        List<Source> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Source source = new Source();
                String cat = cursor.getString(cursor.getColumnIndexOrThrow(SourcePersistence.SourceEntity.COLUMN_CATEGORY));
                String country = cursor.getString(cursor.getColumnIndexOrThrow(SourcePersistence.SourceEntity.COLUMN_COUNTRY));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(SourcePersistence.SourceEntity.COLUMN_DESCRIPTION));
                String lang = cursor.getString(cursor.getColumnIndexOrThrow(SourcePersistence.SourceEntity.COLUMN_LANGUAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(SourcePersistence.SourceEntity.COLUMN_NAME));
                String soryby = cursor.getString(cursor.getColumnIndexOrThrow(SourcePersistence.SourceEntity.COLUMN_SORT_BY_AVAILABLE));
                String sourceId = cursor.getString(cursor.getColumnIndexOrThrow(SourcePersistence.SourceEntity.COLUMN_SOURCE_ID));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(SourcePersistence.SourceEntity.COLUMN_URL));
                String logoLarge = cursor.getString(cursor.getColumnIndexOrThrow(SourcePersistence.SourceEntity.COLUMN_URL_TO_LOGOS_LARGE));
                String logoMedium = cursor.getString(cursor.getColumnIndexOrThrow(SourcePersistence.SourceEntity.COLUMN_URL_TO_LOGOS_MEDIUM));
                String logoSmall = cursor.getString(cursor.getColumnIndexOrThrow(SourcePersistence.SourceEntity.COLUMN_URL_TO_LOGOS_SMALL));

                source.setCategory(cat);
                source.setCountry(country);
                source.setDescription(desc);
                source.setLanguage(lang);
                source.setName(name);
                if (!Strings.isNullOrEmpty(soryby)) {
                    String[] sorts = soryby.split(",");
                    source.setSortBysAvailable(Arrays.asList(sorts));
                }
                source.setId(sourceId);
                source.setUrl(url);
                Source.UrlsToLogosEntity logosEntity = new Source.UrlsToLogosEntity();
                logosEntity.setLarge(logoLarge);
                logosEntity.setMedium(logoMedium);
                logosEntity.setSmall(logoSmall);

                source.setUrlsToLogos(logosEntity);
                list.add(source);
                cursor.moveToNext();

            }
        }

        return list;
    }

}
