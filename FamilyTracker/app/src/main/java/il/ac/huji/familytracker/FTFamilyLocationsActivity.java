package il.ac.huji.familytracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import javax.sql.DataSource;


public class FTFamilyLocationsActivity extends ActionBarActivity {


    /*********************
     ***** Constants ******
     ********************/
    public static final String LOCATION_MESSAGE = "il.ac.huji.familytracker.MESSAGE";
    public static final String TAG = "LOCATIONS_ACTIVITY";

    /*********************
     ***** Globals ******
     ********************/
    int familyID = 0; //the family ID

    ArrayList<FTLocation> _locations;
    ArrayList<String> _locNames;
    ArrayAdapter<String> _locationsAdapter;
    FTDataSource dataSource;

    /*********************
     ***** Widgets ******
     ********************/

    ListView locationsListView;
    ImageButton addButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftfamily_locations);

        //Get db access with read permissions to fill all the activity data

        dataSource = new FTDataSource(this);
        dataSource.OpenToRead();

//      get family id from parent activity
        Intent intent = getIntent();
        Family family = intent.getParcelableExtra(getResources().
                                                  getString(R.string.Family_Locations_Family_Data));
        familyID = family.getFamilyID();

        //widgets
        addButton = (ImageButton) findViewById(R.id.addBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dataSource.OpenToWrite();
                //add new location to DB
                String tempStr= "temp";

                int locId = dataSource.AddLocationToFamily(tempStr,tempStr,familyID,tempStr); //get id for new location
                dataSource.close();
                FTLocation newLoc = new FTLocation(locId,familyID);

                Intent intent = new Intent(view.getContext(), FTLocationActivity.class);
                intent.putExtra(LOCATION_MESSAGE,newLoc); //send the location to the next activity
                startActivityForResult(intent, 1);

            }
        });


        //TODO read locations from db
        _locations = dataSource.GetLocationsByFamily(familyID);
        //get the location names to display in the activity

        _locNames = new ArrayList<String>();
        for (int i=0; i<_locations.size(); i++){
           _locNames.add(_locations.get(i).getLocationName());
        }


        locationsListView =(ListView) findViewById(R.id.locationsListView);
        _locationsAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, _locNames);
        locationsListView.setAdapter(_locationsAdapter);
        locationsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

//                new AlertDialog.Builder(FTFamilyLocationsActivity.this)
//                        .setTitle("Delete Location")
//                        .setMessage("Are you sure you want to delete this Location?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();


                return false;
            }
        });

        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(FTFamilyLocationsActivity.this,FTLocationActivity.class);
                intent.putExtra(LOCATION_MESSAGE,_locations.get(i)); //send the location to the next activity
                startActivityForResult(intent,1);
            }
        });

        //close db access
        dataSource.close();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                FTLocation newLoc = data.getParcelableExtra(LOCATION_MESSAGE);

                //check if this is an update or new location
                boolean locIsNew = isNewLoc(newLoc);

                //if its a new location add it to the locations list
                if (locIsNew){
                    _locations.add(newLoc);
                    _locNames.add(newLoc.getLocationName());
                }
                dataSource.OpenToWrite();
                dataSource.UpdateLocationData(newLoc);
                dataSource.close();
                String logOut = "new location " + newLoc.getLocationAddr();
                Log.v(TAG,"new location: ");
                _locationsAdapter.notifyDataSetChanged();


            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
                FTLocation canceledLoc = data.getParcelableExtra(LOCATION_MESSAGE);
                if (canceledLoc.getLocationName().isEmpty()) {
                    dataSource.OpenToWrite();
                    dataSource.removeLocation(canceledLoc.getID());
                    dataSource.close();
                }
                Log.v(TAG,"canceled");

            }
        }
    }//onActivityResult

    /*
    * checks if the given location is a new one or its being edited
    * */
    public boolean isNewLoc(FTLocation newLoc){
        for (int i=0; i<_locations.size(); i++ ){
            if (_locations.get(i).getID() == newLoc.getID()){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ftfamily_locations, menu);
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
