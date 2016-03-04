package akuo.represent;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.app.FragmentManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by angelakuo on 3/1/16.
 * https://gist.github.com/gabrielemariotti/
 */
public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ArrayList<SimpleRow> mPages;

    public SampleGridPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        initPages();
    }

    private void initPages() {
        Random rand = new Random();

        int obama = rand.nextInt(60) + 30;
        int romny = 100 - obama;

        mPages = new ArrayList<>();

        SimpleRow row1 = new SimpleRow();
        row1.addPages(new SimplePage("CrazyCounty, CA", "Obama "+obama+"%\nRomny "+romny+"%", 0, R.drawable.patriotism));
        row1.addPages(new SimplePage("", "", 0, R.drawable.example_rep1));

        SimpleRow row2 = new SimpleRow();
        row2.addPages(new SimplePage("", "Barbara Lee", 0, R.drawable.example_rep1));
        row2.addPages(new SimplePage("", "", 0, R.drawable.example_rep1));

        SimpleRow row3 = new SimpleRow();
        row3.addPages(new SimplePage("", "Dianne Feinstein", 0, R.drawable.example_rep2));
        row3.addPages(new SimplePage("", "", 0, R.drawable.example_rep2));

        SimpleRow row4 = new SimpleRow();
        row4.addPages(new SimplePage("", "Barbara Boxer", 0, R.drawable.example_rep3));
        row4.addPages(new SimplePage("", "", 0, R.drawable.example_rep3));

        mPages.add(row1);
        mPages.add(row2);
        mPages.add(row3);
        mPages.add(row4);
    }

    @Override
    public Fragment getFragment(int row, int col) {
        SimplePage page = (mPages.get(row)).getPages(col);
        CardFragment fragment = CardFragment.create(page.mTitle, page.mText);

        if (col == 1) {
            return RepViewPhone.newInstance();
        }

        if (row == 0) {
            fragment.setContentPadding(10, 35, 10, 75);
        }
        fragment.setExpansionEnabled(true);
        return fragment;
    }

    @Override
    public Drawable getBackgroundForPage(int row, int col) {
        SimplePage page = (mPages.get(row)).getPages(col);
        return mContext.getResources().getDrawable(page.mBackgroundId, mContext.getTheme());
    }

    @Override
    public int getRowCount() {
        return mPages.size();
    }

    @Override
    public int getColumnCount(int row) {
        return mPages.get(row).size();
    }
}