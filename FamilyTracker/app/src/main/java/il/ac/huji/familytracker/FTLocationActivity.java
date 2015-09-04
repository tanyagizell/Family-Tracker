package il.ac.huji.familytracker;

import android.app.FragmentManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseAnalytics;
/*
* FTLocationActivity
*
* This Activity handles the FT location - From here the location coordinates and its followers
* list is managed
* */

public class FTLocationActivity extends ActionBarActivity {

    /*********************
     ***** constants *****
     ********************/
    public static final String EXTRA_MESSAGE= "il.ac.huji.familytracker.MESSAGE";
    public static final String TAG = "FTLocationActivity";

    /*********************
     ***** Globals ******
     ********************/
    //followers list
    ArrayList<String> followersList;
    //address
    String addrName;
    LatLng addrLatLng;
    /*********************
     ***** Widgets ******
     ********************/
    Button addBtn; //adding new followers
    Button mapViewBtn; //Button to open map view for the location
    Button searchBtn;
    EditText edtTxt;
    AutoCompleteTextView ACtxtView;

    ListView followerListView; //list view for the locations followers
    int locationID;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftlocation);

        //TODO read followers from db

        //get location ID from locations activity
//        Intent intent = getIntent();
//        String message = intent.getStringExtra(FTFamilyLocationsActivity.EXTRA_MESSAGE);
//        int locID = Integer.valueOf(message);

        followersList = new ArrayList<String>();
        //todo read from DB and set adapter

        /*** Widgets ***/

        //mp view button
        mapViewBtn = (Button) findViewById(R.id.mapViewButton);
        mapViewBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                openMap(v);
            }
        });



        //add button
        addBtn = (Button) findViewById(R.id.addFollowerBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

//                showDialog(v);
                //TODO Add a follower
            }
        });

        //address text text
        edtTxt = (EditText) findViewById(R.id.editAddr);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        ACtxtView = (AutoCompleteTextView)
                findViewById(R.id.countries_list);
        ACtxtView.setAdapter(adapter);


        private static final String[] COUNTRIES = new String[] {
                "Belgium", "France", "Italy", "Germany", "Spain"
        };


        //search Btn
        searchBtn = (Button) findViewById(R.id.SrchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String addr = edtTxt.getText().toString();
                addrLatLng = getLocationFromAddress(addr);

            }
        });


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ftlocation, menu);
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

    public void showDialog(View v){
        FragmentManager manager = getFragmentManager();

        FTDialogFragment df = new FTDialogFragment().getInstance();
//        df.setOnItemClickListener(listener);
        Fragment fr = getSupportFragmentManager().findFragmentByTag(FTDialogFragment.TAG);
        if (fr == null) {
            df.show(manager, FTDialogFragment.TAG);
        }
    }

    public void openMap (View view){

        //TODO resolve URI
        double latitude = addrLatLng.latitude;
        double longitude = addrLatLng.longitude;

        String message =latitude + "," + longitude;
        Intent intent = new Intent(this, FTMapsActivity.class);
        intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent);
    }


    /*Name: addFollower
     * Add a new member to this locations list of followers
     */
    public void addFollower(){
        //TODO open a dialog to display all family members and choose one from the list

    }

    public ArrayList<String> getFamilyMembers(){
        ArrayList<String> members =  new ArrayList<String>();
        return members;
        //TODO get family members from the data base
    }

    /*
    * Name: getLocationFromAddress
    *
    * */
    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng latLng = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            String lat = "" + location.getLatitude();
            String lng = "" + location.getLongitude();

            latLng = new LatLng(location.getLatitude(), location.getLongitude() );

//            Log.v(lat, TAG);
//            Log.v(lng, TAG);

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return latLng;
    }

}

