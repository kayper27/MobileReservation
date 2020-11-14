package com.example.mobilereservation.view.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mobilereservation.MainActivity;
import com.example.mobilereservation.R;
import com.example.mobilereservation.model.LoggedInUser;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.user;
import com.example.mobilereservation.util.PrefUtils;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    private EditText username, password;
    private Button login;
    private TextView registration;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        username = (EditText) root.findViewById(R.id.username);
        password = (EditText) root.findViewById(R.id.password);
        login = (Button) root.findViewById(R.id.login);
        registration = (TextView) root.findViewById(R.id.register);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUserNameValid(username.getText().toString()) && isPasswordValid(password.getText().toString())){
                    LoginAsyncTask asyncTask = new LoginAsyncTask(username.getText().toString(), password.getText().toString());
                    asyncTask.execute();
                }
                else{
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", "Invalid login Credentials");
                    errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                }
            }
        });


        // Inflate the layout for this fragment
        return root;
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (!isNumeric(username)) {
            return false;
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        private String username, password;

        LoginAsyncTask(String username, String password){
            this.username = username;
            this.password = password;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            user api = ApiClient.getClient(getActivity()).create(user.class);
            Call<LoggedInUser> call = api.userLogin(username, password);
            call.enqueue(new Callback<LoggedInUser>() {
                @Override
                public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {

                    if(!(response.code() == 201 || response.code() == 200)){
                        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", response.code()+" "+response.message());
                        errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                    }
                    else if( response.body().getUserStatus().equals("deactivated")){
                        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", response.code()+" "+response.message());
                        errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                    }
                    else{
                        PrefUtils.storeApiKey(getActivity(), "Bearer "+response.body().getToken());
                        LoggedInUser user = new LoggedInUser(response.body().getAccount_id(), response.body().getUserStatus(), response.body().getAccount_type(), "Bearer "+response.body().getToken());
                        Toast.makeText(getActivity().getApplicationContext(), "Welcome! "+response.body().getAccount_id(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
                        startActivity(intent);
                    }

                }
                @Override
                public void onFailure(Call<LoggedInUser> call, Throwable t) {
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", t.getCause()+"\n=========\n"+t.getMessage());
                    errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                }

            });

            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), "Processing", "Validating Request");
        }
    }
}