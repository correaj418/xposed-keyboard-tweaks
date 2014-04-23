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

    public void filterResults(List<String> words) {
        for (Iterator<String> iterator = words.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            if (blackList.contains(next)) {
                iterator.remove();
            }
        }
    }
}
