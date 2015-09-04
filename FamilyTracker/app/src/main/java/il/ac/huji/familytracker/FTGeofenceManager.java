package il.ac.huji.familytracker;

import com.google.android.gms.location.Geofence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanyagizell on 01/09/2015.
 *
 *
 * This class manages the application's Geofence interactions
 */
public class FTGeofenceManager {


    private static final double GEOFENCE_RADIUS_IN_METERS = 200;


    List<Geofence.Builder> geofenceList;

    public FTGeofenceManager()
    {
        geofenceList = new ArrayList<Geofence.Builder>();
    }

    public void addGeofence(String id, double lat, double lng)
    {
        geofenceList.add(new Geofence.Builder());
                // Set the request ID of the geofence. This is a string to identify this
//                // geofence.
//                .setRequestId(id)
//
//                .setCircularRegion(
//                       lat,
//                       lng,
//                       GEOFENCE_RADIUS_IN_METERS
//                )
//                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
//                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
//                        Geofence.GEOFENCE_TRANSITION_EXIT)
//                .build());
    }


}
