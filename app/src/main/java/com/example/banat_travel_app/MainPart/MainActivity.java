package com.example.banat_travel_app.MainPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.banat_travel_app.CountySelector;
import com.example.banat_travel_app.Models.User;
import com.example.banat_travel_app.MyCustomMethods;
import com.example.banat_travel_app.MyCustomVariables;
import com.example.banat_travel_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;
    private MainPartViewModel viewModel;
    private FrameLayout fragmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyCustomMethods.hideStatusBar(this);
        setContentView(R.layout.activity_main);
        setVariables();
        setFragments();
        setBottomNavigationListener(navListener);

        if (MyCustomMethods.sharedPreferencesContainsKey(preferences, "authenticatedUser")) {
            final User authenticatedUser = MyCustomMethods.retrieveUserFromSharedPreferences(preferences,
                    "authenticatedUser");
            Log.d("authenticatedUser", authenticatedUser.toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setFragment(viewModel.getSelectedFragment());
        setBottomNavigationSelectedItem();
    }

    private void setVariables() {
        preferences = getSharedPreferences(MyCustomVariables.getSharedPreferencesFileName(), MODE_PRIVATE);
        viewModel = new ViewModelProvider(this).get(MainPartViewModel.class);
        bottomNavigationView = findViewById(R.id.main_activity_bottom_navigation_view);
        fragmentLayout = findViewById(R.id.main_activity_main_fragment);
    }

    private void setFragment(Fragment fragment) {
        fragmentManager.beginTransaction().show(fragment).commit();
    }

    private void setFragments() {
        fragmentManager.beginTransaction()
                .add(fragmentLayout.getId(), viewModel.getSettingsFragment(), "settings_fragment")
                .hide(viewModel.getSettingsFragment())
                .commit();
        fragmentManager.beginTransaction()
                .add(fragmentLayout.getId(), viewModel.getProfileFragment(), "profile_fragment")
                .hide(viewModel.getProfileFragment())
                .commit();
        fragmentManager.beginTransaction()
                .add(fragmentLayout.getId(), viewModel.getFavoritesFragment(), "favorites_fragment")
                .hide(viewModel.getFavoritesFragment())
                .commit();
        fragmentManager.beginTransaction()
                .add(fragmentLayout.getId(), viewModel.getHomeFragment(), "home_fragment")
                .hide(viewModel.getHomeFragment())
                .commit();
    }

    private void setBottomNavigationSelectedItem() {
        bottomNavigationView.setSelectedItemId(viewModel.getSelectedFragment() == viewModel.getHomeFragment() ?
                0 : viewModel.getSelectedFragment() == viewModel.getFavoritesFragment() ?
                1 : viewModel.getSelectedFragment() == viewModel.getProfileFragment() ?
                2 : 3);
    }

    private void setBottomNavigationListener(NavigationBarView.OnItemSelectedListener listener) {
        bottomNavigationView.setOnItemSelectedListener(listener);
    }

    private final NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView
            .OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // de incercat cu ternary operator



            //

            if (item.getItemId() == R.id.nav_home) {
                if (viewModel.getSelectedFragment() != viewModel.getHomeFragment()) {
                    fragmentManager.beginTransaction()
                            .hide(viewModel.getSelectedFragment())
                            .show(viewModel.getHomeFragment())
                            .commit();
                    viewModel.setSelectedFragment(viewModel.getHomeFragment());
                }
            } else if (item.getItemId() == R.id.nav_favorites) {
                if (viewModel.getSelectedFragment() != viewModel.getFavoritesFragment()) {
                    fragmentManager.beginTransaction()
                            .hide(viewModel.getSelectedFragment())
                            .show(viewModel.getFavoritesFragment())
                            .commit();
                    viewModel.setSelectedFragment(viewModel.getFavoritesFragment());
                }
            } else if (item.getItemId() == R.id.nav_profile) {
                Intent intent = new Intent(MainActivity.this, CountySelector.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.nav_settings) {
                if (viewModel.getSelectedFragment() != viewModel.getSettingsFragment()) {
                    fragmentManager.beginTransaction()
                            .hide(viewModel.getSelectedFragment())
                            .show(viewModel.getSettingsFragment())
                            .commit();
                    viewModel.setSelectedFragment(viewModel.getSettingsFragment());
                }
            }

            return true;
        }
    };
}