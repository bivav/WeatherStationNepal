package com.weatherstation.weatherstation;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Bivav on 7/17/2017.
 */

public class FirebasePersistenceHandler extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //startService(new Intent(this, WeatherService.class));

    }
}
