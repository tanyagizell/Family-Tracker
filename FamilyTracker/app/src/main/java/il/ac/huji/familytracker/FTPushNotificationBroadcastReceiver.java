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

/**
 * Created by Tanyagizell on 25/08/2015.
 *
 * Customized push broadcast receiver
 * Handles applications push notifications
 */
public class FTPushNotificationBroadcastReceiver extends ParsePushBroadcastReceiver{
    private static final int NOTIFICATION_ID = 25;

    private FTDataSource m_dsLocalDataAccessor;

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        m_dsLocalDataAccessor = new FTDataSource(context);
        m_dsLocalDataAccessor.OpenToRead();
        Boolean blnIsAppOn = m_dsLocalDataAccessor.GetStaticReceiverStatus();
        if (!blnIsAppOn) {

        }
        super.onPushReceive(context, intent);
        CharSequence text = "push recieved";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            String notificationText = json.getString("alert");

            //TODO parse the notification


            NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.notification_template_icon_bg)
                    .setContentTitle("ParsePushTest")
                    .setContentText(notificationText);
            Intent resultIntent = new Intent(context,FTWelcomeActivity.class);

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
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
