package com.example.banat_travel_app;

public class MyCustomVariables {
    private static final String SHARED_PREFERENCES_FILE_NAME = "BANAT_TRAVEL";
    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int MINIMUM_NUMBER_OF_NAME_CHARACTERS = 2;
    private static final int MINIMUM_NUMBER_OF_PASSWORD_CHARACTERS = 7;
    private static final int NUMBER_OF_WAITING_MILLISECONDS = 30000;
    private static final int COUNTDOWN_INTERVAL_MILLISECONDS = 1000;

    public static String getSharedPreferencesFileName() {
        return SHARED_PREFERENCES_FILE_NAME;
    }

    public static int getMaximumNumberOfAttempts() {
        return MAXIMUM_NUMBER_OF_ATTEMPTS;
    }

    public static int getMinimumNumberOfNameCharacters() {
        return MINIMUM_NUMBER_OF_NAME_CHARACTERS;
    }

    public static int getMinimumNumberOfPasswordCharacters() {
        return MINIMUM_NUMBER_OF_PASSWORD_CHARACTERS;
    }

    public static int getNumberOfWaitingMilliseconds() {
        return NUMBER_OF_WAITING_MILLISECONDS;
    }

    public static int getCountdownIntervalMilliseconds() {
        return COUNTDOWN_INTERVAL_MILLISECONDS;
    }
}