package il.ac.huji.familytracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tanyagizell on 25/08/2015.
 * <p/>
 * Class for managing data access
 */
public class FTDataSource {

    public static final String APP_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    ;
    private static final String SINGLE_COLUMN_VALUE_CONDITION = "%s = ?";
    SQLiteDatabase _db;
    FTDBHelper _dbHelper;

    public FTDataSource(Context context) {
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

    //Notification table methods

    public ArrayList<FTNotification> GeteMostRecentRequiredNumberOfNotifications(int p_nReqNumber) {
        ArrayList<FTNotification> arrReturnItems = new ArrayList<>();
        Cursor crsrDataRetriever = _db.query(false, FTDBHelper.NOTIFICATION_TABLE_NAME, FTDBHelper.NOTIFICATION_TABLE_DATA_COLUMNS, null, null, null, null, null, null);
        crsrDataRetriever.moveToFirst();
        while (!crsrDataRetriever.isAfterLast()) {
            arrReturnItems.add(BuildNotificationFromRecord(crsrDataRetriever));
            crsrDataRetriever.moveToNext();
        }
        // make sure to close the cursor
        crsrDataRetriever.close();
        Collections.sort(arrReturnItems);
        arrReturnItems = new ArrayList<FTNotification>(arrReturnItems.subList(0, Math.min(p_nReqNumber, arrReturnItems.size())));
        Collections.sort(arrReturnItems);
        return arrReturnItems;
    }

    private FTNotification BuildNotificationFromRecord(Cursor crsrDataRetriever) {
        int nNotifMgr = crsrDataRetriever.getInt(crsrDataRetriever.getColumnIndex(FTDBHelper.NOTIFICATION_COLUMN_FAMILY_ID));
        String strCoord = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.NOTIFICATION_COLUMN_LOC_DISPLAY));
        String strTimeStamp = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.NOTIFICATION_COLUMN_TIME_STAMP));
        String strType = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.NOTIFICATION_COLUMN_TYPE));
        String strSubject = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.NOTIFICATION_COLUMN_SUBJECT_NAME));
        return new FTNotification(strType, strTimeStamp, strCoord, nNotifMgr, strSubject);
    }

    public void InsertNotification(FTNotification p_ntfToInsert) {
        ContentValues values = new ContentValues();
        values.put(FTDBHelper.NOTIFICATION_COLUMN_TYPE, p_ntfToInsert.getType() == FTNotification.FTNotifStateENUM.ARRIVAL ? FTNotification.DB_REP_NOTIF_TYPE_ARRIVED : FTNotification.DB_REP_NOTIF_TYPE_DEFARTED);
        values.put(FTDBHelper.NOTIFICATION_COLUMN_LOC_DISPLAY, p_ntfToInsert.getNotificationLocationDisplayName());
        values.put(FTDBHelper.NOTIFICATION_COLUMN_TIME_STAMP, ProduceDateForDB(p_ntfToInsert.getTimeStamp()));
        values.put(FTDBHelper.NOTIFICATION_COLUMN_FAMILY_ID, p_ntfToInsert.getRelaventFamilyId());
        values.put(FTDBHelper.NOTIFICATION_COLUMN_SUBJECT_NAME, p_ntfToInsert.getSubjectName());
// insert the row
        long id = _db.insert(FTDBHelper.NOTIFICATION_TABLE_NAME, null, values);
    }

    //Notification Helper methods
    private String ProduceDateForDB(Date p_dtDateToConvert) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                APP_DATETIME_FORMAT, Locale.getDefault());
        return dateFormat.format(p_dtDateToConvert);
    }

    //Families table methods
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
        int nDBId = crsrDataRetriever.getInt(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILIES_COLUMN_ID));
        String strName = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILIES_COLUMN_NAME));
        return new Family(strName, nDBId);
    }

    public int InsertFamilyToDB(String familyName) {
        ContentValues values = new ContentValues();
        values.put(FTDBHelper.FAMILIES_COLUMN_NAME, familyName);
// insert the row
        long id = _db.insert(FTDBHelper.FAMILIES_TABLE_NAME, null, values);
        return (int) id;
    }

    public void UpdateFamily(Family p_fmToUpdate) {
        ContentValues values = new ContentValues();
        values.put(FTDBHelper.FAMILIES_COLUMN_NAME, p_fmToUpdate.getfamilyName());
        String[] arrstrUpdateArgs = {Integer.toString(p_fmToUpdate.getFamilyID())};
        _db.update(FTDBHelper.FAMILIES_TABLE_NAME, values, String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.FAMILIES_COLUMN_ID), arrstrUpdateArgs);
    }

    // Family members table Methods
    public ArrayList<FamilyMember> GetFamilyMembersFromDB(int familyId) {
        ArrayList<FamilyMember> arrReturnItems = new ArrayList<>();
        String[] arrstrFamilyIdCondArgs = {Integer.toString(familyId)};
        Cursor crsrDataRetriever = _db.query(false, FTDBHelper.FAMILY_MEMBERS_TABLE_NAME, FTDBHelper.FAMILY_MEMBERS_TABLE_DATA_COLUMNS, String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.FAMILY_MEMBERS_COLUMN_FAMILY_ID), arrstrFamilyIdCondArgs, null, null, null, null);
        crsrDataRetriever.moveToFirst();
        while (!crsrDataRetriever.isAfterLast()) {
            arrReturnItems.add(BuildFamilyMemberFromRecord(crsrDataRetriever));
            crsrDataRetriever.moveToNext();
        }
        crsrDataRetriever.close();
        return arrReturnItems;
    }

    public void UpdateFamilyMember(FamilyMember p_fmToUpdate) {
        ContentValues values = new ContentValues();
        values.put(FTDBHelper.FAMILY_MEMBERS_COLUMN_NAME, p_fmToUpdate.getName());
        values.put(FTDBHelper.FAMILY_MEMBERS_COLUMN_EMAIL, p_fmToUpdate.getEmail());
        String[] arrstrUpdateArgs = {Integer.toString(p_fmToUpdate.getFamilyId())};
        _db.update(FTDBHelper.FAMILY_MEMBERS_TABLE_NAME, values, String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.FAMILY_MEMBERS_COLUMN_MEMBER_ID), arrstrUpdateArgs);
    }

    public FamilyMember GetMemberByPhone(String p_strPhone) {
        ArrayList<FamilyMember> arrReturnItems = new ArrayList<>();
        String[] arrstrWhereArgs = {p_strPhone};
        Cursor crsrDataRetriever = _db.query(false, FTDBHelper.FAMILY_MEMBERS_TABLE_NAME, FTDBHelper.FAMILY_MEMBERS_TABLE_DATA_COLUMNS, String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.FAMILY_MEMBERS_COLUMN_PHONE), arrstrWhereArgs, null, null, null, null);
        crsrDataRetriever.moveToFirst();
        while (!crsrDataRetriever.isAfterLast()) {
            arrReturnItems.add(BuildFamilyMemberFromRecord(crsrDataRetriever));
            crsrDataRetriever.moveToNext();
        }
        crsrDataRetriever.close();
        return arrReturnItems.isEmpty() ? null : arrReturnItems.get(0);
    }

    private FamilyMember BuildFamilyMemberFromRecord(Cursor crsrDataRetriever) {
        int nFamilyId = crsrDataRetriever.getInt(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILY_MEMBERS_COLUMN_FAMILY_ID));
        int nMemberId = crsrDataRetriever.getInt(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILY_MEMBERS_COLUMN_MEMBER_ID));
        String strEmail = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILY_MEMBERS_COLUMN_EMAIL));
        String strPhone = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILY_MEMBERS_COLUMN_PHONE));
        String strName = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.FAMILY_MEMBERS_COLUMN_NAME));
        return new FamilyMember(strName, strPhone, strEmail, nFamilyId, nMemberId);
    }

    //Places Table Methods
    public FTLocation GetLocationByCoordsAndFamily(String p_strCoords, int p_nFamilyId) {
        //TODO edit method to select location according to family
        ArrayList<FTLocation> arrlstlcQueryRes = new ArrayList<>();
        String strTwoColCondition = String.format("%s AND %s", String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.PLACES_COLUMN_COORD), String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.PLACES_COLUMN_FAMILY));
        String[] arrstrLocSelectionArgs = {p_strCoords, Integer.toString(p_nFamilyId)};
        Cursor crsrDataRetriever = _db.query(false, FTDBHelper.PLACES_TABLE_NAME, FTDBHelper.PLACES_TABLE_DATA_COLUMNS, strTwoColCondition, arrstrLocSelectionArgs, null, null, null, null);
        crsrDataRetriever.moveToFirst();
        while (!crsrDataRetriever.isAfterLast()) {
            arrlstlcQueryRes.add(CreateLocationWithCursor(crsrDataRetriever));
            crsrDataRetriever.moveToNext();
        }
        crsrDataRetriever.close();
        return arrlstlcQueryRes.isEmpty() ? null : arrlstlcQueryRes.get(0);
    }

    public ArrayList<FTLocation> GetLocationsByFamily(int p_nFamilyId) {
        ArrayList<FTLocation> arrlstlcQueryRes = new ArrayList<>();

        String[] arrstrLocSelectionArgs = {Integer.toString(p_nFamilyId)};
        Cursor crsrDataRetriever = _db.query(false, FTDBHelper.PLACES_TABLE_NAME, FTDBHelper.PLACES_TABLE_DATA_COLUMNS, String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.PLACES_COLUMN_FAMILY), arrstrLocSelectionArgs, null, null, null, null);
        crsrDataRetriever.moveToFirst();
        while (!crsrDataRetriever.isAfterLast()) {
            arrlstlcQueryRes.add(CreateLocationWithCursor(crsrDataRetriever));
            crsrDataRetriever.moveToNext();
        }
        crsrDataRetriever.close();
        return arrlstlcQueryRes;
    }

    public int AddLocationToFamily(String p_strCoords, String p_strName, int p_nFamily, String p_strAddress) {
        ContentValues values = new ContentValues();
        values.put(FTDBHelper.PLACES_COLUMN_COORD, p_strCoords);
        values.put(FTDBHelper.PLACES_COLUMN_FAMILY, p_nFamily);
        values.put(FTDBHelper.PLACES_COLUMN_NAME, p_strName);
        values.put(FTDBHelper.PLACES_COLUMN_PLACE_ADDRESS, p_strAddress);
// insert the row
        long id = _db.insert(FTDBHelper.PLACES_TABLE_NAME, null, values);
        return (int) id;
    }

    public ArrayList<FamilyMember> removeLocation(int p_nLocId) {
        //get user id's of those registered for location for notification process
        ArrayList<Integer> arrnMemberIdsToNotify = GetIdsOfMembersRegisteredForLoc(p_nLocId);
        ArrayList<FamilyMember> arrfmRetVal = new ArrayList<>();
        //send notifications to the devices registered so that they'll remove the geofences
        if (!arrnMemberIdsToNotify.isEmpty()) {
            arrfmRetVal = GetMembersByIds(arrnMemberIdsToNotify);
            deleteRegistrationFromLocOfMembers(arrnMemberIdsToNotify, p_nLocId);
        }

        DeleteLocFromLocTable(p_nLocId);
        return arrfmRetVal;


    }

    private void DeleteLocFromLocTable(int p_nLocId) {
        String[] arrstrDeleteArgs = {Integer.toString(p_nLocId)};
        String strQuery = String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.PLACES_COLUMN_PLACE_ID);
        _db.delete(FTDBHelper.PLACES_TABLE_NAME, strQuery, arrstrDeleteArgs);
    }

    private FTLocation CreateLocationWithCursor(Cursor crsrDataRetriever) {
        String strCoord = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.PLACES_COLUMN_COORD));
        String strLocName = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.PLACES_COLUMN_NAME));
        int nLocId = crsrDataRetriever.getInt(crsrDataRetriever.getColumnIndex(FTDBHelper.PLACES_COLUMN_PLACE_ID));
        int nFamilyId = crsrDataRetriever.getInt(crsrDataRetriever.getColumnIndex(FTDBHelper.PLACES_COLUMN_FAMILY));
        String strAddress = crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.PLACES_COLUMN_PLACE_ADDRESS));
        return new FTLocation(strCoord, strLocName, strAddress, nLocId, nFamilyId);
    }

    //Members To Places Table Methods
    public void InsertMemberRegistrationToLocation(int p_nLocId, int p_nMemberId) {
        ContentValues values = new ContentValues();
        values.put(FTDBHelper.MEMBER_TO_PLACE_COLUMN_PLACE_ID, p_nLocId);
        values.put(FTDBHelper.MEMBER_TO_PLACE_COLUMN_MEMBER_ID, p_nMemberId);
// insert the row
        long id = _db.insert(FTDBHelper.MEMBER_TO_PLACE_TABLE_NAME, null, values);
    }

    public void DeleteMemberRegistrationToLocation(int p_nLocId, int p_nMemberId) {
        String[] arrstrDeleteArgs = {Integer.toString(p_nMemberId), Integer.toString(p_nLocId)};
        String strQuery = String.format("%s AND %s", String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.MEMBER_TO_PLACE_COLUMN_MEMBER_ID), String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.MEMBER_TO_PLACE_COLUMN_PLACE_ID));
        _db.delete(FTDBHelper.MEMBER_TO_PLACE_TABLE_NAME, strQuery, arrstrDeleteArgs);
    }

    private void deleteRegistrationFromLocOfMembers(ArrayList<Integer> arrnMemberIdsToNotify, int p_nLocId) {
        ArrayList<String> arrstrComponentsOfQuery = ProvideWithDeleteComponents(arrnMemberIdsToNotify, p_nLocId);
        String strQuery = arrstrComponentsOfQuery.get(0);
        ArrayList<String> arrstrArgs = new ArrayList<>(arrstrComponentsOfQuery.subList(1, arrstrComponentsOfQuery.size() - 1));
        String[] arrstrDeleteArgs = new String[arrstrArgs.size()];
        arrstrDeleteArgs = arrstrArgs.toArray(arrstrDeleteArgs);
        _db.delete(FTDBHelper.MEMBER_TO_PLACE_TABLE_NAME, strQuery, arrstrDeleteArgs);
    }

    private ArrayList<Integer> GetIdsOfMembersRegisteredForLoc(int p_nLocId) {
        ArrayList<Integer> arrnRetVal = new ArrayList<>();
        String[] arrstrMemberIdCol = {FTDBHelper.MEMBER_TO_PLACE_COLUMN_MEMBER_ID};
        String[] arrstrMemberSelectionArgs = {Integer.toString(p_nLocId)};
        Cursor crsrDataRetriever = _db.query(false, FTDBHelper.MEMBER_TO_PLACE_TABLE_NAME, arrstrMemberIdCol, String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.MEMBER_TO_PLACE_COLUMN_PLACE_ID), arrstrMemberSelectionArgs, null, null, null, null);
        crsrDataRetriever.moveToFirst();
        while (!crsrDataRetriever.isAfterLast()) {
            arrnRetVal.add(crsrDataRetriever.getInt(crsrDataRetriever.getColumnIndex(FTDBHelper.MEMBER_TO_PLACE_COLUMN_MEMBER_ID)));
            crsrDataRetriever.moveToNext();
        }
        crsrDataRetriever.close();
        return arrnRetVal;
    }

    // Family Members Table Methods
    public void InsertFamilyMemberToDB(String p_strName, String p_strPhone, String p_strEmail, int p_nFamilyId) {
        ContentValues values = new ContentValues();
        values.put(FTDBHelper.FAMILY_MEMBERS_COLUMN_EMAIL, p_strEmail);
        values.put(FTDBHelper.FAMILY_MEMBERS_COLUMN_NAME, p_strName);
        values.put(FTDBHelper.FAMILY_MEMBERS_COLUMN_PHONE, p_strPhone);
        values.put(FTDBHelper.FAMILY_MEMBERS_COLUMN_FAMILY_ID, p_nFamilyId);
// insert the row
        long id = _db.insert(FTDBHelper.FAMILY_MEMBERS_TABLE_NAME, null, values);
    }

    public void DeleteFamilyMember(int p_nMemberId) {
        String[] arrstrRegDeleteArgs = {FTDBHelper.MEMBER_TO_PLACE_COLUMN_MEMBER_ID, Integer.toString(p_nMemberId)};
        String strQuery = String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.FAMILY_MEMBERS_COLUMN_MEMBER_ID);
        _db.delete(FTDBHelper.MEMBER_TO_PLACE_TABLE_NAME, strQuery, arrstrRegDeleteArgs);
        String[] arrstrMemberDeleteArgs = {Integer.toString(p_nMemberId)};
        _db.delete(FTDBHelper.MEMBER_TO_PLACE_TABLE_NAME, strQuery, arrstrMemberDeleteArgs);
    }

    private ArrayList<FamilyMember> GetMembersByIds(ArrayList<Integer> arrnMemberIdsToNotify) {
        ArrayList<FamilyMember> arrlstlcQueryRes = new ArrayList<>();
        ArrayList<String> arrstrFuncOutput = new ArrayList<>();
        String strMemberSelectionClause = ConstructMultipleOptionsSqlClause(FTDBHelper.FAMILY_MEMBERS_COLUMN_MEMBER_ID, false, arrnMemberIdsToNotify, arrstrFuncOutput);
        String[] arrstrLocSelectionArgs = new String[arrstrFuncOutput.size()];
        arrstrLocSelectionArgs = arrstrFuncOutput.toArray(arrstrLocSelectionArgs);
        Cursor crsrDataRetriever = _db.query(false, FTDBHelper.FAMILY_MEMBERS_TABLE_NAME, FTDBHelper.FAMILY_MEMBERS_TABLE_DATA_COLUMNS, strMemberSelectionClause, arrstrLocSelectionArgs, null, null, null, null);
        crsrDataRetriever.moveToFirst();
        while (!crsrDataRetriever.isAfterLast()) {
            arrlstlcQueryRes.add(BuildFamilyMemberFromRecord(crsrDataRetriever));
            crsrDataRetriever.moveToNext();
        }
        crsrDataRetriever.close();
        return arrlstlcQueryRes;
    }

    public void UpdateLocationData(FTLocation p_objLocToUpdate)
    {
        ContentValues Values = new ContentValues();
        String[] arrstrUpdateKeyArgs = { Integer.toString(p_objLocToUpdate.getID())};
        Values.put(FTDBHelper.PLACES_COLUMN_COORD, p_objLocToUpdate.getLocationCoordinates());
        Values.put(FTDBHelper.PLACES_COLUMN_NAME, p_objLocToUpdate.getLocationName());
        Values.put(FTDBHelper.PLACES_COLUMN_PLACE_ADDRESS, p_objLocToUpdate.getLocationAddr());

        _db.update(FTDBHelper.PLACES_TABLE_NAME, Values, String.format(SINGLE_COLUMN_VALUE_CONDITION,FTDBHelper.PLACES_COLUMN_PLACE_ID), arrstrUpdateKeyArgs);
    }

    //General use private methods
    private ArrayList<String> ProvideWithDeleteComponents(ArrayList<Integer> arrnMemberIdsToNotify, int p_nLocId) {
        ArrayList<String> arrstrRetVal = new ArrayList<>();
        ArrayList<String> arrstrQueryArgs = new ArrayList<>();
        String strDeleteQuery;
        String strIdPosQuerySegment = ConstructMultipleOptionsSqlClause(FTDBHelper.MEMBER_TO_PLACE_COLUMN_MEMBER_ID, false, arrnMemberIdsToNotify, arrstrQueryArgs);
        if (arrnMemberIdsToNotify.size() > 1) {
            strIdPosQuerySegment = String.format("(%s)", strIdPosQuerySegment);
        }
        strDeleteQuery = strIdPosQuerySegment + " AND " + String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.MEMBER_TO_PLACE_COLUMN_PLACE_ID);
        arrstrQueryArgs.add(Integer.toString(p_nLocId));
        arrstrRetVal.add(strDeleteQuery);
        arrstrRetVal.addAll(arrstrQueryArgs);
        return arrstrRetVal;
    }

    private String ConstructMultipleOptionsSqlClause(String p_strClauseColOfElems, boolean blnIsAnd, ArrayList<Integer> arrnClauseElems, ArrayList<String> arrstrQueryArgsOutput) {
        String strJoiningOperator = blnIsAnd ? " AND " : " OR ";
        String strIdPosQuerySegment = String.format(SINGLE_COLUMN_VALUE_CONDITION, p_strClauseColOfElems);
        arrstrQueryArgsOutput.add(Integer.toString(arrnClauseElems.get(0)));
        for (int nMembersIterator = 1; nMembersIterator < arrnClauseElems.size(); nMembersIterator++) {
            strIdPosQuerySegment += strJoiningOperator + String.format(SINGLE_COLUMN_VALUE_CONDITION, p_strClauseColOfElems);
            arrstrQueryArgsOutput.add(Integer.toString(arrnClauseElems.get(nMembersIterator)));
        }

        return strIdPosQuerySegment;
    }

    public ArrayList<FamilyMember> GetFamilyMembersRegisteredForLoc(int p_nLocId) {
        ArrayList<Integer> arrnReqMembersIds = GetIdsOfMembersRegisteredForLoc(p_nLocId);
        return (arrnReqMembersIds.size() == 0)? new ArrayList<FamilyMember>(): GetMembersByIds(arrnReqMembersIds);
    }

    public String getLocNameByFamily(int familyId, String strLatLng) {
        ArrayList<String> arrReturnItems = new ArrayList<>();
        String[] arrstrWhereArgs = {Integer.toString(familyId), strLatLng};
        String[] trialargs = {Integer.toString(familyId)};
        String[] arrstrSelectCols = {FTDBHelper.PLACES_COLUMN_NAME};
        String[] arrAttemptCols = {FTDBHelper.PLACES_COLUMN_COORD};
        Cursor crsrDataRetriever = _db.query(false, FTDBHelper.PLACES_TABLE_NAME, arrstrSelectCols, String.format("%s AND %s", String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.PLACES_COLUMN_FAMILY), String.format(SINGLE_COLUMN_VALUE_CONDITION, FTDBHelper.PLACES_COLUMN_COORD)), arrstrWhereArgs, null, null, null, null);
        crsrDataRetriever.moveToFirst();
        while (!crsrDataRetriever.isAfterLast()) {
            arrReturnItems.add(crsrDataRetriever.getString(crsrDataRetriever.getColumnIndex(FTDBHelper.PLACES_COLUMN_NAME)));
            crsrDataRetriever.moveToNext();
        }
        crsrDataRetriever.close();
        return arrReturnItems.isEmpty() ? null : arrReturnItems.get(0);
    }

    public Boolean GetAppActivationStatus() {
        ArrayList<Boolean> arrblnRes = new ArrayList<>();
        String[] arrstrStaticReceiverStatusCol = {FTDBHelper.APP_REQ_COLUMN_IS_APP_ON};
        Cursor crsrRes = _db.query(false, FTDBHelper.APP_REQ_TABLE_NAME, arrstrStaticReceiverStatusCol, null, null, null, null, null, null);
        crsrRes.moveToFirst();
        while (!crsrRes.isAfterLast()) {
            arrblnRes.add(crsrRes.getInt(crsrRes.getColumnIndex(FTDBHelper.APP_REQ_COLUMN_IS_APP_ON)) == 0 ? false : true);
            crsrRes.moveToNext();
        }
        crsrRes.close();
        return arrblnRes.get(0);
    }

    public Boolean IsAppFirstActivation() {
        ArrayList<Boolean> arrblnRes = new ArrayList<>();
        String[] arrstrIsFirstTimeCol = {FTDBHelper.APP_REQ_COLUMN_IS_FIRST_TIME};
        Cursor crsrRes = _db.query(false, FTDBHelper.APP_REQ_TABLE_NAME, arrstrIsFirstTimeCol, null, null, null, null, null, null);
        crsrRes.moveToFirst();
        while (!crsrRes.isAfterLast()) {
            arrblnRes.add(crsrRes.getInt(crsrRes.getColumnIndex(FTDBHelper.APP_REQ_COLUMN_IS_FIRST_TIME)) == 0 ? false : true);
            crsrRes.moveToNext();
        }
        crsrRes.close();
        return arrblnRes.get(0);
    }

    public void UpdateAppState(boolean blnReceiverStatus) {
        ContentValues Values = new ContentValues();
        Values.put(FTDBHelper.APP_REQ_COLUMN_IS_APP_ON, blnReceiverStatus);
        _db.update(FTDBHelper.APP_REQ_TABLE_NAME, Values, null, null);
    }

    public void UpdateAppPassedInstallation() {
        ContentValues Values = new ContentValues();
        Values.put(FTDBHelper.APP_REQ_COLUMN_IS_FIRST_TIME, false);
        _db.update(FTDBHelper.APP_REQ_TABLE_NAME, Values, null, null);
    }

    public void InsertCurrUserData(String p_strPhone, String p_strName, enmUserStatus p_enmUserState) {
        ContentValues values = new ContentValues();
        values.put(FTDBHelper.CURR_USER_COLUMN_NAME, p_strName);
        values.put(FTDBHelper.CURR_USER_COLUMN_PHONE, p_strPhone);
        values.put(FTDBHelper.CURR_USER_COLUMN_STATUS, p_enmUserState.toString());
// insert the row
        long id = _db.insert(FTDBHelper.CURR_USER_TABLE_NAME, null, values);
    }

    public Boolean IsParentUser() {
        ArrayList<enmUserStatus> arrblnRes = new ArrayList<>();
        String[] arrstrUserState = {FTDBHelper.CURR_USER_COLUMN_STATUS};
        Cursor crsrRes = _db.query(false, FTDBHelper.CURR_USER_TABLE_NAME, arrstrUserState, null, null, null, null, null, null);
        crsrRes.moveToFirst();
        while (!crsrRes.isAfterLast()) {
            arrblnRes.add(enmUserStatus.valueOf(crsrRes.getString(crsrRes.getColumnIndex(FTDBHelper.CURR_USER_COLUMN_STATUS))));
            crsrRes.moveToNext();
        }
        crsrRes.close();
        return arrblnRes.get(0) == enmUserStatus.PARENT;
    }

    public void CreateAuthUsersTable() {
        _db.execSQL(FTDBHelper.AUTH_USER_TABLE_CREATION);

    }

    public int GetLastUsedNotifId() {
        ArrayList<Integer> arrnRes = new ArrayList<>();
        String[] arrstrStaticReceiverStatusCol = {FTDBHelper.APP_REQ_COLUMN_NOTIFICATION_LAST_ID};
        Cursor crsrRes = _db.query(false, FTDBHelper.APP_REQ_TABLE_NAME, arrstrStaticReceiverStatusCol, null, null, null, null, null, null);
        crsrRes.moveToFirst();
        while (!crsrRes.isAfterLast()) {
            arrnRes.add(crsrRes.getInt(crsrRes.getColumnIndex(FTDBHelper.APP_REQ_COLUMN_NOTIFICATION_LAST_ID)));
            crsrRes.moveToNext();
        }
        crsrRes.close();
        return arrnRes.get(0);
    }

    public void UpdateLastUsedNotifId(int p_nLastNotifId) {
        ContentValues Values = new ContentValues();
        Values.put(FTDBHelper.APP_REQ_COLUMN_NOTIFICATION_LAST_ID, p_nLastNotifId);
        _db.update(FTDBHelper.APP_REQ_TABLE_NAME, Values, null, null);
    }
    public String GetCurPhone() {
        ArrayList<String> arrblnRes = new ArrayList<>();
        String[] arrstrUserPhone = {FTDBHelper.CURR_USER_COLUMN_PHONE};
        Cursor crsrRes = _db.query(false, FTDBHelper.CURR_USER_TABLE_NAME, arrstrUserPhone, null, null, null, null, null, null);
        crsrRes.moveToFirst();
        while (!crsrRes.isAfterLast()) {
            arrblnRes.add(crsrRes.getString(crsrRes.getColumnIndex(FTDBHelper.CURR_USER_COLUMN_PHONE)));
            crsrRes.moveToNext();
        }
        crsrRes.close();
        return arrblnRes.get(0);
    }

    public void InsertAuthorized(String p_strAuthPhone) {
        ContentValues values = new ContentValues();
        values.put(FTDBHelper.AUTH_USER_COLUMN_PHONE, p_strAuthPhone);
// insert the row
        long id = _db.insert(FTDBHelper.AUTH_USER_TABLE_NAME, null, values);
    }

    public void updateGeofenceToCreator(String latlng, String s) {
        ContentValues values = new ContentValues();
        values.put(FTDBHelper.GEOFENCE_MAPPING_COLUMN_LATLANG, latlng);
        values.put(FTDBHelper.GEOFENCE_MAPPING_COLUMN_CREATOR, s);
// insert the row
        long id = _db.insert(FTDBHelper.GEOFENCE_MAPPING_TABLE_NAME, null, values);
    }

    public String GetGeofenceCreator(String LatLang)
    {
        ArrayList<String> arrblnRes = new ArrayList<>();
        String[] arrstrGeofenceLoc = {LatLang};
        Cursor crsrRes = _db.query(false, FTDBHelper.GEOFENCE_MAPPING_TABLE_NAME, arrstrGeofenceLoc,
                String.format(SINGLE_COLUMN_VALUE_CONDITION,FTDBHelper.GEOFENCE_MAPPING_COLUMN_LATLANG),
                arrstrGeofenceLoc, null, null, null, null);
        crsrRes.moveToFirst();
        while (!crsrRes.isAfterLast()) {
            arrblnRes.add(crsrRes.getString(crsrRes.getColumnIndex(FTDBHelper.CURR_USER_COLUMN_PHONE)));
            crsrRes.moveToNext();
        }
        crsrRes.close();
        return arrblnRes.get(0);
    }

    public enum enmUserStatus {
        PARENT, CHILD
    }
}
