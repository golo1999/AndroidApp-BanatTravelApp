package com.example.banat_travel_app.StartingPart.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banat_travel_app.MyCustomVariables;
import com.example.banat_travel_app.R;
import com.example.banat_travel_app.StartingPart.Activity.StartingActivity;
import com.example.banat_travel_app.StartingPart.ViewModel.StartingPartViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    private StartingPartViewModel viewModel;
    private FirebaseAuth fbAuth;
    private EditText emailField;
    private EditText passwordField;
    private Button logInButton;
    private TextView signUpText;
    private TextView remainingAttemptsText;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        setVariables(view);
        setOnClickListeners();

        return view;
    }

    private void setVariables(View v) {
        viewModel = new ViewModelProvider(this).get(StartingPartViewModel.class);
        fbAuth = FirebaseAuth.getInstance();
        emailField = v.findViewById(R.id.login_email);
        passwordField = v.findViewById(R.id.login_password);
        logInButton = v.findViewById(R.id.login_button);
        signUpText = v.findViewById(R.id.login_sign_up);
        remainingAttemptsText = v.findViewById(R.id.login_remaining_attempts);
    }

    private void setOnClickListeners() {
        // apelam metoda de validare daca apasam butonul de autentificare
        logInButton.setOnClickListener(v -> validation(String.valueOf(emailField.getText()).trim(),
                String.valueOf(passwordField.getText()).trim()));

        signUpText.setOnClickListener(v -> {
//            ((StartingActivity) requireActivity()).setFragment(new RegisterFragment());
            ((StartingActivity) requireActivity()).addFragment(new RegisterFragment());
        });
    }

    // metoda pentru validare
    private void validation(final String email, String password) {
        // incercam sa autentificam utilizatorul in cazul in care email-ul este valid
        // si parola are cel putin 7 caractere
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length() >= MyCustomVariables.getMinimumNumberOfPasswordCharacters()) {
            fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                // daca atat email-ul, cat si parola corespund, se face log in si
                // se trece la activitatea urmatoare
                if (task.isSuccessful()) {
                    ((StartingActivity) requireActivity()).goToMainPart();
                }
                // in cazul in care atat email-ul, cat si parola sunt valide,
                // dar nu corespund informatiilor din baza de date Firebase
                else {
                    // stergem parola
                    passwordField.setText("");
                    // decrementam variabila ce contorizeaza numarul de incercari
                    viewModel.decrementNumberOfRemainingAttempts();
                    // setam noul numar de incercari
                    setRemainingAttemptsText("Remaining attempts: " + viewModel.getRemainingAttempts());

                    // daca nu mai avem incercari disponibile pentru sesiunea curenta
                    if (viewModel.getRemainingAttempts() == 0) {
                        enableOrDisableFields(false);
                        // stergem input-ul de la email
                        emailField.setText("");

                        setCountdownTimer(MyCustomVariables.getNumberOfWaitingMilliseconds(),
                                MyCustomVariables.getCountdownIntervalMilliseconds());
                    }
                }
            });
        }
        // in cazul in care datele introduse nu sunt valide si nu se poate face validarea acestora
        else {
            // daca atat email-ul, cat si parola nu contin niciun caracter
            if (email.isEmpty() && password.isEmpty()) {
                showMessage("Email and password should not be empty");
            }
            // daca email-ul nu contine niciun caracter
            else if (email.isEmpty()) {
                showMessage("Email should not be empty");
                passwordField.setText("");
            }
            // daca parola nu contine niciun caracter
            else if (password.isEmpty()) {
                showMessage("Password should not be empty");
            }
            // daca email-ul nu are forma valida si parola este prea scurta
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                    password.length() < MyCustomVariables.getMinimumNumberOfPasswordCharacters()) {
                showMessage("Both email address and password are not valid");
                passwordField.setText("");
                // daca email-ul nu are forma valida, dar parola este in regula
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showMessage("Email address is not valid");
                passwordField.setText("");
            }
            // daca parola este prea scurta, dar email-ul este in regula
            else {
                showMessage("Password should have at least " +
                        MyCustomVariables.getMinimumNumberOfPasswordCharacters() + " characters");
                passwordField.setText("");
            }
            // decrementam variabila ce contorizeaza numarul de incercari
            viewModel.decrementNumberOfRemainingAttempts();
            // setam noul numar de incercari
            setRemainingAttemptsText("Remaining attempts: " + viewModel.getRemainingAttempts());

            // daca nu mai avem incercari disponibile pentru sesiunea curenta
            if (viewModel.getRemainingAttempts() == 0) {
                enableOrDisableFields(false);
                // stergem input-ul de la email
                emailField.setText("");

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

    private void enableOrDisableFields(boolean enabled) {
        emailField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        logInButton.setEnabled(enabled);
    }

    private void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setCountDownText(String text) {
//        countdownText.setText(text);
    }

    private void setRemainingAttemptsText(String text) {
        remainingAttemptsText.setText(text);
    }
}