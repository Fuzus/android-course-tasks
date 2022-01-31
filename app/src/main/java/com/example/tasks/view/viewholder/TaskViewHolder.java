package com.example.tasks.view.viewholder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasks.R;
import com.example.tasks.service.listener.TaskListener;
import com.example.tasks.service.model.TaskModel;
import com.example.tasks.service.repository.PriorityRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    private SimpleDateFormat _dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private TaskListener _listener;
    private PriorityRepository _priorityRepository;

    private ImageView _imageComplete = itemView.findViewById(R.id.image_complete);;
    private TextView _textDescription = itemView.findViewById(R.id.text_description);
    private TextView _textPriority = itemView.findViewById(R.id.text_priority);
    private TextView _textDueDate = itemView.findViewById(R.id.text_duedate);

    public TaskViewHolder(@NonNull View itemView, TaskListener listener) {
        super(itemView);
        this._listener = listener;
        this._priorityRepository = new PriorityRepository(itemView.getContext());
    }

    /**
     * Atribui valores aos elementos de interface e também eventos
     */
    public void bindData(final TaskModel task) {
        this._textDescription.setText(task.getDescription());

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(task.getDueDate());
            this._textDueDate.setText(this._dateFormat.format(date));
        } catch (ParseException e) {
            this._textDueDate.setText("--");
        }

        String priority = this._priorityRepository.getDescription(task.getPriorityId());
        this._textPriority.setText(priority);

        if(task.isComplete()) {
            this._imageComplete.setImageResource(R.drawable.ic_done);
        } else {
            this._imageComplete.setImageResource(R.drawable.ic_todo);
        }

        this._textDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _listener.onListClick(task.getId());
            }
        });

        this._textDescription.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle(R.string.remocao_de_tarefa)
                        .setMessage(R.string.remover_tarefa)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                _listener.onDeleteClick(task.getId());
                            }
                        })
                        .setNeutralButton(R.string.cancelar, null).show();
               return true;
            }
        });

        this._imageComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!task.isComplete()) {
                    _listener.onCompleteClick(task.getId());
                } else {
                    _listener.onUndoClick(task.getId());
                }
            }
        });
    }

}
