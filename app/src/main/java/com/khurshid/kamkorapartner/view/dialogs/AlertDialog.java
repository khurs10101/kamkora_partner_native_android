package com.khurshid.kamkorapartner.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AlertDialog extends DialogFragment {

    public static AlertDialog newInstance(String title, String message) {
        AlertDialog alertDialog = new AlertDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        alertDialog.setArguments(args);
        return alertDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        return null;
    }
}
