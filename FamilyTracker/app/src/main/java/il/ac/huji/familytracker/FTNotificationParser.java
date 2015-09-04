package il.ac.huji.familytracker;


import android.media.JetPlayer;

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


    /* Function: parseNotification
     * Parses an incoming notification
     */
    public static void parseNotification(JSONObject jsonObject){

        String notificationType ="";
        try {
            notificationType = jsonObject.getString("alert");
        }

        catch (Exception e){

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
        //TODO
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
