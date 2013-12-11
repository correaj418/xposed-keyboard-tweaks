package ca.spacek.gkdd;

import ca.spacek.gkdd.contentprovider.DictionaryWordContentProvider;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public class CachedBlackListDictionaryWordContentObserver extends
		ContentObserver {
	private final CachedBlackList cachedBlackList;

	public CachedBlackListDictionaryWordContentObserver(Handler handler,
			CachedBlackList cachedBlackList) {
		super(handler);
		this.cachedBlackList = cachedBlackList;
	}

	@Override
	public void onChange(boolean selfChange) {
		onChange(selfChange, null);
	}

	@Override
	public void onChange(boolean selfChange, Uri uri) {
		if (!uri.equals(DictionaryWordContentProvider.CONTENT_URI)) {
			return;
		}
		cachedBlackList.flagDirty();
	}
}
