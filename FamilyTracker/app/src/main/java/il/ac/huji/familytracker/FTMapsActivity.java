package il.ac.huji.familytracker;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FTMapsActivity extends FragmentActivity {


    /**** constants ****/
    private static final String TAG = "FTMapsActivity";
    private static final float ZOOM_LEVEL = 14;

    private GoogleMap mMap; //Google map object. Might be null if Google Play services APK is not available.



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftmaps);

        //get data from parent activity
        Intent intent = getIntent();
        String message = intent.getStringExtra(FTLocationActivity.EXTRA_MESSAGE);
        latlang = parseLocation(message);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if the map has not already been instantiated.
     *
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * Add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     *
     * should only be called once and when we are sure that mMap is not null.
     */
    private void setUpMap() {
        CameraUpdate camera= CameraUpdateFactory.newLatLngZoom(latlang,ZOOM_LEVEL);
        mMap.animateCamera(camera);
        mMap.addMarker(new MarkerOptions().position(new LatLng(latlang.latitude,latlang.longitude)).title("Marker"));

    }

    public LatLng parseLocation(String loc){
        String[] parsedArgs = loc.split(",");
        double lat = Double.valueOf(parsedArgs[0]);
        double lang = Double.valueOf(parsedArgs[1]);
        return new LatLng(lat,lang);

    }
}
