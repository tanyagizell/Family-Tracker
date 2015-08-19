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


public class FTFamilyMemberActivity extends ActionBarActivity {

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

    private FamilyMember _currentMember;
    private Boolean m_blnIsEditMode;

    private String m_strMemberExtraKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftfamily_member);

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
        Intent intntCurrActivity = getIntent();
        m_blnIsEditMode = intntCurrActivity.getBooleanExtra((String) getResources().getText(R.string.Extras_Key_Is_Edit_Mode), true);
        m_strMemberExtraKey = (String) getResources().getText(R.string.Extras_Key_Current_Member);
        if (intntCurrActivity.hasExtra(m_strMemberExtraKey)) {
            _currentMember = intntCurrActivity.getParcelableExtra(m_strMemberExtraKey);
            setExistingDataIntoView();
        } else {
            _currentMember = new FamilyMember();
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
        SetEditTextEnabledState(_edtNumber);
        _txtHeader.setText(setHeaderLabelContent());
        _lnrlytNameInputSection.setVisibility(m_blnIsEditMode ? LinearLayout.VISIBLE : LinearLayout.GONE);
        setButtonsPerActivityState();

    }

    private void setButtonsPerActivityState() {
        View.OnClickListener lstnEditOpsHandler;
        View.OnClickListener lstnFrontActivityChangeHandler;
        SetImageToButton();
        if (m_blnIsEditMode) {
            lstnEditOpsHandler = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenDataImportDialog(v);
                }
            };
            lstnFrontActivityChangeHandler = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmDataInputHandler(v);
                }
            };
        } else {
            lstnEditOpsHandler = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenMemberEditWindow(v);
                }
            };
            lstnFrontActivityChangeHandler = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenMapHandler(v);
                }
            };
        }

        btnExtendedEditingOpts.setOnClickListener(lstnEditOpsHandler);
        btnChangeFrontActivity.setOnClickListener(lstnFrontActivityChangeHandler);
    }

    private void SetImageToButton() {
        //TODO check link http://stackoverflow.com/questions/17100583/where-are-the-drawables-or-clipart-from-android-r-drawable-stored
        // and learn best way to integrate icons into app
    }

    private String setHeaderLabelContent() {
        return m_blnIsEditMode ? (String) getResources().getText(R.string.Edit_Mode_Header) : _currentMember.getName();
    }

    private void OpenMapHandler(View p_vwCurrentCallbackData) {
        //TODO use intent of map and coordinates from service , or from the user data loaded to activity
    }

    private void ConfirmDataInputHandler(View p_vwCurrentCallbackData) {
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
