package ca.spacek.gkdd.blacklist.current.hook;

import ca.spacek.gkdd.blacklist.current.ContextAccessor;
import ca.spacek.gkdd.blacklist.current.PackageReflection;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

/**
 * Created by temp on 22/04/14.
 */
public class ContextChangeHook {
    private final ContextAccessor contextAccessor;
    private final PackageReflection packageReflection;

    public ContextChangeHook(ContextAccessor contextAccessor, PackageReflection packageReflection) {
        this.contextAccessor = contextAccessor;
        this.packageReflection = packageReflection;
    }

    public void hook() {
        XposedBridge.hookAllConstructors(packageReflection.getMainKeyboardViewClass(), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param)
                    throws Throwable {
                contextAccessor.set(packageReflection.getMainKeyboardViewConstructorContextArg(param.args));
            }
        });
    }
}
