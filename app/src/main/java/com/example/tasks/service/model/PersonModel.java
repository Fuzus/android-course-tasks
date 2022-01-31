package com.example.tasks.service.model;

import com.google.gson.annotations.SerializedName;

public class PersonModel {

    @SerializedName("token")
    private String token;

    @SerializedName("personKey")
    private String personKey;

    @SerializedName("name")
    private String name;

    private String email;

    public String getToken() {
        return this.token;
    }

    public String getPersonKey() {
        return this.personKey;
    }

    public String getName() {
        return this.name;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPersonKey(String personKey) {
        this.personKey = personKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
