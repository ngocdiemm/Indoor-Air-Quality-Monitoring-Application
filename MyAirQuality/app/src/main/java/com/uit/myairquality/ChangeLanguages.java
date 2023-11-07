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
        if (!currentLanguage.equals(language)) {
            persistLanguage(activity, language);
            updateResources(activity, language);
            activity.recreate();
        }
    }
    private static void persistLanguage(Activity activity, String language) {
        SharedPreferenceManager.getInstance(activity).saveStateLanguage(language);
    }

    public static void updateResources(Activity activity, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public static String getSelectedLanguage(Activity activity) {
        return SharedPreferenceManager.getInstance(activity).getStateLanguage();
    }
}

