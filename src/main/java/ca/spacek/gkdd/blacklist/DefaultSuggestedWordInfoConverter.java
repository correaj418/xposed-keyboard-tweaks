package ca.spacek.gkdd.blacklist;

import ca.spacek.gkdd.blacklist.proxy.SuggestedWordInfo;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by temp on 21/04/14.
 */
public class DefaultSuggestedWordInfoConverter implements SuggestedWordInfoConverter {
    public static final String ANDROID_SUGGESTED_WORD_INFO_CLASS = "com.android.inputmethod.latin.SuggestedWords$SuggestedWordInfo";
    public static final String FIELD_WORD = "mWord";

    @Override
    public boolean supports(Object object) {
        String className = object.getClass().getName();
        XposedBridge.log("Seeing if we can convert " + className);

        return className.equals(ANDROID_SUGGESTED_WORD_INFO_CLASS)
                && XposedHelpers.findField(object.getClass(), FIELD_WORD) != null;
    }

    @Override
    public SuggestedWordInfo convert(Object object) {
        return new SuggestedWordInfo((String) XposedHelpers.getObjectField(object, FIELD_WORD));
    }
}
