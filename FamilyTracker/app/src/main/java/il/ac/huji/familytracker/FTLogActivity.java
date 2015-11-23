package il.ac.huji.familytracker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;


public class FTLogActivity extends FTNotifiableActivity {

    public ArrayList<FTNotification> m_arrNotifications;
    FTLogAdapter _logEntriesAdapter;
    ListView lvNotificationsDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftlog);
        lvNotificationsDisplay = (ListView) findViewById(R.id.NotificationsListView);
        m_dsActivityDataAccess = new FTDataSource(this);
        m_dsActivityDataAccess.OpenToRead();
        m_arrNotifications = m_dsActivityDataAccess.GeteNotificationsItemsFromDB();
        m_dsActivityDataAccess.close();
        _logEntriesAdapter = new FTLogAdapter(this, m_arrNotifications);
        lvNotificationsDisplay.setAdapter(_logEntriesAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ftlog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
