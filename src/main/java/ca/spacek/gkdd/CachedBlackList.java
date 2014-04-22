package ca.spacek.gkdd;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import android.database.Cursor;
import android.util.Log;

import ca.spacek.gkdd.blacklist.current.ContextAccessor;
import ca.spacek.gkdd.contentprovider.DictionaryWordContentProvider;
import ca.spacek.gkdd.data.DictionaryWordTable;
import de.robv.android.xposed.XposedBridge;

public class CachedBlackList implements BlackList {
	private final ContextAccessor contextAccessor;
	private Set<String> words = new TreeSet<String>(CASE_INSENSITVE);
	private boolean dirty = true;

	public CachedBlackList(ContextAccessor contextAccessor) {
		this.contextAccessor = contextAccessor;
	}

	@Override
	public boolean contains(String word) {
		if (dirty) {
            Log.d("blacklist", "Was dirty, refreshing");
            refresh();
		}

        Log.d("blacklist", "Checking if blacklist contains " + word);
		return words.contains(word);
	}

	private void refresh() {
		dirty = false;
		words.clear();

        if (!contextAccessor.get().isPresent()) {
            return;
        }

        Cursor cursor = contextAccessor.get().get().getContentResolver().query(
				DictionaryWordContentProvider.CONTENT_URI,
				new String[] { DictionaryWordTable.COLUMN_ID, DictionaryWordTable.COLUMN_WORD }, null, null, null);

		if (!cursor.moveToFirst()) {
			return;
		}

		do {
			String word = cursor.getString(1);
			Log.d("blacklist", "Adding word: " + word);
			words.add(word);
		} while (cursor.moveToNext());

        Log.i("blacklist", "Using blacklist of " + words.size() + " words");
	}

	private static Comparator<String> CASE_INSENSITVE = new Comparator<String>() {
		@Override
		public int compare(String lhs, String rhs) {
			return lhs.compareToIgnoreCase(rhs);
		}
	};

	public void flagDirty() {
		dirty = true;
	}
}
