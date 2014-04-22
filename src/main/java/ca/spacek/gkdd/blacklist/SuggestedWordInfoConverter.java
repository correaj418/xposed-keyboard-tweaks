package ca.spacek.gkdd.blacklist;

import java.util.List;

import ca.spacek.gkdd.blacklist.proxy.SuggestedWordInfo;

/**
 * Created by temp on 21/04/14.
 */
public interface SuggestedWordInfoConverter {
    SuggestedWordInfo convert(Object object);

    List<SuggestedWordInfo> convert(Iterable<?> unknownWordInfoList);
}
