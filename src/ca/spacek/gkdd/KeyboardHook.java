package ca.spacek.gkdd;

import java.lang.reflect.Proxy;

import android.content.Context;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class KeyboardHook implements IXposedHookLoadPackage {
	private static final String PACKAGE_AOSP_LATIN_IME = "com.android.inputmethod.latin";
	private static final String PACKAGE_GOOGLE_LATIN_IME = "com.google.android.inputmethod.latin";

	private static final String CLASS_SUGGEST = "Suggest";
	private static final String CLASS_CALLBACK = "Suggest$OnGetSuggestedWordsCallback";
	
	private Context context;

	@Override
	public void handleLoadPackage(final LoadPackageParam lpparam)
			throws Throwable {
		XposedBridge.log("Loading package: " + lpparam.packageName);
		if (!lpparam.packageName.equals(PACKAGE_GOOGLE_LATIN_IME)) {
			return;
		}

		final Class<?> callbackInterface = loadClass(lpparam.classLoader,
				CLASS_CALLBACK);
		final Class<?> suggestClass = loadClass(lpparam.classLoader,
				CLASS_SUGGEST);
		
		XposedBridge.hookAllConstructors(suggestClass, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param)
					throws Throwable {
				XposedBridge.log("Creating instance of Suggest");
				context = (Context) param.args[0];
			}
		});

		XposedBridge.hookAllMethods(suggestClass, "getSuggestedWords",
				new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param)
							throws Throwable {
						XposedBridge.log("Before keyboard suggest");
						Context context = getContext(param.thisObject);
						replaceCallbackWithProxy(param, context);
					}

					private void replaceCallbackWithProxy(MethodHookParam param, Context context) {
						final Object original = param.args[param.args.length - 1];
						param.args[param.args.length - 1] = createProxyInstance(
								lpparam, callbackInterface, original, context);
					}
				});
	}
	
	private Context getContext(Object suggestInstance) {
		return context;
	}

	private Class<?> loadClass(ClassLoader classLoader, String clazz)
			throws ClassNotFoundException {
		return classLoader.loadClass(PACKAGE_AOSP_LATIN_IME + "." + clazz);
	}

	private Object createProxyInstance(final LoadPackageParam lpparam,
			final Class<?> callbackInterface, final Object original, Context context) {
		return Proxy.newProxyInstance(lpparam.classLoader,
				new Class<?>[] { callbackInterface },
				new OnSuggestedWordCallbackHandler(original, context));
	}
}
