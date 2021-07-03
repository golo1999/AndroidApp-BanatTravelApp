package com.example.banat_travel_app.StartingPart.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.banat_travel_app.MainPart.MainActivity;
import com.example.banat_travel_app.Models.User;
import com.example.banat_travel_app.MyCustomMethods;
import com.example.banat_travel_app.MyCustomVariables;
import com.example.banat_travel_app.R;
import com.example.banat_travel_app.StartingPart.ViewModel.StartingPartViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private final FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StartingPartViewModel viewModel;
    private EditText emailField;
    private EditText passwordField;
    private CheckBox rememberMe;
    private Button logInButton;
    private TextView signUpText;
    private TextView forgotPassword;
    private TextView remainingAttemptsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyCustomMethods.hideStatusBar(this);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_login);
        setVariables();
        setOnClickListeners();
        setRemainingAttemptsText(getResources().getString(R.string.remaining_attempts) +
                ": " + viewModel.getRemainingAttempts());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setVariables() {
        preferences = getSharedPreferences(MyCustomVariables.getSharedPreferencesFileName(), MODE_PRIVATE);
        viewModel = new ViewModelProvider(this).get(StartingPartViewModel.class);
        emailField = findViewById(R.id.login_email);
        passwordField = findViewById(R.id.login_password);
        rememberMe = findViewById(R.id.login_remember_me);
        logInButton = findViewById(R.id.login_button);
        signUpText = findViewById(R.id.login_sign_up);
        forgotPassword = findViewById(R.id.login_forgot_password);
        remainingAttemptsText = findViewById(R.id.login_remaining_attempts);
    }

    private void setOnClickListeners() {
        forgotPassword.setOnClickListener(view -> MyCustomMethods
                .goToActivityInDirection(LoginActivity.this, ForgotPasswordActivity.class,
                        "right"));

        // calling the authentication validation method
        logInButton.setOnClickListener(v -> validation(String.valueOf(emailField.getText()).trim(),
                String.valueOf(passwordField.getText()).trim(), rememberMe.isChecked()));

        // redirecting to the registration activity
        signUpText.setOnClickListener(v -> MyCustomMethods
                .goToActivityInDirection(LoginActivity.this, RegisterActivity.class,
                        "left"));
    }

    // authentication validation method
    private void validation(final String email, final String password, final boolean rememberMeChecked) {
        MyCustomMethods.closeKeyboard(this);
        // trying to authenticate if all the fields are valid
        if (MyCustomMethods.emailAddressIsValid(email) &&
                password.length() >= MyCustomVariables.getMinimumNumberOfPasswordCharacters()) {
            fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                // redirecting to the main activity if the credentials match
                if (task.isSuccessful()) {
                    final FirebaseUser currentUser = fbAuth.getCurrentUser();

                    if (currentUser != null) {
                        if (currentUser.isEmailVerified()) {
                            if (rememberMeChecked) {
                                databaseReference.child("UsersList").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists() && snapshot.hasChildren()) {
                                            for (DataSnapshot user : snapshot.getChildren()) {
                                                if (String.valueOf(user.getKey()).equals(currentUser.getUid())) {
                                                    if (user.hasChild("rememberMeChecked") &&
                                                            String.valueOf(user.child(currentUser.getUid())
                                                                    .child("rememberMeChecked").getValue())
                                                                    .equals("false")) {
                                                        databaseReference.child("UsersList").child(currentUser.getUid())
                                                                .child("rememberMeChecked").setValue(true);
                                                    }

                                                    final User currentUser1 = user.getValue(User.class);

                                                    if (currentUser1 != null) {
                                                        // saving the recently authenticated user to SharedPreferences
                                                        MyCustomMethods
                                                                .saveUserToSharedPreferences(preferences, currentUser1,
                                                                        "authenticatedUser");
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            saveUserToDatabaseIfItsDetailsDoNotExist(currentUser);

                            MyCustomMethods.showMessage(LoginActivity.this, "Login successful",
                                    Toast.LENGTH_SHORT);
                            MyCustomMethods
                                    .goToActivityWithFadeAnimationAndCloseCurrentOne(LoginActivity.this,
                                            MainActivity.class);
                        } else {
                            MyCustomMethods.showMessage(LoginActivity.this,
                                    "Please verify your email first", Toast.LENGTH_SHORT);
                        }
                    }
                }
                // if the email is valid and the password is long enough, but the credentials don't match
                else {
                    MyCustomMethods.showMessage(LoginActivity.this,
                            getResources().getString(R.string.incorrect_credentials), Toast.LENGTH_LONG);

                    MyCustomMethods.emptyField(passwordField);
                    // decrementing the number of remaining attempts
                    viewModel.decrementNumberOfRemainingAttempts();
                    // setting the new number of remaining attempts
                    setRemainingAttemptsText(getResources().getString(R.string.remaining_attempts) +
                            ": " + viewModel.getRemainingAttempts());

                    // if there are no more remaining attempts for the current session
                    if (viewModel.getRemainingAttempts() == 0) {
                        enableOrDisableFields(false);

                        MyCustomMethods.emptyField(emailField);

                        setCountdownTimer(MyCustomVariables.getNumberOfWaitingMilliseconds(),
                                MyCustomVariables.getCountdownIntervalMilliseconds());
                    }
                }
            });
        }
        // if not all the fields are valid
        else {
            // if both email and password are empty
            if (email.isEmpty() && password.isEmpty()) {
                MyCustomMethods.showMessage(LoginActivity.this,
                        "Email and password should not be empty", Toast.LENGTH_SHORT);
            }
            // if the email is empty
            else if (email.isEmpty()) {
                MyCustomMethods.showMessage(LoginActivity.this, "Email should not be empty",
                        Toast.LENGTH_SHORT);
                MyCustomMethods.emptyField(passwordField);
            }
            // if the password is empty
            else if (password.isEmpty()) {
                MyCustomMethods.showMessage(LoginActivity.this, "Password should not be empty",
                        Toast.LENGTH_SHORT);
            }
            // if the email isn't valid and the password is too short
            else if (!MyCustomMethods.emailAddressIsValid(email) &&
                    password.length() < MyCustomVariables.getMinimumNumberOfPasswordCharacters()) {
                MyCustomMethods.showMessage(LoginActivity.this,
                        "Both email address and password are not valid", Toast.LENGTH_SHORT);
                MyCustomMethods.emptyField(passwordField);
            }
            // if the email isn't valid
            else if (!MyCustomMethods.emailAddressIsValid(email)) {
                MyCustomMethods.showMessage(LoginActivity.this, "Email address is not valid",
                        Toast.LENGTH_SHORT);
                MyCustomMethods.emptyField(passwordField);
            }
            // if the password is too short
            else {
                MyCustomMethods.showMessage(LoginActivity.this, "Password should have at least " +
                        MyCustomVariables.getMinimumNumberOfPasswordCharacters() + " characters", Toast.LENGTH_SHORT);
                MyCustomMethods.emptyField(passwordField);
            }
            // decrementing the number of remaining attempts
            viewModel.decrementNumberOfRemainingAttempts();
            // setting the new number of remaining attempts
            setRemainingAttemptsText(getResources().getString(R.string.remaining_attempts) +
                    ": " + viewModel.getRemainingAttempts());

            // if there are no more remaining attempts for the current session
            if (viewModel.getRemainingAttempts() == 0) {
                enableOrDisableFields(false);

                MyCustomMethods.emptyField(emailField);

                setCountdownTimer(MyCustomVariables.getNumberOfWaitingMilliseconds(),
                        MyCustomVariables.getCountdownIntervalMilliseconds());
            }
        }
    }

    // setting a 30 seconds countdown before resetting the number of remaining attempts
    private void setCountdownTimer(final int numberOfSeconds, final int countDownInterval) {
        new CountDownTimer(numberOfSeconds, countDownInterval) {
            @Override
            public void onTick(final long millisUntilFinished) {
                // showing the countdown after each second passes
                setRemainingAttemptsText("Next attempt in " + millisUntilFinished / countDownInterval + " seconds");
            }

            @Override
            public void onFinish() {
                // resetting the number of remaining attempts when the countdown is over
                enableOrDisableFields(true);
                viewModel.resetRemainingAttempts();
                setRemainingAttemptsText(getResources().getString(R.string.remaining_attempts) +
                        ": " + viewModel.getRemainingAttempts());
            }
        }.start();
    }

    private void enableOrDisableFields(final boolean enabled) {
        emailField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        logInButton.setEnabled(enabled);
    }

    private void setRemainingAttemptsText(final String text) {
        remainingAttemptsText.setText(text);
    }

    private void saveUserToDatabaseIfItsDetailsDoNotExist(final FirebaseUser currentUser) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean userDetailsExist = false;

                // looping through the database and searching if the current user already has his/her details
                if (snapshot.exists() && snapshot.hasChild("UsersList")) {
                    if (snapshot.child("UsersList").hasChildren()) {
                        for (final DataSnapshot user : snapshot.child("UsersList").getChildren()) {
                            if (String.valueOf(user.getKey()).equals(currentUser.getUid())) {
                                userDetailsExist = true;
                                break;
                            }
                        }
                    }
                }

                // if the user doesn't have his/her details, we manually set them from the user
                // that is saved into SharedPreferences
                if (!userDetailsExist) {
                    final User userFromSharedPreferences = MyCustomMethods
                            .retrieveUserFromSharedPreferences(preferences, "currentUser");
                    databaseReference.child("UsersList")
                            .child(userFromSharedPreferences.getId())
                            .setValue(userFromSharedPreferences);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}