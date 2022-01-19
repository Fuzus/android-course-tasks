package com.example.tasks.service.repository;

import android.content.Context;

import com.example.tasks.R;
import com.google.gson.Gson;

import okhttp3.ResponseBody;

public class BaseRepository {

    Context _context;

    public BaseRepository(Context context) {
        this._context = context;
    }

    public String handleFailure(ResponseBody response) {
        try {
            return new Gson().fromJson(response.string(), String.class);
        } catch (Exception e) {
            return _context.getString(R.string.ERROR_UNEXPECTED);
        }
    }
}
