package com.example.tasks.service.model;

import com.google.gson.annotations.SerializedName;

public class TaskModel {

    @SerializedName("Id")
    private int id;

    @SerializedName("PriorityId")
    private int priorityId;

    @SerializedName("Description")
    private String description;

    @SerializedName("DueDate")
    private String dueDate;

    @SerializedName("Complete")
    private boolean complete;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
