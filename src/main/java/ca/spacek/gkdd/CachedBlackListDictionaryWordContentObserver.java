package ca.spacek.gkdd;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import ca.spacek.gkdd.contentprovider.DictionaryWordContentProvider;

public class CachedBlackListDictionaryWordContentObserver extends
        ContentObserver {
    private final CachedBlackList cachedBlackList;

    public CachedBlackListDictionaryWordContentObserver(Handler handler, CachedBlackList cachedBlackList) {
        super(handler);
        this.cachedBlackList = cachedBlackList;
    }

    @Override
    public void onChange(boolean selfChange) {
        cachedBlackList.flagDirty();
    }
}
