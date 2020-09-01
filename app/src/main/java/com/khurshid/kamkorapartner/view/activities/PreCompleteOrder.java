package com.khurshid.kamkorapartner.view.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.JsonObject;
import com.khurshid.kamkorapartner.R;
import com.khurshid.kamkorapartner.api.ApiClient;
import com.khurshid.kamkorapartner.model.Order;
import com.khurshid.kamkorapartner.utils.ProgressBarManager;
import com.khurshid.kamkorapartner.utils.SessionManager;
import com.khurshid.kamkorapartner.view.dialogs.DialogGoToHome;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreCompleteOrder extends AppCompatActivity implements View.OnClickListener {

    private static final String MYTAG = PreCompleteOrder.class.getSimpleName();
    @BindView(R.id.bt_pre_complete_order_complete)
    Button btComplete;
    @BindView(R.id.pbPreComplete)
    ProgressBar pb;
    private FragmentManager fragmentManager;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_complete_order);
        ButterKnife.bind(this);
        btComplete.setOnClickListener(this);
        order = (Order) getIntent().getSerializableExtra("orderObject");
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_pre_complete_order_complete) {
            if (order != null)
                prepareNetworkCall();
        }
    }

    private void prepareNetworkCall() {
        ProgressBarManager.startProgressBar(pb);
        btComplete.setVisibility(View.GONE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("orderId", order.getId());
        jsonObject.addProperty("partnerId", SessionManager.getLoggedInUserId(this));

        Call<JsonObject> call = ApiClient
                .getInterface()
                .completeCurrentOrder(order.getId(), jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ProgressBarManager.stopProgressBar(pb);
                btComplete.setVisibility(View.VISIBLE);
                if (response.code() == 200) {
                    DialogGoToHome
                            .newInstance("Order completed", 1)
                            .show(fragmentManager, "homeDialog");
                }

                if (response.code() == 404) {
                    DialogGoToHome
                            .newInstance("Order no longer exists", 2)
                            .show(fragmentManager, "homeDialog");
                }

                if (response.code() == 500) {
                    DialogGoToHome
                            .newInstance("Server error", 2)
                            .show(fragmentManager, "homeDialog");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ProgressBarManager.stopProgressBar(pb);
                btComplete.setVisibility(View.VISIBLE);
                Log.d(MYTAG, "Error: " + t.getMessage());
                DialogGoToHome
                        .newInstance("Fatal error", 2)
                        .show(fragmentManager, "homeDialog");
            }
        });

    }
}