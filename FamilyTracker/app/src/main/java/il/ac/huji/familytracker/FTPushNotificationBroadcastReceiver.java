package il.ac.huji.familytracker;

import com.parse.ParsePushBroadcastReceiver;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tanyagizell on 25/08/2015.
 *
 * Customized push broadcast receiver
 * Handles applications push notifications
 */
public class FTPushNotificationBroadcastReceiver extends ParsePushBroadcastReceiver{
    private static final int NOTIFICATION_ID = 25;
    private static final String NOTIFICATION_CONTENT_FORMAT = "%s %s %s";
    private static final String NOTIFICATION_ARRIVED_AT_SEGMENT = "arrived at";
    private static final String NOTIFICATION_LEFT_SEGMENT = "left";
    private FTDataSource m_dsLocalDataAccessor;

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        m_dsLocalDataAccessor = new FTDataSource(context);
        m_dsLocalDataAccessor.OpenToRead();
        Boolean blnIsAppOn = m_dsLocalDataAccessor.GetAppActivationStatus();
        FTParsedNotification.enmNotificationTypes enmNotifType = null;
        JSONObject json = null;
        try {
            json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            enmNotifType = FTParsedNotification.enmNotificationTypes.valueOf(json.getString("alert"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!blnIsAppOn) {
            switch (enmNotifType) {
                case GEOFENCE_ALERT:
                    FTNotification ntfCurrNotification = (FTNotification) (FTNotificationParser.parseGeofenceAlert(json, m_dsLocalDataAccessor).get(0));
                    FTBroadcastReceiversLogic.InsertGeofenceNotifToDB(ntfCurrNotification, m_dsLocalDataAccessor);
                    PostNotificationToBar(ntfCurrNotification, context);
                    break;
                case CURRENT_LOC_REQUEST:
                    FTNotificationParser.parseCurrentLocRequest(json);
                    break;
            }
        }
        super.onPushReceive(context, intent);

    }

    private void PostNotificationToBar(FTNotification ntfCurrNotification, Context context) {
        m_dsLocalDataAccessor.OpenToRead();
        int nNotifIdForCurrent = m_dsLocalDataAccessor.GetLastUsedNotifId() + 1;
        m_dsLocalDataAccessor.close();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle("Family Tracker")
                .setContentText(ConstructNotificationDisplay(ntfCurrNotification));
        Intent resultIntent = new Intent(context, FTWelcomeActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(FTWelcomeActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(nNotifIdForCurrent, mBuilder.build());
        m_dsLocalDataAccessor.OpenToWrite();
        m_dsLocalDataAccessor.UpdateLastUsedNotifId(nNotifIdForCurrent);
        m_dsLocalDataAccessor.close();

    }

    private String ConstructNotificationDisplay(FTNotification p_ntfDataContainer) {
        return String.format(NOTIFICATION_CONTENT_FORMAT, p_ntfDataContainer.getSubjectName(), getNotificationActionString(p_ntfDataContainer), p_ntfDataContainer.getNotificationLocationDisplayName());
    }

    private String getNotificationActionString(FTNotification p_ntfDataContainer) {
        return p_ntfDataContainer.getType() == FTNotification.FTNotifStateENUM.ARRIVAL ? NOTIFICATION_ARRIVED_AT_SEGMENT : NOTIFICATION_LEFT_SEGMENT;
    }
    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        return super.getNotification(context, intent);
    }

}
