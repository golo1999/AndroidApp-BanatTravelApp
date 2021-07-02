package com.example.banat_travel_app.StartingPart.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.banat_travel_app.Models.User;
import com.example.banat_travel_app.MyCustomMethods;
import com.example.banat_travel_app.MyCustomVariables;
import com.example.banat_travel_app.R;
import com.example.banat_travel_app.StartingPart.ViewModel.StartingPartViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private StartingPartViewModel viewModel;
    private TextView registerLogin;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText passwordField;
    private Button signUpButton;
    private final FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyCustomMethods.hideStatusBar(this);
        setContentView(R.layout.activity_register);
        setVariables();
        setOnClickListeners();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void setVariables() {
        viewModel = new ViewModelProvider(this).get(StartingPartViewModel.class);
        registerLogin = findViewById(R.id.register_login);
        firstNameField = findViewById(R.id.register_first_name);
        lastNameField = findViewById(R.id.register_last_name);
        emailField = findViewById(R.id.register_email);
        passwordField = findViewById(R.id.register_password);
        signUpButton = findViewById(R.id.register_button);
    }

    private void setOnClickListeners() {
        // daca dam click pe 'am deja cont', incheiem activitatea curenta si trecem la log in
        registerLogin.setOnClickListener(v -> onBackPressed());

        // daca dam click pe butonul de sign up, se incepe verificarea email-ului si a parolei
        signUpButton.setOnClickListener(v -> validation(String.valueOf(firstNameField.getText()).trim(),
                String.valueOf(lastNameField.getText()).trim(), String.valueOf(emailField.getText()).trim(),
                String.valueOf(passwordField.getText())));
    }

    // metoda de validare a inregistrarii
    private void validation(final String firstNameValue, final String lastNameValue, final String emailValue,
                            final String passwordValue) {
        // daca toate campurile sunt valide
        if (allFieldsAreValid(firstNameValue, lastNameValue, emailValue, passwordValue)) {
            // metoda FireBase de sign up
            fbAuth.createUserWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(task -> {
                // daca se poate crea contul
                if (task.isSuccessful()) {
                    final FirebaseUser firebaseUser = fbAuth.getCurrentUser();

                    if (firebaseUser != null) {
                        firebaseUser.sendEmailVerification().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                final User newUser = new User(firstNameValue, lastNameValue, emailValue);

                                MyCustomMethods.showMessage(RegisterActivity.this,
                                        "Please verify your email", Toast.LENGTH_SHORT);
                                databaseReference.child("UsersList").child(newUser.getId()).setValue(newUser);

                                // cream un intent ce permite trecerea in activitatea de log in
                                final Intent intent = new Intent(RegisterActivity.this,
                                        LoginActivity.class);
                                // afisam mesaj de succes
                                MyCustomMethods.showMessage(RegisterActivity.this,
                                        "Registration successful", Toast.LENGTH_SHORT);
                                // incheiem activitatea curenta
                                finish();
                                // incepem activitatea de log in
                                startActivity(intent);
                            } else {
                                MyCustomMethods.showMessage(RegisterActivity.this,
                                        "Email already exists", Toast.LENGTH_SHORT);
                                MyCustomMethods.emptyField(passwordField);
                            }
                        });
                    }
                }
                // daca nu se poate crea contul
                else {
                    // afisam 'eroarea' ca email-ul deja exista
                    MyCustomMethods.showMessage(RegisterActivity.this,
                            getResources().getString(R.string.signup_error1), Toast.LENGTH_SHORT);
                    // resetam parola
                    MyCustomMethods.emptyField(passwordField);
                }
            });
        }
        // daca nu toate campurile sunt valide
        else {
            getValidationError(firstNameValue, lastNameValue, emailValue, passwordValue);
        }

//        // daca atat emailul, cat si parola nu contin niciun caracter
//        else if (emailValue.isEmpty() && passwordValue.isEmpty()) {
//            // afisam 'eroarea'
//            MyCustomMethods.showMessage(RegisterActivity.this, getResources().getString(R.string.signup_error2),
//                    Toast.LENGTH_SHORT);
//        }
//        // daca doar emailul nu contine niciun caracter
//        else if (emailValue.isEmpty()) {
//            // afisam 'eroarea'
//            MyCustomMethods.showMessage(RegisterActivity.this, getResources().getString(R.string.signup_error3),
//                    Toast.LENGTH_SHORT);
//            // resetam parola
//            MyCustomMethods.emptyField(passwordField);
//        }
//        // daca doar parola nu contine caractere
//        else if (passwordValue.isEmpty()) {
//            // afisam 'eroarea'
//            MyCustomMethods.showMessage(RegisterActivity.this, getResources().getString(R.string.signup_error4),
//                    Toast.LENGTH_SHORT);
//        }
//        // daca parola este prea scurta si email-ul nu este valid
//        else if (passwordValue.length() < viewModel.getMinimumNumberOfPasswordCharacters() &&
//                !MyCustomMethods.emailAddressIsValid(emailValue)) {
//            // afisam 'eroarea'
//            MyCustomMethods.showMessage(RegisterActivity.this, getResources().getString(R.string.signup_error5),
//                    Toast.LENGTH_SHORT);
//            // resetam parola
//            MyCustomMethods.emptyField(passwordField);
//        }
//        // daca email-ul nu este valid
//        else if (!MyCustomMethods.emailAddressIsValid(emailValue)) {
//            // afisam 'eroarea'
//            MyCustomMethods.showMessage(RegisterActivity.this, getResources().getString(R.string.signup_error6),
//                    Toast.LENGTH_SHORT);
//            // resetam parola
//            MyCustomMethods.emptyField(passwordField);
//        }
//        // daca parola este prea scurta
//        else {
//            // afisam 'eroarea'
//            MyCustomMethods.showMessage(RegisterActivity.this, getResources().getString(R.string.signup_error7),
//                    Toast.LENGTH_SHORT);
//            // resetam parola
//            MyCustomMethods.emptyField(passwordField);
//        }
    }

    private boolean allFieldsAreValid(final String firstNameValue, final String lastNameValue, final String emailValue,
                                      final String passwordValue) {
        return MyCustomMethods.nameIsValid(firstNameValue, MyCustomVariables.getMinimumNumberOfNameCharacters()) &&
                MyCustomMethods.nameIsValid(lastNameValue, MyCustomVariables.getMinimumNumberOfNameCharacters()) &&
                MyCustomMethods.emailAddressIsValid(emailValue) &&
                passwordValue.length() >= MyCustomVariables.getMinimumNumberOfPasswordCharacters();
    }

    private void getValidationError(final String firstNameValue, final String lastNameValue, final String emailValue,
                                    final String passwordValue) {
        if (firstNameValue.isEmpty() && lastNameValue.isEmpty() && emailValue.isEmpty() && passwordValue.isEmpty()) {
            MyCustomMethods.showMessage(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT);
        } else if (firstNameValue.isEmpty()) {
            MyCustomMethods.showMessage(RegisterActivity.this, "First name should not be empty", Toast.LENGTH_SHORT);
        } else if (lastNameValue.isEmpty()) {
            MyCustomMethods.showMessage(RegisterActivity.this, "Last name should not be empty", Toast.LENGTH_SHORT);
        } else if (emailValue.isEmpty()) {
            // afisam 'eroarea'
            MyCustomMethods.showMessage(RegisterActivity.this, getResources().getString(R.string.signup_error3),
                    Toast.LENGTH_SHORT);
            // resetam parola
            MyCustomMethods.emptyField(passwordField);
        } else if (passwordValue.isEmpty()) {
            // afisam 'eroarea'
            MyCustomMethods.showMessage(RegisterActivity.this, getResources().getString(R.string.signup_error4),
                    Toast.LENGTH_SHORT);
        } else if (firstNameValue.length() < MyCustomVariables.getMinimumNumberOfNameCharacters() &&
                lastNameValue.length() < MyCustomVariables.getMinimumNumberOfNameCharacters() &&
                !MyCustomMethods.emailAddressIsValid(emailValue) &&
                passwordValue.length() < MyCustomVariables.getMinimumNumberOfPasswordCharacters()) {
            MyCustomMethods.showMessage(RegisterActivity.this, "No field is valid", Toast.LENGTH_SHORT);
        } else if (firstNameValue.length() < MyCustomVariables.getMinimumNumberOfNameCharacters()) {
            MyCustomMethods.showMessage(RegisterActivity.this, "Fist name should have at least" + " " +
                    MyCustomVariables.getMinimumNumberOfNameCharacters() + " " + "characters", Toast.LENGTH_SHORT);
        } else if (lastNameValue.length() < MyCustomVariables.getMinimumNumberOfNameCharacters()) {
            MyCustomMethods.showMessage(RegisterActivity.this, "Last name should have at least" + " " +
                    MyCustomVariables.getMinimumNumberOfNameCharacters() + " " + "characters", Toast.LENGTH_SHORT);
        } else if (!MyCustomMethods.emailAddressIsValid(emailValue)) {
            // afisam 'eroarea'
            MyCustomMethods.showMessage(RegisterActivity.this, getResources().getString(R.string.signup_error6),
                    Toast.LENGTH_SHORT);
            // resetam parola
            MyCustomMethods.emptyField(passwordField);
        } else if (passwordValue.length() < MyCustomVariables.getMinimumNumberOfPasswordCharacters()) {
            // afisam 'eroarea'
            MyCustomMethods.showMessage(RegisterActivity.this, getResources().getString(R.string.signup_error7),
                    Toast.LENGTH_SHORT);
            // resetam parola
            MyCustomMethods.emptyField(passwordField);
        }
    }
}