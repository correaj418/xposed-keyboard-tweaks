package ca.spacek.gkdd.blacklist;

/**
 * Created by temp on 20/04/14.
 */
public class SuggestedWordInfo {
    private final String word;

    public SuggestedWordInfo(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return word;
    }
}
