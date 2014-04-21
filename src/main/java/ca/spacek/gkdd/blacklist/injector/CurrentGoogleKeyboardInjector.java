package ca.spacek.gkdd.blacklist.injector;

import ca.spacek.gkdd.Injector;
import ca.spacek.gkdd.blacklist.ReflectionException;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by temp on 20/04/14.
 */
public class CurrentGoogleKeyboardInjector implements Injector {
    private static final String PACKAGE_NAME = "com.google.android.inputmethod.latin";

    private PackageReflection packageReflection;

    @Override
    public boolean supports(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!PACKAGE_NAME.equals(loadPackageParam.packageName)) {
            return false;
        }

        packageReflection = new PackageReflection();
        try {
            packageReflection.initialize(loadPackageParam.classLoader);
        } catch (ReflectionException e) {
            return false;
        }

        return true;
    }

    @Override
    public void inject(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        GetSuggestedWordsHook hook = new GetSuggestedWordsHook(packageReflection);
        hook.hook();
    }
}
