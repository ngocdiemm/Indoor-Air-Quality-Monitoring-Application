package com.uit.myairquality;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.os.ConfigurationCompat;

import java.util.ArrayList;
import java.util.Locale;

public class ChangeLanguages {

    public static void setLocale(Activity activity, String language) {
        String currentLanguage = getSelectedLanguage(activity);
//        if (!currentLanguage.equals(language)) {

        updateResources(activity, language);
        persistLanguage(activity, language);
        // activity.recreate();
//        }
    }

    //Lưu trạng thái ngôn ngữ
    private static void persistLanguage(Activity activity, String language) {
        SharedPreferenceManager.getInstance(activity).saveStateLanguage(language);
    }

    public static void updateResources(Activity activity, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        activity.getBaseContext().getResources().updateConfiguration(configuration, activity.getBaseContext().getResources().getDisplayMetrics());
    }

    public static String getSelectedLanguage(Activity activity) {
        return SharedPreferenceManager.getInstance(activity).getStateLanguage();
    }

    public static void loadLocaleChanged(Activity activity) {
        String currentLang = SharedPreferenceManager.getInstance(activity.getBaseContext()).getStateLanguage();
        if (currentLang.isEmpty()) {
            currentLang = "en";
        }

        ChangeLanguages.setLocale(activity, currentLang);
    }
}

