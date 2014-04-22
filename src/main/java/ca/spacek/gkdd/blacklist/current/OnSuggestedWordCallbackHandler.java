package ca.spacek.gkdd.blacklist.current;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import ca.spacek.gkdd.blacklist.SuggestionBlackLister;
import ca.spacek.gkdd.blacklist.current.PackageReflection;
import ca.spacek.gkdd.blacklist.proxy.SuggestedWordInfo;
import de.robv.android.xposed.XposedBridge;

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
		List<SuggestedWordInfo> wordInfoList = packageReflection.getCallbackHandlerWordListArg(args);
		suggestionBlackLister.filterResults(wordInfoList);
		XposedBridge.log("Invoking original: " + args);
		return method.invoke(original, args);
	}
}