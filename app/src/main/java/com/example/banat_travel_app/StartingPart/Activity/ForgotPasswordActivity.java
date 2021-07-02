package com.example.banat_travel_app.StartingPart.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.banat_travel_app.MyCustomMethods;
import com.example.banat_travel_app.R;
import com.example.banat_travel_app.StartingPart.Fragment.ForgotPasswordResetFragment;
import com.example.banat_travel_app.StartingPart.Fragment.ForgotPasswordSendFragment;
import com.example.banat_travel_app.StartingPart.ViewModel.StartingPartViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {
    private StartingPartViewModel viewModel;
    private Fragment selectedFragment;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyCustomMethods.hideStatusBar(this);
        setContentView(R.layout.activity_forgot_password);
        setVariables();
        setFragment(selectedFragment);
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof ForgotPasswordSendFragment) {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (getCurrentFragment() instanceof ForgotPasswordResetFragment)
            setFragment(new ForgotPasswordSendFragment(), "left");
    }

    private void setVariables() {
        viewModel = new ViewModelProvider(this).get(StartingPartViewModel.class);
        selectedFragment = viewModel.getCurrentFragment();
        fragmentContainer = findViewById(R.id.forgot_password_fragment_container);
    }

    public void setFragment(Fragment fragment, String direction) {
        viewModel.setCurrentFragment(fragment);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(direction.equals("left") ? R.anim.slide_in_left : R.anim.slide_in_right,
                        direction.equals("left") ? R.anim.slide_out_right : R.anim.slide_out_left)
                .replace(fragmentContainer.getId(), fragment).commit();
    }

    public void setFragment(Fragment fragment) {
        viewModel.setCurrentFragment(fragment);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.forgot_password_fragment_container, fragment).commit();
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(fragmentContainer.getId());
    }
}