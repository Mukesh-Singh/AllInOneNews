package app.com.allinonenews.data.remote.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static app.com.allinonenews.data.remote.api.ApiMethods.API_URL;


/**
 * Created by mukesh on 27/3/17.
 */

public class RestHelper {
    private static ApiMethods apiMethods;

    public static ApiMethods getInstance() {
        if (apiMethods==null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            // add your other interceptors …

            // add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            apiMethods=retrofit.create(ApiMethods.class);
        }
        return apiMethods;

    }
}
