package il.ac.huji.familytracker;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

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
    public FTGeofenceListenerService(){
        super("name");}


    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            //TODO error?
//            String errorMessage = GeofenceErrorMessages.getErrorString(this,
//                    geofencingEvent.getErrorCode());
//            Log.e(TAG, errorMessage);
            return;
        }

        Location loc = geofencingEvent.getTriggeringLocation();
        double lat = loc.getLatitude();
        double lng = loc.getLongitude();

        String latLng = "";
        latLng += lat;
        latLng+=",";
        latLng+= lng;

        String date = "";
        Calendar c = Calendar.getInstance();
        date += c.get(Calendar.DAY_OF_MONTH);
        date +="//";
        date += c.get(Calendar.MONTH);
        date +="//";
        date += c.get(Calendar.YEAR);

        date +=" ";
        date += c.get(Calendar.HOUR_OF_DAY);
        date +=":";
        date += c.get(Calendar.MINUTE);
        date +=":";
        date += c.get(Calendar.SECOND);


        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();



            FTDataSource dts = new FTDataSource(FTStarter.getAppContext());
            dts.OpenToRead();
            String number= dts.GetCurPhone();
            dts.close();


            String geoAction = "";
            if ( geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
                geoAction = "ARRIVAL";
            else
                geoAction = "DEPARTURE";



            // Send notification and log the transition details.
            sendNotification(latLng,date,number,geoAction);
        } else {
            // TODO Log the error?
//            Log.e(TAG, getString(R.string.geofence_transition_invalid_type,
//                    geofenceTransition));
        }

    }

    /*
    * Function: sendNotification
    *
    * Creates a geofence alret with the followingJSON structure :
    *
    *       "alert":"GEOFENCE_ALERT"
    *       "location":"lat,lang"
    *       "action":"departure/arrival"
    *       "sender_phone":"phone number"
    *       "timestamp":"date and time"
    *
    *  And sends it to the followers of the location corresponding to
    *  the given Geofence
    * */
    private void sendNotification(String latLng, String timestamp,String number, String geoAction) {


        String loc = latLng;
        String phoneNumber = number;
        String action = geoAction;
        String time = timestamp;

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


        //Get destination of notification (the client that needs to be notified of this notification)
        FTDataSource dataSource = new FTDataSource(this);


        //send the Geofence alert to the monitoring client
        String alertDest = FTGeofenceManager.getInstance().getAlertDestination();
        //TODO send the notification

    }

    private String getGeofenceTransitionDetails(FTGeofenceListenerService ftGeofenceListenerService,
                                                int geofenceTransition, List<Geofence> triggeringGeofences) {

        String details = "";
        //TODO parse transition to notification string

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
            details = FTNotificationParser.GEOFENCE_ENTER;
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            details = FTNotificationParser.GEOFENCE_EXIT;
        }
        details += ",";



        if (triggeringGeofences.size() == 1){
            triggeringGeofences.get(0).getRequestId();
        }

        else {
            //TODO might have to handle overlapping geofences
        }





        return details;
    }

}


