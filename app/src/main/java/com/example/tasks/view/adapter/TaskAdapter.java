package com.example.tasks.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasks.R;
import com.example.tasks.service.listener.TaskListener;
import com.example.tasks.service.model.TaskModel;
import com.example.tasks.view.viewholder.TaskViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

     private List<TaskModel> _list = new ArrayList<>();
    private TaskListener _listener;

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_list, parent, false);
        return new TaskViewHolder(item, _listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
         TaskModel taskModel = this._list.get(position);
         holder.bindData(taskModel);
    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    /**
     * Atribui o tratamento de eventos
     */
    public void attachListener(TaskListener listener) {
        this._listener = listener;
    }

    /**
     * Atualiza listagem
     */
    public void updateList(List<TaskModel> list) {
        this._list = list;
        notifyDataSetChanged();
    }

}
