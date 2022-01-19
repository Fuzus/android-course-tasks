package com.example.tasks.service.repository;


import android.content.Context;

import com.example.tasks.service.constants.TaskConstants;
import com.example.tasks.service.listener.APIListener;
import com.example.tasks.service.model.PriorityModel;
import com.example.tasks.service.repository.local.PriorityDAO;
import com.example.tasks.service.repository.local.TaskDatabase;
import com.example.tasks.service.repository.remote.PriorityService;
import com.example.tasks.service.repository.remote.RetrofitClient;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriorityRepository extends BaseRepository{

    private PriorityService _priorityService;
    private PriorityDAO _priorityDAO;

    public PriorityRepository(Context context){
        super(context);
        this._priorityService = RetrofitClient.createService(PriorityService.class);
        this._priorityDAO = TaskDatabase.getDataBase(context).priorityDAO();
    }

    public void all(final APIListener<List<PriorityModel>> listener){
        Call<List<PriorityModel>> call = this._priorityService.all();
        call.enqueue(new Callback<List<PriorityModel>>() {
            @Override
            public void onResponse(Call<List<PriorityModel>> call, Response<List<PriorityModel>> response) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(handleFailure(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<List<PriorityModel>> call, Throwable t) {
                //TODO
            }
        });
    }

    public void save(List<PriorityModel> list) {
        this._priorityDAO.clear();
        this._priorityDAO.save(list);
    }
}
