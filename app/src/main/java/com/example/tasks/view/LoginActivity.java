package com.example.tasks.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tasks.R;
import com.example.tasks.service.listener.FeedBack;
import com.example.tasks.viewmodel.LoginViewModel;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder _viewHolder = new ViewHolder();
    private LoginViewModel _loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this._viewHolder.editEmail = findViewById(R.id.edit_email);
        this._viewHolder.editPassword = findViewById(R.id.edit_password);
        this._viewHolder.buttonLogin = findViewById(R.id.button_login);
        this._viewHolder.textRegister = findViewById(R.id.text_register);

        // Incializa as variáveis
        this._loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        this.setListeners();

        // Cria observadores
        this.loadObservers();

        this._loginViewModel.checkFingerPrintRequirement();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.button_login){
            String email = this._viewHolder.editEmail.getText().toString();
            String password = this._viewHolder.editPassword.getText().toString();

            this._loginViewModel.login(email, password);
        } else if (id == R.id.text_register) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }
    }

    private void setListeners(){
        this._viewHolder.buttonLogin.setOnClickListener(this);
        this._viewHolder.textRegister.setOnClickListener(this);
    }

    private void loadObservers() {
        this._loginViewModel.login.observe(this, new Observer<FeedBack>() {
            @Override
            public void onChanged(FeedBack feedBack) {
                if(!feedBack.isSuccess()){
                    Toast.makeText(getApplicationContext(), feedBack.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.login_realizado_sucesso), Toast.LENGTH_SHORT).show();
                    startMain();
                }
            }
        });
        this._loginViewModel.userLogged.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean logged) {
                if(logged){
                    startMain();
                }
            }
        });
        this._loginViewModel.fingerPrintRequired.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isRequired) {
                if (isRequired){
                    openAuthentication();
                }
            }
        });
    }

    private void startMain(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openAuthentication() {
        //executor
        Executor executor = ContextCompat.getMainExecutor(this);

        //biometric prompt
        BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startMain();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        //biometric info
        BiometricPrompt.PromptInfo info = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Tasks")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(info);
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        EditText editEmail;
        EditText editPassword;
        Button buttonLogin;
        TextView textRegister;
    }

}