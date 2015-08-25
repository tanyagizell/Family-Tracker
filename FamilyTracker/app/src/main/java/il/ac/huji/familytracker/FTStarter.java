package il.ac.huji.familytracker;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by Tanyagizell on 25/08/2015.
 *
 * project starter subclass. will run before any other activity
 */
public class FTStarter extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        //Enable local datastore
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, (String) getResources().getText(R.string.parse_app_id),
                (String) getResources().getText(R.string.parse_client_key));
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
