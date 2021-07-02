package com.example.banat_travel_app.StartingPart.ViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.example.banat_travel_app.MyCustomVariables;
import com.example.banat_travel_app.StartingPart.Fragment.ForgotPasswordSendFragment;

public class StartingPartViewModel extends ViewModel {
    private int remainingAttempts = MyCustomVariables.getMaximumNumberOfAttempts();
    private Fragment currentForgotPasswordFragment = new ForgotPasswordSendFragment();

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public void decrementNumberOfRemainingAttempts() {
        --remainingAttempts;
    }

    public void resetRemainingAttempts() {
        this.remainingAttempts = MyCustomVariables.getMaximumNumberOfAttempts();
    }

    public Fragment getCurrentFragment() {
        return currentForgotPasswordFragment;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentForgotPasswordFragment = currentFragment;
    }
}