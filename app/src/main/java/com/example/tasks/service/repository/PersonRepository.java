package com.example.tasks.service.repository;

import android.content.Context;

import com.example.tasks.R;
import com.example.tasks.service.constants.TaskConstants;
import com.example.tasks.service.listener.APIListener;
import com.example.tasks.service.model.PersonModel;
import com.example.tasks.service.repository.local.SecurityPreferences;
import com.example.tasks.service.repository.remote.PersonService;
import com.example.tasks.service.repository.remote.RetrofitClient;

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

    private void callUserAPI(Call<PersonModel> call, final APIListener<PersonModel> listener) {
        if (!super.isConnectionAvailable()) {
            listener.onFailure(_context.getString(R.string.ERROR_INTERNET_CONNECTION));
            return;
        }

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

    public void create(String name, String email, String password, final APIListener<PersonModel> listener){
        Call<PersonModel> call = this._personService.create(name, email, password);
        this.callUserAPI(call, listener);
    }

    public void login(String email, String password, final APIListener<PersonModel> listener) {
        Call<PersonModel> call = this._personService.login(email, password);
        this.callUserAPI(call, listener);
    }

    public void saveUserData(PersonModel person){
        this._securityPreferences.storeString(TaskConstants.SHARED.PERSON_KEY, person.getPersonKey());
        this._securityPreferences.storeString(TaskConstants.SHARED.TOKEN_KEY, person.getToken());
        this._securityPreferences.storeString(TaskConstants.SHARED.PERSON_NAME, person.getName());
        this._securityPreferences.storeString(TaskConstants.SHARED.PERSON_EMAIL, person.getEmail());

        RetrofitClient.saveHeaders(person.getToken(), person.getPersonKey());
    }

    public void clearUserData() {
        this._securityPreferences.remove(TaskConstants.SHARED.PERSON_KEY);
        this._securityPreferences.remove(TaskConstants.SHARED.TOKEN_KEY);
        this._securityPreferences.remove(TaskConstants.SHARED.PERSON_NAME);
    }

    public PersonModel getUserData() {
        PersonModel person = new PersonModel();
        person.setName(this._securityPreferences.getStoredString(TaskConstants.SHARED.PERSON_NAME));
        person.setPersonKey(this._securityPreferences.getStoredString(TaskConstants.SHARED.PERSON_KEY));
        person.setToken(this._securityPreferences.getStoredString(TaskConstants.SHARED.TOKEN_KEY));
        person.setEmail(this._securityPreferences.getStoredString(TaskConstants.SHARED.PERSON_EMAIL));

        return person;
    }
}
