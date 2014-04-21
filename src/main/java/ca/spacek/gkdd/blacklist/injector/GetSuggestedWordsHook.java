package ca.spacek.gkdd.blacklist.injector;

import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

/**
 * Created by temp on 21/04/14.
 */
public class GetSuggestedWordsHook {
    private final PackageReflection packageReflection;

    public GetSuggestedWordsHook(PackageReflection packageReflection) {
        this.packageReflection = packageReflection;
    }

    public void hook() {
        XposedBridge.hookMethod(packageReflection.getGetSuggestedWordsMethod(), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("Before getSuggestedWordsMethod");
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("After getSuggestedWordsMethod");
                super.afterHookedMethod(param);
            }
        });
    }
}
