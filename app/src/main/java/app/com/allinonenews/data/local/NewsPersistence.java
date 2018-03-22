package app.com.allinonenews.data.local;

import android.provider.BaseColumns;

/**
 * Created by mukesh on 31/3/17.
 */

public class NewsPersistence implements Persistence{
    private NewsPersistence(){

    }
    public static abstract class NewsEntity implements BaseColumns {
        public static final String TABLE_NAME="news";
        public static final String COLUMN_AUTHOR="author";
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_DESCRIPTION="description";
        public static final String COLUMN_URL="url";
        public static final String COLUMN_URL_TO_IMAGE="urlToImage";
        public static final String COLUMN_PUBLISHED_AT="publishedAt";
        public static final String COLUMN_SOURCE_ID="source";


        public static final String CREATE_TABLE=
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + INTEGER_TYPE + " PRIMARY KEY " +AUTO_INCREMENT+COMMA_SEP +
                        COLUMN_AUTHOR+ TEXT_TYPE + COMMA_SEP +
                        COLUMN_TITLE+ TEXT_TYPE + COMMA_SEP +
                        COLUMN_DESCRIPTION+ TEXT_TYPE + COMMA_SEP +
                        COLUMN_URL+ TEXT_TYPE + COMMA_SEP +
                        COLUMN_URL_TO_IMAGE+ TEXT_TYPE + COMMA_SEP +
                        COLUMN_PUBLISHED_AT+ TEXT_TYPE + COMMA_SEP +
                        COLUMN_SOURCE_ID+ TEXT_TYPE +
                        " )";

    }
}
