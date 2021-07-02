package com.example.banat_travel_app.MainPart;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

public class MainPartViewModel extends ViewModel {
    private final HomeFragment homeFragment = new HomeFragment();
    private final FavoritesFragment favoritesFragment = new FavoritesFragment();
    private Fragment selectedFragment = homeFragment;

    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    public FavoritesFragment getFavoritesFragment() {
        return favoritesFragment;
    }

    public Fragment getSelectedFragment() {
        return selectedFragment;
    }

    public void setSelectedFragment(Fragment selectedFragment) {
        this.selectedFragment = selectedFragment;
    }
}