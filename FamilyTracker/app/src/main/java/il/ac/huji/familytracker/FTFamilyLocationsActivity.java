package il.ac.huji.familytracker;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class FTFamilyLocationsActivity extends ActionBarActivity {


    /*********************
     ***** Constants ******
     ********************/
    public static final String EXTRA_MESSAGE = "il.ac.huji.familytracker.MESSAGE";

    /*********************
     ***** Globals ******
     ********************/
    int familyID //the family ID

    ArrayList<FTLocation> _locations;
    ArrayList<String> _locNames;
    ArrayAdapter<String> _locationsAdapter;

    /*********************
     ***** Widgets ******
     ********************/

    ListView locationsListView;
    Button addButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftfamily_locations);

        //TODO below
        //get family id from parent activity
//        Intent intent = getIntent();
//        familyID = intent.getIntExtra("todo","todo");

        //widgets
        addButton = (Button) findViewById(R.id.addBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add a location
                int locId = _locations.size();
                String temp;

                Intent intent = new Intent();

            }
        });

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
