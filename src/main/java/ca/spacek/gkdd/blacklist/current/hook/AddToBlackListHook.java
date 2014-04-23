package ca.spacek.gkdd.blacklist.current.hook;

import android.view.View;

import ca.spacek.gkdd.blacklist.current.LongPressReplacementMethod;
import ca.spacek.gkdd.blacklist.current.PackageReflection;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by temp on 22/04/14.
 */
public class AddToBlackListHook {
    private final PackageReflection packageReflection;

    public AddToBlackListHook(PackageReflection packageReflection) {
        this.packageReflection = packageReflection;
    }

    public void hook() {
        XposedBridge.hookMethod(packageReflection.getOnLongClickMethod(), new LongPressReplacementMethod(packageReflection));
    }
}
