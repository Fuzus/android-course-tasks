package com.example.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tasks.service.listener.APIListener;
import com.example.tasks.service.listener.FeedBack;
import com.example.tasks.service.model.PriorityModel;
import com.example.tasks.service.model.TaskModel;
import com.example.tasks.service.repository.PriorityRepository;
import com.example.tasks.service.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private PriorityRepository _priorityRepository;
    private TaskRepository _repository;

    private final MutableLiveData<List<PriorityModel>> _priorityList = new MutableLiveData<>();
    public final LiveData<List<PriorityModel>> priorityList = this._priorityList;

    private final MutableLiveData<TaskModel> _taskLoad = new MutableLiveData<>();
    public final LiveData<TaskModel> taskLoad = this._taskLoad;

    private final MutableLiveData<FeedBack> _feedback = new MutableLiveData<>();
    public final LiveData<FeedBack> feedback = this._feedback;


    public TaskViewModel(@NonNull Application application) {
        super(application);
        this._priorityRepository = new PriorityRepository(application);
        this._repository = new TaskRepository(application);
    }

    public void getList() {
        List<PriorityModel> list = this._priorityRepository.getList();
        this._priorityList.setValue(list);
    }


    public void save(TaskModel task) {
        APIListener<Boolean> listener = new APIListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                _feedback.setValue(new FeedBack());
            }

            @Override
            public void onFailure(String message) {
                _feedback.setValue(new FeedBack(message));
            }
        };

        if (task.getId() == 0) {
            this._repository.create(task, listener);
        } else {
            this._repository.update(task, listener);
        }
    }

    public void loadTask(int id) {
        this._repository.load(id, new APIListener<TaskModel>() {
            @Override
            public void onSuccess(TaskModel result) {
                _taskLoad.setValue(result);
            }

            @Override
            public void onFailure(String message) {
                _feedback.setValue(new FeedBack(message));
            }
        });
    }
}
