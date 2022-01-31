package com.example.tasks.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tasks.R;
import com.example.tasks.service.listener.FeedBack;
import com.example.tasks.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder _viewHolder = new ViewHolder();
    private RegisterViewModel _registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Botão de voltar nativo
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this._viewHolder.editName = findViewById(R.id.edit_name);
        this._viewHolder.editEmail = findViewById(R.id.edit_email);
        this._viewHolder.editPassword = findViewById(R.id.edit_password);
        this._viewHolder.buttonCreate = findViewById(R.id.button_create);

        this._viewHolder.buttonCreate.setOnClickListener(this);

        // Incializa variáveis
        this._registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Cria observadores
        this.loadObservers();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.button_create) {
            String name = this._viewHolder.editName.getText().toString();
            String email = this._viewHolder.editEmail.getText().toString();
            String password = this._viewHolder.editPassword.getText().toString();

            this._registerViewModel.create(name, email, password);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadObservers() {
        this._registerViewModel.feedBack.observe(this, new Observer<FeedBack>() {
            @Override
            public void onChanged(FeedBack feedBack) {
                if(!feedBack.isSuccess()){
                    Toast.makeText(getApplicationContext(), feedBack.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.usuario_criado_sucesso), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        EditText editName;
        EditText editEmail;
        EditText editPassword;
        Button buttonCreate;
    }

}