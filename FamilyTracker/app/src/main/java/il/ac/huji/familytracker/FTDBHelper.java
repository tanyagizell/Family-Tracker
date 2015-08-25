package il.ac.huji.familytracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tanyagizell on 25/08/2015.
 *
 * Class to manage datbase access
 *
 */
public class FTDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "familytracker.db";

    private static final int DB_VERSION = 1;


    public FTDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        super.onCreate();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
