package il.ac.huji.familytracker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class FTFamilyActivity extends FTNotifiableActivity {
    //components for listviews
    ArrayAdapter<String> m_adptFamilyMembersListAdapter;
    ArrayAdapter<String> m_adptLocationsListAdapter;
    ArrayList<FamilyMember> m_arrmbrFamilyMembers;
    ArrayList<FTLocation> m_arrlcFamilyLocs;
    //TODO combine context menu for each family member in list -allow editing of family member
    //TODO add family member button opens Family member activity on creation state and opened as open activity for result ,on activityresult will reload the list of family members from db and notify the adapter
    //hooks to controls
    private Button m_btnAddMember;
    private Button m_btnAddLocation;
    private ListView m_lvMembers;
    private ListView m_lvLocations;
    private EditText m_edtFamilyName;
    private Button m_btnConfirmChanges;
    private Family m_fmlCurrentFamily;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_btnAddMember = (Button) findViewById(R.id.addMemberBtn);
        m_btnAddLocation = (Button) findViewById(R.id.btnAddFamilyLoc);
        m_lvLocations = (ListView) findViewById(R.id.lvFamilyLocations);
        m_lvMembers = (ListView) findViewById(R.id.familyMemberList);
        m_edtFamilyName = (EditText) findViewById(R.id.familyNameEditTxt);
        m_btnConfirmChanges = (Button) findViewById(R.id.okFamilyButton);
        setContentView(R.layout.activity_ftfamily);
        Intent intntCurrActivityIntent = getIntent();
        boolean blnIsFamilyCreation = false;
        m_fmlCurrentFamily = null;
        if (intntCurrActivityIntent.hasExtra(String.valueOf(R.string.Extras_Key_Family))) {
            m_fmlCurrentFamily = intntCurrActivityIntent.getParcelableExtra(String.valueOf(R.string.Extras_Key_Family));
            blnIsFamilyCreation = true;
            LoadDataFromDB();
        }
        m_edtFamilyName.setEnabled(blnIsFamilyCreation);
        m_btnConfirmChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          
            }
        });

    }

    private void LoadDataFromDB() {
        m_arrmbrFamilyMembers = new ArrayList<>();
        m_arrlcFamilyLocs = new ArrayList<>();
        LoadDataAndUpdateDisplay(m_lvMembers, m_adptFamilyMembersListAdapter, m_arrmbrFamilyMembers, true);
        LoadDataAndUpdateDisplay(m_lvLocations, m_adptLocationsListAdapter, m_arrlcFamilyLocs, false);
    }

    private void LoadDataAndUpdateDisplay(ListView p_lvUiCompToUpdate, ArrayAdapter<String> p_adptToUpdateDisplay, ArrayList p_arrToLoadDataTo, boolean p_blnIsFromFamilyMembers) {
        if (p_blnIsFromFamilyMembers) {
            p_arrToLoadDataTo = m_dsActivityDataAccess.GetFamilyMembersFromDB(m_fmlCurrentFamily.getFamilyID());
        } else {
            p_arrToLoadDataTo = m_dsActivityDataAccess.GetLocationsByFamily(m_fmlCurrentFamily.getFamilyID());
        }
        p_adptToUpdateDisplay = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, p_arrToLoadDataTo);
        p_lvUiCompToUpdate.setAdapter(p_adptToUpdateDisplay);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
