package ca.spacek.gkdd.blacklist;

import java.util.List;

/**
 * Created by temp on 21/04/14.
 */
public interface SuggestedWordInfoConverter {
    SuggestedWordInfo convert(Object object);

    List<SuggestedWordInfo> convert(Iterable<?> unknownWordInfoList);
}
