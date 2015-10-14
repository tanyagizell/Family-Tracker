package il.ac.huji.familytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

//TODO handle opening of context menu for long clicking on family
//TODO handle receiving log notification while welcome menu is open
//TODO define logic for adding a family
public class FTWelcomeActivity extends FTNotifiableActivity {
    private static final int MAX_NOTIF_IN_WELCOME = 3;
    private static final int INSTALLTION_SEQUENCE_RESPONSE = 1;
    ArrayAdapter<Family> m_adptFamiliesAdapter;
    ArrayList<Family> m_arrfmlFamilies;
    FTLogAdapter m_lgadptLogPeekAdapter;
    ArrayList<FTNotification> m_arrntfMostRecentNotifications;
    FTDataSource m_dsDataRetreiver;
    ListView m_lvLogPeek;
    ListView m_lvFamilies;
    Button m_btnAddFamily;
    Button m_btnOpenLogActivity;
    private LinearLayout m_lnrlytFamilies;
    private LinearLayout m_lnrlytShortLog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ftwelcome);
        m_dsDataRetreiver = new FTDataSource(this);
        m_dsDataRetreiver.OpenToRead();
        Boolean blnIsInstalltionActivation = m_dsDataRetreiver.IsAppFirstActivation();
        m_dsDataRetreiver.close();

        if (blnIsInstalltionActivation) {
            Intent intntInstallation = new Intent(this, FTInstallationActivity.class);
            startActivityForResult(intntInstallation, INSTALLTION_SEQUENCE_RESPONSE);
        } else {
            ConstructWelcomeActivityDisplay(false);
        }
    }

    private void ConstructWelcomeActivityDisplay(Boolean p_blnIsFirstOpen) {
        m_dsDataRetreiver.OpenToRead();
        Boolean blnIsUserParent = m_dsDataRetreiver.IsParentUser();
        m_dsDataRetreiver.close();
        SetDisplayState(blnIsUserParent);
        if(!blnIsUserParent)
        {
            String strMessageToChild = null;
            if(p_blnIsFirstOpen)
            {
                strMessageToChild = (String) getResources().getText(R.string.Welcome_Thanks_For_Installing);
            }
            else
            {
                strMessageToChild = (String) getResources().getText(R.string.Welcome_No_Permissions);
            }
            Toast.makeText(this,strMessageToChild,Toast.LENGTH_LONG).show();
        }


    }

    private void SetDisplayState(Boolean blnIsUserParent) {
        m_lvFamilies = (ListView) findViewById(R.id.lvFamilies);
        m_lvLogPeek = (ListView) findViewById(R.id.lvShortLogView);
        m_btnAddFamily = (Button) findViewById(R.id.btnAddFamily);
        m_btnOpenLogActivity = (Button) findViewById(R.id.btnOpenLogActivity);
        m_lnrlytFamilies = (LinearLayout) findViewById(R.id.lnrExistingFamiliesGroup);
        m_lnrlytShortLog = (LinearLayout) findViewById(R.id.lnrShortLogViewGroup);
        if (blnIsUserParent) {
            ConstructAdaptersForDisplay();
            m_btnOpenLogActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intntLog = new Intent(getApplicationContext(), FTLogActivity.class);
                    startActivity(intntLog);
                }
            });
        }
        SetControlsVisibility(blnIsUserParent);

    }

    private void SetControlsVisibility(Boolean blnIsUserParent) {
        int nReqVisibility = blnIsUserParent ? LinearLayout.VISIBLE : LinearLayout.GONE;
        m_lnrlytFamilies.setVisibility(nReqVisibility);
        m_lnrlytShortLog.setVisibility(nReqVisibility);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INSTALLTION_SEQUENCE_RESPONSE) {
            if (resultCode == RESULT_OK) {
                UpdateAppWasInstalled();
                ConstructWelcomeActivityDisplay(true);
            }
        }
    }

    private void UpdateAppWasInstalled() {
        m_dsDataRetreiver.OpenToWrite();
        m_dsDataRetreiver.UpdateAppPassedInstallation();
        m_dsDataRetreiver.close();
    }

    private void ConstructAdaptersForDisplay() {
        m_dsDataRetreiver.OpenToRead();
        m_arrfmlFamilies = m_dsDataRetreiver.GetFamiliesFromDB();
        m_arrntfMostRecentNotifications = m_dsDataRetreiver.GeteMostRecentRequiredNumberOfNotifications(MAX_NOTIF_IN_WELCOME);
        m_dsDataRetreiver.close();
        m_adptFamiliesAdapter = new ArrayAdapter<Family>(this, android.R.layout.simple_list_item_1, m_arrfmlFamilies);
        m_lvFamilies.setAdapter(m_adptFamiliesAdapter);
        m_lgadptLogPeekAdapter = new FTLogAdapter(this, m_arrntfMostRecentNotifications);
        m_lvLogPeek.setAdapter(m_lgadptLogPeekAdapter);

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
