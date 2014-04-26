package ca.spacek.gkdd.blacklist.gk.hook;

import ca.spacek.gkdd.blacklist.AddToBlackListHook;
import ca.spacek.gkdd.blacklist.gk.LongPressReplacementMethod;
import ca.spacek.gkdd.blacklist.gk.PackageReflection;
import de.robv.android.xposed.XposedBridge;

/**
 * Created by temp on 22/04/14.
 */
public class ProfileAddToBlackListHook implements AddToBlackListHook {
    private final PackageReflection packageReflection;

    public ProfileAddToBlackListHook(PackageReflection packageReflection) {
        this.packageReflection = packageReflection;
    }

    @Override
    public void hookKeyboard() {
        XposedBridge.hookMethod(packageReflection.getOnLongClickMethod(), new LongPressReplacementMethod(packageReflection));
    }
}
