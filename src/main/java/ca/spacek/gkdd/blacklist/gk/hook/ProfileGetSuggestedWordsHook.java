package ca.spacek.gkdd.blacklist.gk.hook;

import ca.spacek.gkdd.blacklist.GetSuggestedWordsHook;
import ca.spacek.gkdd.blacklist.gk.OnSuggestedWordCallbackProxyFactory;
import ca.spacek.gkdd.blacklist.gk.PackageReflection;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

/**
 * Created by temp on 21/04/14.
 */
public class ProfileGetSuggestedWordsHook implements GetSuggestedWordsHook {
    private final PackageReflection packageReflection;
    private final OnSuggestedWordCallbackProxyFactory factory;

    public ProfileGetSuggestedWordsHook(PackageReflection packageReflection, OnSuggestedWordCallbackProxyFactory factory) {
        this.packageReflection = packageReflection;
        this.factory = factory;
    }

    @Override
    public void hookSuggestWords() {
        XposedBridge.hookMethod(packageReflection.getGetSuggestedWordsMethod(), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                packageReflection.replaceCallbackArg(param.args, factory);
                super.beforeHookedMethod(param);
            }
        });
    }
}
