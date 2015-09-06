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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


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

public class FTLocationActivity extends ActionBarActivity implements FTDialogFragment.DialogListener {

    /*********************
     ***** constants *****
     ********************/
    public static final String EXTRA_MESSAGE= "il.ac.huji.familytracker.MESSAGE";
    public static final String TAG = "FTLocationActivity";

    /*********************
     ***** Globals ******
     ********************/

    int familyID=0;

    ArrayList<String> followersList; //followers list
    ArrayList<String> familyMembers;
    ArrayAdapter<String> followerAdapter;

    LatLng addrLatLng;
    FT_placesAutoComplete autoComplete;
    FTDataSource dataSrc; //db access
    ListView followerListView; //list view for the locations followers
    FTLocation location; //repr    esenting current location


    /*********************
     ***** Widgets ******
     ********************/
    Button addBtn; //adding new followers
    Button mapViewBtn; //Button to open map view for the location
    Button searchBtn;
    EditText edtTxt;
    AutoCompleteTextView ACtxtView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftlocation);

        dataSrc = new FTDataSource(this);

        //get location  locations activity
//        Intent intent = getIntent();
//        String message = intent.getStringExtra(FTFamilyLocationsActivity.EXTRA_MESSAGE);
//        int locID = Integer.valueOf(message);

        String temploc ="";
        location = new FTLocation(temploc,temploc,temploc,0,0);

        //TODO read family members from DB
        familyMembers = new ArrayList<String>();





        /*** Widgets ***/
        //followers list

        dataSrc.GetFamilyMembersRegisteredForLoc(location.getID());
        followersList = new ArrayList<String>();

        followerListView = (ListView) findViewById(R.id.followersListView);
        followerAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, followersList);
        followerListView.setAdapter(followerAdapter);
        followerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO dialog to delete
                return false;
            }
        });

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

                showDialog(v);
                //TODO Add a follower
            }
        });

        //address -search with autocomplete
        autoComplete = new FT_placesAutoComplete(this,location);

        ACtxtView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        ACtxtView.setAdapter(new FT_placesAutoComplete.GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        ACtxtView.setOnItemClickListener(autoComplete);


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

        FTDialogFragment dialog = new FTDialogFragment();
        familyMembers.add("ba");
        dialog.setItemsList(familyMembers);
        dialog.show(manager, "dialog");
    }


    /*
     * Open map activity with the stored coordinates
     */
    public void openMap (View view){

        //update location before opening the map
        addrLatLng = getLocationFromAddress(location.getLocationAddr());
        String addressUpdate = addrLatLng.latitude + "," + addrLatLng.longitude;
        location.setLocationCoordinates(addressUpdate);

        double latitude = addrLatLng.latitude;
        double longitude = addrLatLng.longitude;

        String message =latitude + "," + longitude;
        Intent intent = new Intent(this, FTMapsActivity.class);
        intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent);
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


        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return latLng;
    }

    @Override
    public void onDialogItemSelect(int position) {

        followersList.add(familyMembers.get(position)); //add follower to list
        followerAdapter.notifyDataSetChanged(); //notify adapter
        dataSrc.InsertMemberRegistrationToLocation(location.getID(),position);

    }
}

