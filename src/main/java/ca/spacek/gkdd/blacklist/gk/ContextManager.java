package ca.spacek.gkdd.blacklist.gk;

import android.content.Context;

import com.google.common.base.Optional;

import ca.spacek.gkdd.CachedBlackListDictionaryWordContentObserver;
import ca.spacek.gkdd.contentprovider.DictionaryWordContentProvider;

/**
 * Created by temp on 22/04/14.
 */
public class ContextManager {
    private Optional<Context> currentContext = Optional.absent();
    private CachedBlackListDictionaryWordContentObserver contentObserver;

    public ContextManager(CachedBlackListDictionaryWordContentObserver contentObserver) {
        this.contentObserver = contentObserver;
    }

    public Optional<Context> get() {
        return currentContext;
    }

    public void update(Context context) {
        if (currentContext.isPresent()) {
            unregisterContentObserver(currentContext.get());
        }
        this.currentContext = Optional.of(context);
        registerContentObserver(context);
    }

    private void registerContentObserver(Context context) {
        context.getContentResolver().registerContentObserver(DictionaryWordContentProvider.CONTENT_URI, false, contentObserver);
    }

    private void unregisterContentObserver(Context context) {
        context.getContentResolver().unregisterContentObserver(contentObserver);
    }

    public void clear() {
        currentContext = Optional.absent();
    }
}
