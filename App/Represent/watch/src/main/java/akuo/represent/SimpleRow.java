package akuo.represent;

import java.util.ArrayList;

/**
 * Created by angelakuo on 3/1/16.
 * https://gist.github.com/gabrielemariotti/
 */
public class SimpleRow {

    ArrayList<SimplePage> mPagesRow = new ArrayList<>();

    public void addPages(SimplePage page) {
        mPagesRow.add(page);
    }

    public SimplePage getPages(int index) {
        return mPagesRow.get(index);
    }

    public int size(){
        return mPagesRow.size();
    }
}
