package akuo.represent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class InputLocation extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "FTJN0nRXlIFwGJmPndwTapgo7";
    private static final String TWITTER_SECRET = "TqeL9nMAJbBy7CGxrhnaBNbi3YTNBBIkMlNpsmMnvSK7xnpJN6";

    private GoogleApiClient mGoogleApiClient;
    private String mLatitudeText = "";
    private String mLongitudeText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_location);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                        .addApi(Wearable.API)
                    .build();
        }

        Button btn = (Button)findViewById(R.id.search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(InputLocation.this, PhoneToWatchService.class));
                Intent intent = new Intent(InputLocation.this, RepList.class);
                CheckBox local = (CheckBox) findViewById(R.id.use_current);
                if (local.isChecked()) {
                    Log.d("/buttsbuttsbuttsbutts","checkedBox");
                    intent.putExtra("/zip", "");
                    intent.putExtra("/lat", mLatitudeText);
                    intent.putExtra("/lon", mLongitudeText);
                } else {
                    Log.d("/assassassassassass","UNcheckedBox");
                    EditText zip = (EditText) findViewById(R.id.zip_code);
                    intent.putExtra("/zip", zip.getText().toString());
                    intent.putExtra("/lat", "");
                    intent.putExtra("/lon", "");
                }
                startActivity(intent);
            }
        });

    }

    public void onCheckBoxClicked(View view) {
        // Is the button now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.use_current:
                if (checked) {
                    EditText zip = (EditText) findViewById(R.id.zip_code);
                    zip.setText("");
                    break;
                }
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(InputLocation.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION )
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(InputLocation.this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
        } else {
            Log.d("/location", "location error");
        }
    }
    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}


}