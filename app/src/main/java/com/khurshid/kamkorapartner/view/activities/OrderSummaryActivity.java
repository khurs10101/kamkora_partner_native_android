package com.khurshid.kamkorapartner.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.khurshid.kamkorapartner.R;
import com.khurshid.kamkorapartner.api.ApiClient;
import com.khurshid.kamkorapartner.model.Order;
import com.khurshid.kamkorapartner.model.OrderStatusResponseModel;
import com.khurshid.kamkorapartner.utils.ProgressBarManager;
import com.khurshid.kamkorapartner.utils.SessionManager;
import com.khurshid.kamkorapartner.view.dialogs.DialogGoToHome;
import com.khurshid.kamkorapartner.view.dialogs.DialogSuccess;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSummaryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MYTAG = OrderSummaryActivity.class.getSimpleName();

    @BindView(R.id.bt_order_summary_accept)
    Button btAccept;
    @BindView(R.id.bt_order_summary_reject)
    Button btReject;
    @BindView(R.id.bt_order_summary_complete)
    Button btComplete;
    @BindView(R.id.tv_order_summary_order_info)
    TextView tvServiceInfo;
    @BindView(R.id.tv_order_summary_customer_details)
    TextView tvCustomerInfo;
    @BindView(R.id.tv_order_summary_charge)
    TextView tvCharge;
    @BindView(R.id.pbOrderSummary)
    ProgressBar pb;
    private Order order;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        ButterKnife.bind(this);
        initView();
        order = (Order) getIntent().getSerializableExtra("orderObject");
        if (order != null) {

            tvServiceInfo.setText("Service: " + order.getSubServiceName() + "\n"
                    + "Docket ID: " + order.getDocketId());

            tvCharge.setText("Rs. " + order.getRate() + " per hour");

            if (order.getStatus().trim().equals("pending")) {
                btAccept.setVisibility(View.VISIBLE);
                btReject.setVisibility(View.VISIBLE);
                btComplete.setVisibility(View.GONE);
                btAccept.setOnClickListener(this);
                btReject.setOnClickListener(this);

            }


            if (order.getStatus().trim().equals("cancelled")) {
                btAccept.setVisibility(View.GONE);
                btReject.setVisibility(View.VISIBLE);
                btReject.setText("Order cancelled");
                btComplete.setVisibility(View.GONE);
                btReject.setOnClickListener(this);
            }

            if (order.getStatus().trim().equals("accepted")) {
                btAccept.setVisibility(View.VISIBLE);
                btAccept.setText("Order accepted");
                btReject.setVisibility(View.GONE);
                btComplete.setVisibility(View.VISIBLE);
                btAccept.setOnClickListener(this);
                btComplete.setOnClickListener(this);
            }

            if (order.getStatus().trim().equals("completed")) {
                btAccept.setVisibility(View.GONE);
                btComplete.setVisibility(View.VISIBLE);
                btComplete.setText("Order Completed");
                btReject.setVisibility(View.GONE);
            }

        }
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
//        btAccept.setOnClickListener(this);
//        btReject.setOnClickListener(this);
//        btComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.bt_order_summary_accept) {
            prepareNetworkOrderAcceptCall(order);
        }

        if (v.getId() == R.id.bt_order_summary_reject) {
            prepareNetworkOrderRejectCall(order);
        }

        if (v.getId() == R.id.bt_order_summary_complete) {
            Intent intent = new Intent(this, PreCompleteOrder.class);
            intent.putExtra("orderObject", order);
            startActivity(intent);
        }
    }

    private void prepareNetworkOrderAcceptCall(Order order) {
        ProgressBarManager.startProgressBar(pb);
        btAccept.setVisibility(View.GONE);
        btComplete.setVisibility(View.GONE);
        btReject.setVisibility(View.GONE);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("orderId", order.getId());
        jsonObject.addProperty("partnerId", SessionManager.getLoggedInUserId(this));
        Log.d(MYTAG, "OrderId is: " + order.getId());
        Call<JsonObject> call = ApiClient
                .getInterface()
                .acceptCurrentOrder(order.getId(), jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ProgressBarManager.stopProgressBar(pb);
                Log.d(MYTAG, "Response code: " + response.code());
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    OrderStatusResponseModel orderStatusResponseModel
                            = gson.fromJson(response.body(), OrderStatusResponseModel.class);


                    Log.d(MYTAG, orderStatusResponseModel.getStatus());

                    if (order.getStatus().trim().equals("pending")) {
                        Log.d(MYTAG, "Trigger: " + orderStatusResponseModel.getStatus());
                        btAccept.setVisibility(View.VISIBLE);
                        btReject.setVisibility(View.VISIBLE);
                        btComplete.setVisibility(View.GONE);
                        btAccept.setOnClickListener(OrderSummaryActivity.this);
                        btReject.setOnClickListener(OrderSummaryActivity.this);

                    }


                    if (order.getStatus().trim().equals("cancelled")) {
                        Log.d(MYTAG, "Trigger: " + orderStatusResponseModel.getStatus());
                        btAccept.setVisibility(View.GONE);
                        btReject.setVisibility(View.VISIBLE);
                        btComplete.setVisibility(View.GONE);
                        btReject.setOnClickListener(OrderSummaryActivity.this);
                    }

                    if (order.getStatus().trim().equals("accepted")) {
                        Log.d(MYTAG, "Trigger: " + orderStatusResponseModel.getStatus());
                        btAccept.setVisibility(View.VISIBLE);
                        btReject.setVisibility(View.GONE);
                        btComplete.setVisibility(View.VISIBLE);
                        btAccept.setOnClickListener(OrderSummaryActivity.this);
                        btComplete.setOnClickListener(OrderSummaryActivity.this);
                    }

                    if (order.getStatus().trim().equals("completed")) {
                        Log.d(MYTAG, "Trigger: " + orderStatusResponseModel.getStatus());
                        btAccept.setVisibility(View.GONE);
                        btComplete.setVisibility(View.VISIBLE);
                        btComplete.setText("Order Completed");
                        btReject.setVisibility(View.GONE);
                    }


                    DialogSuccess
                            .newInstance(orderStatusResponseModel.getMessage())
                            .show(fragmentManager, "DialogSuccess");
                }

                if (response.code() == 404) {
                    DialogGoToHome
                            .newInstance("Fatal error", 2)
                            .show(fragmentManager, "homeDialog");
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ProgressBarManager.stopProgressBar(pb);
                Log.d(MYTAG, "Retrofit error: " + t.getMessage());
                DialogGoToHome
                        .newInstance("Fatal error", 2)
                        .show(fragmentManager, "homeDialog");
            }
        });
    }


    private void prepareNetworkOrderRejectCall(Order order) {
        ProgressBarManager.startProgressBar(pb);
        btAccept.setVisibility(View.GONE);
        btComplete.setVisibility(View.GONE);
        btReject.setVisibility(View.GONE);

//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("orderId", order.getId());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderId", order.getId());
            jsonObject.put("partnerId", SessionManager.getLoggedInUserId(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(MYTAG, "OrderId is: " + order.getId());
        Call<JsonObject> call = ApiClient
                .getInterface()
                .rejectCurrentOrder(order.getId(), (JsonObject) JsonParser.parseString(jsonObject.toString()));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ProgressBarManager.stopProgressBar(pb);
                Log.d(MYTAG, "Response code: " + response.code());
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    OrderStatusResponseModel orderStatusResponseModel
                            = gson.fromJson(response.body(), OrderStatusResponseModel.class);

                    Log.d(MYTAG, orderStatusResponseModel.getStatus());
                    if (order.getStatus().trim().equals("pending")) {
                        btAccept.setVisibility(View.VISIBLE);
                        btReject.setVisibility(View.VISIBLE);
                        btComplete.setVisibility(View.GONE);
                        btAccept.setOnClickListener(OrderSummaryActivity.this);
                        btReject.setOnClickListener(OrderSummaryActivity.this);

                    }


                    if (order.getStatus().trim().equals("cancelled")) {
                        btAccept.setVisibility(View.GONE);
                        btReject.setVisibility(View.VISIBLE);
                        btComplete.setVisibility(View.GONE);
                        btReject.setOnClickListener(OrderSummaryActivity.this);
                    }

                    if (order.getStatus().trim().equals("accepted")) {
                        btAccept.setVisibility(View.VISIBLE);
                        btReject.setVisibility(View.GONE);
                        btComplete.setVisibility(View.VISIBLE);
                        btAccept.setOnClickListener(OrderSummaryActivity.this);
                        btComplete.setOnClickListener(OrderSummaryActivity.this);
                    }

                    if (order.getStatus().trim().equals("completed")) {
                        btAccept.setVisibility(View.GONE);
                        btComplete.setVisibility(View.VISIBLE);
                        btComplete.setText("Order Completed");
                        btReject.setVisibility(View.GONE);
                    }


                    DialogSuccess
                            .newInstance(orderStatusResponseModel.getMessage())
                            .show(fragmentManager, "DialogSuccess");
                }

                if (response.code() == 404) {
                    DialogGoToHome
                            .newInstance("Not found", 2)
                            .show(fragmentManager, "homeDialog");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ProgressBarManager.stopProgressBar(pb);
                Log.d(MYTAG, "Retrofit error: " + t.getMessage());
                DialogGoToHome
                        .newInstance("Fatal error", 2)
                        .show(fragmentManager, "homeDialog");
            }
        });

    }
}