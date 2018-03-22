package app.com.allinonenews.data.local;

import android.provider.BaseColumns;

/**
 * Created by mukesh on 31/3/17.
 */

public class SourcePersistence implements Persistence{
    private SourcePersistence (){

    }

    public static abstract class SourceEntity implements BaseColumns{
        public static final String TABLE_NAME="source";
        public static final String COLUMN_SOURCE_ID="id";
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_DESCRIPTION="description";
        public static final String COLUMN_URL="url";
        public static final String COLUMN_CATEGORY="category";
        public static final String COLUMN_LANGUAGE="language";
        public static final String COLUMN_COUNTRY="country";
        public static final String COLUMN_URL_TO_LOGOS_SMALL="url_logo_small";
        public static final String COLUMN_URL_TO_LOGOS_MEDIUM="url_logo_medium";
        public static final String COLUMN_URL_TO_LOGOS_LARGE="url_logo_large";
        public static final String COLUMN_SORT_BY_AVAILABLE="sort_by_available";

        public static final String CREATE_TABLE=
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + INTEGER_TYPE + " PRIMARY KEY " +AUTO_INCREMENT+COMMA_SEP +
                        COLUMN_SOURCE_ID+ TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME+ TEXT_TYPE + COMMA_SEP +
                        COLUMN_DESCRIPTION+ TEXT_TYPE + COMMA_SEP +
                        COLUMN_URL+ TEXT_TYPE + COMMA_SEP +
                        COLUMN_CATEGORY+ TEXT_TYPE + COMMA_SEP +
                        COLUMN_LANGUAGE+ TEXT_TYPE +COMMA_SEP +
                        COLUMN_COUNTRY+ TEXT_TYPE +COMMA_SEP +
                        COLUMN_URL_TO_LOGOS_SMALL+ TEXT_TYPE +COMMA_SEP +
                        COLUMN_URL_TO_LOGOS_MEDIUM+ TEXT_TYPE +COMMA_SEP +
                        COLUMN_URL_TO_LOGOS_LARGE+ TEXT_TYPE +COMMA_SEP +
                        COLUMN_SORT_BY_AVAILABLE+ TEXT_TYPE +

                        " )";

    }
}
