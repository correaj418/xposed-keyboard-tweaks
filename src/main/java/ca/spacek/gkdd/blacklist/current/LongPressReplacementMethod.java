package ca.spacek.gkdd.blacklist.current;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ca.spacek.gkdd.contentprovider.DictionaryWordContentProvider;
import ca.spacek.gkdd.data.DictionaryWordTable;
import de.robv.android.xposed.XC_MethodReplacement;

public class LongPressReplacementMethod extends XC_MethodReplacement {
    private final PackageReflection packageReflection;

    public LongPressReplacementMethod(PackageReflection packageReflection) {
        this.packageReflection = packageReflection;
    }

    @Override
	protected Object replaceHookedMethod(MethodHookParam param)
			throws Throwable {
        View view = packageReflection.getOnLongClickView(param.args);
        Context context = view.getContext();
        if (context == null) {
            Log.e("blacklist", "Couldn't add word to blacklist, context was null");
            return true;
        }

        List<String> words;
        String word;
        try {
            words = packageReflection.getSuggestedWords(param.thisObject);
            int index = (Integer) view.getTag();
            word = words.remove(index);
        }
        catch (Exception e) {
            Log.e("blacklist", "Couldn't add word to blacklist", e);
            Toast.makeText(context, "Couldn't add word to blacklist", Toast.LENGTH_SHORT).show();
            return true;
        }

        ContentValues values = new ContentValues();
        values.put(DictionaryWordTable.COLUMN_WORD, word);

		context.getContentResolver().insert(DictionaryWordContentProvider.CONTENT_URI, values);

		Toast.makeText(context, "Added word to blacklist!", Toast.LENGTH_SHORT).show();

        packageReflection.updateAndSelectNewWord(param.thisObject, words);

		return true;
	}
}