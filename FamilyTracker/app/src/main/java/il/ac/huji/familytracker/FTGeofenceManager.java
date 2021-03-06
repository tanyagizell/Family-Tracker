package il.ac.huji.familytracker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

/**
 * Created by Tanyagizell on 01/09/2015.
 *
 *
 * This class manages the application's Geofence interactions. Since there should be only one such
 * Manager, this class implements singleton design pattern
 */
public class FTGeofenceManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    //constants
    private static final float GEOFENCE_RADIUS_IN_METERS = 50;
    private static final String TAG = "GEOFENCE_MANAGER";

    //instance
    private static FTGeofenceManager ftGeofenceManager = new FTGeofenceManager();

    //context TODO decide context
    Context context;
    private GoogleApiClient apiClient;
    private PendingIntent pendingIntent;
    private String alertDestination; //the recipient of the GEOFENCE alerts

    //geofence list
    private  List<Geofence> geofenceList;
    private ArrayList<String> notifyLocationList;

    private FTGeofenceManager()
    {

        context = FTStarter.getAppContext();
        geofenceList = new ArrayList<Geofence>();
        notifyLocationList = new ArrayList<String>();
        //api client
        apiClient =  new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        apiClient.connect();

        Log.v(TAG,"starting api client");

    }

    /* Static 'instance' method */
    public static FTGeofenceManager getInstance( ) {
        return ftGeofenceManager;
    }

    public void createGeofence(double lat, double lng)
    {
        //get an ID for geofence
        String id = UUID.randomUUID().toString();

        geofenceList.add(new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(lat, lng, GEOFENCE_RADIUS_IN_METERS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build());

    }

    public GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    public PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (pendingIntent != null) {
            return pendingIntent;
        }
        Intent intent = new Intent(context, FTGeofenceListenerService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(context, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    public GoogleApiClient getGoogleApiClient(){
        return apiClient;
    }

    public void stopGeofence(){
        LocationServices.GeofencingApi.removeGeofences(
                apiClient,
                // This is the same pending intent that was used in addGeofences().
                pendingIntent);
//        ).setResultCallback(this); // Result processed in onResult().
    }

    public String getAlertDestination(){
        return alertDestination;
    }

    public void addLocationRequester(String phone_number){
        notifyLocationList.add(phone_number);
    }

    @Override
    public void onConnected(Bundle bundle) {


        Log.v(TAG, "Connected to api client");


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(TAG,"Connection failed");
    }
}
