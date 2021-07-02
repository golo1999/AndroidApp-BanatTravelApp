package com.example.banat_travel_app.StartingPart.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.banat_travel_app.MainPart.MainActivity;
import com.example.banat_travel_app.MyCustomMethods;
import com.example.banat_travel_app.MyCustomVariables;
import com.example.banat_travel_app.R;
import com.example.banat_travel_app.StartingPart.ViewModel.StartingPartViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private StartingPartViewModel viewModel;
    private final FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private EditText emailField;
    private EditText passwordField;
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
        setRemainingAttemptsText("Remaining attempts: " + viewModel.getRemainingAttempts());
        setOnClickListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // metoda pentru atribuirea variabilelor
    private void setVariables() {
        viewModel = new ViewModelProvider(this).get(StartingPartViewModel.class);
        emailField = findViewById(R.id.login_email);
        passwordField = findViewById(R.id.login_password);
        logInButton = findViewById(R.id.login_button);
        signUpText = findViewById(R.id.login_sign_up);
        forgotPassword = findViewById(R.id.login_forgot_password);
        remainingAttemptsText = findViewById(R.id.login_remaining_attempts);
    }

    private void setOnClickListeners() {
        forgotPassword.setOnClickListener(view -> MyCustomMethods
                .goToActivityInDirection(LoginActivity.this, ForgotPasswordActivity.class,
                        "right"));

        // apelam metoda de validare daca apasam butonul de autentificare
        logInButton.setOnClickListener(v -> validation(String.valueOf(emailField.getText()).trim(),
                String.valueOf(passwordField.getText()).trim()));

        signUpText.setOnClickListener(v -> MyCustomMethods
                .goToActivityInDirection(LoginActivity.this, RegisterActivity.class,
                        "left"));
    }

    // metoda pentru validare
    private void validation(final String email, final String password) {
        // incercam sa autentificam utilizatorul in cazul in care email-ul este valid
        // si parola are cel putin 7 caractere,
        if (MyCustomMethods.emailAddressIsValid(email) &&
                password.length() >= MyCustomVariables.getMinimumNumberOfPasswordCharacters()) {
            fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                // daca atat email-ul, cat si parola corespund, se face log in si
                // se trece la activitatea urmatoare
                if (task.isSuccessful()) {
                    final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    MyCustomMethods.showMessage(LoginActivity.this, "Login successful",
                            Toast.LENGTH_SHORT);
                    // incheiam activitatea de log in
                    finish();
                    // incepem activitatea urmatoare
                    startActivity(intent);
                }
                // in cazul in care atat email-ul, cat si parola sunt valide,
                // dar nu corespund informatiilor din baza de date Firebase
                else {
                    MyCustomMethods.showMessage(LoginActivity.this,
                            getResources().getString(R.string.incorrect_credentials), Toast.LENGTH_LONG);
                    // stergem parola
                    MyCustomMethods.emptyField(passwordField);
                    // decrementam variabila ce contorizeaza numarul de incercari
                    viewModel.decrementNumberOfRemainingAttempts();
                    // setam noul numar de incercari
                    setRemainingAttemptsText("Remaining attempts: " + viewModel.getRemainingAttempts());

                    // daca nu mai avem incercari disponibile pentru sesiunea curenta
                    if (viewModel.getRemainingAttempts() == 0) {
                        enableOrDisableFields(false);
                        // stergem email-ul
                        MyCustomMethods.emptyField(emailField);

                        setCountdownTimer(MyCustomVariables.getNumberOfWaitingMilliseconds(),
                                MyCustomVariables.getCountdownIntervalMilliseconds());
                    }
                }
            });
        }
        // in cazul in care nu se respecta conditia de log in
        else {
            // daca atat email-ul, cat si parola nu contin niciun caracter
            if (email.isEmpty() && password.isEmpty()) {
                MyCustomMethods.showMessage(LoginActivity.this,
                        "Email and password should not be empty", Toast.LENGTH_SHORT);
            }
            // daca email-ul nu contine niciun caracter
            else if (email.isEmpty()) {
                MyCustomMethods.showMessage(LoginActivity.this, "Email should not be empty",
                        Toast.LENGTH_SHORT);
                MyCustomMethods.emptyField(passwordField);
            }
            // daca parola nu contine niciun caracter
            else if (password.isEmpty()) {
                MyCustomMethods.showMessage(LoginActivity.this, "Password should not be empty",
                        Toast.LENGTH_SHORT);
            }
            // daca email-ul nu are forma valida si parola este prea scurta
            else if (!MyCustomMethods.emailAddressIsValid(email) &&
                    password.length() < MyCustomVariables.getMinimumNumberOfPasswordCharacters()) {
                MyCustomMethods.showMessage(LoginActivity.this,
                        "Both email address and password are not valid", Toast.LENGTH_SHORT);
                MyCustomMethods.emptyField(passwordField);
                // daca email-ul nu are forma valida, dar parola este in regula
            } else if (!MyCustomMethods.emailAddressIsValid(email)) {
                MyCustomMethods.showMessage(LoginActivity.this, "Email address is not valid",
                        Toast.LENGTH_SHORT);
                MyCustomMethods.emptyField(passwordField);
            }
            // daca parola este prea scurta, dar email-ul este in regula
            else {
                MyCustomMethods.showMessage(LoginActivity.this, "Password should have at least " +
                        MyCustomVariables.getMinimumNumberOfPasswordCharacters() + " characters", Toast.LENGTH_SHORT);
                MyCustomMethods.emptyField(passwordField);
            }
            // decrementam variabila ce contorizeaza numarul de incercari
            viewModel.decrementNumberOfRemainingAttempts();
            // setam noul numar de incercari
            setRemainingAttemptsText("Remaining attempts: " + viewModel.getRemainingAttempts());

            // daca nu mai avem incercari disponibile pentru sesiunea curenta
            if (viewModel.getRemainingAttempts() == 0) {
                enableOrDisableFields(false);
                // stergem email-ul
                MyCustomMethods.emptyField(emailField);

                setCountdownTimer(MyCustomVariables.getNumberOfWaitingMilliseconds(),
                        MyCustomVariables.getCountdownIntervalMilliseconds());
            }
        }
    }

    // setam un countdown de 30 de secunde, la finalul caruia vom avea din nou 5 incercari
    private void setCountdownTimer(final int numberOfSeconds, final int countDownInterval) {
        new CountDownTimer(numberOfSeconds, countDownInterval) {
            // afiseaza countdown-ul dupa trecerea fiecarei secunde
            @Override
            public void onTick(long millisUntilFinished) {
                setRemainingAttemptsText("Next attempt in " + millisUntilFinished / countDownInterval + " seconds");
            }

            // atunci cand timer-ul ajunge la 0, posibilitatea de log in devine din nou disponibila
            // si avem inca 5 incercari
            @Override
            public void onFinish() {
                enableOrDisableFields(true);
                viewModel.resetRemainingAttempts();
                setRemainingAttemptsText("Remaining attempts: " + viewModel.getRemainingAttempts());
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
}