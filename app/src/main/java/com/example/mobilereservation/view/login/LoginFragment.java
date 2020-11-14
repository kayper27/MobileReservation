package com.example.mobilereservation.view.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;

import java.util.regex.Pattern;

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
}