package ca.spacek.gkdd;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.database.Cursor;
import ca.spacek.gkdd.contentprovider.DictionaryWordContentProvider;
import de.robv.android.xposed.XposedBridge;

public class CachedBlackList implements BlackList {
	private final Context context;
	private Set<String> words = new TreeSet<String>(CASE_INSENSITVE);
	private boolean dirty = true;

	public CachedBlackList(Context context) {
		this.context = context;
	}

	@Override
	public boolean containsWord(String word) {
		if (dirty) {
			refresh();
		}

		return words.contains(word);
	}

	private void refresh() {
		dirty = false;
		words.clear();

		Cursor cursor = context.getContentResolver().query(
				DictionaryWordContentProvider.CONTENT_URI,
				new String[] { "_id", "word" }, null, null, null);

		if (!cursor.moveToFirst()) {
			return;
		}

		do {
			String word = cursor.getString(1);
			XposedBridge.log("Adding word: " + word);
			words.add(word);
		} while (cursor.moveToNext());
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
