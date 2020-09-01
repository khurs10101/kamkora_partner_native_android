package com.khurshid.kamkorapartner.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.khurshid.kamkorapartner.R;
import com.khurshid.kamkorapartner.api.ApiClient;
import com.khurshid.kamkorapartner.model.Partner;
import com.khurshid.kamkorapartner.model.SignInModel;
import com.khurshid.kamkorapartner.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MYTAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.et_login_username)
    EditText etUsername;
    @BindView(R.id.et_login_password)
    EditText etPassword;
    @BindView(R.id.tv_login_forgotpassword)
    TextView tvForgotPassword;
    @BindView(R.id.tv_login_login)
    TextView tvLogin;
    @BindView(R.id.tv_login_signup)
    TextView tvSignUp;

    private String stUsername, stPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvForgotPassword.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_login_login) {
            prepareParams();
        }

        if (v.getId() == R.id.tv_login_forgotpassword) {

        }

        if (v.getId() == R.id.tv_login_signup) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void prepareParams() {
        stUsername = etUsername.getText().toString();
        stPassword = etPassword.getText().toString();

        JSONObject object = new JSONObject();
        try {
            object.put("email", stUsername);
            object.put("password", stPassword);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(MYTAG, "JSONObject error: " + e.getMessage());
        }

        prepareNetworkCall(object);
    }

    private void prepareNetworkCall(JSONObject object) {
        Call<JsonObject> call = ApiClient
                .getInterface()
                .getSignIn((JsonObject) JsonParser.parseString(object.toString()));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    SignInModel signInModel = gson.fromJson(response.body(), SignInModel.class);
                    String token = signInModel.getToken();
                    Log.d(MYTAG, "SignIn token: " + token);
                    Partner partner = signInModel.getPartner();
                    Log.d(MYTAG, "SignIn partner Id: " + partner.getId());
                    SessionManager.startSession(LoginActivity.this, partner, token);

                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();

                }

                if (response.code() == 500) {
                    Log.d(MYTAG, "SignIn failed At server");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(MYTAG, "Internal Login Error: " + t.getMessage());
            }
        });
    }
}