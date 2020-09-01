package com.khurshid.kamkorapartner.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.khurshid.kamkorapartner.R;
import com.khurshid.kamkorapartner.utils.SessionManager;

public class HomeFragment extends Fragment {

    private LinearLayout lvLoggedIn, lvLoggedOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        lvLoggedOut = v.findViewById(R.id.lv_fragment_home_not_logged_in);
        lvLoggedIn = v.findViewById(R.id.lv_fragment_home_logged_in);
        hideLv(lvLoggedIn);
        hideLv(lvLoggedOut);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (SessionManager.isLoggedIn(getActivity())) {
            showLv(lvLoggedIn);
            hideLv(lvLoggedOut);
        } else {
            showLv(lvLoggedOut);
            hideLv(lvLoggedIn);
        }
    }

    private void hideLv(LinearLayout linearLayout) {
        linearLayout.setVisibility(View.GONE);

//        if (linearLayout.getVisibility() != View.GONE) {
//            linearLayout.setVisibility(View.GONE);
//        } else {
//            linearLayout.setVisibility(View.VISIBLE);
//        }

    }

    private void showLv(LinearLayout linearLayout) {
        linearLayout.setVisibility(View.VISIBLE);
    }
}
