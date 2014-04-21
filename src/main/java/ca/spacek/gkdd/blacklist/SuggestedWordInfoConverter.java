package ca.spacek.gkdd.blacklist;

import ca.spacek.gkdd.blacklist.proxy.SuggestedWordInfo;

/**
 * Created by temp on 21/04/14.
 */
public interface SuggestedWordInfoConverter {
    boolean supports(Object object);

    SuggestedWordInfo convert(Object object);
}
