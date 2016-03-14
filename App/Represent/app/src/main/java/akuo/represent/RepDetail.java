package akuo.represent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;

public class RepDetail extends AppCompatActivity {

    String id = "";
    String profileURL = "";
    String bgURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_detail);

        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            id = intent.getStringExtra("/bioID");
            profileURL = intent.getStringExtra("/profURL");
            bgURL = intent.getStringExtra("/bgURL");
        }

        BasicImageDownloader imageDownloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
            @Override
            public void onError(BasicImageDownloader.ImageError error) {
                Log.d("/twitter", "Failed to load profile pic", error);
            }

            @Override
            public void onProgressChange(int percent) {}

            @Override
            public void onComplete(Bitmap result) {
                ImageView profile = (ImageView) findViewById(R.id.profile_pic);
                profile.setImageBitmap(ImageHelper.getRoundedCornerBitmap(result, 30));
            }
        });

        imageDownloader.download(profileURL, false);

        String legiString = "http://congress.api.sunlightfoundation.com/legislators?bioguide_id=" + id + "&apikey=1042c64bf3da4c1d9f84d30ebac7326b";
        String commString = "http://congress.api.sunlightfoundation.com/committees?member_ids=" + id + "&apikey=1042c64bf3da4c1d9f84d30ebac7326b";
        String billString = "http://congress.api.sunlightfoundation.com/bills?sponsor_id=" + id + "&apikey=1042c64bf3da4c1d9f84d30ebac7326b";

        new ProcessLegiJSON().execute(legiString);
        new ProcessCommJSON().execute(commString);
        new ProcessBillJSON().execute(billString);
    }

    private class ProcessLegiJSON extends AsyncTask<String, Void, String> {
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
                    JSONArray array = reader.getJSONArray("results");
                    JSONObject results = array.getJSONObject(0);
                    String party = results.getString("party");
                    String term = results.getString("term_end");
                    TextView partyView = (TextView) findViewById(R.id.rep_deets_party);
                    TextView termView = (TextView) findViewById(R.id.rep_deets_term);
                    TextView nameView = (TextView) findViewById(R.id.rep_deets_name);
                    String name =  results.getString("title")+" "+results.getString("first_name")+" "+results.getString("last_name");
                    nameView.setText(name);
                    partyView.setText(party);
                    termView.setText(term);
                } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("/getJSON", "stream null");
        }
        }
    }

    private class ProcessCommJSON extends AsyncTask<String, Void, String> {
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
                    JSONArray array = reader.getJSONArray("results");
                    String committees = "";

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject comm = array.getJSONObject(i);
                        committees = committees + comm.getString("name") + "\n" + "\n";
                    }

                    TextView committeeList = (TextView) findViewById(R.id.rep_deets_committee);
                    committeeList.setText(committees);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("/getJSON", "stream null");
            }
        }
    }

    private class ProcessBillJSON extends AsyncTask<String, Void, String> {
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
                    JSONArray array = reader.getJSONArray("results");
                    String bills = "";

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject comm = array.getJSONObject(i);
                        if (!comm.getString("short_title").equals("null"))
                            bills = bills + comm.getString("short_title") + "\n" + "\n";
                    }

                    TextView billList = (TextView) findViewById(R.id.rep_deets_bills);
                    billList.setText(bills);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("/getJSON", "stream null");
            }
        }
    }
}