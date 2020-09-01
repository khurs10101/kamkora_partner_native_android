package com.khurshid.kamkorapartner.view.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.khurshid.kamkorapartner.R;


public class DialogSuccess extends DialogFragment implements View.OnClickListener {

    private static DialogSuccess dialogSuccess = null;
    private TextView tvMessage;
    private Button btClose;
    private String message;

    public DialogSuccess() {
    }

    public static DialogSuccess newInstance(String message) {
        if (dialogSuccess == null) {
            dialogSuccess = new DialogSuccess();
        }
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        dialogSuccess.setArguments(bundle);
        return dialogSuccess;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_success, container, false);
        tvMessage = view.findViewById(R.id.tv_dialog_info_message);
        btClose = view.findViewById(R.id.bt_dialog_info_close);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setCanceledOnTouchOutside(false);
        message = getArguments().getString("message");
        getDialog().setTitle("Information");
        tvMessage.setText(message);
        btClose.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_dialog_info_close) {
            getDialog().dismiss();
        }
    }
}
