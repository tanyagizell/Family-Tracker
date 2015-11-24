package il.ac.huji.familytracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FTFamilyActivity extends FTNotifiableActivity {
    //components for listviews
    ArrayAdapter<FamilyMember> m_adptFamilyMembersListAdapter;
    ArrayList<FamilyMember> m_arrmbrFamilyMembers;
    ArrayList<FTLocation> m_arrlcFamilyLocs;
    //TODO combine context menu for each family member in list -allow editing of family member
    //TODO add family member button opens Family member activity on creation state and opened as open activity for result ,on activityresult will reload the list of family members from db and notify the adapter
    //hooks to controls
    private ImageButton m_btnAddMember;
    private ImageButton m_btnAddLocation;
    private ListView m_lvMembers;
    private EditText m_edtFamilyName;
    private Button m_btnConfirmChanges;
    private Family m_fmlCurrentFamily;

    private boolean m_blnIsFamilyCreation ;
    private boolean m_blnIsFirstSaveCommited;

    //variable for closing sequence
    private boolean m_blnShouldContinueClosing;

    //variable to track opening of editing activities
    private boolean m_blnIsOpeningEditActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftfamily);
        m_blnIsOpeningEditActivity = false;
        m_btnAddMember = (ImageButton) findViewById(R.id.addMemberBtn);
        m_btnAddLocation = (ImageButton) findViewById(R.id.btnAddFamilyLoc);
        m_lvMembers = (ListView) findViewById(R.id.familyMemberList);
        m_edtFamilyName = (EditText) findViewById(R.id.familyNameEditTxt);
        m_btnConfirmChanges = (Button) findViewById(R.id.okFamilyButton);
        if (m_dsActivityDataAccess == null)
        {
            m_dsActivityDataAccess = new FTDataSource(this);
        }
        Intent intntCurrActivityIntent = getIntent();
        m_blnIsFamilyCreation = true;
        m_fmlCurrentFamily = null;
        if (intntCurrActivityIntent.hasExtra(getResources().getString(R.string.Extras_Key_Family))) {
            m_fmlCurrentFamily = intntCurrActivityIntent.getParcelableExtra(getResources().getString(R.string.Extras_Key_Family));
            m_blnIsFamilyCreation = false;
            m_blnIsFirstSaveCommited = false;
            LoadDataFromDB();
            ((TextView)findViewById(R.id.txtFamilyHeader)).setText("Edit Family");
            m_edtFamilyName.setText(m_fmlCurrentFamily.getfamilyName());
        }
        m_edtFamilyName.setEnabled(m_blnIsFamilyCreation);
        if (m_blnIsFamilyCreation)
            m_edtFamilyName.setFocusableInTouchMode(m_blnIsFamilyCreation);
        else
            m_edtFamilyName.setFocusable(m_blnIsFamilyCreation);
        m_btnConfirmChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnUserRequestToLeave();

            }
        });
        m_btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenEditableActivityOnCondition(false);
            }
        });
        m_btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenEditableActivityOnCondition(true);
            }
        });
        m_lvMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FamilyMember fmSelectedMember = m_arrmbrFamilyMembers.get(i);
                OpenFTFamilyMemberActivity(fmSelectedMember);
            }
        });
    }

    private void OpenEditableActivityOnCondition(boolean p_blnIsAddLocation) {
        if (m_fmlCurrentFamily == null && !IsFamilyNameFieldEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.family_activity_must_provide_family_name), Toast.LENGTH_LONG).show();
        } else {
            m_blnIsOpeningEditActivity = true;
            if (m_fmlCurrentFamily == null) {
                String strInputFamilyName = m_edtFamilyName.getText().toString();
                int nFamilyId = m_dsActivityDataAccess.InsertFamilyToDB(strInputFamilyName);
                m_fmlCurrentFamily = new Family(strInputFamilyName, nFamilyId);
            }
            if (p_blnIsAddLocation) {
                OpenFTFamilyLocationsActivity();
            } else {
                OpenFTFamilyMemberActivity(null);
            }
        }

    }

    private void OpenFTFamilyMemberActivity(FamilyMember fmSelectedMember) {
        Intent intntCreateMember = new Intent(this, FTFamilyMemberActivity.class);
        if (fmSelectedMember == null) {
            intntCreateMember.putExtra((String) getResources().getText(R.string.Extras_Key_Is_Edit_Mode), true);
            intntCreateMember.putExtra((String) getResources().getText(R.string.Extras_Key_Is_First_Edit_Mode), true);
            intntCreateMember.putExtra((String) getResources().getText(R.string.Extras_Key_Family_Id), m_fmlCurrentFamily.getFamilyID());
        } else {
            intntCreateMember.putExtra((String) getResources().getText(R.string.Extras_Key_Is_Edit_Mode), false);
            intntCreateMember.putExtra((String) getResources().getText(R.string.Extras_Key_Current_Member), fmSelectedMember);

        }
        //TODO when i will work on opening elements from the list ,should provide ability to set other extras
        startActivity(intntCreateMember);
    }

    private void OpenFTFamilyLocationsActivity() {
        Intent intntFamilyLocationsWindow = new Intent(this,FTFamilyLocationsActivity.class);
        intntFamilyLocationsWindow.putExtra(getResources().
                                            getString(R.string.Family_Locations_Family_Data),m_fmlCurrentFamily);
        startActivity(intntFamilyLocationsWindow);

    }


    private void OnUserRequestToLeave() {
        CheckIfUserWantsToLeave();
    }

    /*
     * Checks if the family name field is emtpy
     */
    private boolean IsFamilyNameFieldEmpty() {
        return m_edtFamilyName.getText().toString().equals("");
    }

    private void CheckIfAllDataInserted() {
        if (IsFamilyNameFieldEmpty()) {
            if (m_blnIsFamilyCreation) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.family_activity_error_name_not_provided), Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder alrtbldShouldExitWithoutEdit = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert);
                alrtbldShouldExitWithoutEdit.setTitle(R.string.confirm_editable_activity_exit_without_change_title);
                alrtbldShouldExitWithoutEdit.setMessage(R.string.confirm_editable_activity_exit_without_change_message);
                alrtbldShouldExitWithoutEdit.setPositiveButton(R.string.save_confirmation_ok_text, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Stop the activity
                        finish();
                    }

                });
                alrtbldShouldExitWithoutEdit.setNegativeButton(R.string.save_confirmation_cancel_text, null);
                alrtbldShouldExitWithoutEdit.show();
            }
        }
        else
        {
            m_dsActivityDataAccess.OpenToWrite();
            if (m_blnIsFamilyCreation && !m_blnIsFirstSaveCommited) {
                m_dsActivityDataAccess.InsertFamilyToDB(m_edtFamilyName.getText().toString());
            } else {
                if (!IsFamilyNameFieldEmpty()) {
                    m_fmlCurrentFamily.setFamilyName(m_edtFamilyName.getText().toString());
                    m_dsActivityDataAccess.UpdateFamily(m_fmlCurrentFamily);
                }
            }
            m_dsActivityDataAccess.close();
            finish();
        }
    }

    private void CheckIfUserWantsToLeave() {
        m_blnShouldContinueClosing = false;
        AlertDialog.Builder alrtbldConfirm = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert);
        alrtbldConfirm.setTitle(R.string.confirm_editable_activity_exit_title);
        alrtbldConfirm.setMessage(R.string.confirm_editable_activity_exit_message);
        alrtbldConfirm.setPositiveButton(R.string.save_confirmation_ok_text, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                CheckIfAllDataInserted();
            }

        });
        alrtbldConfirm.setNegativeButton(R.string.save_confirmation_cancel_text, null);
        alrtbldConfirm.show();
    }

    private void setShouldContinue(boolean p_blnValToSet) {

    }

    private void LoadDataFromDB() {
        m_arrmbrFamilyMembers = new ArrayList<>();
        m_arrlcFamilyLocs = new ArrayList<>();
        m_dsActivityDataAccess.OpenToRead();
        m_arrmbrFamilyMembers = m_dsActivityDataAccess.GetFamilyMembersFromDB(m_fmlCurrentFamily.getFamilyID());
        m_dsActivityDataAccess.close();
        //LoadDataAndUpdateDisplay(m_lvMembers, m_adptFamilyMembersListAdapter, m_arrmbrFamilyMembers, true);
        m_adptFamilyMembersListAdapter = new ArrayAdapter<FamilyMember>(this,
                            R.layout.ft_simple_list_item_typed_array, R.id.tvListItemControl, m_arrmbrFamilyMembers);
        m_lvMembers.setAdapter(m_adptFamilyMembersListAdapter);
    }

//    private void LoadDataAndUpdateDisplay(ListView p_lvUiCompToUpdate, ArrayAdapter<String> p_adptToUpdateDisplay, ArrayList p_arrToLoadDataTo, boolean p_blnIsFromFamilyMembers) {
//        m_dsActivityDataAccess.OpenToRead();
//        if (p_blnIsFromFamilyMembers) {
//            p_arrToLoadDataTo = m_dsActivityDataAccess.GetFamilyMembersFromDB(m_fmlCurrentFamily.getFamilyID());
//        } else {
//            p_arrToLoadDataTo = m_dsActivityDataAccess.GetLocationsByFamily(m_fmlCurrentFamily.getFamilyID());
//        }
//        m_dsActivityDataAccess.close();
//        p_adptToUpdateDisplay = new ArrayAdapter<String>(getApplicationContext(), R.layout.ft_simple_list_item_typed_array, R.id.tvListItemControl, p_arrToLoadDataTo);
//        p_lvUiCompToUpdate.setAdapter(p_adptToUpdateDisplay);
//
//    }


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

    @Override
    protected void onResume() {
        super.onResume();
        if (m_blnIsOpeningEditActivity) {
            m_blnIsOpeningEditActivity = false;
            m_dsActivityDataAccess.OpenToRead();
            m_arrmbrFamilyMembers.clear();
            m_arrmbrFamilyMembers.addAll(m_dsActivityDataAccess.GetFamilyMembersFromDB(m_fmlCurrentFamily.getFamilyID()));
            m_dsActivityDataAccess.close();
            m_adptFamilyMembersListAdapter.notifyDataSetChanged();
        }

    }
}
