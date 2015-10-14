package il.ac.huji.familytracker;

import android.content.ContentValues;
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

    //notifications table constants
    public static final String NOTIFICATION_TABLE_NAME = "notifications";
    public static final String NOTIFICATION_COLUMN_FAMILY_ID = "FAMILY_ID";
    public static final String NOTIFICATION_COLUMN_TYPE = "NOTIF_TYPE";
    public static final String NOTIFICATION_COLUMN_SUBJECT_NAME = "SUBJECT_NAME";
    public static final String NOTIFICATION_COLUMN_TIME_STAMP = "DATE_RECIEVED";
    public static final String NOTIFICATION_COLUMN_LOC_DISPLAY = "LOCATION_DISPLAY_NAME";
    public static final String NOTIFICATION_INSERT_COMMAND = String.format("INSERT INTO %s (%s,%s,%s,%s,%s) VALUES (?,?,?,?,?)", NOTIFICATION_TABLE_NAME, NOTIFICATION_COLUMN_TYPE, NOTIFICATION_COLUMN_LOC_DISPLAY, NOTIFICATION_COLUMN_TIME_STAMP, NOTIFICATION_COLUMN_FAMILY_ID, NOTIFICATION_COLUMN_SUBJECT_NAME);
    public static final String NOTIFICATION_COLUMN_DB_ID = "ID";
    private final String NOTIFICATION_TABLE_CREATION = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s DATETIME  NOT NULL," +
                    "%s INTEGER NOT NULL);", NOTIFICATION_TABLE_NAME,
            NOTIFICATION_COLUMN_DB_ID,
            NOTIFICATION_COLUMN_TYPE,
            NOTIFICATION_COLUMN_LOC_DISPLAY,
            NOTIFICATION_COLUMN_SUBJECT_NAME,
            NOTIFICATION_COLUMN_TIME_STAMP,
            NOTIFICATION_COLUMN_FAMILY_ID);
    public static final String[] NOTIFICATION_TABLE_DATA_COLUMNS = {NOTIFICATION_COLUMN_DB_ID, NOTIFICATION_COLUMN_TYPE, NOTIFICATION_COLUMN_LOC_DISPLAY, NOTIFICATION_COLUMN_SUBJECT_NAME, NOTIFICATION_COLUMN_TIME_STAMP, NOTIFICATION_COLUMN_FAMILY_ID};
    //families table constants
    public static final String FAMILIES_TABLE_NAME = "families";
    public static final String FAMILIES_COLUMN_NAME = "FAMILY_NAME";
    public static final String FAMILIES_INSERT_COMMAND = String.format("INSERT INTO  %s (%s) VALUES (?)", FAMILIES_TABLE_NAME, FAMILIES_COLUMN_NAME);
    public static final String FAMILIES_COLUMN_ID = "ID";
    private final String FAMILIES_TABLE_CREATION = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s  TEXT NOT NULL);", FAMILIES_TABLE_NAME,
            FAMILIES_COLUMN_ID,
            FAMILIES_COLUMN_NAME);
    public static final String[] FAMILIES_TABLE_DATA_COLUMNS = {FAMILIES_COLUMN_ID, FAMILIES_COLUMN_NAME};

    //places table constants
    public static final String PLACES_TABLE_NAME = "places";
    public static final String PLACES_COLUMN_NAME = "PLACE_NAME";
    public static final String PLACES_COLUMN_COORD = "COORDS";
    public static final String PLACES_COLUMN_FAMILY = "FAMILY_ID";
    public static final String PLACES_COLUMN_PLACE_ADDRESS = "ADDRESS";
    public static final String PLACES_INSERT_COMMAND = String.format("INSERT INTO %s (%s,%s,%s,%s) VALUES (?,?,?,?)", PLACES_TABLE_NAME, PLACES_COLUMN_NAME, PLACES_COLUMN_COORD, PLACES_COLUMN_FAMILY, PLACES_COLUMN_PLACE_ADDRESS);
    public static final String PLACES_COLUMN_PLACE_ID = "ID";
    public static final String[] PLACES_TABLE_DATA_COLUMNS = {PLACES_COLUMN_PLACE_ID, PLACES_COLUMN_NAME, PLACES_COLUMN_COORD, PLACES_COLUMN_FAMILY, PLACES_COLUMN_PLACE_ADDRESS};

    private final String PLACES_TABLE_CREATION = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL ," +
                    "%s INTEGER NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "FOREIGN KEY(%s) REFERENCES %s(%s));", PLACES_TABLE_NAME,
            PLACES_COLUMN_PLACE_ID,
            PLACES_COLUMN_NAME,
            PLACES_COLUMN_COORD,
            PLACES_COLUMN_FAMILY,
            PLACES_COLUMN_PLACE_ADDRESS,
            PLACES_COLUMN_FAMILY, FAMILIES_TABLE_NAME, FAMILIES_COLUMN_ID);


    //family members table constants
    public static final String FAMILY_MEMBERS_TABLE_NAME = "familymembers";
    public static final String FAMILY_MEMBERS_COLUMN_NAME = "NAME";
    public static final String FAMILY_MEMBERS_COLUMN_FAMILY_ID = "FAMILY_ID";
    public static final String FAMILY_MEMBERS_COLUMN_MEMBER_ID = "ID";
    public static final String FAMILY_MEMBERS_COLUMN_PHONE = "PHONE";
    public static final String FAMILY_MEMBERS_COLUMN_EMAIL = "EMAIL";
    public final String FAMILY_MEMBERS_TABLE_CREATION = String.format("CREATE TABLE %s(" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s INTEGER NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT," +
                    "%s TEXT NOT NULL," +
                    "FOREIGN KEY(%s) REFERENCES %s(%s));", FAMILY_MEMBERS_TABLE_NAME,
            FAMILY_MEMBERS_COLUMN_MEMBER_ID,
            FAMILY_MEMBERS_COLUMN_FAMILY_ID,
            FAMILY_MEMBERS_COLUMN_PHONE,
            FAMILY_MEMBERS_COLUMN_EMAIL,
            FAMILY_MEMBERS_COLUMN_NAME,
            FAMILY_MEMBERS_COLUMN_FAMILY_ID, FAMILIES_TABLE_NAME, FAMILIES_COLUMN_ID);
    public static final String[] FAMILY_MEMBERS_TABLE_DATA_COLUMNS = {FAMILY_MEMBERS_COLUMN_MEMBER_ID, FAMILY_MEMBERS_COLUMN_FAMILY_ID, FAMILY_MEMBERS_COLUMN_PHONE, FAMILY_MEMBERS_COLUMN_EMAIL, FAMILY_MEMBERS_COLUMN_NAME};
    public static final String FAMILY_MEMBERS_INSERT_COMMAND = String.format("INSERT INTO %s(" +
            "%s,%s,%s,%s) " +
            "VALUES (?,?,?,?)", FAMILIES_TABLE_NAME, FAMILY_MEMBERS_COLUMN_FAMILY_ID, FAMILY_MEMBERS_COLUMN_PHONE, FAMILY_MEMBERS_COLUMN_EMAIL, FAMILY_MEMBERS_COLUMN_NAME);

    //places to family members table constants
    public static final String MEMBER_TO_PLACE_TABLE_NAME = "membertoplace";
    public static final String MEMBER_TO_PLACE_COLUMN_MEMBER_ID = "MEMBER";
    public static final String MEMBER_TO_PLACE_COLUMN_PLACE_ID = "PLACE";
    public static final String MEMBER_TO_PLACE_INSERT_COMMAND = String.format("INSERT INTO  %s (%s,%s) VALUES (?,?)", MEMBER_TO_PLACE_TABLE_NAME, MEMBER_TO_PLACE_COLUMN_PLACE_ID, MEMBER_TO_PLACE_COLUMN_MEMBER_ID);
    //current user table constants
    public static final String CURR_USER_TABLE_NAME = "currentuser";
    public static final String CURR_USER_COLUMN_NAME = "NAME";
    public static final String CURR_USER_COLUMN_PHONE = "PHONE";
    public static final String CURR_USER_COLUMN_STATUS = "STATUS";
    public static final String CURR_USER_INSERT_COMMAND = String.format("INSERT INTO  %s (%s,%s,%s) VALUES (?,?,?)", CURR_USER_TABLE_NAME, CURR_USER_COLUMN_NAME, CURR_USER_COLUMN_PHONE, CURR_USER_COLUMN_STATUS);
    public static final String CURR_USER_COLUMN_ID = "ID";
    private final String CURR_USER_TABLE_CREATION = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s  TEXT NOT NULL," +
                    "%s  TEXT NOT NULL," +
                    "%s  TEXT NOT NULL);", CURR_USER_TABLE_NAME,
            CURR_USER_COLUMN_ID,
            CURR_USER_COLUMN_NAME,
            CURR_USER_COLUMN_PHONE,
            CURR_USER_COLUMN_STATUS);
    public static final String[] CURR_USER_TABLE_DATA_COLUMNS = {CURR_USER_COLUMN_ID, CURR_USER_COLUMN_NAME, CURR_USER_COLUMN_PHONE, CURR_USER_COLUMN_STATUS};
    //authorized users table constants
    public static final String AUTH_USER_TABLE_NAME = "authorizedusers";
    public static final String AUTH_USER_COLUMN_PHONE = "PHONE";
    public static final String AUTH_USER_INSERT_COMMAND = String.format("INSERT INTO  %s (%s) VALUES (?)", AUTH_USER_TABLE_NAME, AUTH_USER_COLUMN_PHONE);
    public static final String AUTH_USER_COLUMN_ID = "ID";
    public static final String AUTH_USER_TABLE_CREATION = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s  TEXT NOT NULL);", AUTH_USER_TABLE_NAME,
            AUTH_USER_COLUMN_ID,
            AUTH_USER_COLUMN_PHONE);
    public static final String[] AUTH_USER_TABLE_DATA_COLUMNS = {AUTH_USER_COLUMN_ID, AUTH_USER_COLUMN_PHONE};
    //app required table constants
    public static final String APP_REQ_TABLE_NAME = "apprequired";
    public static final String APP_REQ_COLUMN_IS_FIRST_TIME = "IS_FIRST_TIME";
    public static final String APP_REQ_COLUMN_IS_APP_ON = "IS_APP_ON";
    public static final String APP_REQ_INSERT_COMMAND = String.format("INSERT INTO  %s (%s,%s) VALUES (?,?)", APP_REQ_TABLE_NAME, APP_REQ_COLUMN_IS_FIRST_TIME, APP_REQ_COLUMN_IS_APP_ON);
    public static final String APP_REQ_COLUMN_ID = "ID";
    private final String APP_REQ_TABLE_CREATION = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s  BOOLEAN NOT NULL," +
                    "%s  BOOLEAN NOT NULL);", APP_REQ_TABLE_NAME,
            APP_REQ_COLUMN_ID,
            APP_REQ_COLUMN_IS_FIRST_TIME,
            APP_REQ_COLUMN_IS_APP_ON);
    public static final String[] APP_REQ_TABLE_DATA_COLUMNS = {APP_REQ_COLUMN_ID, APP_REQ_COLUMN_IS_FIRST_TIME, APP_REQ_COLUMN_IS_APP_ON};
    private static final String MEMBER_TO_PLACE_COLUMN_REGISTRATION_ID = "ID";
    private final String MEMBER_TO_PLACE_TABLE_CREATION = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s  INTEGER NOT NULL," +
                    "%s  INTEGER NOT NULL," +
                    "FOREIGN KEY(%s) REFERENCES %s(%s)," +
                    "FOREIGN KEY(%s) REFERENCES %s(%s));", MEMBER_TO_PLACE_TABLE_NAME, MEMBER_TO_PLACE_COLUMN_REGISTRATION_ID,
            MEMBER_TO_PLACE_COLUMN_PLACE_ID,
            MEMBER_TO_PLACE_COLUMN_MEMBER_ID,
            MEMBER_TO_PLACE_COLUMN_PLACE_ID, PLACES_TABLE_NAME, PLACES_COLUMN_PLACE_ID,
            MEMBER_TO_PLACE_COLUMN_MEMBER_ID, FAMILY_MEMBERS_TABLE_NAME, FAMILY_MEMBERS_COLUMN_MEMBER_ID);
    public static final String[] MEMBER_TO_PLACE_DATA_COLUMNS = {MEMBER_TO_PLACE_COLUMN_REGISTRATION_ID, MEMBER_TO_PLACE_COLUMN_PLACE_ID, MEMBER_TO_PLACE_COLUMN_MEMBER_ID};
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "familytracker.db";


    public FTDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(NOTIFICATION_TABLE_CREATION);
        sqLiteDatabase.execSQL(FAMILIES_TABLE_CREATION);
        sqLiteDatabase.execSQL(FAMILY_MEMBERS_TABLE_CREATION);
        sqLiteDatabase.execSQL(PLACES_TABLE_CREATION);
        sqLiteDatabase.execSQL(MEMBER_TO_PLACE_TABLE_CREATION);
        sqLiteDatabase.execSQL(CURR_USER_TABLE_CREATION);
        CreateAndInitAppReqTable(sqLiteDatabase);
//        super.onCreate();
    }

    private void CreateAndInitAppReqTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(APP_REQ_TABLE_CREATION);
        ContentValues Values = new ContentValues();
        Values.put(APP_REQ_COLUMN_IS_FIRST_TIME, true);
        Values.put(APP_REQ_COLUMN_IS_APP_ON, true);
        sqLiteDatabase.insert(APP_REQ_TABLE_NAME, null, Values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
