package akuo.represent;

import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by angelakuo on 3/12/16.
 */
public class MyTwitterApiClient extends TwitterApiClient {

    public MyTwitterApiClient(AppSession session) {
        super(session);
    }

    public UserService getUserService() {
        return getService(UserService.class); // getService is defined in TwitterApiClient
    }
}
 interface UserService {
    @GET("/1.1/users/show.json")
    void show(@Query("screen_name") String one, Callback<User> cb);
}