package il.ac.huji.familytracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Omer on 10/09/2015.
 */
public class FTNotifiableActivity extends ActionBarActivity {
    protected FTStarter m_appMyApp;
    protected FTDataSource m_dsActivityDataAccess;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_appMyApp = (FTStarter) this.getApplicationContext();
    }

    protected void onResume() {
        super.onResume();
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

    public FTDataSource getDataSource() {
        return m_dsActivityDataAccess;
    }

    private void clearReferences() {
        Activity currActivity = m_appMyApp.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
            m_appMyApp.setCurrentActivity(null);
    }
}
