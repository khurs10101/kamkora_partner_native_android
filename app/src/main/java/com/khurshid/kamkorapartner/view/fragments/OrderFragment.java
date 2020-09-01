package com.khurshid.kamkorapartner.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.khurshid.kamkorapartner.R;
import com.khurshid.kamkorapartner.api.ApiClient;
import com.khurshid.kamkorapartner.model.Order;
import com.khurshid.kamkorapartner.model.OrderResponseModel;
import com.khurshid.kamkorapartner.model.Partner;
import com.khurshid.kamkorapartner.utils.ProgressBarManager;
import com.khurshid.kamkorapartner.utils.SessionManager;
import com.khurshid.kamkorapartner.view.adapters.OrderRecyclerViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {

    private static final String MYTAG = OrderFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private OrderRecyclerViewAdapter orderRecyclerViewAdapter;
    private String stUserId, stServiceId;
    private List<Order> orderList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar pb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        recyclerView = view.findViewById(R.id.rvOrders);
        swipeRefreshLayout = view.findViewById(R.id.sr_orders_fragment);
        pb = view.findViewById(R.id.pbOrderFragment);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            prepareNetworkCall();
        });
        if (SessionManager.isLoggedIn(getActivity())) {

            Gson gson = new Gson();
            String jsonObject = SessionManager.getLoggedInUserObject(getActivity());
            if (jsonObject != null) {
                Partner partner = gson.fromJson(jsonObject, Partner.class);
                if (partner != null) {
                    if (partner.getId() != null)
                        stUserId = partner.getId();

                    if (partner.getProfession() != null)
                        stServiceId = partner.getProfession();

                    prepareNetworkCall();
                }
            } else {

            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        prepareNetworkCall();
    }

    private void prepareNetworkCall() {
        ProgressBarManager.startProgressBar(pb);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("serviceId", stServiceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<JsonObject> call = ApiClient.getInterface()
                .getAllOrdersOfPartner((JsonObject) JsonParser.parseString(jsonObject.toString()));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ProgressBarManager.stopProgressBar(pb);
                swipeRefreshLayout.setRefreshing(false);
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    OrderResponseModel orderResponseModel =
                            gson.fromJson(response.body(), OrderResponseModel.class);
                    Log.d(MYTAG, "Order message: " + orderResponseModel.getMessage());
                    orderList = orderResponseModel.getOrders();
                    prepareRecyclerView();
                }

                if (response.code() == 500) {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ProgressBarManager.stopProgressBar(pb);
                swipeRefreshLayout.setRefreshing(false);
                Log.d(MYTAG, "Order error: " + t.getMessage());
            }
        });
    }

    private void prepareRecyclerView() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        orderRecyclerViewAdapter = new OrderRecyclerViewAdapter(getActivity(), fragmentManager, orderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(orderRecyclerViewAdapter);
    }
}
