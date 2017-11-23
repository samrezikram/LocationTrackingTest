package com.crescentcatcher.locationtracking;

import android.location.GpsStatus;
import android.location.Location;

/**
 * Created by samrezikram on 11/20/17.
 */

public interface LocationUpdateListener {

    /**
     * Called immediately the service starts if the service can obtain location
     */
    void canReceiveLocationUpdates();

    /**
     * Called immediately the service tries to start if it cannot obtain location - eg the user has disabled wireless and
     */
    void cannotReceiveLocationUpdates();

    /**
     * Called whenever the location has changed (at least non-trivially)
     * @param location
     */
    void updateLocation(Location location);

}

