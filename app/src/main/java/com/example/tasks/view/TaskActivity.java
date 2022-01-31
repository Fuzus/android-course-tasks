package com.example.tasks.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tasks.R;
import com.example.tasks.service.constants.TaskConstants;
import com.example.tasks.service.listener.FeedBack;
import com.example.tasks.service.model.PriorityModel;
import com.example.tasks.service.model.TaskModel;
import com.example.tasks.viewmodel.TaskViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private SimpleDateFormat _format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private ViewHolder _viewHolder = new ViewHolder();
    private TaskViewModel _viewModel;
    private List<Integer> _listPriorityId = new ArrayList<>();
    private int _taskId = 0;

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
        this._viewModel.getList();

        this.loadDataFromActivity();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_date) {
            this.showDatePicker();
        }

        if (id == R.id.button_save) {
            this.handleSave();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);

        String date = this._format.format(c.getTime());
        this._viewHolder.buttonDate.setText(date);

    }

    private void loadDataFromActivity() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this._taskId = bundle.getInt(TaskConstants.BUNDLE.TASKID);
            this._viewModel.loadTask(this._taskId);
        }
    }

    private void createListeners() {
        this._viewHolder.buttonDate.setOnClickListener(this);
        this._viewHolder.buttonSave.setOnClickListener(this);
    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, this, year, month, day).show();
    }

    private void handleSave() {
        TaskModel task = new TaskModel();
        task.setId(this._taskId);
        task.setDescription(this._viewHolder.editDescription.getText().toString());
        task.setComplete(this._viewHolder.checkComplete.isChecked());
        task.setDueDate(this._viewHolder.buttonDate.getText().toString());
        task.setPriorityId(this._listPriorityId.get(this._viewHolder.spinnerPriority.getSelectedItemPosition()));

        this._viewModel.save(task);
    }

    /**
     * Observadores
     */
    private void loadObservers() {
        this._viewModel.priorityList.observe(this, new Observer<List<PriorityModel>>() {
            @Override
            public void onChanged(List<PriorityModel> priorityModels) {
                populateSpinner(priorityModels);
            }
        });

        this._viewModel.taskLoad.observe(this, new Observer<TaskModel>() {
            @Override
            public void onChanged(TaskModel taskModel) {
                _viewHolder.editDescription.setText(taskModel.getDescription());
                _viewHolder.checkComplete.setChecked(taskModel.isComplete());
                _viewHolder.spinnerPriority.setSelection(getSpinnerIndex(taskModel.getPriorityId()));

                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(taskModel.getDueDate());
                    _viewHolder.buttonDate.setText(_format.format(date));
                } catch (ParseException e) {
                    _viewHolder.buttonDate.setText("--");
                }

                _viewHolder.buttonSave.setText(getApplicationContext().getString(R.string.update_task));
            }
        });

        this._viewModel.feedback.observe(this, new Observer<FeedBack>() {
            @Override
            public void onChanged(FeedBack feedBack) {
                if (feedBack.isSuccess()) {
                    if (_taskId == 0) {
                        toast(getApplicationContext().getString(R.string.task_created));
                    } else {
                        toast(getApplicationContext().getString(R.string.task_updated));
                    }
                    finish();
                } else {
                    toast(feedBack.getMessage());
                }
            }
        });
    }

    private int getSpinnerIndex(int priorityId) {
        for (int i = 0; i < this._listPriorityId.size(); i++) {
            if (this._listPriorityId.get(i) == priorityId) {
                return i;
            }
        }
        return 0;
    }

    private void populateSpinner(List<PriorityModel> list) {

        List<String> priorityList = new ArrayList<>();
        for (PriorityModel p : list) {
            priorityList.add(p.getDescription());
            this._listPriorityId.add(p.getId());
        }

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, priorityList);
        this._viewHolder.spinnerPriority.setAdapter(adapter);


    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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