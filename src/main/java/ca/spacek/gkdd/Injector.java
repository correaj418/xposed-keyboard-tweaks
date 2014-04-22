package ca.spacek.gkdd;

import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by temp on 20/04/14.
 */
public interface Injector {
    boolean supports(XC_LoadPackage.LoadPackageParam loadPackageParam);

    void inject(XC_LoadPackage.LoadPackageParam loadPackageParam);
}
