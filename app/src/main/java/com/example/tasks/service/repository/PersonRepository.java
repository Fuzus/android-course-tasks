package com.example.tasks.service.repository;

import android.content.Context;

import com.example.tasks.R;
import com.example.tasks.service.constants.TaskConstants;
import com.example.tasks.service.listener.APIListener;
import com.example.tasks.service.model.PersonModel;
import com.example.tasks.service.repository.local.SecurityPreferences;
import com.example.tasks.service.repository.remote.PersonService;
import com.example.tasks.service.repository.remote.RetrofitClient;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonRepository extends BaseRepository {

    private PersonService _personService;
    private SecurityPreferences _securityPreferences;

    public PersonRepository(Context context){
        super(context);
        this._personService = RetrofitClient.createService(PersonService.class);
        this._securityPreferences = new SecurityPreferences(context);
    }


    public void create(String name, String email, String password, final APIListener<PersonModel> listener){
        Call<PersonModel> call = this._personService.create(name, email, password);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                if(response.code() == 200) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(handleFailure(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                String s = "";
            }
        });
    }

    public void login(String email, String password, final APIListener<PersonModel> listener) {
        Call<PersonModel> call = this._personService.login(email, password);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                int code = response.code();
                if(code == TaskConstants.HTTP.SUCCESS){
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(handleFailure(response.errorBody()));
                }

            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                listener.onFailure(_context.getString(R.string.ERROR_UNEXPECTED));
            }
        });
    }

    public void saveUserData(PersonModel person){
        this._securityPreferences.storeString(TaskConstants.SHARED.PERSON_KEY, person.getPersonKey());
        this._securityPreferences.storeString(TaskConstants.SHARED.TOKEN_KEY, person.getToken());
        this._securityPreferences.storeString(TaskConstants.SHARED.PERSON_NAME, person.getName());
    }

    public PersonModel getUserData() {
        PersonModel person = new PersonModel();
        person.setName(this._securityPreferences.getStoredString(TaskConstants.SHARED.PERSON_NAME));
        person.setPersonKey(this._securityPreferences.getStoredString(TaskConstants.SHARED.PERSON_KEY));
        person.setToken(this._securityPreferences.getStoredString(TaskConstants.SHARED.TOKEN_KEY));

        return person;
    }
}
