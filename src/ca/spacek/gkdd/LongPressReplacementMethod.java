package ca.spacek.gkdd;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.widget.Toast;
import ca.spacek.gkdd.contentprovider.DictionaryWordContentProvider;
import ca.spacek.gkdd.data.DictionaryWordTable;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

class LongPressReplacementMethod extends XC_MethodReplacement {
	@Override
	protected Object replaceHookedMethod(MethodHookParam param)
			throws Throwable {
		View view = (View) param.args[0];
		String word = getWord(view, param.thisObject);

		ContentValues values = new ContentValues();
		values.put(DictionaryWordTable.COLUMN_WORD, word);

		Context context = view.getContext();
		context.getContentResolver().insert(
				DictionaryWordContentProvider.CONTENT_URI, values);

		Toast.makeText(context, "Added word to blacklist!", Toast.LENGTH_SHORT)
				.show();

		return false;
	}

	private String getWord(View view, Object suggestionStripView) {
		final Object tag = view.getTag();
		if (!(tag instanceof Integer)) {
			return null;
		}

		Object suggestedWords = XposedHelpers.getObjectField(
				suggestionStripView, "mSuggestedWords");
		final int index = (Integer) tag;
		if (index >= (Integer) XposedHelpers.callMethod(suggestedWords, "size")) {
			return null;
		}

		final Object wordInfo = XposedHelpers.callMethod(suggestedWords,
				"getInfo", index);
		String word = (String) XposedHelpers.getObjectField(wordInfo, "mWord");
		XposedBridge.log("Adding word '" + word + "' to dictionary");
		return word;
	}
}