package com.example.tasks.service.listener;

public class FeedBack {
    private boolean success = true;
    private String message = "";

    public FeedBack() {}

    public FeedBack(String message){
        this.success = false;
        this.message = message;
    }

    public boolean isSuccess(){
        return this.success;
    }

    public String getMessage(){
        return this.message;
    }
}
