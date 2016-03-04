package akuo.represent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.widget.Toast;

public class Main extends Activity implements RepViewPhone.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridViewPager mGridPager = (GridViewPager) findViewById(R.id.pager);
        mGridPager.setAdapter(new SampleGridPagerAdapter(this, getFragmentManager()));
    }

    @Override
    public void onContentFragmentInteraction(Uri uri) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }
}