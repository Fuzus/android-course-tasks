package com.example.tasks.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tasks.R;
import com.example.tasks.viewmodel.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener {

    private SimpleDateFormat _format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private ViewHolder _viewHolder = new ViewHolder();
    private TaskViewModel _viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // Botão de voltar nativo
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this._viewHolder.editDescription = findViewById(R.id.edit_description);
        this._viewHolder.spinnerPriority = findViewById(R.id.spinner_priority);
        this._viewHolder.checkComplete = findViewById(R.id.check_complete);
        this._viewHolder.buttonDate = findViewById(R.id.button_date);
        this._viewHolder.buttonSave = findViewById(R.id.button_save);

        // ViewModel
        this._viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        this.createListeners();

        // Cria observadores
        this.loadObservers();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_date){
            //TODO
        }

        if (id == R.id.button_save){
            //TODO
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createListeners() {
        this._viewHolder.buttonDate.setOnClickListener(this);
        this._viewHolder.buttonSave.setOnClickListener(this);
    }

    /**
     * Observadores
     */
    private void loadObservers() {
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        EditText editDescription;
        Spinner spinnerPriority;
        CheckBox checkComplete;
        Button buttonDate;
        Button buttonSave;
    }
}