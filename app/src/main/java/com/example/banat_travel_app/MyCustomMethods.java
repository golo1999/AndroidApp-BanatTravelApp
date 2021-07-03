package com.example.banat_travel_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.banat_travel_app.Models.User;
import com.example.banat_travel_app.StartingPart.Activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class MyCustomMethods {
    public static void showMessage(@NonNull final Context context, @NonNull final String message, final int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static void closeKeyboard(@NonNull final Activity activity) {
        final View v = activity.getCurrentFocus();

        if (v != null) {
            final InputMethodManager manager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

    }

    public static void hideStatusBar(@NonNull final Activity parentActivity) {
        parentActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        parentActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void goBackWithAnimationInDirection(@NonNull final Activity parentActivity,
                                                      @NonNull final String direction) {
        parentActivity.finish();

        parentActivity.overridePendingTransition(direction.equals("left") ?
                        R.anim.slide_in_left : R.anim.slide_in_right,
                direction.equals("left") ? R.anim.slide_out_right : R.anim.slide_out_left);
    }

    public static void goToActivityInDirection(@NonNull final Activity currentActivity,
                                               @NonNull final Class<? extends Activity> nextActivity,
                                               @NonNull final String direction) {
        currentActivity.startActivity(new Intent(currentActivity, nextActivity));
        currentActivity.overridePendingTransition(direction.equals("left") ?
                R.anim.slide_in_left : R.anim.slide_in_right, direction.equals("left") ?
                R.anim.slide_out_right : R.anim.slide_out_left);
    }

    public static void goToActivityWithFadeAnimation(@NonNull final Activity currentActivity,
                                                     @NonNull final Class<? extends Activity> nextActivity) {
        currentActivity.startActivity(new Intent(currentActivity, nextActivity));
        currentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static void goToActivityInDirectionAndCloseCurrentOne(@NonNull final Activity currentActivity,
                                                                 @NonNull final Class<? extends Activity> nextActivity,
                                                                 @NonNull final String direction) {
        currentActivity.startActivity(new Intent(currentActivity, nextActivity));
        currentActivity.finish();
        currentActivity.overridePendingTransition(direction.equals("left") ?
                R.anim.slide_in_left : R.anim.slide_in_right, direction.equals("left") ?
                R.anim.slide_out_right : R.anim.slide_out_left);
    }

    public static void goToActivityWithFadeAnimationAndCloseCurrentOne(@NonNull final Activity currentActivity,
                                                                       @NonNull final Class<? extends Activity> nextActivity) {
        currentActivity.startActivity(new Intent(currentActivity, nextActivity));
        currentActivity.finish();
        currentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static void emptyField(final View view) {
        ((EditText) view).setText("");
    }

    public static boolean emailAddressIsValid(final String emailAddress) {
        return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }

    public static boolean nameIsValid(final String name, final int minimumNumberOfCharacters) {
        if (name == null || name.equals("") || name.length() < minimumNumberOfCharacters ||
                name.charAt(0) < 65 || name.charAt(0) > 90) {
            return false;
        }

        for (char character : name.toCharArray()) {
            if (character != 32 && character != 45 && (character < 65 || character > 90) &&
                    (character < 97 || character > 122)) {
                return false;
            }
        }

        return true;
    }

    public static void saveUserToSharedPreferences(@NonNull final SharedPreferences preferences,
                                                   final User user,
                                                   @NonNull final String key) {
        final SharedPreferences.Editor editor = preferences.edit();
        final Gson gson = new Gson();
        final String json = gson.toJson(user);

        editor.putString(key, json);
        editor.apply();
    }

    public static User retrieveUserFromSharedPreferences(@NonNull final SharedPreferences preferences,
                                                         @NonNull final String key) {
        final Gson gson = new Gson();
        final String json = preferences.getString(key, "");

        return gson.fromJson(json, User.class);
    }

    public static void removeUserFromSharedPreferences(@NonNull final SharedPreferences preferences,
                                                       @NonNull final String key) {
        preferences.edit().remove(key).apply();
    }

    public static boolean sharedPreferencesContainsKey(@NonNull final SharedPreferences preferences,
                                                       @NonNull final String key) {
        return preferences.contains(key);
    }

    public static void logOut(@NonNull final FirebaseAuth firebaseAuth, @NonNull final Activity activity) {
        // Firebase sign out method
        firebaseAuth.signOut();
        final Intent intent = new Intent(activity, LoginActivity.class);
        // ending all activities from stack
        activity.finishAffinity();
        // redirecting to the authentication activity
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
