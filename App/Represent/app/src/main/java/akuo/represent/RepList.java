package akuo.represent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.List;
import io.fabric.sdk.android.Fabric;

public class RepList extends AppCompatActivity {

    private static final String TWITTER_KEY = "FTJN0nRXlIFwGJmPndwTapgo7";
    private static final String TWITTER_SECRET = "TqeL9nMAJbBy7CGxrhnaBNbi3YTNBBIkMlNpsmMnvSK7xnpJN6";

    static String screen1 = "";
    static String screen2 = "";
    static String screen3 = "";
    static String screen4 = "";

    boolean four = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_list);

        String zip = "";
        String lat = "";
        String lon = "";

        Intent location = getIntent();
        if (null != location) {
            zip = location.getStringExtra("/zip");
            lat = location.getStringExtra("/lat");
            lon = location.getStringExtra("/lon");
        }

        String urlString;

        if (zip.equals(""))
            urlString = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + lat + "&longitude=" + lon + "&apikey=1042c64bf3da4c1d9f84d30ebac7326b";
        else
            urlString = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=" + zip + "&apikey=1042c64bf3da4c1d9f84d30ebac7326b";

        new ProcessJSON().execute(urlString);
    }

    private class ProcessJSON extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);
            return stream;
        }

        protected void onPostExecute(String stream) {
            if (stream != null) {
                try {
                    JSONObject reader = new JSONObject(stream);
                    int count = reader.getInt("count");
                    if (count == 3) {
                        four = false;
                        findViewById(R.id.rep4).setVisibility(View.GONE);
                    }

                    JSONArray results = reader.getJSONArray("results");
                    JSONObject rep1 = results.getJSONObject(0);
                    JSONObject rep2 = results.getJSONObject(1);
                    JSONObject rep3 = results.getJSONObject(2);
                    JSONObject rep4 = results.getJSONObject(2);
                    if (four)
                        rep4 = results.getJSONObject(3);

                    String repName1 = rep1.getString("title") + " " + rep1.getString("first_name") + " " + rep1.getString("last_name");
                    String repName2 = rep2.getString("title") + " " + rep2.getString("first_name") + " " + rep2.getString("last_name");
                    String repName3 = rep3.getString("title") + " " + rep3.getString("first_name") + " " + rep3.getString("last_name");

                    final String repID1 = rep1.getString("bioguide_id");
                    final String repID2 = rep2.getString("bioguide_id");
                    final String repID3 = rep3.getString("bioguide_id");
                    final String repID4 = rep4.getString("bioguide_id");

                    TextView rep1_view_name = (TextView) findViewById(R.id.rep_name1);
                    TextView rep2_view_name = (TextView) findViewById(R.id.rep_name2);
                    TextView rep3_view_name = (TextView) findViewById(R.id.rep_name3);

                    TextView rep_email1 = (TextView) findViewById(R.id.rep_email1);
                    TextView rep_email2 = (TextView) findViewById(R.id.rep_email2);
                    TextView rep_email3 = (TextView) findViewById(R.id.rep_email3);

                    TextView rep_website1 = (TextView) findViewById(R.id.rep_web1);
                    TextView rep_website2 = (TextView) findViewById(R.id.rep_web2);
                    TextView rep_website3 = (TextView) findViewById(R.id.rep_web3);

                    rep1_view_name.setText(repName1);
                    rep2_view_name.setText(repName2);
                    rep3_view_name.setText(repName3);

                    String email1 = rep1.getString("oc_email");
                    String email2 = rep2.getString("oc_email");
                    String email3 = rep3.getString("oc_email");

                    rep_email1.setText(email1);
                    rep_email2.setText(email2);
                    rep_email3.setText(email3);
                    
                    String website1 = rep1.getString("website");
                    String website2 = rep2.getString("website");
                    String website3 = rep3.getString("website");

                    rep_website1.setText(website1);
                    rep_website2.setText(website2);
                    rep_website3.setText(website3);

                    screen1 = rep1.getString("twitter_id");
                    screen2 = rep2.getString("twitter_id");
                    screen3 = rep3.getString("twitter_id");

                    if (four) {
                        screen4 = rep4.getString("twitter_id");
                        String repName4 = rep4.getString("title") + " " + rep4.getString("first_name") + " " + rep4.getString("last_name");
                        TextView rep4_view_name = (TextView) findViewById(R.id.rep_name4);
                        rep4_view_name.setText(repName4);
                        TextView rep_email4 = (TextView) findViewById(R.id.rep_email4);
                        TextView rep_website4 = (TextView) findViewById(R.id.rep_web4);
                        rep_email4.setText(rep4.getString("oc_email"));
                        rep_website4.setText(rep4.getString("website"));
                    }

                    TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
                    Fabric.with(RepList.this, new com.twitter.sdk.android.Twitter(authConfig));

                    TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
                        @Override
                        public void success(Result<AppSession> appSessionResult) {
                            AppSession session = appSessionResult.data;
                            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                            StatusesService service = twitterApiClient.getStatusesService();

                            Log.d("/twitter", "screen1: " + screen1);
                            service.userTimeline(null, screen1, 1, null, null, null, null, null, null, new Callback<List<Tweet>>() {
                                @Override
                                public void success(Result<List<Tweet>> result) {
                                    List<Tweet> tweets = result.data;
                                    Tweet firstTweet = tweets.get(0);
                                    TextView tweeter = (TextView) findViewById(R.id.rep_tweet1);
                                    tweeter.setText(firstTweet.text);
                                }

                                @Override
                                public void failure(TwitterException e) {
                                    Log.d("/twitter", "Load Tweet failure", e);
                                }
                            });

                            twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                            service = twitterApiClient.getStatusesService();

                            service.userTimeline(null, screen2, 1, null, null, null, null, null, null, new Callback<List<Tweet>>() {
                                @Override
                                public void success(Result<List<Tweet>> result) {
                                    List<Tweet> tweets = result.data;
                                    Tweet firstTweet = tweets.get(0);
                                    TextView tweeter = (TextView) findViewById(R.id.rep_tweet2);
                                    tweeter.setText(firstTweet.text);
                                }

                                @Override
                                public void failure(TwitterException e) {
                                    Log.d("/twitter", "Load Tweet failure", e);
                                }
                            });

                            twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                            service = twitterApiClient.getStatusesService();

                            service.userTimeline(null, screen3, 1, null, null, null, null, null, null, new Callback<List<Tweet>>() {
                                @Override
                                public void success(Result<List<Tweet>> result) {
                                    List<Tweet> tweets = result.data;
                                    Tweet firstTweet = tweets.get(0);
                                    TextView tweeter = (TextView) findViewById(R.id.rep_tweet3);
                                    tweeter.setText(firstTweet.text);
                                }

                                @Override
                                public void failure(TwitterException e) {
                                    Log.d("/twitter", "Load Tweet failure", e);
                                }
                            });

                            if (four) {
                                service.userTimeline(null, screen4, 1, null, null, null, null, null, null, new Callback<List<Tweet>>() {
                                    @Override
                                    public void success(Result<List<Tweet>> result) {
                                        List<Tweet> tweets = result.data;
                                        Tweet firstTweet = tweets.get(0);
                                        TextView tweeter = (TextView) findViewById(R.id.rep_tweet4);
                                        tweeter.setText(firstTweet.text);
                                    }

                                    @Override
                                    public void failure(TwitterException e) {
                                        Log.d("/twitter", "Load Tweet failure", e);
                                    }
                                });
                            }

                            final String[] profileURLs = new String[4];
                            final String[] bgURLs = new String[4];

                            TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
                                @Override
                                public void success(Result<AppSession> appSessionResult) {
                                    AppSession session = appSessionResult.data;
                                    MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(session);
                                    UserService userService = myTwitterApiClient.getUserService();

                                    userService.show(screen1, new Callback<User>() {
                                        @Override
                                        public void success(Result<User> result) {
                                            User usr = result.data;
                                            BasicImageDownloader imageDownloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                                                @Override
                                                public void onError(BasicImageDownloader.ImageError error) {
                                                    Log.d("/twitter", "Failed to load profile pic", error);
                                                }

                                                @Override
                                                public void onProgressChange(int percent) {}

                                                @Override
                                                public void onComplete(Bitmap result) {
                                                    ImageView profile1 = (ImageView) findViewById(R.id.rep_img1);
                                                    profile1.setImageBitmap(result);
                                                }
                                            });
                                            String url = removeNormal(usr.profileImageUrlHttps);
                                            profileURLs[0] = url;
                                            bgURLs[0] = usr.profileBackgroundImageUrlHttps;
                                            imageDownloader.download(url, false);
                                        }

                                        public void failure(TwitterException exception) {
                                            Log.d("/twitter", "Load Tweet failure", exception);

                                        }
                                    });

                                    userService.show(screen2, new Callback<User>() {
                                        @Override
                                        public void success(Result<User> result) {
                                            User usr = result.data;
                                            BasicImageDownloader imageDownloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                                                @Override
                                                public void onError(BasicImageDownloader.ImageError error) {
                                                    Log.d("/twitter", "Failed to load profile pic", error);
                                                }

                                                @Override
                                                public void onProgressChange(int percent) {}

                                                @Override
                                                public void onComplete(Bitmap result) {
                                                    ImageView profile1 = (ImageView) findViewById(R.id.rep_img2);
                                                    profile1.setImageBitmap(result);
                                                }
                                            });
                                            String url = removeNormal(usr.profileImageUrlHttps);
                                            profileURLs[1] = url;
                                            bgURLs[1] = usr.profileBackgroundImageUrlHttps;
                                            imageDownloader.download(url, false);
                                        }

                                        public void failure(TwitterException exception) {
                                            Log.d("/twitter", "Load Tweet failure", exception);

                                        }
                                    });

                                    userService.show(screen3, new Callback<User>() {
                                        @Override
                                        public void success(Result<User> result) {
                                            User usr = result.data;
                                            BasicImageDownloader imageDownloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                                                @Override
                                                public void onError(BasicImageDownloader.ImageError error) {
                                                    Log.d("/twitter", "Failed to load profile pic", error);
                                                }

                                                @Override
                                                public void onProgressChange(int percent) {}

                                                @Override
                                                public void onComplete(Bitmap result) {
                                                    ImageView profile1 = (ImageView) findViewById(R.id.rep_img3);
                                                    profile1.setImageBitmap(result);
                                                }
                                            });
                                            String url = removeNormal(usr.profileImageUrlHttps);
                                            profileURLs[2] = url;
                                            bgURLs[2] = usr.profileBackgroundImageUrlHttps;
                                            imageDownloader.download(url, false);
                                        }

                                        public void failure(TwitterException exception) {
                                            Log.d("/twitter", "Load Tweet failure", exception);

                                        }
                                    });

                                    Button repButton1 = (Button) findViewById(R.id.rep_deets1);
                                    repButton1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(RepList.this, RepDetail.class);
                                            intent.putExtra("/bioID", repID1);
                                            intent.putExtra("/profURL", profileURLs[0]);
                                            intent.putExtra("/bgURL", bgURLs[0]);
                                            startActivity(intent);
                                        }
                                    });

                                    Button repButton2 = (Button) findViewById(R.id.rep_deets2);
                                    repButton2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(RepList.this, RepDetail.class);
                                            intent.putExtra("/bioID", repID2);
                                            intent.putExtra("/profURL", profileURLs[1]);
                                            intent.putExtra("/bgURL", bgURLs[1]);
                                            startActivity(intent);
                                        }
                                    });

                                    Button repButton3 = (Button) findViewById(R.id.rep_deets3);
                                    repButton3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(RepList.this, RepDetail.class);
                                            intent.putExtra("/bioID", repID3);
                                            intent.putExtra("/profURL", profileURLs[2]);
                                            intent.putExtra("/bgURL", bgURLs[2]);
                                            startActivity(intent);
                                        }
                                    });

                                    if (four) {
                                        userService.show(screen4, new Callback<User>() {
                                            @Override
                                            public void success(Result<User> result) {
                                                User usr = result.data;
                                                BasicImageDownloader imageDownloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                                                    @Override
                                                    public void onError(BasicImageDownloader.ImageError error) {
                                                        Log.d("/twitter", "Failed to load profile pic", error);
                                                    }

                                                    @Override
                                                    public void onProgressChange(int percent) {}

                                                    @Override
                                                    public void onComplete(Bitmap result) {
                                                        ImageView profile1 = (ImageView) findViewById(R.id.rep_img4);
                                                        profile1.setImageBitmap(result);
                                                    }
                                                });
                                                String url = removeNormal(usr.profileImageUrlHttps);
                                                profileURLs[3] = url;
                                                bgURLs[3] = usr.profileBackgroundImageUrlHttps;
                                                imageDownloader.download(url, false);
                                            }

                                            public void failure(TwitterException exception) {
                                                Log.d("/twitter", "Load Tweet failure", exception);
                                            }
                                        });

                                        Button repButton4 = (Button) findViewById(R.id.rep_deets4);
                                        repButton4.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RepList.this, RepDetail.class);
                                                intent.putExtra("/bioID", repID4);
                                                intent.putExtra("/profURL", profileURLs[3]);
                                                intent.putExtra("/bgURL", bgURLs[3]);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void failure(TwitterException e) {
                                    Log.d("/twitter", "Load Tweet failure", e);
                                }
                            });
                        }

                        @Override
                        public void failure(TwitterException e) {
                            Log.d("/twitter", "Load Tweet failure", e);
                            e.printStackTrace();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("/getJSON", "stream null");
            }
        }
    }

    private String removeNormal(String url) {
        int _index = url.length()-1;
        int pIndex = url.length()-1;
        while (url.charAt(_index) != '_') {
            if (url.charAt(pIndex) != '.')
                pIndex--;
            _index--;
        }
        return url.substring(0,_index) + url.substring(pIndex);
    }
}
