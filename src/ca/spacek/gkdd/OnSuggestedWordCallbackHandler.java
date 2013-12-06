package ca.spacek.gkdd;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.database.Cursor;
import ca.spacek.gkdd.contentprovider.DictionaryWordContentProvider;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

final class OnSuggestedWordCallbackHandler implements InvocationHandler {
	private final Object original;
	private final Context context;

	OnSuggestedWordCallbackHandler(Object original, Context context) {
		this.original = original;
		this.context = context;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		ArrayList<?> wordList = (ArrayList<?>) XposedHelpers.getObjectField(
				args[0], "mSuggestedWordInfoList");
		Iterator<?> iterator = wordList.iterator();
		while (iterator.hasNext()) {
			Object suggestedWordInfo = iterator.next();
			String word = (String) XposedHelpers.getObjectField(
					suggestedWordInfo, "mWord");
			if (isWordInDeletes(word)) {
				iterator.remove();
			}
		}

		XposedBridge.log("Invoking original: " + args);
		return method.invoke(original, args);
	}

	private boolean isWordInDeletes(String word) {
		XposedBridge.log("Checking if " + word + " is in delete dictionary");
		Cursor cursor = query(word);
		XposedBridge.log("Query returned count: " + cursor.getCount());
		return cursor.getCount() > 0;
	}

	private Cursor query(String word) {
		return context.getContentResolver().query(
				DictionaryWordContentProvider.CONTENT_URI,
				new String[] { "_id" }, "word=?", new String[] { word }, null);
	}
}