package com.example.tasks.service.repository;

import android.content.Context;

import com.example.tasks.R;
import com.example.tasks.service.constants.TaskConstants;
import com.example.tasks.service.listener.APIListener;
import com.example.tasks.service.model.TaskModel;
import com.example.tasks.service.repository.remote.RetrofitClient;
import com.example.tasks.service.repository.remote.TaskService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskRepository extends BaseRepository {

    private TaskService _taskService;

    public TaskRepository(Context context) {
        super(context);
        this._taskService = RetrofitClient.createService(TaskService.class);
    }

    private void persist(Call<Boolean> call, final APIListener<Boolean> listener) {

        if (!super.isConnectionAvailable()) {
            listener.onFailure(_context.getString(R.string.ERROR_INTERNET_CONNECTION));
            return;
        }

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(handleFailure(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                listener.onFailure(_context.getString(R.string.ERROR_UNEXPECTED));
            }
        });
    }

    public void create(TaskModel task, final APIListener<Boolean> listener) {
        Call<Boolean> call = this._taskService.create(
                task.getPriorityId(),
                task.getDescription(),
                task.getDueDate(),
                task.isComplete()
        );
        
        this.persist(call, listener);

    }

    public void update(TaskModel task, final APIListener<Boolean> listener) {
        Call<Boolean> call = this._taskService.update(
                task.getId(),
                task.getPriorityId(),
                task.getDescription(),
                task.getDueDate(),
                task.isComplete()
        );

        this.persist(call, listener);
    }

    public void delete(int id, APIListener<Boolean> listener) {
        Call<Boolean> call = this._taskService.delete(id);
        this.persist(call, listener);
    }

    public void complete(int id, APIListener<Boolean> listener){
        Call<Boolean> call = this._taskService.complete(id);
        this.persist(call, listener);
    }

    public void undo(int id, APIListener<Boolean> listener) {
        Call<Boolean> call = this._taskService.undo(id);
        this.persist(call, listener);
    }

    private void list(Call<List<TaskModel>> call, final APIListener<List<TaskModel>> listener) {
        if (!super.isConnectionAvailable()) {
            listener.onFailure(_context.getString(R.string.ERROR_INTERNET_CONNECTION));
            return;
        }

        call.enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call, Response<List<TaskModel>> response) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(handleFailure(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<List<TaskModel>> call, Throwable t) {
                listener.onFailure(_context.getString(R.string.ERROR_UNEXPECTED));
            }
        });
    }

    public void all(final APIListener<List<TaskModel>> listener) {
        Call<List<TaskModel>> call = this._taskService.all();
        this.list(call, listener);
    }

    public void nextWeek(final APIListener<List<TaskModel>> listener) {
        Call<List<TaskModel>> call = this._taskService.nextWeek();
        this.list(call, listener);
    }

    public void overDue(final APIListener<List<TaskModel>> listener) {
        Call<List<TaskModel>> call = this._taskService.overdue();
        this.list(call, listener);
    }

    public void load(int id, final APIListener<TaskModel> listener) {

        if (!super.isConnectionAvailable()) {
            listener.onFailure(_context.getString(R.string.ERROR_INTERNET_CONNECTION));
            return;
        }

        Call<TaskModel> call = this._taskService.load(id);
        call.enqueue(new Callback<TaskModel>() {
            @Override
            public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(handleFailure(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<TaskModel> call, Throwable t) {
                listener.onFailure(_context.getString(R.string.ERROR_UNEXPECTED));
            }
        });
    }
}
