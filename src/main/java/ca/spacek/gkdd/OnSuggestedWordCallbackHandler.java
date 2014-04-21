package ca.spacek.gkdd;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

final class OnSuggestedWordCallbackHandler implements InvocationHandler {
	private final Object original;
	private final BlackList blackList;

	OnSuggestedWordCallbackHandler(Object original, BlackList blackList) {
		this.original = original;
		this.blackList = blackList;
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
		return blackList.contains(word);
	}
}