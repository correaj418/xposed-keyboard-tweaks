package ca.spacek.gkdd.blacklist;

import com.google.common.collect.Lists;

import java.util.List;

import ca.spacek.gkdd.blacklist.current.PackageReflection;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by temp on 21/04/14.
 */
public class DefaultSuggestedWordInfoConverter implements SuggestedWordInfoConverter {
    @Override
    public SuggestedWordInfo convert(Object object) {
        return new SuggestedWordInfo((String) XposedHelpers.getObjectField(object, PackageReflection.FIELD_WORD));
    }

    @Override
    public List<SuggestedWordInfo> convert(Iterable<?> unknownWordInfoList) {
        List<SuggestedWordInfo> wordInfoList = Lists.newArrayList();
        for (Object unknownWordInfo : unknownWordInfoList) {
            wordInfoList.add(convert(unknownWordInfo));
        }
        return wordInfoList;
    }
}
