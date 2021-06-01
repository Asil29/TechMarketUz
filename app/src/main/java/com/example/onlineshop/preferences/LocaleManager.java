package com.example.onlineshop.preferences;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.util.Locale;

public class LocaleManager {

    public static Context setLocale(Context c) {
        Log.d("prefs", getLanguage(c));
        return updateResources(c, getLanguage(c));
    }

    public static Context setNewLocale(Context c, String language) {
        persistLanguage(c, language);
        return updateResources(c, language);
    }

    public static String getLanguage(Context c) {
        return Hawk.get("language_pref", "en");
    }

    private static void persistLanguage(Context c, String language) {
        Hawk.put("language_pref", language);
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
        return context;
    }

}
