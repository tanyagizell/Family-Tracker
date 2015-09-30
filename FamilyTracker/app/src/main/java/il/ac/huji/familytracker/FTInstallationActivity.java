package il.ac.huji.familytracker;

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

import java.util.ArrayList;


public class FTInstallationActivity extends FTNotifiableActivity {

    //constants



    // Widgets
    Button nextBtn;
    EditText phoneNumberEdtTxt;
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

                //TODO phone number?




                //get selection from radio
                int selectedId = authorizationRadioGrp.getCheckedRadioButtonId();

                if (selectedId == R.id.radioParent){

                    setResult(RESULT_OK);
                    finish();
                }

                else if (selectedId == R.id.radioChild){


//                    LinearLayout passiveSectionLayout;
//                    ListView parentListView;
//                    Button addParentBtn;
//                    EditText parentNumberTextView;

                    passiveSectionLayout.setVisibility(LinearLayout.VISIBLE);


                }

            }
        });

        addParentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneInput = phoneNumberEdtTxt.getText().toString();
                parentNumbers.add(phoneInput);

                parentListAdapter.notifyDataSetChanged();
            }
        });



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
