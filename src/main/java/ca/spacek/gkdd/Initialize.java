package ca.spacek.gkdd;

import java.util.ArrayList;
import java.util.List;

import ca.spacek.gkdd.blacklist.current.CurrentGoogleKeyboardInjector;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by temp on 16/04/14.
 */
public class Initialize implements IXposedHookLoadPackage, IXposedHookInitPackageResources {
    // Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
    private static final List<Injector> injectors = new ArrayList<Injector>();
    static {
        injectors.add(new CurrentGoogleKeyboardInjector());
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        for (Injector injector : injectors) {
            if (injector.supports(loadPackageParam)) {
                injector.inject(loadPackageParam);
            }
        }
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam initPackageResourcesParam) throws Throwable {
//        for (Injector injector : injectors) {
//            if (injector.supports(initPackageResourcesParam)) {
//                injector.inject(initPackageResourcesParam);
//            }
//        }
    }
}
