package com.example.tasks.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.tasks.R;
import com.example.tasks.service.model.PersonModel;
import com.example.tasks.viewmodel.MainViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration _appBarConfiguration;
    private MainViewModel _mainViewModel;
    private NavigationView _navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ViewModel
        this._mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // Nova tarefa
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TaskActivity.class));
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        this._navigationView = findViewById(R.id.nav_view);

        _appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_all_tasks, R.id.nav_overdue, R.id.nav_next_tasks)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, _appBarConfiguration);
        NavigationUI.setupWithNavController(this._navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.nav_logout) {
                    handleLogout();
                }
            }
        });

        // Cria observadores
        this.loadObservers();
        this._mainViewModel.loadUserData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_logout) {
            handleLogout();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, _appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void handleLogout() {
        this._mainViewModel.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /**
     * Dados mutáveis
     */
    private void loadObservers() {
        this._mainViewModel.userData.observe(this, new Observer<PersonModel>() {
            @Override
            public void onChanged(PersonModel personModel) {
                View headerView = _navigationView.getHeaderView(0);
                TextView textName = headerView.findViewById(R.id.text_name);
                TextView textEmail = headerView.findViewById(R.id.text_email);
                textName.setText(personModel.getName());
                textEmail.setText(personModel.getEmail());
            }
        });
    }
}