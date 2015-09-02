package il.ac.huji.familytracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tanyagizell on 25/08/2015.
 *
 * Class for managing data access
 */
public class FTDataSource {

    public static final String APP_DATETIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private static final String SINGLE_COLUMN_VALUE_CONDITION = "? = ?";
    SQLiteDatabase _db;
    FTDBHelper _dbHelper;

    public FTDataSource(Context context)
    {
        _dbHelper = new FTDBHelper(context);
    }

    public void OpenToRead() {
        //TODO find way to handle concurrency
        _db = _dbHelper.getWritableDatabase();
        //(? need to check)_db.enableWriteAheadLogging();
    }

    public void OpenToWrite() {
        //TODO find way to handle concurrency
        _db = _dbHelper.getWritableDatabase();
    }

    public void close() {
        //TODO find way to handle concurrency
        _dbHelper.close();
    }

    public ArrayList<FTNotification> GeteNotificationsItemsFromDB() {
        ArrayList<FTNotification> arrReturnItems = new ArrayList<>();
        Cursor crsrDataRetriever = _db.query(false, FTDBHelper.NOTIFICATION_TABLE_NAME, FTDBHelper.NOTIFICATION_TABLE_DATA_COLUMNS, null, null, null, null, null, null);
        crsrDataRetriever.moveToFirst();
        while (!crsrDataRetriever.isAfterLast()) {
            arrReturnItems.add(BuildNotificationFromRecord(crsrDataRetriever));
            crsrDataRetriever.moveToNext();
        }
        // make sure to close the cursor
        crsrDataRetriever.close();
        return arrReturnItems;
    }

    private FTNotification BuildNotificationFromRecord(Cursor crsrDataRetriever) {
        int nNotifMgr = crsrDataRetriever.getInt(crsrDataRetriever.getColumnIndex(FTDBHelper.NOTIFICATION_COLUMN_NTF_MGR_ID));
        String strCoord = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.NOTIFICATION_COLUMN_COORD));
        String strTimeStamp = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.NOTIFICATION_COLUMN_TIME_STAMP));
        String strType = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.NOTIFICATION_COLUMN_TYPE));
        return new FTNotification(strType, strTimeStamp, strCoord, nNotifMgr);
    }

    public void InsertNotification(FTNotification p_ntfToInsert) {
        ContentValues values = new ContentValues();
        values.put(FTDBHelper.NOTIFICATION_COLUMN_TYPE, p_ntfToInsert.getType() == FTNotification.FTNotifStateENUM.ARRIVAL ? FTNotification.DB_REP_NOTIF_TYPE_ARRIVED : FTNotification.DB_REP_NOTIF_TYPE_DEFARTED);
        values.put(FTDBHelper.NOTIFICATION_COLUMN_COORD, FormSingleCoordVal(p_ntfToInsert));
        values.put(FTDBHelper.NOTIFICATION_COLUMN_TIME_STAMP, ProduceDateForDB(p_ntfToInsert.getTimeStamp()));
        values.put(FTDBHelper.NOTIFICATION_COLUMN_NTF_MGR_ID, p_ntfToInsert.getIdInNotifMgr());
// insert the row
        long id = _db.insert(FTDBHelper.NOTIFICATION_TABLE_NAME, null, values);
    }

    private String ProduceDateForDB(Date p_dtDateToConvert) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                APP_DATETIME_FORMAT, Locale.getDefault());
        return dateFormat.format(p_dtDateToConvert);
    }

    private String FormSingleCoordVal(FTNotification p_ntfToInsert) {
        return p_ntfToInsert.getXCoord() + ',' + p_ntfToInsert.getYCoord();
    }


    public ArrayList<Family> GetFamiliesFromDB() {
        ArrayList<Family> arrReturnItems = new ArrayList<>();
        Cursor crsrDataRetriever = _db.query(false, FTDBHelper.FAMILIES_TABLE_NAME, FTDBHelper.FAMILIES_TABLE_DATA_COLUMNS, null, null, null, null, null, null);
        crsrDataRetriever.moveToFirst();
        while (!crsrDataRetriever.isAfterLast()) {
            arrReturnItems.add(BuildFamilyFromRecord(crsrDataRetriever));
            crsrDataRetriever.moveToNext();
        }
        // make sure to close the cursor
        crsrDataRetriever.close();
        return arrReturnItems;
        //TODO change return value to array of families -include id in db
    }

    private Family BuildFamilyFromRecord(Cursor crsrDataRetriever) {
        int nDBId = crsrDataRetriever.getInt(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILIES_COLUMN_NAME));
        String strName = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILIES_COLUMN_NAME));
        return new Family(strName, nDBId);
    }

    public void InsertFamilyToDB(String familyName) {
        ContentValues values = new ContentValues();
        values.put(FTDBHelper.FAMILIES_COLUMN_NAME, familyName);
// insert the row
        long id = _db.insert(FTDBHelper.FAMILIES_TABLE_NAME, null, values);
    }

    public ArrayList<FamilyMember> GetFamilyMembersFromDB(int familyId) {
        ArrayList<FamilyMember> arrReturnItems = new ArrayList<>();
        String[] arrstrFamilyIdCondArgs = {FTDBHelper.FAMILY_MEMBERS_COLUMN_FAMILY_ID, Integer.toString(familyId)};
        Cursor crsrDataRetriever = _db.query(false, FTDBHelper.FAMILY_MEMBERS_TABLE_NAME, FTDBHelper.FAMILY_MEMBERS_TABLE_DATA_COLUMNS, SINGLE_COLUMN_VALUE_CONDITION, arrstrFamilyIdCondArgs, null, null, null, null);
        crsrDataRetriever.moveToFirst();
        while (!crsrDataRetriever.isAfterLast()) {
            arrReturnItems.add(BuildFamilyMemberFromRecord(crsrDataRetriever));
            crsrDataRetriever.moveToNext();
        }
        crsrDataRetriever.close();
        return arrReturnItems;
    }

    private FamilyMember BuildFamilyMemberFromRecord(Cursor crsrDataRetriever) {
        int nFamilyId = crsrDataRetriever.getInt(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILY_MEMBERS_COLUMN_MEMBER_ID));
        int nMemberId = crsrDataRetriever.getInt(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILY_MEMBERS_COLUMN_FAMILY_ID));
        String strEmail = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILY_MEMBERS_COLUMN_EMAIL));
        String strPhone = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILY_MEMBERS_COLUMN_PHONE));
        String strName = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILY_MEMBERS_COLUMN_NAME));
        return new FamilyMember(strName, strPhone, strEmail, nFamilyId, nMemberId);
    }

    public void UpdateFamilyMember(FamilyMember p_fmToUpdate) {
        ContentValues values = new ContentValues();
        values.put(FTDBHelper.FAMILY_MEMBERS_COLUMN_NAME, p_fmToUpdate.getName());
        values.put(FTDBHelper.FAMILY_MEMBERS_COLUMN_EMAIL, p_fmToUpdate.getEmail());
        String[] arrstrUpdateArgs = {FTDBHelper.FAMILY_MEMBERS_COLUMN_MEMBER_ID, Integer.toString(p_fmToUpdate.getFamilyId())};
        _db.update(FTDBHelper.FAMILY_MEMBERS_TABLE_NAME, values, SINGLE_COLUMN_VALUE_CONDITION, arrstrUpdateArgs);
    }

    public FTLocation GetLocationByCoords(String p_strCoords) {
        ArrayList<FTLocation> arrlstlcQueryRes = new ArrayList<>();
        String[] arrstrLocSelectionArgs = {FTDBHelper.PLACES_COLUMN_COORD, p_strCoords};
        Cursor crsrDataRetriever = _db.query(false, FTDBHelper.PLACES_TABLE_NAME, FTDBHelper.PLACES_TABLE_DATA_COLUMNS, SINGLE_COLUMN_VALUE_CONDITION, arrstrLocSelectionArgs, null, null, null, null);
        crsrDataRetriever.moveToFirst();
        while (!crsrDataRetriever.isAfterLast()) {
            arrlstlcQueryRes.add(CreateLocationWithCursor(crsrDataRetriever));
            crsrDataRetriever.moveToNext();
        }
        crsrDataRetriever.close();
        return arrlstlcQueryRes.isEmpty() ? null : arrlstlcQueryRes.get(0);
    }

    private FTLocation CreateLocationWithCursor(Cursor crsrDataRetriever) {
        String strCoord = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.PLACES_COLUMN_COORD));
        String strLocName = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.PLACES_COLUMN_NAME));
        int nLocId = crsrDataRetriever.getInt(crsrDataRetriever.getColumnIndex(FTDBHelper.PLACES_COLUMN_PLACE_ID));
        return new FTLocation(strCoord, strLocName, nLocId);
    }
}
