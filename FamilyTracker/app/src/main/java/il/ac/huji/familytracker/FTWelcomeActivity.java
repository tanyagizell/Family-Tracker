package il.ac.huji.familytracker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

//TODO handle opening of context menu for long clicking on family
//TODO handle receiving log notification while welcome menu is open
//TODO define logic for adding a family
public class FTWelcomeActivity extends FTNotifiableActivity {
    private static final int MAX_NOTIF_IN_WELCOME = 3;
    ArrayAdapter<Family> m_adptFamiliesAdapter;
    ArrayList<Family> m_arrfmlFamilies;
    FTLogAdapter m_lgadptLogPeekAdapter;
    ArrayList<FTNotification> m_arrntfMostRecentNotifications;
    FTDataSource m_dsDataRetreiver;
    ListView m_lvLogPeek;
    ListView m_lvFamilies;
    Button m_btnAddFamily;
    Button m_btnOpenLogActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftwelcome);
        m_lvFamilies = (ListView) findViewById(R.id.lvFamilies);
        m_lvLogPeek = (ListView) findViewById(R.id.lvShortLogView);
        m_btnAddFamily = (Button) findViewById(R.id.btnAddFamily);
        m_btnOpenLogActivity = (Button) findViewById(R.id.btnOpenLogActivity);
        m_dsDataRetreiver = new FTDataSource(this);
        m_dsDataRetreiver.OpenToRead();
        m_arrfmlFamilies = m_dsDataRetreiver.GetFamiliesFromDB();
        m_arrntfMostRecentNotifications = m_dsDataRetreiver.GeteMostRecentRequiredNumberOfNotifications(MAX_NOTIF_IN_WELCOME);
        m_adptFamiliesAdapter = new ArrayAdapter<Family>(this, android.R.layout.simple_list_item_1, m_arrfmlFamilies);
        m_lvFamilies.setAdapter(m_adptFamiliesAdapter);
        m_btnOpenLogActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intntLog = new Intent(getApplicationContext(), FTLogActivity.class);
                startActivity(intntLog);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ftwelcome, menu);
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
