package ca.spacek.gkdd.blacklist.gk;

import com.google.common.base.Function;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by temp on 22/04/14.
 */
public class SuggestedWordInfoTransform implements Function<Object, String> {
    @Override
    public String apply(Object input) {
        return (String) XposedHelpers.getObjectField(input, "mWord");
    }
}
