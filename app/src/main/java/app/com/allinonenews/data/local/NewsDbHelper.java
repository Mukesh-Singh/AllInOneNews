package app.com.allinonenews.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mukesh on 31/3/17.
 */

public class NewsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="AllInOneNews.db";
    private static final int DATABASE_VERSION=1;



    public NewsDbHelper (Context context){
        this(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    public NewsDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SourcePersistence.SourceEntity.CREATE_TABLE);
        db.execSQL(NewsPersistence.NewsEntity.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
