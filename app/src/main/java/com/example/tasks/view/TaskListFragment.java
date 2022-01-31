package com.example.tasks.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasks.R;
import com.example.tasks.service.constants.TaskConstants;
import com.example.tasks.service.listener.FeedBack;
import com.example.tasks.service.listener.TaskListener;
import com.example.tasks.service.model.TaskModel;
import com.example.tasks.view.adapter.TaskAdapter;
import com.example.tasks.viewmodel.TaskListViewModel;

import java.util.List;

public class TaskListFragment extends Fragment {

    private TaskAdapter _adapter = new TaskAdapter();
    private TaskListViewModel _viewModel;
    private TaskListener _listener;
    private int _filter = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
        this._viewModel = new ViewModelProvider(this).get(TaskListViewModel.class);

        // Lista de tarefas
        RecyclerView recycler = root.findViewById(R.id.recycler_task_list);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(this._adapter);

        this._listener = new TaskListener() {
            @Override
            public void onListClick(int id) {
                Intent intent = new Intent(getContext(), TaskActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(TaskConstants.BUNDLE.TASKID, id);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int id) {
                _viewModel.deleteTask(id);
            }

            @Override
            public void onCompleteClick(int id) {
                _viewModel.updateStatus(id, true);
            }

            @Override
            public void onUndoClick(int id) {
                _viewModel.updateStatus(id, false);
            }
        };
        this._adapter.attachListener(this._listener);

        // Cria os observadores
        this.loadObservers();

        Bundle bundle = getArguments();
        if(bundle != null) {
            this._filter = bundle.getInt(TaskConstants.FILTER.KEY);
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this._viewModel.list(this._filter);
    }

    private void loadObservers() {
        this._viewModel.listTasks.observe(getViewLifecycleOwner(), new Observer<List<TaskModel>>() {
            @Override
            public void onChanged(List<TaskModel> taskModels) {
                _adapter.updateList(taskModels);
            }
        });

        this._viewModel.feedBack.observe(getViewLifecycleOwner(), new Observer<FeedBack>() {
            @Override
            public void onChanged(FeedBack feedBack) {
                if (!feedBack.isSuccess()){
                    toast(feedBack.getMessage());
                } else {
                    toast(getString(R.string.task_removed));
                }
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}