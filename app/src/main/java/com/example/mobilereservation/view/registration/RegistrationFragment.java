package com.example.mobilereservation.view.registration;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.model.User;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.user;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;
import com.example.mobilereservation.view.login.LoginFragment;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {

    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    private EditText firstnameTxt, lastnameTxt, numberTxt, passwordTxt, repasswordTxt;
    private Button register;
    private ImageView backImage;

    private String firstname, lastname, idNumber, password, repassword;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance() {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration, container, false);

        firstnameTxt = root.findViewById(R.id.registration_firstname);
        lastnameTxt = root.findViewById(R.id.registration_lastname);
        numberTxt = root.findViewById(R.id.registration_number);
        passwordTxt = root.findViewById(R.id.registration_password);
        repasswordTxt = root.findViewById(R.id.registration_re_password);
        register = root.findViewById(R.id.registration_register);
        backImage = root.findViewById(R.id.registration_back);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                firstname = firstnameTxt.getText().toString();
                lastname = lastnameTxt.getText().toString();
                idNumber = numberTxt.getText().toString();
                password = passwordTxt.getText().toString();
                repassword = repasswordTxt.getText().toString();

                String message = "";
                if(!(isStringValid(firstname) && isStringValid(lastname))){
                    message = "Your name contains non letter\n";
                }
                if(!isIDValid(idNumber)){
                    message += "Your ID must be Numbers only and 10 digits long\n";
                }
                if(!password.equals(repassword)){
                    message += "password does not match\n";
                }
                if(!isPasswordValid(password)){
                    message += "password must be greater than 8 characters long\n";
                }
                if(message.length() > 0){
                    flag = false;
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid Registration Credentials", message);
                    errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                }
                if(flag){
                    RegistrationAsyncTask asyncTask = new RegistrationAsyncTask(new User(idNumber, password, firstname, lastname));
                    asyncTask.execute();
                }

            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, loginFragment).commit();
            }
        });

        return root;
    }

    public boolean isStringValid(String word){
        if (word == null || word.length() == 0) {
            return false;
        }
        int len = word.length();
        for (int i = 0; i < len; i++) {
            if ((Character.isLetter(word.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        if(password.trim().length() <  8){
            return false;
        }
        return true;
    }

    public boolean isIDValid(String strNum) {
        if (strNum == null || !pattern.matcher(strNum).matches() || strNum.length() < 10) {
            return false;
        }
        return true;
    }

    private class RegistrationAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        private User user;

        RegistrationAsyncTask(User user){
            this.user = user;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            user api = ApiClient.getClient(getActivity()).create(user.class);
            Call<User> call = api.createUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if(!(response.code() == 201 || response.code() == 200)){
                        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", response.code()+" "+response.message());
                        errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                        LoginFragment loginFragment = new LoginFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, loginFragment).commit();
                    }
                    else{
                        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Successful", response.code()+"\nRegistration was successful");
                        errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                    }

                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
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