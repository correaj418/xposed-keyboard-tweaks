package ca.spacek.gkdd.blacklist.current;

import android.util.Log;

import com.google.common.base.Joiner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import ca.spacek.gkdd.blacklist.SuggestionBlackLister;

public class OnSuggestedWordCallbackHandler implements InvocationHandler {
	private final Object original;
    private final SuggestionBlackLister suggestionBlackLister;
    private final PackageReflection packageReflection;

	public OnSuggestedWordCallbackHandler(Object original, SuggestionBlackLister blackLister, PackageReflection packageReflection) {
		this.original = original;
		this.suggestionBlackLister = blackLister;
        this.packageReflection = packageReflection;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		List<String> wordInfoList = packageReflection.getCallbackHandlerWordListArg(args);
        Log.d("blacklist", Joiner.on(',').join(wordInfoList));
        suggestionBlackLister.filterResults(wordInfoList);
        Log.d("blacklist", Joiner.on(',').join(wordInfoList));
		return method.invoke(original, args);
	}
}