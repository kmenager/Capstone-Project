package io.github.kmenager.getmesomefeed.data.remote;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.github.kmenager.getmesomefeed.data.local.model.FavoriteSubscription;
import io.github.kmenager.getmesomefeed.data.local.model.QueryFeed;
import io.github.kmenager.getmesomefeed.data.local.model.Stream;
import io.github.kmenager.getmesomefeed.data.local.model.Subscription;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;


public interface FeedService {
    String ENDPOINT = "http://cloud.feedly.com/";
    int ITEM_BY_PAGE = 20;
    String AUTHORIZATION = "Authorization";

    @GET("v3/streams/contents")
    Observable<Stream> getStream(@Query("streamId") String streamId);

    @GET("v3/search/feeds")
    Observable<QueryFeed> searchFeed(@Query("q") String term, @QueryMap Map<String, String> options);

    @GET("v3/subscriptions")
    Observable<List<Subscription>> getSubscriptions(@Header(AUTHORIZATION) String auth);

    @POST("v3/subscriptions")
    Observable<retrofit2.Response<ResponseBody>>
    subscribeFeed(@Header(AUTHORIZATION) String auth,
                     @Body FavoriteSubscription favoriteSubscription);

    @HTTP(method = "DELETE", path = "v3/subscriptions/.mdelete", hasBody = true)
    Observable<retrofit2.Response<ResponseBody>>
    unSubscribeFeed(@Header(AUTHORIZATION) String auth,
                    @Body List<String> feedIds);

    class Factory {
        public static FeedService makeFeedService() {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request().newBuilder()
                                    .addHeader("Content-Type", "application/json")
                                    .addHeader("Accept", "application/json")
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(FeedService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            return retrofit.create(FeedService.class);
        }
    }

    class Util {
        // Build API authorization string from a given access token.
        public static String buildAuthorization() {
            return "OAuth AwiDgqnAXRLNO64W_wnGwmU3jr038e5Jh8liGB6O_geez6hngAIRmt37X3Iwl2XHjMSWHmtIsXREoKpXX0k4k8Hf0E7utyfZRoRkGeJBNb6hQvZ3tcmGfMCoMvjg7V9QRzIvYldoCaWoUIJlGNloy3ZZEXBHru88NmUhq45zaSagms0kXPqFmL0zp59y5guDb2RV-RdTRbIFqtEIY8KGCl9gYTl_rL0:feedlydev";
        }

        // Get options for QueryFeed request
        public static Map<String, String> getConstantForQuery() {
            Map<String, String> map = new HashMap<>();
            map.put("n", String.valueOf(ITEM_BY_PAGE));
            String locale = Locale.getDefault().getLanguage();
            if (locale != null && !locale.isEmpty()) {
                map.put("locale", locale);
            }
//            map.put("fullTerm", "true");

            return map;
        }
    }

}
