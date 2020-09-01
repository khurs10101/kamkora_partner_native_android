package com.khurshid.kamkorapartner.view.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.gson.JsonObject;
import com.khurshid.kamkorapartner.R;
import com.khurshid.kamkorapartner.api.ApiClient;
import com.khurshid.kamkorapartner.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogUpdateText extends DialogFragment implements View.OnClickListener {

    private static final String MYTAG = DialogUpdateText.class.getSimpleName();
    private static DialogUpdateText dialogUpdateText = null;
    private TextView tvMessage;
    private EditText etField;
    private Button btOk, btCancel;
    private int type;

    public static DialogUpdateText newInstance(String message, int type) {
        if (dialogUpdateText == null)
            dialogUpdateText = new DialogUpdateText();

        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putInt("type", type);
        dialogUpdateText.setArguments(bundle);
        return dialogUpdateText;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_update_text, container, false);
        tvMessage = view.findViewById(R.id.tv_dialog_update_message);
        etField = view.findViewById(R.id.et_dialog_update_field);
        btOk = view.findViewById(R.id.bt_dialog_update_ok);
        btCancel = view.findViewById(R.id.bt_dialog_update_close);
        btOk.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String message = getArguments().getString("message");
        type = getArguments().getInt("type");

        if (type == 1) {
            tvMessage.setText("Update Name");
            etField.setHint("Enter name");
            etField.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        if (type == 2) {
            tvMessage.setText("Update phone");
            etField.setHint("Enter phone");
            etField.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        if (type == 3) {
            tvMessage.setText("Update Age");
        }

        super.onViewCreated(view, savedInstanceState);
        etField.setHint("Enter Age");
        etField.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_dialog_update_ok) {

            if (type == 2) {

            } else {
                if (etField.getText().toString().length() != 0) {
                    String name = etField.getText().toString();
                    prepareNetworkCall(name);
                }
            }

        }

        if (v.getId() == R.id.bt_dialog_update_close) {

        }
    }

    private void prepareNetworkCall(String name) {

        JsonObject object = new JsonObject();

        if (type == 1) {
            object.addProperty("name", name);
        }

        if (type == 3) {
            object.addProperty("age", name);
        }

        Call<JsonObject> call = ApiClient
                .getInterface()
                .updatePartnersDetails(SessionManager.getLoggedInUserId(getActivity()), object);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    sendDataToFragment(name);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(MYTAG, "Error retrofit " + t.getMessage());
            }
        });

    }

    private void sendDataToFragment(String name) {
        if (getTargetFragment() == null) {
            Log.d(MYTAG, "Target fragment is empty");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("name", name);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();
    }
}
