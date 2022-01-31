package com.example.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tasks.service.listener.APIListener;
import com.example.tasks.service.listener.FeedBack;
import com.example.tasks.service.model.PersonModel;
import com.example.tasks.service.model.PriorityModel;
import com.example.tasks.service.repository.PersonRepository;
import com.example.tasks.service.repository.PriorityRepository;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {

    private PersonRepository _personRepository;
    private PriorityRepository _priorityRepository;

    private final MutableLiveData<FeedBack> _login = new MutableLiveData<>();
    public final LiveData<FeedBack> login = this._login;

    private final MutableLiveData<Boolean> _userLogged = new MutableLiveData<>();
    public final LiveData<Boolean> userLogged = this._userLogged;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this._personRepository = new PersonRepository(application);
        this._priorityRepository = new PriorityRepository(application);
    }

    public void login(final String email, String password) {
        _personRepository.login(email, password, new APIListener<PersonModel>() {
            @Override
            public void onSuccess(PersonModel result) {

                //salva os dados de login
                result.setEmail(email);
                _personRepository.saveUserData(result);

                //informa sucesso
                _login.setValue(new FeedBack());
            }

            @Override
            public void onFailure(String message) {
                _login.setValue(new FeedBack(message));
            }
        });
    }


    public void verifyUserLogged() {
        PersonModel person = this._personRepository.getUserData();
        boolean logged = !person.getName().equals("");

        //adciona os headers
        this._personRepository.saveUserData(person);

        if(!logged){
            this._priorityRepository.all(new APIListener<List<PriorityModel>>() {
                @Override
                public void onSuccess(List<PriorityModel> result) {
                    _priorityRepository.save(result);
                }

                @Override
                public void onFailure(String message) {

                }
            });
        }

        this._userLogged.setValue(logged);
    }
}
