package com.example.tasks.service.repository.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences
 */
public class SecurityPreferences {

    private SharedPreferences _sharedPreferences;

    public SecurityPreferences(Context context) {
        this._sharedPreferences = context.getSharedPreferences("TasksShared", Context.MODE_PRIVATE);
    }

    public void storeString(String key, String value) {
        this._sharedPreferences.edit().putString(key, value).apply();
    }

    public String getStoredString(String key) {
        return this._sharedPreferences.getString(key, "");
    }

    public void remove(String key) {
        this._sharedPreferences.edit().remove(key).apply();
    }


}
