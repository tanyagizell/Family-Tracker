package il.ac.huji.familytracker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Omer on 10/09/2015.
 */
public class FTNotifiableActivity extends ActionBarActivity {
    public static String PHONE_RECOG_REGEX_STRING = "^(05)[0-9][0-9]{7}$";
    protected FTStarter m_appMyApp;
    protected FTDataSource m_dsActivityDataAccess;
    protected FTCountDownTimer m_tmrNotifWaitTool;
    private boolean m_blnIsWaiting;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_blnIsWaiting = false;
        m_appMyApp = (FTStarter) this.getApplicationContext();
        m_tmrNotifWaitTool = new FTCountDownTimer(FTCountDownTimer.MAX_WAIT_FOR_RESPONSE, FTCountDownTimer.WAIT_INTERVAL_UPDATE);
    }

    protected void onResume() {
        super.onResume();
        m_dsActivityDataAccess = new FTDataSource(this);
        m_appMyApp.setCurrentActivity(this);
        m_appMyApp.NotifyReceiversAppUp(m_dsActivityDataAccess);
    }

    protected void onPause() {
        clearReferences();
        m_appMyApp.NotifyReceiversAppDown(m_dsActivityDataAccess);
        super.onPause();
    }

    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    /*
    to be called when a request is made to parse which should wait for response
    should be overriden for extended behavior upon start of waiting period
     */
    protected void StartWaitForResponse() {
        m_blnIsWaiting = true;
        m_tmrNotifWaitTool.start();
    }

    /*
    should be called from the broadcastreciever at appropriate times when response to request was supplied
     */
    protected void StopTimerAbruptly() {
        if (m_blnIsWaiting) {
            m_tmrNotifWaitTool.cancel();
            m_blnIsWaiting = false;
        }
    }
    public FTDataSource getDataSource() {
        return m_dsActivityDataAccess;
    }

    private void clearReferences() {
        Context currActivity = m_appMyApp.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
            m_appMyApp.setCurrentActivity(null);
    }

    /*
    a method that is called when timer reached zero -meaning-no response received
    should be extended per activity for extended use cases
     */
    private void NotifyCountDownFinished() {
        m_blnIsWaiting = false;
    }

    protected boolean ValidateInput(String p_strContent, String p_strPattern) {
        Pattern ptrnToExec = Pattern.compile(p_strPattern);
        boolean blnRetVal = p_strContent.isEmpty();
        if ((!blnRetVal) && (ptrnToExec.matcher(p_strContent).matches())) {
            blnRetVal = true;
        }
        return blnRetVal;
    }

    private class FTCountDownTimer extends CountDownTimer {
        public static final long MAX_WAIT_FOR_RESPONSE = 15000;
        public static final long WAIT_INTERVAL_UPDATE = 1000;

        public FTCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            NotifyCountDownFinished();
        }
    }
}
