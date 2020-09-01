package com.khurshid.kamkorapartner.utils;

import android.view.View;
import android.widget.ProgressBar;

public class ProgressBarManager {

    public static void startProgressBar(ProgressBar progressBar) {
//        if (progressBar.getVisibility() == View.GONE) {
//            progressBar.setVisibility(View.VISIBLE);
//        } else {
//            progressBar.setVisibility(View.GONE);
//        }
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void stopProgressBar(ProgressBar progressBar) {
//        if (progressBar.getVisibility() == View.VISIBLE) {
//            progressBar.setVisibility(View.GONE);
//        } else {
//            progressBar.setVisibility(View.GONE);
//        }
        progressBar.setVisibility(View.GONE);
    }

}
