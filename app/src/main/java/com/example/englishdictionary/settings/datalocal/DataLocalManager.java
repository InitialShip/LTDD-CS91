package com.example.englishdictionary.settings.datalocal;

import android.content.Context;

public class DataLocalManager {
    private static DataLocalManager instance;
    private MySharePreferences mySharePreferences;

    public static void init(Context context) {
        instance = new DataLocalManager();
        instance.mySharePreferences = new MySharePreferences(context);
    }

    public static DataLocalManager getInstance() {
        if(instance == null)
            instance = new DataLocalManager();
        return instance;
    }

    public static void setStringPrefs(String key, String value) {
        DataLocalManager.getInstance().mySharePreferences.setStringValue(key, value);
    }

    public static String getStringPrefs(String key) {
        return DataLocalManager.getInstance().mySharePreferences.getStringValue(key);
    }

    public static void setBooleanPrefs(String key, boolean value) {
        DataLocalManager.getInstance().mySharePreferences.setBooleanValue(key, value);
    }

    public static boolean getBooleanPrefs(String key) {
        return DataLocalManager.getInstance().mySharePreferences.getBooleanValue(key);
    }

    public static void setIntPrefs(String key, int value) {
        DataLocalManager.getInstance().mySharePreferences.setIntValue(key, value);
    }

    public static int getIntPrefs(String key) {
        return DataLocalManager.getInstance().mySharePreferences.getIntValue(key);
    }

}
