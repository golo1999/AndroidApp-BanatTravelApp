package com.example.banat_travel_app.StartingPart.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.banat_travel_app.CountySelector;
import com.example.banat_travel_app.MyCustomMethods;
import com.example.banat_travel_app.StartingPart.ViewModel.StartingPartViewModel;
import com.example.banat_travel_app.R;

public class StartingActivity extends AppCompatActivity {
    private StartingPartViewModel viewModel;
    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_authentication);
        setVariables();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setFragment(viewModel.getCurrentFragment());
    }

    @Override
    public void onBackPressed() {
//        if (viewModel.getCurrentFragment() instanceof RegisterFragment) {
//            setFragment(new LoginFragment());
//        } else {
//            super.onBackPressed();
//        }

        super.onBackPressed();
    }

    private void setVariables() {
        viewModel = new ViewModelProvider(this).get(StartingPartViewModel.class);
        mainLayout = findViewById(R.id.authentication_main_layout);
    }

    public void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(mainLayout.getId(), fragment).commit();
        viewModel.setCurrentFragment(fragment);
    }

    public void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(mainLayout.getId(), fragment).addToBackStack(null).commit();
        viewModel.setCurrentFragment(fragment);
    }

    public void hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void goToMainPart() {
        Intent intent = new Intent(StartingActivity.this, CountySelector.class);
        MyCustomMethods.showMessage(this, "Login successful", Toast.LENGTH_SHORT);
        // incheiam activitatea de log in
        finish();
        // incepem activitatea urmatoare
        startActivity(intent);
    }
}