package com.khurshid.kamkorapartner.view.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.khurshid.kamkorapartner.R;
import com.khurshid.kamkorapartner.view.activities.DashboardActivity;

public class DialogGoToHome extends DialogFragment {
    private static DialogGoToHome dialogGoToHome = null;
    private TextView tvMessage;
    private Button btOk;
    private ImageView ivIcon;

    public DialogGoToHome() {
    }

    public static DialogGoToHome newInstance(String message, int status) {
        if (dialogGoToHome == null)
            dialogGoToHome = new DialogGoToHome();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putInt("status", status);
        dialogGoToHome.setArguments(bundle);
        return dialogGoToHome;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_go_to_home, container, false);
        tvMessage = view.findViewById(R.id.tv_dialog_home_message);
        btOk = view.findViewById(R.id.bt_dialog_home_close);
        ivIcon = view.findViewById(R.id.iv_dialog_go_home_icon);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setCanceledOnTouchOutside(false);
        int status = getArguments().getInt("status");
        if (status == 1) {
            ivIcon.setImageResource(R.drawable.ic_tick);
        }

        if (status == 2) {
            ivIcon.setImageResource(R.drawable.ic_close);
        }
        tvMessage.setText(getArguments().getString("message"));
        btOk.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DashboardActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }
}
