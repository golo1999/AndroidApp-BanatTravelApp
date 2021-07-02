package com.example.banat_travel_app.StartingPart.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.banat_travel_app.MainPart.MainActivity;
import com.example.banat_travel_app.MyCustomMethods;
import com.example.banat_travel_app.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyCustomMethods.hideStatusBar(this);
        setContentView(R.layout.activity_splash_screen);
        initializeSplashScreen();
    }

    private void initializeSplashScreen() {
        new Launcher().start();
    }

    public class Launcher extends Thread {
        private final FirebaseAuth auth = FirebaseAuth.getInstance();

        public void run() {
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            UserDetails details = retrieveUserFromSharedPreferences();
            Intent intent;

//            if(details != null)
//                intent = new Intent(ActivitySplashScreen.this, ActivityConnectAccessHome.class);
//            else intent = new Intent(ActivitySplashScreen.this, ActivityLogin.class);

            intent = new Intent(SplashScreenActivity.this, auth.getCurrentUser() != null ?
                    MainActivity.class : LoginActivity.class);

            startActivity(intent);
            finish();
        }
    }
}