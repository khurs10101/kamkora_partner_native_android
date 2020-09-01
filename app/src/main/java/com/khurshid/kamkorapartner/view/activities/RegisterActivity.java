package com.khurshid.kamkorapartner.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.khurshid.kamkorapartner.R;
import com.khurshid.kamkorapartner.api.ApiClient;
import com.khurshid.kamkorapartner.model.Partner;
import com.khurshid.kamkorapartner.model.SignUpModel;
import com.khurshid.kamkorapartner.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String MYTAG = RegisterActivity.class.getSimpleName();
    @BindView(R.id.et_reg_name)
    EditText etName;
    @BindView(R.id.et_reg_email)
    EditText etEmail;
    @BindView(R.id.et_reg_mobile)
    EditText etPhone;
    @BindView(R.id.et_reg_password)
    EditText etPassword;
    @BindView(R.id.et_reg_repassword)
    EditText etRePassword;
    @BindView(R.id.tv_reg_signup)
    TextView tvSignUp;
    @BindView(R.id.sp_register_gender)
    Spinner spGender;
    @BindView(R.id.sp_register_profession)
    Spinner spProfession;
    @BindView(R.id.sp_area_code)
    Spinner spAreaCode;
    @BindView(R.id.sp_register_city)
    Spinner spCity;
    ImageView ivAddressProofPreview;
    private ArrayAdapter<String> genderArrayAdapter, professionArrayAdapter, areaCodeArrayAdapter, cityArrayAdapter;
    private String spaGender[], spaProfession[], spaAreaCode[], spaCity[];
    private String stName, stEmail, stPhone, stPassword, stRePassword, stGender, stProfession, stCity, stAreaCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        spaAreaCode = getResources().getStringArray(R.array.sa_country_code);
        spaCity = getResources().getStringArray(R.array.sa_city);
        spaGender = getResources().getStringArray(R.array.sa_gender);
        spaProfession = getResources().getStringArray(R.array.sa_profession);
        areaCodeArrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, spaAreaCode);
        genderArrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, spaGender);
        professionArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, spaProfession);
        cityArrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, spaCity);
        spAreaCode.setAdapter(areaCodeArrayAdapter);
        spGender.setAdapter(genderArrayAdapter);
        spProfession.setAdapter(professionArrayAdapter);
        spCity.setAdapter(cityArrayAdapter);
        spAreaCode.setOnItemSelectedListener(this);
        spGender.setOnItemSelectedListener(this);
        spProfession.setOnItemSelectedListener(this);
        spCity.setOnItemSelectedListener(this);
        tvSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tv_reg_signup) {
            prepareParams();
        }
    }

    private void prepareParams() {
        stName = etName.getText().toString();
        stEmail = etEmail.getText().toString();
        stPhone = etPhone.getText().toString();
        stPassword = etPassword.getText().toString();
        stRePassword = etRePassword.getText().toString();

        JSONObject object = new JSONObject();
        try {
            object.put("name", stName);
            object.put("email", stEmail);
            object.put("phone", stPhone);
            object.put("password", stPassword);
            object.put("gender", stGender);
            object.put("isAccountVerified", false);
            object.put("profession", stProfession);
            object.put("city", stCity);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(MYTAG, "JSON error: " + e.getMessage());
        }

        prepareNetworkCall(object);
    }

    private void prepareNetworkCall(JSONObject object) {
        Call<JsonObject> call = ApiClient
                .getInterface()
                .getSignUp((JsonObject) JsonParser.parseString(object.toString()));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 201) {
                    Gson gson = new Gson();
                    SignUpModel signUpModel = gson.fromJson(response.body(), SignUpModel.class);
                    String token = signUpModel.getToken();
                    Log.d(MYTAG, "token is: " + token);
                    Partner partner = signUpModel.getPartner();
                    Log.d(MYTAG, "Partner Id is: " + partner.getId());
                    SessionManager.startSession(RegisterActivity.this, partner, token);

                    Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();

                }

                if (response.code() == 500) {
                    Log.e(MYTAG, "SignUp Server Error:");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(MYTAG, "Internal SignUp error: " + t.getMessage());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (view.getId() == R.id.sp_area_code) {
            stAreaCode = spaAreaCode[position];
        }

        if (view.getId() == R.id.sp_register_gender) {
            stGender = spaGender[position];
        }

        if (view.getId() == R.id.sp_register_profession) {
            stProfession = spaProfession[position];
        }

        if (view.getId() == R.id.sp_register_city) {
            stCity = spaCity[position];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}