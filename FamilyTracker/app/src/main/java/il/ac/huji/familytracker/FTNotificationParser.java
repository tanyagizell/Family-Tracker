package il.ac.huji.familytracker;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.JetPlayer;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import il.ac.huji.familytracker.FTParsedNotification;

import java.util.ArrayList;

/**
 * Created by Tanyagizell on 02/09/2015.
 * <p/>
 * This class is responsible for parsing push notification from the parce backend
 */
public class FTNotificationParser {
    //notification data keys
    public static final String NOTIFICATION_TITLE = "alert";
    public static final String COORDINATES_TITLE = "location";
    public static final String PHONE_NUMBER_TITLE = "sender phone";
    public static final String GEOFENCE_ACTION_TITLE = "action";
    public static final String TIMESTAMP_TITLE = "timestamp";

    //parsing indices
    private static final int LAT_INDEX = 0;
    private static final int LNG_INDEX = 1;


    /**
     * ******************
     * **** Globals *****
     * ******************
     */

    Context context; // TODO need to decide what the context is
    // no use for globals since this is a static class....should be discussed


    /* Function: parseNotification
     * Parses an incoming notification
     */
    public static FTParsedNotification parseNotification(JSONObject jsonObject, FTDataSource p_dsDataRetriever) {

        String notificationType = "";

        try {
            notificationType = jsonObject.getString(NOTIFICATION_TITLE);
        } catch (Exception e) {
            //TODO exception
        }
        FTParsedNotification.enmNotificationTypes enmNotifType = FTParsedNotification.enmNotificationTypes.valueOf(notificationType);

        FTParsedNotification objRetVal = new FTParsedNotification(enmNotifType);
        ArrayList<Object> arrObjArgs = null;
        switch (enmNotifType) {
            case CREATE_GEOFENCE:
                parseCreateGeofence(jsonObject);
                break;
            case GEOFENCE_ALERT:
                arrObjArgs = parseGeofenceAlert(jsonObject, p_dsDataRetriever);
                break;
            case CURRENT_LOC_REQUEST:
                parseCurrentLocRequest(jsonObject);
                break;
            case CURRENT_LOC_RESPONSE:
                arrObjArgs = parseCurrentLocResponse(jsonObject);
                break;
            default:
        }
        objRetVal.setNotificationsArgs(arrObjArgs);
        return objRetVal;
    }

    /*Function:
    * "alert":"CREATE_GEOFENCE"
    *  "location":"lat,lang"
    * */
    private static void parseCreateGeofence(JSONObject jsonObject) {
        //TODO finish
        //TODO check if it is better , design wise ,to extract the actions taken here to a place intended for operations
        //and instead use this method to return operatable values


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


            LocationServices.GeofencingApi.addGeofences(googleApiClient, gr, pi);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    ;

    /*Function: parseGeofenceAlert
    * Parses a notification alerting a geofence was crossed.
    * Json structure expected:
    * "alert":"GEOFENCE_ALERT"
    * "location":"lat,lang"
    * "action":"departure/arrival"
    * "sender_phone":"phone number"
    * "timestamp":"date and time"
    * */
    private static ArrayList<Object> parseGeofenceAlert(JSONObject jsonObject, FTDataSource p_dsDataRetriever) {
        ArrayList<Object> arrobjRetVal = null;
        try {
            if (p_dsDataRetriever != null) {
                p_dsDataRetriever.OpenToRead();
                FamilyMember fmNotifSubject = p_dsDataRetriever.GetMemberByPhone(jsonObject.getString("sender_phone"));
                String strTimeStamp = jsonObject.getString("timestamp");
                String strType = jsonObject.getString("action");
                String strLatLng = jsonObject.getString("location");
                String strLocRetVal = p_dsDataRetriever.getLocNameByFamily(fmNotifSubject.getFamilyId(), strLatLng);
                p_dsDataRetriever.close();
                String strLocNameByFamily = strLocRetVal == null ? strLatLng : strLocRetVal;
                FTNotification ntfCurrentNotification = new FTNotification(strType, strTimeStamp, strLocNameByFamily, fmNotifSubject.getFamilyId(), fmNotifSubject.getName());
                arrobjRetVal.add(ntfCurrentNotification);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return arrobjRetVal;
        }

    }

    ;

    /*Function:
    * "alert":"CURRENT_LOC_REQUEST"
    * "sender_phone":"phone number"
    * */
    private static void parseCurrentLocRequest(JSONObject jsonObject) {
        //TODO handle custom events

        String requetingNumber = "";
        try {
           requetingNumber = jsonObject.getString(PHONE_NUMBER_TITLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FTGeofenceManager gfm = FTGeofenceManager.getInstance();
        GoogleApiClient apiClient = gfm.getGoogleApiClient();

        //TODO might have to do this on connect
        Location loc = LocationServices.FusedLocationApi.getLastLocation(
                apiClient);

        //Make the response
        String locStr = "";
        double lat = loc.getLatitude();
        double lng = loc.getLongitude();
        locStr+= lat + "," + lng;

        String currentNumber = ""; //TODO get phone number?
        try {

            //build a response with the structure
            /* "alert":"CURRENT_LOC_RESPONSE"
            * "ReturnValid":"boolean"
            * if returnValid is true
            * "location":"lat,lang"
            * "sender_phone":"phone number"
            */

            //create a Json object the hold the content of the response
            JSONObject respObj = new JSONObject();
            respObj.put(NOTIFICATION_TITLE,FTParsedNotification.enmNotificationTypes.CURRENT_LOC_RESPONSE);
            respObj.put("ReturnValid",true); //TODO ? what is this
            respObj.put(COORDINATES_TITLE,locStr);
            respObj.put(PHONE_NUMBER_TITLE,currentNumber);

            //TODO send notification with result
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    ;

    /*Function:
    * "alert":"CURRENT_LOC_RESPONSE"
    * "ReturnValid":"boolean"
    * if returnValid is true
    * "location":"lat,lang"
    * "sender_phone":"phone number"
    * */
    private static ArrayList<Object> parseCurrentLocResponse(JSONObject jsonObject) {
        ArrayList<Object> arrobjRetVal = null;
        String strPhone = null;
        try {
            arrobjRetVal = new ArrayList<>();
            boolean blnIsActionValid = jsonObject.getBoolean("ReturnValid");
            arrobjRetVal.add(blnIsActionValid);
            if (blnIsActionValid) {
                ArrayList<Double> arrCoords = ParseLocStringToCoords(jsonObject);
                Double dblLat = arrCoords.get(LAT_INDEX);
                Double dblLng = arrCoords.get(LNG_INDEX);
                arrobjRetVal.add(dblLat);
                arrobjRetVal.add(dblLng);
                arrobjRetVal.add(jsonObject.getString("sender_phone"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return arrobjRetVal;
        }

    }

    private static ArrayList<Double> ParseLocStringToCoords(JSONObject jsonObject) throws JSONException {
        String strLatLng = jsonObject.getString("location");
        String[] latLngStrArray = strLatLng.split(",");
        Double dblLat = Double.parseDouble(latLngStrArray[LAT_INDEX]);
        Double dblLng = Double.parseDouble(latLngStrArray[LNG_INDEX]);
        ArrayList<Double> arrdblRetVal = new ArrayList<>();
        arrdblRetVal.add(LAT_INDEX, dblLat);
        arrdblRetVal.add(LNG_INDEX, dblLng);
        return arrdblRetVal;

    }

    ;


}
