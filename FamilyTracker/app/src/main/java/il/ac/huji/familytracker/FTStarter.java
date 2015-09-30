package il.ac.huji.familytracker;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tanyagizell on 25/08/2015.
 *
 * project starter subclass. will run before any other activity
 */
public class FTStarter extends Application{

    //TODO ADD the phone number after the installation sequence
    public static final String INSTALL_PHONE_NO_FIELD = "FTPhoneNumberField";
    FTInAppBroadcastReceiver m_pbrInAppReceiver;
    FTDataSource m_dsCreationTimeDataAccess;
    private Activity m_aCurrentActivity = null;
    private boolean m_blnIsInAppReceiverOn;

    public Activity getCurrentActivity() {
        return m_aCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.m_aCurrentActivity = mCurrentActivity;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        m_blnIsInAppReceiverOn = false;
        //Enable local datastore
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, (String) getResources().getText(R.string.parse_app_id),
                (String) getResources().getText(R.string.parse_client_key));
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        m_pbrInAppReceiver = new FTInAppBroadcastReceiver();
        m_dsCreationTimeDataAccess = new FTDataSource(getApplicationContext());
        NotifyReceiversAppUp(m_dsCreationTimeDataAccess);
    }

    public void NotifyReceiversAppDown(FTDataSource m_dsActivityDataAccess) {
        if (m_blnIsInAppReceiverOn) {
            this.unregisterReceiver(m_pbrInAppReceiver);
            m_blnIsInAppReceiverOn = false;
        }
        SetAppState(m_dsActivityDataAccess, false);
    }

    public void NotifyReceiversAppUp(FTDataSource m_dsActivityDataAccess) {
        if (!m_blnIsInAppReceiverOn) {
            IntentFilter ifActionsToCatch = new IntentFilter();
            ifActionsToCatch.addAction(ParsePushBroadcastReceiver.ACTION_PUSH_RECEIVE);
            ifActionsToCatch.addAction(ParsePushBroadcastReceiver.ACTION_PUSH_OPEN);
            ifActionsToCatch.addAction(ParsePushBroadcastReceiver.ACTION_PUSH_DELETE);
            this.registerReceiver(m_pbrInAppReceiver, ifActionsToCatch);
            m_blnIsInAppReceiverOn = true;
        }
        SetAppState(m_dsActivityDataAccess, true);

    }

    public void SetAppState(FTDataSource p_dsActivityDataAccess, boolean p_blnToSetTo) {
        p_dsActivityDataAccess.OpenToWrite();

        Boolean blnIsStaticOpen = p_dsActivityDataAccess.GetAppActivationStatus();
        if (blnIsStaticOpen != p_blnToSetTo) {
            p_dsActivityDataAccess.UpdateAppState(p_blnToSetTo);
        }
        p_dsActivityDataAccess.close();
    }

    private class FTInAppBroadcastReceiver extends ParsePushBroadcastReceiver {
        private FTStarter m_appFamTrackerRelator;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (m_appFamTrackerRelator == null) {
                m_appFamTrackerRelator = (FTStarter) context;
            }
            try {
                JSONObject jsnNotifData = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                FTParsedNotification prsntfParsedData = FTNotificationParser.parseNotification(jsnNotifData, ((FTNotifiableActivity) m_appFamTrackerRelator.m_aCurrentActivity).getDataSource());
                HandleNotificationAccordingly(prsntfParsedData);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private void HandleNotificationAccordingly(FTParsedNotification arrobjParsedData) {
            //TODO according to the title of the notification -decide how to procede
            //TODO -if geofence alert - if at log window or welcome window -update display , otherwise simply insert into DB

            //TODO if response to current location request -check which window we're in ,and use data to open necessery activity with received input
            
            //TODO-if request of current location - we're in passive user - should perform actions to acquire current location and generate a response push notification containing the data
            //TODO-create geofence - implemented in the parser itself .note - check if insert a validation prior to creation ,to check
        }
    }
}
