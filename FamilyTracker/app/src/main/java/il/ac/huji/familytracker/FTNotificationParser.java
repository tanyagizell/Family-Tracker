package il.ac.huji.familytracker;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.JetPlayer;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tanyagizell on 02/09/2015.
 *
 * This class is responsible for parsing push notification from the parce backend
 */
public class FTNotificationParser {

    /*********************
     ***** constants *****
     ********************/

    public enum enmNotificationTypes {
        CREATE_GEOFENCE, GEOFENCE_ALERT, CURRENT_LOC_REQUEST, CURRENT_LOC_RESPONSE
    };


    //notification data keys
    private static final String NOTIFICATION_TITLE = "alert";
    private static final String COORDINATES_TITLE = "location";

    //parsing indices
    private static final int LAT_INDEX = 0;
    private static final int LNG_INDEX = 1;


    /*********************
     ***** Globals *****
     ********************/

    Context context; // TODO need to decide what the context is


    /* Function: parseNotification
     * Parses an incoming notification
     */
    public static void parseNotification(JSONObject jsonObject){

        String notificationType ="";
        try {
            notificationType = jsonObject.getString(NOTIFICATION_TITLE);
        }

        catch (Exception e){
            //TODO exception
        }

        switch (enmNotificationTypes.valueOf(notificationType))
        {
            case CREATE_GEOFENCE:
                parseCreateGeofence(jsonObject);
                break;
            case GEOFENCE_ALERT:
                parseGeofenceAlert(jsonObject);
                break;
            case CURRENT_LOC_REQUEST:
                parseCurrentLocRequest(jsonObject);
                break;
            case CURRENT_LOC_RESPONSE:
                parseCurrentLocRequest(jsonObject);
                break;
            default:
        }

    }

    /*Function:
    * "alert":"CREATE_GEOFENCE"
    *  "location":"lat,lang"
    * */
    private static void parseCreateGeofence(JSONObject jsonObject){
        //TODO finish


        //get access to geofence manager
        FTGeofenceManager GFManager = FTGeofenceManager.getInstance();

        String latlng = "";
        String id = ""; //TODO id?

        try {
            latlng = jsonObject.getString("location");
            String[] latLngStrArray = latlng.split(",");
            Double lat = Double.parseDouble(latLngStrArray[LAT_INDEX]);
            Double lng = Double.parseDouble(latLngStrArray[LNG_INDEX]);

            GFManager.createGeofence(id, lat, lng);

            GeofencingRequest gr = GFManager.getGeofencingRequest();
            PendingIntent pi = GFManager.getGeofencePendingIntent();

            GoogleApiClient googleApiClient = GFManager.getGoogleApiClient();


            LocationServices.GeofencingApi.addGeofences(googleApiClient,gr,pi);



        } catch (JSONException e) {
            e.printStackTrace();
        }





    };

    /*Function: parseGeofenceAlert
    * Parses a notification alerting a geofence was crossed.
    * Json structure expected:
    * "alert":"GEOFENCE_ALERT"
    * "location":"lat,lang"
    * "action":"departure/arrival"
    *
    * */
    private static void parseGeofenceAlert(JSONObject jsonObject){
        //TODO
    };

    /*Function:
    * "alert":"CURRENT_LOC_REQUEST"
    * */
    private static void parseCurrentLocRequest(JSONObject jsonObject){
        //TODO handle custom events

    };

    /*Function:
    * "alert":"CURRENT_LOC_RESPONSE"
    * "location":"lat,lang"
    * */
    private void parseCurrentLocResponse(JSONObject jsonObject){
        //TODO
    };
}
