package ca.spacek.gkdd.blacklist.current;

import com.google.common.base.Function;

import ca.spacek.gkdd.blacklist.SuggestedWordInfo;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by temp on 22/04/14.
 */
public class SuggestedWordInfoTransform implements Function<Object, SuggestedWordInfo> {
    @Override
    public SuggestedWordInfo apply(Object input) {
        return new SuggestedWordInfo((String) XposedHelpers.getObjectField(input, "mWord"));
    }
}
