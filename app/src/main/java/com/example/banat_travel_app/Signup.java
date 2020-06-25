package com.example.banat_travel_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class Signup extends AppCompatActivity // clasa in care se desfasoara activitatea de sign up
{
    private TextView account, incorrect_signup;
    private EditText email, password;
    private Button signup_btn;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) // aceste metode vor fi apelate atunci cand activitatea este creata
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        setVariables(); // metoda pentru atribuirea variabilelor
        setOnClickListeners(); // metoda pentru setarea metodelor onClick
    }

    private void setVariables()
    {
        account=findViewById(R.id.already_account);
        email=findViewById(R.id.signup_email);
        password=findViewById(R.id.signup_password);
        signup_btn=findViewById(R.id.signup_button);
        incorrect_signup=findViewById(R.id.signup_incorrect);
        fbAuth=FirebaseAuth.getInstance();
    }

    private void validation() // metoda de validare a sign up-ului
    {
        String val_email=email.getText().toString().trim(); // salvam valoarea input-ului din email, fara spatii la inceput sau la final
        String val_pass=password.getText().toString(); // salvam valoarea inputului din parola

        if(val_pass.length()>6 && Patterns.EMAIL_ADDRESS.matcher(val_email).matches()) // daca parola are cel putin 7 caractere si email-ul este valid
        {
            fbAuth.createUserWithEmailAndPassword(val_email,val_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() // metoda FireBase de sign up
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful()) // daca se poate crea contul
                    {
                        Intent intent=new Intent(Signup.this, Login.class); // cream un intent ce permite trecerea in activitatea de log in
                        Toast.makeText(Signup.this, "Signup successful", Toast.LENGTH_SHORT).show(); // afisam mesaj de succes
                        finish(); // incheiem activitatea curenta
                        startActivity(intent); // incepem activitatea de log in
                    }
                    else // daca nu se poate crea contul
                    {
                        incorrect_signup.setText(R.string.signup_error1); // afisam 'eroarea' ca email-ul deja exista
                        password.setText(""); // resetam parola
                    }
                }
            });
        }
        else if(val_email.isEmpty() && val_pass.isEmpty()) // daca atat emailul, cat si parola nu contin niciun caracter
            incorrect_signup.setText(R.string.signup_error2); // afisam 'eroarea'
        else if(val_email.isEmpty()) // daca doar emailul nu contine niciun caracter
        {
            incorrect_signup.setText(R.string.signup_error3); // afisam 'eroarea'
            password.setText(""); // resetam parola
        }
        else if(val_pass.isEmpty()) // daca doar parola nu contine caractere
            incorrect_signup.setText(R.string.signup_error4); // afisam 'eroarea'
        else if(val_pass.length()<7 && !Patterns.EMAIL_ADDRESS.matcher(val_email).matches()) // daca parola este prea scurta si email-ul nu este valid
        {
            incorrect_signup.setText(R.string.signup_error5); // afisam 'eroarea'
            password.setText(""); // resetam parola
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(val_email).matches()) // daca email-ul nu este valid
        {
            incorrect_signup.setText(R.string.signup_error6); // afisam 'eroarea'
            password.setText(""); // resetam parola
        }
        else // daca parola este prea scurta
        {
            incorrect_signup.setText(R.string.signup_error7); // afisam 'eroarea'
            password.setText(""); // resetam parola
        }
    }

    private void setOnClickListeners() // metoda de setarea a evenimentelor onClick
    {
        account.setOnClickListener(new View.OnClickListener() // daca dam click pe 'am deja cont', incheiem activitatea curenta si trecem la log in
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Signup.this, Login.class);
                finish();
                startActivity(intent);
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() // daca dam click pe butonul de sign up, se incepe verificarea email-ului si a parolei
        {
            @Override
            public void onClick(View v)
            {
                validation();
            }
        });
    }
}