package com.example.banat_travel_app.StartingPart.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.banat_travel_app.R;
import com.example.banat_travel_app.StartingPart.Activity.StartingActivity;

public class SplashScreenFragment extends Fragment {
    public SplashScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createCountDownTimer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        return view;
    }

    private void createCountDownTimer() {
        new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ((StartingActivity) requireActivity()).setFragment(new LoginFragment());
            }
        }.start();
    }
}