package il.ac.huji.familytracker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;


public class FTFamilyMemberActivity extends FTNotifiableActivity {

    //TODO when leaving the activity, validate name & number

    //TODO import data

    private EditText _edtName;
    private EditText _edtEMail;
    private EditText _edtNumber;
    private TextView _txtName;
    private TextView _txtEMail;
    private TextView _txtNumber;
    private TextView _txtHeader;

    private LinearLayout _lnrlytNameInputSection;
    private Button btnExtendedEditingOpts;
    private Button btnChangeFrontActivity;
    private Button btnConfirmChanges;

    private FamilyMember _currentMember;
    private Boolean m_blnIsEditMode;
    private Boolean m_blnIsFirstEdit;
    private String m_strMemberExtraKey;
    private int m_nMemberFamilyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftfamily_member);
        m_dsActivityDataAccess = new FTDataSource(this);
        btnConfirmChanges = (Button)findViewById(R.id.btnConfirmMemberEdit);
        btnChangeFrontActivity = (Button)findViewById(R.id.ContinueFromActivity);
        //saving references to visual elements necessery for the setting of the activity's mode
        //edit boxes
        _edtEMail = (EditText) findViewById(R.id.email_address);
        _edtName = (EditText) findViewById(R.id.name);
        _edtNumber = (EditText) findViewById(R.id.phone_number);
        //edit boxes' labels
        _txtEMail = (TextView) findViewById(R.id.Email_Label_Id);
        _txtName = (TextView) findViewById(R.id.Name_Label_Id);
        _txtNumber = (TextView) findViewById(R.id.Phone_Label_Id);
        _txtHeader = (TextView) findViewById(R.id.Member_Activity_Header);
        _lnrlytNameInputSection = (LinearLayout) findViewById(R.id.Name_Edit_Section);

        //buttons
        btnChangeFrontActivity = (Button) findViewById(R.id.ContinueFromActivity);
        Intent intntCurrActivity = getIntent();
        m_blnIsEditMode = true;//intntCurrActivity.getBooleanExtra((String) getResources().getText(R.string.Extras_Key_Is_Edit_Mode), true);
        m_strMemberExtraKey = (String) getResources().getText(R.string.Extras_Key_Current_Member);
        m_blnIsFirstEdit = m_blnIsEditMode ? intntCurrActivity.getBooleanExtra((String) getResources().getText(R.string.Extras_Key_Is_First_Edit_Mode), true) : false;

        if (intntCurrActivity.hasExtra(m_strMemberExtraKey)) {
            _currentMember = intntCurrActivity.getParcelableExtra(m_strMemberExtraKey);
            setExistingDataIntoView();
        } else {
            _currentMember = new FamilyMember();
            m_nMemberFamilyId = intntCurrActivity.getIntExtra((String) getResources().getText(R.string.Extras_Key_Family_Id), -1);
        }
        SetActivityState();

    }

    private void setExistingDataIntoView() {
        _edtName.setText(_currentMember.getName());
        _edtEMail.setText(_currentMember.getEmail());
        _edtNumber.setText(_currentMember.getPhoneNumber());
    }

    private void SetActivityState() {
        //TODO if activity was opened on edit mode
        //button with id continue from activity has dual meaning - when activity in edit mode -
        //it means confirm changes
        //if in view mode , opens map view activity with currrent member's coordinates
        // according to the activity state -set editability for each editText
        SetEditTextEnabledState(_edtNumber);
        SetEditTextEnabledState(_edtName);
        SetEditTextEnabledState(_edtEMail);
        _txtHeader.setText(setHeaderLabelContent());
        _lnrlytNameInputSection.setVisibility(m_blnIsEditMode ? LinearLayout.VISIBLE : LinearLayout.GONE);
        setButtonsPerActivityState();

    }

    private void setButtonsPerActivityState() {
        SetImageToButton();
        if (m_blnIsEditMode) {
            //btnExtendedEditingOpts.setVisibility(Button.INVISIBLE);
            btnConfirmChanges.setVisibility(Button.VISIBLE);
            btnChangeFrontActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  OpenMapHandler(v);
            }
            }
            );
            btnConfirmChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConfirmDataInputHandler(view);
                    //leave the activity after adding editing is done
                    finish();
                }
            });
        } else {
//            btnExtendedEditingOpts.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    OpenMemberEditWindow(v);
//                }
//            });
            btnChangeFrontActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenMapHandler(v);
                }
            }
            );
            btnConfirmChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConfirmDataInputHandler(view);
                }
            });
        }
    }

    private void SetImageToButton() {
        //TODO check link http://stackoverflow.com/questions/17100583/where-are-the-drawables-or-clipart-from-android-r-drawable-stored
        // and learn best way to integrate icons into app
    }

    private String setHeaderLabelContent() {
        return m_blnIsEditMode ? (String) getResources().getText(R.string.Edit_Mode_Header) : _currentMember.getName();
    }

    private void OpenMapHandler(View p_vwCurrentCallbackData) {
        //TODO send request for current location -opening of activity done from onreceive
        if(!_edtNumber.getText().equals(""))
        {
            SetActivityToWait();
        /*create Json object for notification content with the structure:
            * "alert":"CURRENT_LOC_REQUEST"
            * "sender_phone":"phone number"
         */
            m_dsActivityDataAccess.OpenToWrite();
            String strCurrPhone = m_dsActivityDataAccess.GetCurPhone();
            m_dsActivityDataAccess.close();
            JSONObject jsonObj =new JSONObject();
            try {
                jsonObj.put(FTNotificationParser.NOTIFICATION_TITLE,FTParsedNotification.enmNotificationTypes.CURRENT_LOC_REQUEST);
                jsonObj.put(FTNotificationParser.PHONE_NUMBER_TITLE,strCurrPhone);
                ParseQuery SendToRequestingQuery = ParseInstallation.getQuery();
                SendToRequestingQuery.whereEqualTo(getResources().getString(R.string.PARSE_PHONE_ID),_edtNumber.getText().toString());
                ParsePush SentParseObject = new ParsePush();
                SentParseObject.setQuery(SendToRequestingQuery);
                SentParseObject.setData(jsonObj);
                SentParseObject.sendInBackground();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "Target phone number is required in order to view location",Toast.LENGTH_LONG).show();
        }

        //TODO use intent of map and coordinates from service , or from the user data loaded to activity
    }

    private void SetActivityToWait() {
        //TODO start timer and disable all controls
    }

    private void ConfirmDataInputHandler(View p_vwCurrentCallbackData) {

        m_dsActivityDataAccess.OpenToWrite();
        if (m_blnIsFirstEdit) {
            m_dsActivityDataAccess.InsertFamilyMemberToDB(_edtName.getText().toString(), _edtNumber.getText().toString(), _edtEMail.getText().toString(), m_nMemberFamilyId);
        } else {
            _currentMember.setName(_edtName.getText().toString());
            _currentMember.setEmail(_edtEMail.getText().toString());
            m_dsActivityDataAccess.UpdateFamilyMember(_currentMember);
        }
    }

    private void SetEditTextEnabledState(EditText p_edtToDisable) {
        p_edtToDisable.setEnabled(m_blnIsEditMode);
        p_edtToDisable.setFocusable(m_blnIsEditMode);
        p_edtToDisable.setClickable(m_blnIsEditMode);
    }

    void OpenDataImportDialog(View p_vwCurrentCallbackData) {
        //TODO decide on the logic in this activity ,and it's nature-a modal dialog ,or a seperate
        // activity
    }

    void OpenMemberEditWindow(View p_vwCurrentCallbackData) {
        //TODO finish-started before uploading to work on this
        Intent intntOnEdit = new Intent(this, FTFamilyMemberActivity.class);
        intntOnEdit.putExtra((String) getResources().getText(R.string.Extras_Key_Is_Edit_Mode), true);
        intntOnEdit.putExtra((String) getResources().getText(R.string.Extras_Key_Is_First_Edit_Mode), false);
        intntOnEdit.putExtra((String) getResources().getText(R.string.Extras_Key_Current_Member), _currentMember);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ftfamily_member, menu);
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
