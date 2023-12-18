package com.uit.myairquality;

import android.content.Context;
import android.content.SharedPreferences;

import java.security.Key;
import java.util.Locale;

public class SharedPreferenceManager {
    private static final String PREF_NAME = "PREF_AIR_QUALITY";
    private static final String KEY_LANGUAGES = "KEY_LANGUAGES";
    private static final String KEY_MODES = "KEY_MODES";
    private static final String KEY_NEW_USER = "KEY_NEW_USER";
    //biến instance đảm bảo rằng chỉ có 1 phiên bản duy nhất được tạo ra và sử dụng cho toàn bộ ứng dụng
    private static SharedPreferenceManager instance;
    private SharedPreferences sharedPreferences;

    //dùng để thiết lập đối tượng sharedPreferences cho lớp SharedPreferenceManager
    private SharedPreferenceManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    //dùng để tạo hoặc truy cập vào đối tượng SharePreferences trong android để truy xuất hoặc lưu trữ
    public static synchronized SharedPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceManager(context);
        }
        return instance;
    }

    //lưu lại trạng thái của ngôn ngữ
    public void saveStateLanguage(String state) {
        sharedPreferences.edit().putString(KEY_LANGUAGES, state).apply();
    }

    //lấy ra trạng thái của ngôn ngữ hiện tại
    public String getStateLanguage() {
        return sharedPreferences.getString(KEY_LANGUAGES, Locale.getDefault().getLanguage());
    }

    //lưu lại trạng thái hiện tại của dark-mode
    public void saveStateMode(Boolean state) {
        sharedPreferences.edit().putBoolean(KEY_MODES, state).apply();
    }

    //lấy ra trạng thái hiện tại xem phải người dùng mới hay không
    public Boolean getStateMode() {
        return sharedPreferences.getBoolean(KEY_MODES, false);
    }

    //lấy ra trạng thái hiện tại xem phải người dùng mới hay không
    public Boolean getStateNewUser() {
        return sharedPreferences.getBoolean(KEY_NEW_USER, true);
    }

    //lưu lại trạng thái hiện tại của người dùng mới hay không
    public void saveStateNewUser(Boolean state) {
        sharedPreferences.edit().putBoolean(KEY_NEW_USER, state).apply();
    }


}