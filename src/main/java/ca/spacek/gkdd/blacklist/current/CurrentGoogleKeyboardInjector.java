package ca.spacek.gkdd.blacklist.current;

import ca.spacek.gkdd.BlackList;
import ca.spacek.gkdd.CachedBlackList;
import ca.spacek.gkdd.Injector;
import ca.spacek.gkdd.blacklist.ReflectionException;
import ca.spacek.gkdd.blacklist.SuggestionBlackLister;
import ca.spacek.gkdd.blacklist.current.hook.AddToBlackListHook;
import ca.spacek.gkdd.blacklist.current.hook.ContextChangeHook;
import ca.spacek.gkdd.blacklist.current.hook.GetSuggestedWordsHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by temp on 20/04/14.
 */
public class CurrentGoogleKeyboardInjector implements Injector {
    private ContextAccessor contextAccessor = new ContextAccessor();

    private PackageReflection packageReflection;
    private GetSuggestedWordsHook getSuggestedWordsHook;
    private ContextChangeHook contextChangeHook;
    private AddToBlackListHook addToBlackListHook;

    @Override
    public boolean supports(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!PackageReflection.PACKAGE_NAME.equals(loadPackageParam.packageName)) {
            return false;
        }

        XposedBridge.log("Found the package, trying to reflect all the necessary classes");
        packageReflection = new PackageReflection();
        try {
            packageReflection.initialize(loadPackageParam.classLoader);
        } catch (ReflectionException e) {
            XposedBridge.log("Couldn't initialize: " + e.getCause());
            return false;
        }

        return true;
    }

    @Override
    public void inject(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedBridge.log("Initialized, hooking");

        contextChangeHook = new ContextChangeHook(contextAccessor, packageReflection);
        contextChangeHook.hook();

        BlackList blackList = new CachedBlackList(contextAccessor);
        getSuggestedWordsHook = new GetSuggestedWordsHook(packageReflection, new OnSuggestedWordCallbackProxyFactory(new SuggestionBlackLister(blackList), packageReflection));
        getSuggestedWordsHook.hook();

        addToBlackListHook = new AddToBlackListHook(packageReflection);
        addToBlackListHook.hook();
    }
}
