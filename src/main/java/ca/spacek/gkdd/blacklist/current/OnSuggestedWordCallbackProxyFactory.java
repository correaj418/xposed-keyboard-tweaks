package ca.spacek.gkdd.blacklist.current;

import com.google.common.reflect.Reflection;

import ca.spacek.gkdd.blacklist.SuggestionBlackLister;

/**
 * Created by temp on 22/04/14.
 */
public class OnSuggestedWordCallbackProxyFactory {
    private final SuggestionBlackLister suggestionBlackLister;
    private final PackageReflection packageReflection;

    public OnSuggestedWordCallbackProxyFactory(SuggestionBlackLister suggestionBlackLister, PackageReflection packageReflection) {
        this.suggestionBlackLister = suggestionBlackLister;
        this.packageReflection = packageReflection;
    }

    public Object createProxy(Object original) {
        return Reflection.newProxy(packageReflection.getOnGetSuggestedWordsCallbackClass(),
                new OnSuggestedWordCallbackHandler(original, suggestionBlackLister, packageReflection));
    }
}
