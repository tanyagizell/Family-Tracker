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

    //notifications table constants
    public static final String NOTIFICATION_TABLE_NAME = "notifications";
    public static final String NOTIFICATION_COLUMN_NTF_MGR_ID = "NOTIFICATION_MGR_ID";
    public static final String NOTIFICATION_COLUMN_TYPE = "NOTIF_TYPE";
    public static final String NOTIFICATION_COLUMN_TIME_STAMP = "DATE_RECIEVED";
    public static final String NOTIFICATION_COLUMN_COORD = "COORD";
    public static final String NOTIFICATION_INSERT_COMMAND = String.format("INSERT INTO %s (%s,%s,%s,%s) VALUES (?,?,?,?)", NOTIFICATION_TABLE_NAME, NOTIFICATION_COLUMN_TYPE, NOTIFICATION_COLUMN_COORD, NOTIFICATION_COLUMN_TIME_STAMP, NOTIFICATION_COLUMN_NTF_MGR_ID);
    public static final String NOTIFICATION_COLUMN_DB_ID = "ID";
    private final String NOTIFICATION_TABLE_CREATION = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s DATETIME  NOT NULL," +
                    "%s INTEGER NOT NULL);", NOTIFICATION_TABLE_NAME,
            NOTIFICATION_COLUMN_DB_ID,
            NOTIFICATION_COLUMN_TYPE,
            NOTIFICATION_COLUMN_COORD,
            NOTIFICATION_COLUMN_TIME_STAMP,
            NOTIFICATION_COLUMN_NTF_MGR_ID);
    public static final String[] NOTIFICATION_TABLE_DATA_COLUMNS = {NOTIFICATION_COLUMN_DB_ID, NOTIFICATION_COLUMN_TYPE, NOTIFICATION_COLUMN_COORD, NOTIFICATION_COLUMN_TIME_STAMP, NOTIFICATION_COLUMN_NTF_MGR_ID};
    //places table constants
    public static final String PLACES_TABLE_NAME = "places";
    public static final String PLACES_COLUMN_NAME = "PLACE_NAME";
    public static final String PLACES_COLUMN_COORD = "COORDS";
    public static final String PLACES_COLUMN_FAMILY_ID = "FAMILY";
    public static final String PLACES_INSERT_COMMAND = String.format("INSERT INTO %s (%s,%s) VALUES (?,?)", PLACES_TABLE_NAME, PLACES_COLUMN_NAME, PLACES_COLUMN_COORD);
    public static final String PLACES_COLUMN_PLACE_ID = "ID";
    private final String PLACES_TABLE_CREATION = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s INTEGER NOT NULL," +
                    "FOREIGN KEY(%s) REFERENCES %s(%s));", PLACES_TABLE_NAME,
            PLACES_COLUMN_PLACE_ID,
            PLACES_COLUMN_NAME,
            PLACES_COLUMN_COORD,
            PLACES_COLUMN_FAMILY_ID,
            PLACES_COLUMN_FAMILY_ID,FAMILIES_TABLE_NAME,FAMILIES_COLUMN_ID);
    public static final String[] PLACES_TABLE_DATA_COLUMNS = {PLACES_COLUMN_PLACE_ID, PLACES_COLUMN_NAME, PLACES_COLUMN_COORD};

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
    public static final String MEMBER_TO_PLACE_INSERT_COMMAND = String.format("INSERT INTO  %s (%s,%s) VALUES (?,?)", MEMBER_TO_PLACE_COLUMN_PLACE_ID, MEMBER_TO_PLACE_COLUMN_MEMBER_ID);
    private static final String MEMBER_TO_FAMILY_COLUMN_REGISTRATION_ID = "ID";
    private final String MEMBER_TO_FAMILY_TABLE_CREATION = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s  INTEGER NOT NULL," +
                    "%s  INTEGER NOT NULL" +
                    "FOREIGN KEY(%s) REFERENCES %s(%s)," +
                    "FOREIGN KEY(%s) REFERENCES %s(%s));", MEMBER_TO_PLACE_TABLE_NAME, MEMBER_TO_FAMILY_COLUMN_REGISTRATION_ID,
            MEMBER_TO_PLACE_COLUMN_PLACE_ID,
            MEMBER_TO_PLACE_COLUMN_MEMBER_ID,
            MEMBER_TO_PLACE_COLUMN_PLACE_ID, PLACES_TABLE_NAME, PLACES_COLUMN_PLACE_ID,
            MEMBER_TO_PLACE_COLUMN_MEMBER_ID, FAMILY_MEMBERS_TABLE_NAME, FAMILY_MEMBERS_COLUMN_MEMBER_ID);
    public static final String[] MEMBER_TO_PLACE_DATA_COLUMNS = {MEMBER_TO_FAMILY_COLUMN_REGISTRATION_ID, MEMBER_TO_PLACE_COLUMN_PLACE_ID, MEMBER_TO_PLACE_COLUMN_MEMBER_ID};
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
        sqLiteDatabase.execSQL(MEMBER_TO_FAMILY_TABLE_CREATION);
//        super.onCreate();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
