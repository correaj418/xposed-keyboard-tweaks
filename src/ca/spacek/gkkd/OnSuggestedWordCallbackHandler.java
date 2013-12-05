package ca.spacek.gkkd;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

final class OnSuggestedWordCallbackHandler implements
		InvocationHandler {
	private final Object original;

	OnSuggestedWordCallbackHandler(Object original) {
		this.original = original;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		ArrayList<?> wordList = (ArrayList<?>) XposedHelpers.getObjectField(args[0], "mSuggestedWordInfoList");
		Iterator<?> iterator = wordList.iterator();
		while (iterator.hasNext()) {
			Object suggestedWordInfo = iterator.next();
			String word = (String) XposedHelpers.getObjectField(suggestedWordInfo, "mWord");
			if (word.equals("Hello")) {
				iterator.remove();
			}
		}
		
		XposedBridge.log("Invoking original: " + args);
		return method.invoke(original, args);
	}
}