package akuo.represent;

/**
 * Created by angelakuo on 3/1/16.
 * https://gist.github.com/gabrielemariotti/
 */
public class SimplePage {

    public String mTitle;
    public String mText;
    public int mIconId;
    public int mBackgroundId;

    public SimplePage(String title, String text, int iconId, int backgroundId) {
        this.mTitle = title;
        this.mText = text;
        this.mIconId = iconId;
        this.mBackgroundId = backgroundId;
    }
}
