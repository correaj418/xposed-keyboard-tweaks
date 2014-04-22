package ca.spacek.gkdd.blacklist.current;

import android.content.Context;

import com.google.common.collect.Lists;

import java.lang.reflect.Method;
import java.util.List;

import ca.spacek.gkdd.blacklist.DefaultSuggestedWordInfoConverter;
import ca.spacek.gkdd.blacklist.ReflectionException;
import ca.spacek.gkdd.blacklist.SuggestedWordInfoConverter;
import ca.spacek.gkdd.blacklist.proxy.SuggestedWordInfo;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by temp on 21/04/14.
 */
public class PackageReflection {
    public static final String PACKAGE_NAME = "com.google.android.inputmethod.latin";
    public static final String FIELD_WORD = "mWord";

    private static final String CLASS_SUGGEST = "com.android.inputmethod.latin.Suggest";
    private static final String CLASS_WORD_COMPOSER = "com.android.inputmethod.latin.WordComposer";
    private static final String CLASS_PROXIMITY_INFO = "com.android.inputmethod.keyboard.ProximityInfo";
    private static final String CLASS_ON_GET_SUGGESTED_WORDS_CALLBACK = "com.android.inputmethod.latin.Suggest$OnGetSuggestedWordsCallback";
    private static final String CLASS_SUGGESTED_WORD_INFO = "com.android.inputmethod.latin.SuggestedWords$SuggestedWordInfo";
    private static final String CLASS_MAIN_KEYBOARD_VIEW = "com.android.inputmethod.keyboard.MainKeyboardView";
    private static final String METHOD_GET_SUGGESTED_WORDS = "getSuggestedWords";
    private static final int ARG_CALLBACK = 8;
    private static final int ARG_MAIN_KEYBOARD_VIEW_CONSTRUCTOR_CONTEXT = 0;

    private Class<?> suggestClass;
    private Class<?> wordComposerClass;
    private Class<?> proximityInfoClass;
    private Class<?> onGetSuggestedWordsCallbackClass;
    private Class<?> suggestedWordInfoClass;
    private Class<?> mainKeyboardViewClass;

    private Method getSuggestedWordsMethod;
    private SuggestedWordInfoConverter converter = new DefaultSuggestedWordInfoConverter();

    public void initialize(ClassLoader classLoader) throws ReflectionException {
        try {
            suggestClass = classLoader.loadClass(CLASS_SUGGEST);
            wordComposerClass = classLoader.loadClass(CLASS_WORD_COMPOSER);
            proximityInfoClass = classLoader.loadClass(CLASS_PROXIMITY_INFO);
            onGetSuggestedWordsCallbackClass = classLoader.loadClass(CLASS_ON_GET_SUGGESTED_WORDS_CALLBACK);
            suggestedWordInfoClass = classLoader.loadClass(CLASS_SUGGESTED_WORD_INFO);
            mainKeyboardViewClass = classLoader.loadClass(CLASS_MAIN_KEYBOARD_VIEW);
        } catch (ClassNotFoundException e) {
            throw new ReflectionException(e);
        }

//        1266 .method public getSuggestedWords(Lcom/android/inputmethod/latin/WordComposer;Ljava/lang/String;Lcom/android/inputmethod/keyboard/ProximityInfo;ZZ[IIILcom/android/inputmethod/latin/Suggest$OnGetSuggestedWordsCallback;)V
//        1268     .param p1, "wordComposer"    # Lcom/android/inputmethod/latin/WordComposer;
//        1269     .param p2, "prevWordForBigram"    # Ljava/lang/String;
//        1270     .param p3, "proximityInfo"    # Lcom/android/inputmethod/keyboard/ProximityInfo;
//        1271     .param p4, "blockOffensiveWords"    # Z
//        1272     .param p5, "isCorrectionEnabled"    # Z
//        1273     .param p6, "additionalFeaturesOptions"    # [I
//        1274     .param p7, "sessionId"    # I
//        1275     .param p8, "sequenceNumber"    # I
//        1276     .param p9, "callback"    # Lcom/android/inputmethod/latin/Suggest$OnGetSuggestedWordsCallback;

        getSuggestedWordsMethod = XposedHelpers.findMethodBestMatch(suggestClass, METHOD_GET_SUGGESTED_WORDS, wordComposerClass, String.class, proximityInfoClass, boolean.class, boolean.class, int[].class, int.class, int.class, onGetSuggestedWordsCallbackClass);
        if (getSuggestedWordsMethod == null) {
            throw new ReflectionException("Couldn't find getSuggestedWords method");
        }
    }

    public Class<?> getSuggestClass() {
        return suggestClass;
    }

    public Class<?> getWordComposerClass() {
        return wordComposerClass;
    }

    public Class<?> getProximityInfoClass() {
        return proximityInfoClass;
    }

    public Class<?> getOnGetSuggestedWordsCallbackClass() {
        return onGetSuggestedWordsCallbackClass;
    }

    public Method getGetSuggestedWordsMethod() {
        return getSuggestedWordsMethod;
    }

    public void replaceCallbackArg(Object[] methodArgs, OnSuggestedWordCallbackProxyFactory factory) {
        // Determine position of arg
        methodArgs[ARG_CALLBACK] = factory.createProxy(methodArgs[ARG_CALLBACK]);
    }

    public List<SuggestedWordInfo> getCallbackHandlerWordListArg(Object[] args) {
        List<?> wordInfoList = (List<?>) XposedHelpers.getObjectField(args[0], "mSuggestedWordInfoList");
        if (wordInfoList.isEmpty()) {
            return Lists.newArrayList();
        }

        Object wordInfo = wordInfoList.get(0);
        if (wordInfo.getClass() != suggestedWordInfoClass) {
            throw new RuntimeException("SuggestedWordInfo class did not match expected: " + wordInfo.getClass().getName());
        }

        return converter.convert(wordInfoList);
    }

    public Context getMainKeyboardViewConstructorContextArg(Object[] args) {
        return (Context) args[ARG_MAIN_KEYBOARD_VIEW_CONSTRUCTOR_CONTEXT];
    }

    public Class<?> getMainKeyboardViewClass() {
        return mainKeyboardViewClass;
    }
}
