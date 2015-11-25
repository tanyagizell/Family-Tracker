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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;
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

    ArrayList<FamilyMember> followersList; //followers list
    ArrayList<String> followerNamesList;
    ArrayList<FamilyMember> familyMembers;
    ArrayList<FamilyMember> candidates;
    ArrayList<String> candidateNames;
    ArrayAdapter<String> followerAdapter;

    LatLng addrLatLng;
    FT_placesAutoComplete autoComplete;
    FTDataSource dataSrc; //db access
    ListView followerListView; //list view for the locations followers
    FTLocation location; //repr    esenting current location


    /*********************
     ***** Widgets ******
     ********************/
    ImageButton addBtn; //adding new followers
    Button mapViewBtn; //Button to open map view for the location
    Button searchBtn;
    Button okBtn;
    EditText locNameEdtText;
    AutoCompleteTextView ACtxtView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftlocation);

        //get location  locations activity
        Intent intent = getIntent();
        location = intent.getParcelableExtra(FTFamilyLocationsActivity.LOCATION_MESSAGE);
        familyID = location.getFamilyId();


        dataSrc = new FTDataSource(this);
        dataSrc.OpenToRead();
        familyMembers = dataSrc.GetFamilyMembersFromDB(familyID);

        /*** Widgets ***/
        //followers list

        followersList = dataSrc.GetFamilyMembersRegisteredForLoc(location.getID());
        dataSrc.close();

        candidates = new ArrayList<FamilyMember>();
        candidateNames = new ArrayList<String>();

        for (int i = 0; i < familyMembers.size(); i++){
            boolean validCandidate = true;
            for (int j = 0; j < followersList.size(); j++){
                if (familyMembers.get(i).getMemberId() == followersList.get(j).getMemberId()){
                    validCandidate = false;
                    break;
                }
            }
            if (validCandidate){
                candidates.add(familyMembers.get(i));
            }
        }

        for (int i = 0; i < candidates.size(); i++){
            candidateNames.add(candidates.get(i).getName());
        }


        followerNamesList = new ArrayList<String>();
        for (int i =0; i< followersList.size();i++){
            followerNamesList.add(followersList.get(i).getName());
        }

        followerListView = (ListView) findViewById(R.id.followersListView);
        followerAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, followerNamesList);
        followerListView.setAdapter(followerAdapter);
        followerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO dialog to delete
                return false;
            }
        });


        //ok button
        okBtn = (Button) findViewById(R.id.okButton);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendLocation(view);

            }
        });

        //map view button
        mapViewBtn = (Button) findViewById(R.id.mapViewButton);
        mapViewBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                openMap(v);
            }
        });

        //location name Edit text
        locNameEdtText = (EditText) findViewById(R.id.locationNameEditText);


        //add button
        addBtn = (ImageButton) findViewById(R.id.addFollowerBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (candidates.size()!=0)
                  showDialog(v);
                else
                    Toast.makeText(FTLocationActivity.this,"No family members to add",Toast.LENGTH_LONG).show();
            }
        });

        //address -search with autocomplete
        autoComplete = new FT_placesAutoComplete(this,location);

        ACtxtView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        ACtxtView.setAdapter(new FT_placesAutoComplete.GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        ACtxtView.setOnItemClickListener(autoComplete);

        //update fields if necessary
        updateFields();

    }

    private void updateFields() {

        if (!location.getLocationName().isEmpty()) {
            locNameEdtText.setText(location.getLocationName());
            ACtxtView.setText(location.getLocationAddr());
        }
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

        //copy family member names
        FragmentManager manager = getFragmentManager();

        FTDialogFragment dialog = new FTDialogFragment();
        dialog.setItemsList(candidateNames);
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
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(FTFamilyLocationsActivity.LOCATION_MESSAGE, location);
        setResult(RESULT_CANCELED,returnIntent);
        super.onBackPressed();
    }

    public void sendLocation(View v){
        String locName = locNameEdtText.getText().toString();
        location.setLocationName(locName);
        addrLatLng = getLocationFromAddress(location.getLocationAddr());
        String addressUpdate = addrLatLng.latitude + "," + addrLatLng.longitude;
        location.setLocationCoordinates(addressUpdate);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(FTFamilyLocationsActivity.LOCATION_MESSAGE,location);

        if (locName.isEmpty()){
            setResult(RESULT_CANCELED,returnIntent);
        }
        else {
            setResult(RESULT_OK,returnIntent);
        }

        finish();
    }

    @Override
    public void onDialogItemSelect(int position) {


        //TODO send notification to create geofence
        dataSrc.OpenToWrite();
        String currentNumber = dataSrc.GetCurPhone();
        dataSrc.close();
        try {

            //build a response with the structure
            /* *  "alert":"CREATE_GEOFENCE"
                *  "location":"lat,lang"
                * */

            //create a Json object the hold the content of the response
            JSONObject respObj = new JSONObject();
            respObj.put(FTNotificationParser.NOTIFICATION_TITLE,FTParsedNotification.enmNotificationTypes.CREATE_GEOFENCE);
            respObj.put(FTNotificationParser.COORDINATES_TITLE,location.getLocationCoordinates());
            respObj.put(FTNotificationParser.PHONE_NUMBER_TITLE,currentNumber);
            ParseQuery SendToRequestingQuery = ParseInstallation.getQuery();
            SendToRequestingQuery.whereEqualTo(FTNotificationParser.QUERY_FIELD,candidates.get(position).getPhoneNumber());
            ParsePush SentParseObject = new ParsePush();
            SentParseObject.setQuery(SendToRequestingQuery);
            SentParseObject.setData(respObj);
            SentParseObject.sendInBackground();

            //TODO send notification with result
        } catch (JSONException e) {
            e.printStackTrace();
        }

        followersList.add(candidates.get(position)); //add follower to list
        followerNamesList.add(candidateNames.get(position));

        followerAdapter.notifyDataSetChanged(); //notify adapter
        dataSrc.OpenToWrite();
        dataSrc.InsertMemberRegistrationToLocation(location.getID(),candidates.get(position).getMemberId());
        dataSrc.close();

        candidateNames.remove(position);
        candidates.remove(position);
    }



}

