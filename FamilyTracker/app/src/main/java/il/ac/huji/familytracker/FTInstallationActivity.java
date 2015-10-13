package il.ac.huji.familytracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseInstallation;

import java.util.ArrayList;


public class FTInstallationActivity extends FTNotifiableActivity {
    private static final String NO_NAME_INPUT_INSTALLATION_VALIDATION = "Must input user name";
    private static final String INVALID_PHONE_INSTALLATION_VALIDATION = "Must input phone number of form 05X-XXXX-XXX";

    //constants



    // Widgets
    Button nextBtn;
    Button ConfirmInstallationBtn;
    EditText phoneNumberEdtTxt;
    EditText CurrUserNameEdtTxt;
    RadioGroup authorizationRadioGrp;
    RadioButton authorizationRadioBtn;

    //passive section widgets
    LinearLayout passiveSectionLayout;
    ListView parentListView;
    Button addParentBtn;
    EditText parentNumberTextView;


    //Globals
    ArrayList<String> parentNumbers;
    ArrayAdapter<String> parentListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftinstallation);
        //Widgets

        ConfirmInstallationBtn = (Button) findViewById(R.id.btnConfirmInstallationData);

        CurrUserNameEdtTxt = (EditText) findViewById(R.id.UserNameInput);
        phoneNumberEdtTxt = (EditText) findViewById(R.id.phoneNumberEditText);
        authorizationRadioGrp = (RadioGroup) findViewById(R.id.authorizationRadioGrp);
        passiveSectionLayout = (LinearLayout)findViewById(R.id.passiveSection);
        addParentBtn = (Button) findViewById(R.id.addPhoneBtn);
        parentNumberTextView = (EditText) findViewById(R.id.parentPhoneEditText);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        parentListView = (ListView) findViewById(R.id.parentsListView);

        //init
        parentNumbers = new ArrayList<String>();
        parentListAdapter = new ArrayAdapter<String>(this,R.layout.list_item,parentNumbers);
        parentListView.setAdapter(parentListAdapter);

        //listeners
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean blnCanProcede = ValidateFirstPhaseInput();

                //get selection from radio
                int selectedId = authorizationRadioGrp.getCheckedRadioButtonId();
                if (blnCanProcede) {
                    if (selectedId == R.id.radioParent) {
                        passiveSectionLayout.setVisibility(LinearLayout.VISIBLE);
                    } else {
                        ExportData();
                    }
                }


            }
        });

        addParentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneInput = parentNumberTextView.getText().toString();
                boolean blnIsNumberValid = ValidateInput(phoneInput, PHONE_RECOG_REGEX_STRING);
                if (blnIsNumberValid) {
                    parentNumbers.add(phoneInput);
                    parentListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), INVALID_PHONE_INSTALLATION_VALIDATION, Toast.LENGTH_LONG).show();
                }
                parentNumberTextView.setText("");

            }
        });

        ConfirmInstallationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean blnCanLeave = ValidateFirstPhaseInput();
                if (blnCanLeave) {
                    int selectedId = authorizationRadioGrp.getCheckedRadioButtonId();
                    if (selectedId == R.id.radioParent) {
                        blnCanLeave = parentNumbers.size() > 0;
                    }
                }

                if (blnCanLeave) {
                    ExportData();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });


    }

    private void ExportData() {
        String strPhone = phoneNumberEdtTxt.getText().toString();
        String strName = CurrUserNameEdtTxt.getText().toString();
        int selectedId = authorizationRadioGrp.getCheckedRadioButtonId();
        //write information to db
        //   if child - create authorized table and insert numbers
        m_dsActivityDataAccess = new FTDataSource(getApplicationContext());
        m_dsActivityDataAccess.OpenToWrite();
        m_dsActivityDataAccess.InsertCurrUserData(strPhone, strName, selectedId == R.id.radioParent ? FTDataSource.enmUserStatus.PARENT : FTDataSource.enmUserStatus.CHILD);
        if (selectedId == R.id.radioChild) {
            CreateAuthUsersAndInsertNumbers();
        }
        m_dsActivityDataAccess.close();
        //insert phone number to parseinstallation
        ParseInstallation instlCoreTableConnector = ParseInstallation.getCurrentInstallation();
        instlCoreTableConnector.put(getResources().getString(R.string.PARSE_PHONE_ID), strPhone);
        instlCoreTableConnector.saveInBackground();
    }

    private void CreateAuthUsersAndInsertNumbers() {
        m_dsActivityDataAccess.CreateAuthUsersTable();
        for (String strAuthPhone : parentNumbers) {
            m_dsActivityDataAccess.InsertAuthorized(strAuthPhone);
        }
    }

    private boolean ValidateFirstPhaseInput() {
        //validate number was entered and is valid
        boolean blnIsConfirmable = ValidateInput(phoneNumberEdtTxt.getText().toString(), PHONE_RECOG_REGEX_STRING);
        Toast tstValidationMsg = null;
        //validate name entered
        if (blnIsConfirmable) {
            blnIsConfirmable = !(CurrUserNameEdtTxt.getText().toString().replaceAll(" ", "").isEmpty());
            if (!blnIsConfirmable) {
                tstValidationMsg = Toast.makeText(getApplicationContext(), NO_NAME_INPUT_INSTALLATION_VALIDATION, Toast.LENGTH_LONG);
            }
        } else {
            tstValidationMsg = Toast.makeText(getApplicationContext(), INVALID_PHONE_INSTALLATION_VALIDATION, Toast.LENGTH_LONG);
        }
        if (!blnIsConfirmable) {
            tstValidationMsg.show();
        }
        return blnIsConfirmable;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ftinstallation, menu);
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
