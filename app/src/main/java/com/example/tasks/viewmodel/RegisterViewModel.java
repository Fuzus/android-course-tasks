package com.example.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tasks.service.listener.APIListener;
import com.example.tasks.service.listener.FeedBack;
import com.example.tasks.service.model.PersonModel;
import com.example.tasks.service.repository.PersonRepository;

public class RegisterViewModel extends AndroidViewModel {

    private PersonRepository _personRepository;

    private final MutableLiveData<FeedBack> _feedBack = new MutableLiveData<>();
    public final LiveData<FeedBack> feedBack = this._feedBack;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        this._personRepository = new PersonRepository(application);
    }

    public void create(String name, final String email, String password) {
        this._personRepository.create(name, email, password, new APIListener<PersonModel>() {
            @Override
            public void onSuccess(PersonModel result) {
                result.setEmail(email);
                _personRepository.saveUserData(result);

                _feedBack.setValue(new FeedBack());
            }

            @Override
            public void onFailure(String message) {
                _feedBack.setValue(new FeedBack(message));
            }
        });
    }
}
