package com.example.tasks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tasks.service.constants.TaskConstants;
import com.example.tasks.service.listener.APIListener;
import com.example.tasks.service.listener.FeedBack;
import com.example.tasks.service.model.TaskModel;
import com.example.tasks.service.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskListViewModel extends AndroidViewModel {

    private TaskRepository _taskRepository;
    private int _filter = 0;

    private final MutableLiveData<List<TaskModel>> _listTasks = new MutableLiveData<>();
    public final LiveData<List<TaskModel>> listTasks = this._listTasks;

    private final MutableLiveData<FeedBack> _feedBack = new MutableLiveData<>();
    public final LiveData<FeedBack> feedBack = this._feedBack;

    public TaskListViewModel(@NonNull Application application) {
        super(application);
        this._taskRepository = new TaskRepository(application);
    }

    public void list(int filter) {
        this._filter = filter;
        if (filter == TaskConstants.FILTER.NO_FILTER) {
            this._taskRepository.all(handleList());
        } else if (filter == TaskConstants.FILTER.NEXT_7_DAYS) {
            this._taskRepository.nextWeek(handleList());
        } else if (filter == TaskConstants.FILTER.OVERDUE) {
            this._taskRepository.overDue(handleList());
        }
    }

    private APIListener<List<TaskModel>> handleList() {
        return new APIListener<List<TaskModel>>() {
            @Override
            public void onSuccess(List<TaskModel> result) {
                _listTasks.setValue(result);
            }

            @Override
            public void onFailure(String message) {
                _listTasks.setValue(new ArrayList<TaskModel>());
                _feedBack.setValue(new FeedBack(message));
            }
        };
    }

    public void deleteTask(int id) {
        this._taskRepository.delete(id, new APIListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if(result) {
                    list(_filter);
                    _feedBack.setValue(new FeedBack());
                }
            }

            @Override
            public void onFailure(String message) {
                _feedBack.setValue(new FeedBack(message));
            }
        });
    }

    public void updateStatus(int id, boolean status) {
        APIListener<Boolean> listener = new APIListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if(result) {
                    list(_filter);
                }
            }

            @Override
            public void onFailure(String message) {
                _feedBack.setValue(new FeedBack(message));
            }
        };

        if(status){
            this._taskRepository.complete(id, listener);
        } else {
            this._taskRepository.undo(id, listener);
        }
    }

}