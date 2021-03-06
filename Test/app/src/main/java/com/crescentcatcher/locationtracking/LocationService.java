package com.crescentcatcher.locationtracking;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;

import timber.log.Timber;

/**
 * Created by samrezikram on 11/20/17.
 */
public class LocationService extends Service {

    private GoogleLocationService googleLocationService;

    @Override
    public void onCreate() {
        super.onCreate();
        //start the handler for getting locations
        //create component

        updateLocation(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    //get current location os user
    private void updateLocation(Context context) {
        googleLocationService = new GoogleLocationService(context, new LocationUpdateListener() {
            @Override
            public void canReceiveLocationUpdates() {

                Timber.e("LocationService Can recieve location update");
            }

            @Override
            public void cannotReceiveLocationUpdates() {
                Timber.e("LocationService Cannot recieve location update");

            }

            //update location to our servers for tracking purpose
            @Override
            public void updateLocation(Location location) {
                if (location != null ) {
                    Timber.e("LocationService updated location %1$s %2$s", location.getLatitude(), location.getLongitude());

                }
            }


        });
        googleLocationService.startUpdates();
    }


    IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        public LocationService getServerInstance() {
            return LocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //stop location updates on stopping the service
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleLocationService != null) {
            googleLocationService.stopLocationUpdates();
        }
    }
}

