package il.ac.huji.familytracker;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 * This class is responsible for monitoring the clients arrival at a given Geofence
 *
 */
public class FTGeofenceListenerService extends IntentService {


    private static final String TAG = "GEOFENCE_LISTENER";

    public FTGeofenceListenerService(String name) {
        super(name);
    }

    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            //TODO error?
//            String errorMessage = GeofenceErrorMessages.getErrorString(this,
//                    geofencingEvent.getErrorCode());
//            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );

            // Send notification and log the transition details.
            sendNotification(geofenceTransitionDetails);
            Log.i(TAG, geofenceTransitionDetails);
        } else {
            // TODO Log the error?
//            Log.e(TAG, getString(R.string.geofence_transition_invalid_type,
//                    geofenceTransition));
        }

    }

    /*
    * Funcion: sendNotification
    *
    * Creates a geofence alrat with the JSON structure :
    *   "alert":"GEOFENCE_ALERT"
    *   "location":"lat,lang"
    *   "action":"departure/arrival"
    *   "sender_phone":"phone number"
    *   "timestamp":"date and time"
    *  And sends it to the followers of the location corresponding to
    *  the given geofence
    * */
    private void sendNotification(String geofenceTransitionDetails) {

        //todo parse geofenceTransitionDetails;
        String loc = ""; //todo get loc
        String phoneNumber = ""; //todo get phone number?
        String action = ""; //todo getaction
        String time = ""; //todo get time

        try {
        //create a JSONObject to to hold the notification content
        JSONObject notificationObj = new JSONObject();

            notificationObj.put(FTNotificationParser.NOTIFICATION_TITLE,FTParsedNotification.enmNotificationTypes.GEOFENCE_ALERT);
            notificationObj.put(FTNotificationParser.COORDINATES_TITLE,loc);
            notificationObj.put(FTNotificationParser.GEOFENCE_ACTION_TITLE,action);
            notificationObj.put(FTNotificationParser.PHONE_NUMBER_TITLE,phoneNumber);
            notificationObj.put(FTNotificationParser.TIMESTAMP_TITLE,time);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        int locID = 0; //todo get id of relevant location

        FTDataSource dataSource = new FTDataSource(this);

        //read followers from DB
        dataSource.OpenToRead();
        ArrayList<FamilyMember> followers = dataSource.GetFamilyMembersRegisteredForLoc(locID);
        dataSource.close();

        //send the Geofence alert to each one of the follower, if there are any
        for (int i = 0; i<followers.size(); i++){
            //TODO send notification
        }

    }

    private String getGeofenceTransitionDetails(FTGeofenceListenerService ftGeofenceListenerService, int geofenceTransition, List triggeringGeofences) {

        String details = "";
        //TODO parse transition to notification string

        return details;
    }

}


