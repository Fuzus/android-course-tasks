package com.example.tasks.viewmodel;

import android.app.Application;
import android.app.Person;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tasks.service.model.PersonModel;
import com.example.tasks.service.repository.PersonRepository;

public class MainViewModel extends AndroidViewModel {

    private PersonRepository _personRepository;

    private final MutableLiveData<PersonModel> _userData = new MutableLiveData<>();
    public final LiveData<PersonModel> userData = this._userData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this._personRepository = new PersonRepository(application);
    }

    public void logout() {
        this._personRepository.clearUserData();
    }

    public void loadUserData() {
        this._userData.setValue(this._personRepository.getUserData());
    }
}
