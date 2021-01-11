package com.example.mobilereservation.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mobilereservation.R;

public class ConfirmationDialogFragment extends AppCompatDialogFragment {

    private TextView confirmation_title, confirmation_reason;
    private EditText confirmation_why;

    public ConfirmationDialogFragment(){
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ConfirmationDialogFragment newInstance(String title, String reason, boolean flag) {
        ConfirmationDialogFragment frag = new ConfirmationDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("reason", reason);
        args.putBoolean("flag", flag);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_confirmation, null);
        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reason = confirmation_reason.getText().toString();

                    }
                });
        confirmation_title = (TextView) view.findViewById(R.id.reason_title);
        confirmation_reason = (TextView) view.findViewById(R.id.confirmation_reason);
        confirmation_why = (EditText) view.findViewById(R.id.confirmation_why);

        confirmation_title.setText(getArguments().getString("title"));
        confirmation_reason.setText(getArguments().getString("reason"));
        if(getArguments().getBoolean("flag") == true){
            confirmation_why.setEnabled(getArguments().getBoolean("flag"));
            confirmation_why.setVisibility(View.GONE);
        }
        else{
            confirmation_reason.setEnabled(getArguments().getBoolean("flag"));
            confirmation_reason.setVisibility(View.GONE);
        }
        return builder.create();
    }


}
