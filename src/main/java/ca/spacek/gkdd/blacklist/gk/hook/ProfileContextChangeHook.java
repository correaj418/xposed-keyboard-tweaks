package ca.spacek.gkdd.blacklist.gk.hook;

import ca.spacek.gkdd.blacklist.ContextChangeHook;
import ca.spacek.gkdd.blacklist.gk.ContextAccessor;
import ca.spacek.gkdd.blacklist.gk.PackageReflection;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

/**
 * Created by temp on 22/04/14.
 */
public class ProfileContextChangeHook implements ContextChangeHook {
    private final ContextAccessor contextAccessor;
    private final PackageReflection packageReflection;

    public ProfileContextChangeHook(ContextAccessor contextAccessor, PackageReflection packageReflection) {
        this.contextAccessor = contextAccessor;
        this.packageReflection = packageReflection;
    }

    @Override
    public void hookContextChange() {
        XposedBridge.hookAllConstructors(packageReflection.getMainKeyboardViewClass(), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param)
                    throws Throwable {
                contextAccessor.set(packageReflection.getMainKeyboardViewConstructorContextArg(param.args));
            }
        });
    }
}
