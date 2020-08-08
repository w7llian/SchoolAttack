package com.example.schoolattack.app;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;

public class MyApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
    }
}