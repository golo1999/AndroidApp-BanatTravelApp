package com.example.banat_travel_app.StartingPart.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.banat_travel_app.MyCustomMethods;
import com.example.banat_travel_app.R;
import com.example.banat_travel_app.StartingPart.Activity.RegisterActivity;
import com.example.banat_travel_app.StartingPart.Activity.ForgotPasswordActivity;

public class ForgotPasswordSendFragment extends Fragment {
    private TextView login;
    private TextView register;
    private EditText email;
    private Button sendButton;

    public ForgotPasswordSendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_forgot_password_send, container, false);

        setVariables(view);
        setOnClickListeners();

        return view;
    }

    private void setVariables(View v) {
        login = v.findViewById(R.id.forgotPasswordLogin);
        register = v.findViewById(R.id.forgotPasswordRegister);
        email = v.findViewById(R.id.forgotPasswordEmail);
        sendButton = v.findViewById(R.id.forgotPasswordSendButton);
    }

    private void setOnClickListeners() {
        login.setOnClickListener(v -> MyCustomMethods
                .goBackWithAnimationInDirection(requireActivity(), "left"));

        register.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), RegisterActivity.class));
            MyCustomMethods.goBackWithAnimationInDirection(requireActivity(), "left");
        });

        sendButton.setOnClickListener(v -> {
            // if the email is valid
            if (Patterns.EMAIL_ADDRESS.matcher(String.valueOf(email.getText()).trim()).matches())
                ((ForgotPasswordActivity) requireActivity())
                        .setFragment(new ForgotPasswordResetFragment(), "right");
            else {
                MyCustomMethods.showMessage(requireActivity(), "Please enter a valid email",
                        Toast.LENGTH_SHORT);
            }
        });
    }
}