package com.khurshid.kamkorapartner.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.khurshid.kamkorapartner.R;
import com.khurshid.kamkorapartner.api.ApiClient;
import com.khurshid.kamkorapartner.model.Order;
import com.khurshid.kamkorapartner.model.OrderStatusResponseModel;
import com.khurshid.kamkorapartner.utils.SessionManager;
import com.khurshid.kamkorapartner.view.activities.OrderSummaryActivity;
import com.khurshid.kamkorapartner.view.dialogs.DialogError;
import com.khurshid.kamkorapartner.view.dialogs.DialogSuccess;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.MyViewHolder> {

    private static final String MYTAG = OrderRecyclerViewAdapter.class.getSimpleName();
    private List<Order> orderList = new ArrayList<>();
    private Context context;
    private FragmentManager fragmentManager;

    public OrderRecyclerViewAdapter(Context context, FragmentManager fragmentManager, List<Order> orderList) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_item_order, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Order order = orderList.get(position);
        if (order.getDocketId() != null)
            holder.tvDocketId.setText("Docket ID: " + order.getDocketId());
        if (order.getSubServiceName() != null)
            holder.tvServiceId.setText(order.getSubServiceName());
        if (order.getStatus() != null)
            holder.tvStatus.setText(order.getStatus());

        if (order.getStatus().trim().equals("pending")) {
            Log.d(MYTAG, order.getStatus());
            holder.btReject.setVisibility(View.GONE);
//            holder.btReject.setText("Reject");
            holder.btAccept.setVisibility(View.VISIBLE);
            holder.btAccept.setBackgroundResource(R.drawable.rounded_green2);
            holder.btAccept.setText("View Details");
            holder.btAccept.setOnClickListener(v -> {
                Intent intent = new Intent(context, OrderSummaryActivity.class);
                intent.putExtra("orderObject", order);
                context.startActivity(intent);
            });
        }


        if (order.getStatus().trim().equals("cancelled")) {
            Log.d(MYTAG, order.getStatus());
            holder.btReject.setVisibility(View.VISIBLE);
            holder.btReject.setText(order.getStatus());
            holder.btAccept.setVisibility(View.GONE);
        }

        if (order.getStatus().trim().equals("accepted")) {
            Log.d(MYTAG, order.getStatus());
            holder.btAccept.setVisibility(View.VISIBLE);
            holder.btAccept.setText(order.getStatus());
            holder.btReject.setVisibility(View.GONE);
        }

        if (order.getStatus().trim().equals("completed")) {
            Log.d(MYTAG, order.getStatus());
            holder.btAccept.setVisibility(View.VISIBLE);
            holder.btAccept.setText(order.getStatus());
            holder.btReject.setVisibility(View.GONE);
        }

        if (holder.btAccept.getVisibility() == View.VISIBLE)
            holder.btAccept.setOnClickListener(v -> {
                Intent intent = new Intent(context, OrderSummaryActivity.class);
                intent.putExtra("orderObject", order);
                context.startActivity(intent);
//                prepareNetworkOrderAcceptCall(order, holder);
//            DialogSuccess dialogSuccess =
//                    DialogSuccess.newInstance("Accepted Order\nDocketID: " + order.getDocketId());
//            dialogSuccess.show(fragmentManager, "DialogSuccess");
            });

        if (holder.btReject.getVisibility() == View.VISIBLE)
            holder.btReject.setOnClickListener(v -> {
                Intent intent = new Intent(context, OrderSummaryActivity.class);
                intent.putExtra("orderObject", order);
                context.startActivity(intent);
//                prepareNetworkOrderRejectCall(order, holder);
//            DialogError dialogError = DialogError.newInstance("Order Rejected");
//            dialogError.show(fragmentManager, "DialogError");
            });
    }

    private void prepareNetworkOrderRejectCall(Order order, MyViewHolder holder) {

//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("orderId", order.getId());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderId", order.getId());
            jsonObject.put("partnerId", SessionManager.getLoggedInUserId(context));
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
                Log.d(MYTAG, "Response code: " + response.code());
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    OrderStatusResponseModel orderStatusResponseModel
                            = gson.fromJson(response.body(), OrderStatusResponseModel.class);

                    Log.d(MYTAG, orderStatusResponseModel.getStatus());

                    if (orderStatusResponseModel.getStatus().trim().equals("cancelled")) {
                        Log.d(MYTAG, orderStatusResponseModel.getStatus());
                        holder.btReject.setVisibility(View.VISIBLE);
                        holder.btReject.setText(orderStatusResponseModel.getStatus());
                        holder.btAccept.setVisibility(View.GONE);
                    }

                    if (orderStatusResponseModel.getStatus().trim().equals("accepted")) {
                        Log.d(MYTAG, orderStatusResponseModel.getStatus());
                        holder.btAccept.setVisibility(View.VISIBLE);
                        holder.btAccept.setText(orderStatusResponseModel.getStatus());
                        holder.btReject.setVisibility(View.GONE);
                    }

                    if (orderStatusResponseModel.getStatus().trim().equals("completed")) {
                        Log.d(MYTAG, orderStatusResponseModel.getStatus());
                        holder.btAccept.setVisibility(View.VISIBLE);
                        holder.btAccept.setText(orderStatusResponseModel.getStatus());
                        holder.btReject.setVisibility(View.GONE);
                    }

                    DialogSuccess
                            .newInstance(orderStatusResponseModel.getMessage())
                            .show(fragmentManager, "DialogSuccess");
                }

                if (response.code() == 404) {
                    DialogError
                            .newInstance("No such orders found")
                            .show(fragmentManager, "Dialog Error");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(MYTAG, "Retrofit error: " + t.getMessage());
            }
        });

    }

    private void prepareNetworkOrderAcceptCall(Order order, MyViewHolder holder) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("orderId", order.getId());
        jsonObject.addProperty("partnerId", SessionManager.getLoggedInUserId(context));
        Log.d(MYTAG, "OrderId is: " + order.getId());
        Call<JsonObject> call = ApiClient
                .getInterface()
                .acceptCurrentOrder(order.getId(), jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(MYTAG, "Response code: " + response.code());
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    OrderStatusResponseModel orderStatusResponseModel
                            = gson.fromJson(response.body(), OrderStatusResponseModel.class);


                    Log.d(MYTAG, orderStatusResponseModel.getStatus());
                    if (orderStatusResponseModel.getStatus().trim().equals("cancelled")) {
                        Log.d(MYTAG, orderStatusResponseModel.getStatus());
                        holder.btReject.setVisibility(View.VISIBLE);
                        holder.btReject.setText(orderStatusResponseModel.getStatus());
                        holder.btAccept.setVisibility(View.GONE);
                    }

                    if (orderStatusResponseModel.getStatus().trim().equals("accepted")) {
                        Log.d(MYTAG, orderStatusResponseModel.getStatus());
                        holder.btAccept.setVisibility(View.VISIBLE);
                        holder.btAccept.setText(orderStatusResponseModel.getStatus());
                        holder.btReject.setVisibility(View.GONE);
                    }

                    if (orderStatusResponseModel.getStatus().trim().equals("completed")) {
                        Log.d(MYTAG, orderStatusResponseModel.getStatus());
                        holder.btAccept.setVisibility(View.VISIBLE);
                        holder.btAccept.setText(orderStatusResponseModel.getStatus());
                        holder.btReject.setVisibility(View.GONE);
                    }

                    DialogSuccess
                            .newInstance(orderStatusResponseModel.getMessage())
                            .show(fragmentManager, "DialogSuccess");
                }

                if (response.code() == 404) {
                    DialogError
                            .newInstance("No such orders found")
                            .show(fragmentManager, "Dialog Error");
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(MYTAG, "Retrofit error: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tvDocketId, tvServiceId, tvStatus;
        Button btAccept, btReject;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDocketId = itemView.findViewById(R.id.tv_single_docket_id);
            tvServiceId = itemView.findViewById(R.id.tv_single_service_id);
            tvStatus = itemView.findViewById(R.id.tv_single_status_id);
            btAccept = itemView.findViewById(R.id.bt_order_accept);
            btReject = itemView.findViewById(R.id.bt_order_reject);
        }
    }
}
