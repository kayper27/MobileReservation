package com.example.mobilereservation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mobilereservation.model.LoggedInUser;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.logs;
import com.example.mobilereservation.util.PrefUtils;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView name, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        LogsAsyncTask asyncTask = new LogsAsyncTask(new LoggedInUser(PrefUtils.getUserLogID(getApplicationContext()), PrefUtils.getUserLogType(getApplicationContext()), "", "",""), "login");
        asyncTask.execute();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navigationView.getMenu().clear();
        switch (PrefUtils.getUserLogType(getApplicationContext())) {

            case "student":
            case "professor":
                navigationView.inflateMenu(R.menu.client_drawer);
                break;

            case "facilitator":
            case "administrator":
                navigationView.inflateMenu(R.menu.management_drawer);
                break;
            default:
                break;
        }
        name = (TextView) headerView.findViewById(R.id.nav_header_name);
        username = (TextView) headerView.findViewById(R.id.nav_header_username);

        username.setText(PrefUtils.getUserLogID(getApplicationContext()));
        name.setText(PrefUtils.getUserName(getApplicationContext()));

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_facility, R.id.nav_equipment, R.id.nav_request, R.id.nav_reservation, R.id.nav_schedule, R.id.nav_toReturn, R.id.nav_pending)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                LogsAsyncTask asyncTask = new LogsAsyncTask(new LoggedInUser(PrefUtils.getUserLogID(getApplicationContext()), PrefUtils.getUserLogType(getApplicationContext()), "", "",""), "logout");
                asyncTask.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private class LogsAsyncTask extends AsyncTask<Void, Void, Void> {

        private LoggedInUser logUser;


        LogsAsyncTask(LoggedInUser logUser, String details) {
            this.logUser = logUser;
            logUser.setDetails(details);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            logs api = ApiClient.getClient(getApplicationContext()).create(logs.class);
            Call<LoggedInUser> call = api.createLogs(logUser);
            call.enqueue(new Callback<LoggedInUser>() {
                @Override
                public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                    if (logUser.getDetails().equals("logout")) {
                        LoggedInUser user = null;
                        PrefUtils.storeUserLogType(getApplicationContext(), "");
                        PrefUtils.storeUserLogID(getApplicationContext(), "");
                        PrefUtils.storeApiKey(getApplicationContext(), "");

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<LoggedInUser> call, Throwable t) {
                }

            });

            return null;
        }
    }
}