package ca.spacek.gkdd.blacklist.current.hook;

import ca.spacek.gkdd.blacklist.current.OnSuggestedWordCallbackProxyFactory;
import ca.spacek.gkdd.blacklist.current.PackageReflection;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

/**
 * Created by temp on 21/04/14.
 */
public class GetSuggestedWordsHook {
    private final PackageReflection packageReflection;
    private final OnSuggestedWordCallbackProxyFactory factory;

    public GetSuggestedWordsHook(PackageReflection packageReflection, OnSuggestedWordCallbackProxyFactory factory) {
        this.packageReflection = packageReflection;
        this.factory = factory;
    }

    public void hook() {
        XposedBridge.hookMethod(packageReflection.getGetSuggestedWordsMethod(), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                packageReflection.replaceCallbackArg(param.args, factory);
                super.beforeHookedMethod(param);
            }
        });
    }
}
