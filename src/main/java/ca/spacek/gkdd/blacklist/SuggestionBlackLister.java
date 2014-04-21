package ca.spacek.gkdd.blacklist;

import java.util.Iterator;
import java.util.List;

import ca.spacek.gkdd.BlackList;
import ca.spacek.gkdd.blacklist.proxy.SuggestedWordInfo;

/**
 * Created by temp on 20/04/14.
 */
public class SuggestionBlackLister {
    private BlackList blackList;

    public void filterResults(List<SuggestedWordInfo> original) {
        for (Iterator<SuggestedWordInfo> iterator = original.iterator(); iterator.hasNext(); ) {
            SuggestedWordInfo next = iterator.next();
            if (blackList.contains(next.getWord())) {
                iterator.remove();
            }
        }
    }
}
