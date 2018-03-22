package app.com.allinonenews.data;

import android.content.Context;

import app.com.allinonenews.data.repository.NewsRepository;
import app.com.allinonenews.data.repository.SourceRepository;

/**
 * Created by mukesh on 31/3/17.
 */

public class RepositoryFactory {

    public static NewsRepository getNewsRepository(Context context){
        return new NewsRepository(context) ;
    }
    public static SourceRepository getSourceRepository(Context context){
        return new SourceRepository(context);
    }
}
