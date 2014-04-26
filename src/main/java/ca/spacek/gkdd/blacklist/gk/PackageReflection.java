package ca.spacek.gkdd.blacklist.gk;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ca.spacek.gkdd.blacklist.ReflectionException;
import de.robv.android.xposed.XposedHelpers;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

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
    private static final String CLASS_SUGGESTION_STRIP_VIEW = "com.android.inputmethod.latin.suggestions.SuggestionStripView";
    private static final String CLASS_SUGGESTION_STRIP_VIEW_LISTENER = "com.android.inputmethod.latin.suggestions.SuggestionStripView$Listener";
    private static final String CLASS_SUGGESTED_WORDS = "com.android.inputmethod.latin.SuggestedWords";
    private static final String CLASS_DICTIONARY = "com.android.inputmethod.latin.Dictionary";
    private static final String METHOD_GET_SUGGESTED_WORDS = "getSuggestedWords";
    private static final String METHOD_ON_LONG_CLICK = "onLongClick";
    private static final String METHOD_SET_SUGGESTIONS = "setSuggestions";
    private static final String FIELD_SUGGESTED_WORDS = "mSuggestedWords";
    private static final String FIELD_SUGGESTED_WORD_INFO_LIST = "mSuggestedWordInfoList";
    private static final String FIELD_LISTENER = "mListener";
    private static final int ARG_CALLBACK = 8;
    private static final int ARG_MAIN_KEYBOARD_VIEW_CONSTRUCTOR_CONTEXT = 0;
    private static final int ARG_VIEW = 0;
    private static final int DEFAULT_SCORE = 0x7fffffff;
    private static final String FIELD_DICTIONARY_USER_TYPED = "DICTIONARY_USER_TYPED";
    private static final String METHOD_PICK_SUGGESTION_MANUALLY = "pickSuggestionManually";

    private Class<?> suggestClass;
    private Class<?> wordComposerClass;
    private Class<?> proximityInfoClass;
    private Class<?> onGetSuggestedWordsCallbackClass;
    private Class<?> suggestedWordInfoClass;
    private Class<?> mainKeyboardViewClass;
    private Class<?> suggestionStripViewClass;
    private Class<?> suggestionStripViewListenerClass;
    private Class<?> suggestedWordsClass;
    private Class<?> dictionaryClass;

    private Method getSuggestedWordsMethod;
    private Method onLongClickMethod;
    private Method setSuggestedWordsMethod;
    private Method pickSuggestionManuallyMethod;

    private Constructor<?> suggestedWordsConstructor;
    private Constructor<?> suggestedWordInfoConstructor;

    private Object dictionaryUserTyped;

    public void initialize(ClassLoader classLoader) throws ReflectionException {
        try {
            suggestClass = classLoader.loadClass(CLASS_SUGGEST);
            wordComposerClass = classLoader.loadClass(CLASS_WORD_COMPOSER);
            proximityInfoClass = classLoader.loadClass(CLASS_PROXIMITY_INFO);
            onGetSuggestedWordsCallbackClass = classLoader.loadClass(CLASS_ON_GET_SUGGESTED_WORDS_CALLBACK);
            suggestedWordInfoClass = classLoader.loadClass(CLASS_SUGGESTED_WORD_INFO);
            mainKeyboardViewClass = classLoader.loadClass(CLASS_MAIN_KEYBOARD_VIEW);
            suggestionStripViewClass = classLoader.loadClass(CLASS_SUGGESTION_STRIP_VIEW);
            suggestionStripViewListenerClass = classLoader.loadClass(CLASS_SUGGESTION_STRIP_VIEW_LISTENER);
            suggestedWordsClass = classLoader.loadClass(CLASS_SUGGESTED_WORDS);
            dictionaryClass = classLoader.loadClass(CLASS_DICTIONARY);
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

        getSuggestedWordsMethod = XposedHelpers.findMethodExact(suggestClass, METHOD_GET_SUGGESTED_WORDS, wordComposerClass, String.class, proximityInfoClass, boolean.class, boolean.class, int[].class, int.class, int.class, onGetSuggestedWordsCallbackClass);
        if (getSuggestedWordsMethod == null) {
            throw new ReflectionException("Couldn't find getSuggestedWords method");
        }

        onLongClickMethod = XposedHelpers.findMethodExact(suggestionStripViewClass, METHOD_ON_LONG_CLICK, View.class);
        if (onLongClickMethod == null) {
            throw new ReflectionException("Couldn't find onLongClick method");
        }

        setSuggestedWordsMethod = XposedHelpers.findMethodExact(suggestionStripViewClass, METHOD_SET_SUGGESTIONS, suggestedWordsClass, boolean.class);
        if (setSuggestedWordsMethod == null) {
            throw new ReflectionException("Couldn't find setSuggestions method");
        }

        try {
            pickSuggestionManuallyMethod = suggestionStripViewListenerClass.getMethod(METHOD_PICK_SUGGESTION_MANUALLY, int.class, suggestedWordInfoClass);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException("Couldn't find method", e);
        }

        try {
            suggestedWordsConstructor = suggestedWordsClass.getConstructor(ArrayList.class, ArrayList.class, boolean.class, boolean.class, boolean.class, boolean.class, int.class);
            suggestedWordInfoConstructor = suggestedWordInfoClass.getConstructor(String.class, int.class, int.class, dictionaryClass, int.class, int.class);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException("Couldn't load constructor", e);
        }


        try {
            Field dictionaryUserTypedField = dictionaryClass.getDeclaredField(FIELD_DICTIONARY_USER_TYPED);
            dictionaryUserTyped = dictionaryUserTypedField.get(null);
        } catch (NoSuchFieldException e) {
            throw new ReflectionException("Couldn't get field", e);
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Couldn't get value of static field", e);
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

    public List<String> getCallbackHandlerWordListArg(Object[] args) {
        // TODO Get rid of magic number, what type is this first parameter?
        List<?> wordInfoList = (List<?>) XposedHelpers.getObjectField(args[0], FIELD_SUGGESTED_WORD_INFO_LIST);
        if (wordInfoList.isEmpty()) {
            return Lists.newArrayList();
        }

        Object wordInfo = wordInfoList.get(0);
        if (wordInfo.getClass() != suggestedWordInfoClass) {
            throw new RuntimeException("SuggestedWordInfo class did not match expected: " + wordInfo.getClass().getName());
        }

        return transform(wordInfoList, new SuggestedWordInfoTransform());
    }

    public Context getMainKeyboardViewConstructorContextArg(Object[] args) {
        return (Context) args[ARG_MAIN_KEYBOARD_VIEW_CONSTRUCTOR_CONTEXT];
    }

    public Class<?> getMainKeyboardViewClass() {
        return mainKeyboardViewClass;
    }

    public Method getOnLongClickMethod() {
        return onLongClickMethod;
    }

    public View getOnLongClickView(Object[] args) {
        return (View) args[ARG_VIEW];
    }

    public List<String> getSuggestedWords(Object suggestionStripView) {
        Object suggestedWords = XposedHelpers.getObjectField(suggestionStripView, FIELD_SUGGESTED_WORDS);
        List<?> suggestedWordInfos = (List<?>) XposedHelpers.getObjectField(suggestedWords, FIELD_SUGGESTED_WORD_INFO_LIST);
        return Lists.newArrayList(transform(suggestedWordInfos, new SuggestedWordInfoTransform()));
    }

    public void updateAndSelectNewWord(Object thisObject, List<String> words) {
        // Prune the suggestion list
        Object suggestedWords;
        ArrayList<Object> suggestedWordInfos;
        try {
            suggestedWordInfos = newArrayList(transform(words, new Function<String, Object>() {
                @Override
                public Object apply(String input) {
                    try {
                        return suggestedWordInfoConstructor.newInstance(input, DEFAULT_SCORE, 0, dictionaryUserTyped, -1, -1);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }));
            suggestedWords = suggestedWordsConstructor.newInstance(suggestedWordInfos, null, false, false, false, false, 0);
        } catch (Exception e) {
            Log.e("blacklist", "Couldn't prune suggested words", e);
            return;
        }

        // Update the SuggestionStripView: updateAndSelectNewWord
        try {
            setSuggestedWordsMethod.invoke(thisObject, suggestedWords, false);
        } catch (Exception e) {
            Log.e("blacklist", "Couldn't update suggested words", e);
        }
    }
}
