package com.example.doan7;

import android.app.Application;

public class MyApplication extends Application {

    private static String selectedLanguage = "English";

    public static String getSelectedLanguage() {
        return selectedLanguage;
    }

    public static void setSelectedLanguage(String language) {
        selectedLanguage = language;
    }
}
