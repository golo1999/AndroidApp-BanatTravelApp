package com.example.banat_travel_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity // clasa in care se desfasoara activitatea de log in
{
    private EditText email, password;
    private Button log_in;
    private TextView countdown, sign_up, incorrect_login;
    private int count;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) // aceste metode vor fi apelate atunci cand activitatea este creata
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setVariables(); // metoda pentru atribuirea variabilelor
        setOnClickListeners(); // metoda pentru setarea evenimentelor onClick
    }

    private void setVariables() // metoda pentru atribuirea variabilelor
    {
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        log_in=(Button)findViewById(R.id.login_button);
        countdown=(TextView)findViewById(R.id.countdown);
        sign_up=(TextView)findViewById(R.id.sign_up);
        incorrect_login=(TextView)findViewById(R.id.login_incorrect);
        count=5;
        countdown.setText("Remaining attempts: " + count);
        fbAuth=FirebaseAuth.getInstance();
    }

    private void validation(final String _email, String _password) // metoda pentru validare
    {
        if(Patterns.EMAIL_ADDRESS.matcher(_email).matches() && _password.length()>=7) // in cazul in care email-ul este valid si parola are cel putin 7 caractere, incercam sa facem log in
        {
            fbAuth.signInWithEmailAndPassword(_email,_password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful()) // daca atat email-ul, cat si parola corespund, se face log in si se trece la activitatea urmatoare
                    {
                        Intent intent=new Intent(Login.this, County_selector.class);
                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        finish(); // incheiam activitatea de log in
                        startActivity(intent); // incepem activitatea urmatoare
                    }
                    else
                    {
                        incorrect_login.setText("Incorrect username or password"); // in cazul in care atat email-ul, cat si parola sunt valide, dar nu corespund informatiilor din baza de date Firebase
                        password.setText(""); // stergem parola
                        count--; // decrementam variabila ce contorizeaza numarul de incercari
                        countdown.setText("Remaining attempts: " + count); // setam noul numar de incercari
                        if(count==0) // daca nu mai avem incercari disponibile pentru sesiunea curenta
                        {
                            email.setEnabled(false); // input-ul pentru email devine indisponibil
                            password.setEnabled(false); // input-ul pentru parola devine indisponibil
                            log_in.setEnabled(false); // butonul de log in devine indisponibil
                            incorrect_login.setText(""); // stergem eroarea
                            email.setText(""); // stergem input-ul de la email
                            new CountDownTimer(30000,1000) // setam un countdown de 30 de secunde, la finalul caruia vom avea din nou 5 incercari
                            {
                                @Override
                                public void onTick(long millisUntilFinished)
                                {
                                    countdown.setText("Next attempt in " + millisUntilFinished/1000 + " seconds"); // afiseaza countdown-ul dupa trecerea fiecarei secunde
                                }

                                @Override
                                public void onFinish()
                                { // atunci cand timer-ul ajunge la 0, posibilitatea de log in devine din nou disponibila si avem inca 5 incercari
                                    email.setEnabled(true); // input-ul pentru email redevine disponibil
                                    password.setEnabled(true); // input-ul pentru parola redevine disponibil
                                    log_in.setEnabled(true); // butonul de log in redevine disponibil
                                    count=5;
                                    countdown.setText("Remaining attempts: " + count);
                                }
                            }.start();
                        }
                    }
                }
            });
        }
        else // in cazul in care nu se respecta conditia de log in
        {
            if(_email.isEmpty() && _password.isEmpty()) // daca atat email-ul, cat si parola nu contin niciun caracter
                incorrect_login.setText("Email and password should not be empty");
            else if(_email.isEmpty()) // daca email-ul nu contine niciun caracter
            {
                incorrect_login.setText("Email should not be empty");
                password.setText("");
            }
            else if(_password.isEmpty()) // daca parola nu contine niciun caracter
                incorrect_login.setText("Password should not be empty");
            else if(!Patterns.EMAIL_ADDRESS.matcher(_email).matches() && _password.length()<4) // daca email-ul nu are forma valida si parola este prea scurta
            {
                incorrect_login.setText("Both email address and password are not valid");
                password.setText("");
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(_email).matches()) // daca email-ul nu are forma valida, dar parola este in regula
            {
                incorrect_login.setText("Email address is not valid");
                password.setText("");
            }
            else // daca parola este prea scurta, dar email-ul este in regula
            {
                incorrect_login.setText("Password should have at least 7 characters");
                password.setText("");
            }
            count--; // decrementam variabila ce contorizeaza numarul de incercari
            countdown.setText("Remaining attempts: " + count); // setam noul numar de incercari
            if(count==0) // daca nu mai avem incercari disponibile pentru sesiunea curenta
            {
                email.setEnabled(false); // input-ul pentru email devine indisponibil
                password.setEnabled(false); // input-ul pentru parola devine indisponibil
                log_in.setEnabled(false); // butonul de log in devine indisponibil
                incorrect_login.setText(""); // stergem eroarea
                email.setText(""); // stergem input-ul de la email
                new CountDownTimer(30000,1000) // setam un countdown de 30 de secunde, la finalul caruia vom avea din nou 5 incercari
                {
                    @Override
                    public void onTick(long millisUntilFinished)
                    {
                        countdown.setText("Next attempt in " + millisUntilFinished/1000 + " seconds"); // afiseaza countdown-ul dupa trecerea fiecarei secunde
                    }

                    @Override
                    public void onFinish() // atunci cand timer-ul ajunge la 0, posibilitatea de log in devine din nou disponibila si avem inca 5 incercari
                    {
                        email.setEnabled(true); // input-ul pentru email redevine disponibil
                        password.setEnabled(true); // input-ul pentru parola redevine disponibil
                        log_in.setEnabled(true); // butonul de log in redevine disponibil
                        count=5;
                        countdown.setText("Remaining attempts: " + count);
                    }
                }.start();
            }
        }
    }

    private void setOnClickListeners() // metoda pentru setarea evenimentelor onClick
    {
        log_in.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) // i-am implementat butonului de submit o metoda onClick pentru validare
            {
                validation(email.getText().toString().trim(),password.getText().toString().trim()); // metoda pentru validare este apelata (ea este definita mai jos in cadrul metodei 'OnCreate')
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() // i-am implementat textview-ului "Don't have an account?" o metoda onClick pentru a merge la activitatea de sign up
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Login.this, Signup.class);
                finish(); // inchidem activitatea curenta (dupa ce ajungem la activitatea urmatoare, daca dam click pe butonul de inapoi, nu mai putem ajunge la activitatea de log in)
                startActivity(intent); // trecem la activitatea de sign up
            }
        });
    }
}