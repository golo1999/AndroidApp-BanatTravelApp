package com.example.banat_travel_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class MyCustomMethods {
    public static void showMessage(final Context context, final String message, final int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static void hideStatusBar(final Activity parentActivity) {
        parentActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        parentActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void goBackWithAnimationInDirection(final Activity parentActivity, final String direction) {
        parentActivity.finish();

        parentActivity.overridePendingTransition(direction.equals("left") ?
                        R.anim.slide_in_left : R.anim.slide_in_right,
                direction.equals("left") ? R.anim.slide_out_right : R.anim.slide_out_left);
    }

    public static void goToActivityInDirection(final Activity currentActivity,
                                               final Class<? extends Activity> nextActivity, final String direction) {
        currentActivity.startActivity(new Intent(currentActivity, nextActivity));
        currentActivity.overridePendingTransition(direction.equals("left") ?
                R.anim.slide_in_left : R.anim.slide_in_right, direction.equals("left") ?
                R.anim.slide_out_right : R.anim.slide_out_left);
    }

    public static void emptyField(final View view) {
        ((EditText) view).setText("");
    }

    public static boolean emailAddressIsValid(final String emailAddress) {
        return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }

    public static boolean nameIsValid(final String name, final int minimumNumberOfCharacters) {
        if (name.equals("") || name.length() < minimumNumberOfCharacters ||
                name.charAt(0) < 65 || name.charAt(0) > 90) {
            return false;
        }

        for (char character : name.toCharArray()) {
            if (character != 32 && (character < 65 || character > 90) && (character < 97 || character > 122)) {
                return false;
            }
        }

        return true;
    }
}
