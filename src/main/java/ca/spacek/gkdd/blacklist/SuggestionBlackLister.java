package ca.spacek.gkdd.blacklist;

import java.util.Iterator;
import java.util.List;

import ca.spacek.gkdd.BlackList;

/**
 * Created by temp on 20/04/14.
 */
public class SuggestionBlackLister {
    private final BlackList blackList;

    public SuggestionBlackLister(BlackList blackList) {
        this.blackList = blackList;
    }

    public void filterResults(List<SuggestedWordInfo> wordInfos) {
        for (Iterator<SuggestedWordInfo> iterator = wordInfos.iterator(); iterator.hasNext(); ) {
            SuggestedWordInfo next = iterator.next();
            if (blackList.contains(next.getWord())) {
                iterator.remove();
            }
        }
    }
}
