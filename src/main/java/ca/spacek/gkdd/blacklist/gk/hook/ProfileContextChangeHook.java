package ca.spacek.gkdd.blacklist.gk.hook;

import android.content.Context;

import ca.spacek.gkdd.blacklist.ContextChangeHook;
import ca.spacek.gkdd.blacklist.gk.ContextManager;
import ca.spacek.gkdd.blacklist.gk.PackageReflection;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

/**
 * Created by temp on 22/04/14.
 */
public class ProfileContextChangeHook implements ContextChangeHook {
    private final ContextManager contextManager;
    private final PackageReflection packageReflection;

    public ProfileContextChangeHook(ContextManager contextManager, PackageReflection packageReflection) {
        this.contextManager = contextManager;
        this.packageReflection = packageReflection;
    }

    @Override
    public void hookContextChange() {
        XposedBridge.hookAllConstructors(packageReflection.getMainKeyboardViewClass(), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param)
                    throws Throwable {
                Context context = packageReflection.getMainKeyboardViewConstructorContextArg(param.args);
                contextManager.update(context);
            }
        });
    }
}
