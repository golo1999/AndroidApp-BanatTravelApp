package com.example.banat_travel_app.StartingPart.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.banat_travel_app.MainPart.MainActivity;
import com.example.banat_travel_app.Models.User;
import com.example.banat_travel_app.MyCustomMethods;
import com.example.banat_travel_app.R;

public class SplashScreenActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyCustomMethods.hideStatusBar(this);
        setContentView(R.layout.activity_splash_screen);
        setPreferences();
        initializeSplashScreen();
    }

    private void setPreferences() {
        preferences = getSharedPreferences("BANAT_TRAVEL", MODE_PRIVATE);
    }

    private void initializeSplashScreen() {
        new Launcher().start();
    }

    public class Launcher extends Thread {
        public void run() {
            boolean authenticatedUserExists = false;

            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (MyCustomMethods.sharedPreferencesContainsKey(preferences, "authenticatedUser")) {
                final User authenticatedUser = MyCustomMethods.retrieveUserFromSharedPreferences(preferences,
                        "authenticatedUser");

                if (authenticatedUser != null) {
                    authenticatedUserExists = true;
                    Log.d("authenticatedUser", authenticatedUser.toString());
                }
            }

            final Intent intent = new Intent(SplashScreenActivity.this, authenticatedUserExists ?
                    MainActivity.class : LoginActivity.class);

            startActivity(intent);
            finish();
        }
    }
}