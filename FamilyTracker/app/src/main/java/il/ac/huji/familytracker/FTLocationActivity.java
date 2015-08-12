package il.ac.huji.familytracker;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;


/*
* FTLocationActivity
*
* This Activity handles the FT location - From here the location coordinates and its followers
* list is managed
* */

public class FTLocationActivity extends ActionBarActivity {

    /*********************
     ***** Globals ******
     ********************/


    //Widgets
    Button addBtn; //adding new followers
    Button mapViewBtn; //Button to open map view for the location

    ListView followerListView; //list view for the locations followers

    //variables
    ArrayList<String> followersList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftlocation);

        //TODO read followers from db

        followersList = new ArrayList<String>();

        /*** Widgets ***/

        //mp view button
        mapViewBtn = (Button) findViewById(R.id.mapViewButton);
        mapViewBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //TODO resolve URI
                double latitude = 40.714728;
                double longitude = -73.998672;
                String label = "ABC Label";
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        //add button
//        addBtn = (Button) findViewById(R.id.mapViewButton);
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                //TODO Add a follower
//            }
//        });

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
}
