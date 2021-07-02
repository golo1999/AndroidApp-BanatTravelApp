package com.example.banat_travel_app.StartingPart.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.banat_travel_app.MyCustomMethods;
import com.example.banat_travel_app.MyCustomVariables;
import com.example.banat_travel_app.R;
import com.example.banat_travel_app.StartingPart.Activity.RegisterActivity;
import com.example.banat_travel_app.StartingPart.ViewModel.StartingPartViewModel;

public class ForgotPasswordResetFragment extends Fragment {
    private EditText newPassword;
    private EditText confirmNewPassword;
    private Button resetButton;
    private TextView login;
    private TextView register;
    private StartingPartViewModel viewModel;

    public ForgotPasswordResetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_forgot_password_reset, container, false);

        setVariables(view);
        setOnClickListeners();

        return view;
    }

    private void setVariables(final View v) {
        viewModel = new ViewModelProvider(this).get(StartingPartViewModel.class);
        login = v.findViewById(R.id.forgotPasswordLogin);
        register = v.findViewById(R.id.forgotPasswordRegister);
        newPassword = v.findViewById(R.id.forgotPasswordNewPassword);
        confirmNewPassword = v.findViewById(R.id.forgotPasswordConfirmNewPassword);
        resetButton = v.findViewById(R.id.forgotPasswordResetButton);
    }

    private void setOnClickListeners() {
        login.setOnClickListener(v -> MyCustomMethods
                .goBackWithAnimationInDirection(requireActivity(), "left"));

        register.setOnClickListener(v -> {
            final Intent intent = new Intent(requireActivity(), RegisterActivity.class);
            startActivity(intent);
            MyCustomMethods.goBackWithAnimationInDirection(requireActivity(), "left");
        });

        resetButton.setOnClickListener(v -> {
            if (validate(String.valueOf(newPassword.getText()), String.valueOf(confirmNewPassword.getText()))) {
                MyCustomMethods.showMessage(requireActivity(), "Password updated successfully",
                        Toast.LENGTH_SHORT);
                MyCustomMethods.goBackWithAnimationInDirection(requireActivity(), "right");
            }
        });
    }

    private boolean validate(final String newPassword, final String confirmNewPassword) {
        if (newPassword.length() >= MyCustomVariables.getMinimumNumberOfPasswordCharacters() &&
                confirmNewPassword.length() >= MyCustomVariables.getMinimumNumberOfPasswordCharacters() &&
                newPassword.equals(confirmNewPassword)) {
            return true;
        } else {
            MyCustomMethods.showMessage(requireActivity(), newPassword.isEmpty() || confirmNewPassword.isEmpty() ?
                    "The passwords should not be empty" :
                    newPassword.length() < MyCustomVariables.getMinimumNumberOfPasswordCharacters() ||
                            confirmNewPassword.length() < MyCustomVariables.getMinimumNumberOfPasswordCharacters() ?
                            "The passwords should have at least " + MyCustomVariables.getMinimumNumberOfPasswordCharacters() +
                                    " characters" : "Passwords don't match", Toast.LENGTH_SHORT);
        }

        return false;
    }
}