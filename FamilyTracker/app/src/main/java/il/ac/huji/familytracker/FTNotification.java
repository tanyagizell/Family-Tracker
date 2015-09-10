package il.ac.huji.familytracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Omer on 01/09/2015.
 */
public class FTNotification implements Parcelable {
    public static final String DB_REP_NOTIF_TYPE_ARRIVED = "ARRIVED";
    public static final String DB_REP_NOTIF_TYPE_DEFARTED = "DEPARTED";
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FTNotification> CREATOR = new Parcelable.Creator<FTNotification>() {
        @Override
        public FTNotification createFromParcel(Parcel in) {
            return new FTNotification(in);
        }

        @Override
        public FTNotification[] newArray(int size) {
            return new FTNotification[size];
        }
    };
    private FTNotifStateENUM m_enmType;
    private Date m_dtTimeStamp;
    private String m_strLocName;
    private int m_nRelFamilyId;
    private String m_strNotifSubjectName;


    public FTNotification(String p_strType, String p_strTimeStamp, String p_strLocDisName, int p_nRelFamilyId, String p_strSubjectName) {
        m_enmType = p_strType.equals(DB_REP_NOTIF_TYPE_ARRIVED) ? FTNotifStateENUM.ARRIVAL : FTNotifStateENUM.DEPARTURE;
        //TODO check what parsing is needed
        m_dtTimeStamp = convertPushDateToAppDate(p_strTimeStamp);
        m_strLocName = p_strLocDisName;
        m_nRelFamilyId = p_nRelFamilyId;
        m_strNotifSubjectName = p_strSubjectName;
    }

    protected FTNotification(Parcel in) {
        m_enmType = (FTNotifStateENUM) in.readValue(FTNotifStateENUM.class.getClassLoader());
        long tmpM_dtTimeStamp = in.readLong();
        m_dtTimeStamp = tmpM_dtTimeStamp != -1 ? new Date(tmpM_dtTimeStamp) : null;
        m_strLocName = in.readString();
        m_nRelFamilyId = in.readInt();
        m_strNotifSubjectName = in.readString();
    }

    public FTNotifStateENUM getType() {
        return m_enmType;
    }

    public Date getTimeStamp() {
        return m_dtTimeStamp;
    }

    public String getNotificationLocationDisplayName() {
        return m_strLocName;
    }


    public int getRelaventFamilyId() {
        return m_nRelFamilyId;
    }

    private Date convertPushDateToAppDate(String p_strTimeStamp) {
        SimpleDateFormat sdfStringToDateConvertor = new SimpleDateFormat(FTDataSource.APP_DATETIME_FORMAT);
        Date dtRetVal = null;
        try {
            dtRetVal = (Date) sdfStringToDateConvertor.parse(p_strTimeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return dtRetVal;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(m_enmType);
        dest.writeLong(m_dtTimeStamp != null ? m_dtTimeStamp.getTime() : -1L);
        dest.writeString(m_strLocName);
        dest.writeInt(m_nRelFamilyId);
        dest.writeString(m_strNotifSubjectName);
    }

    public String getSubjectName() {
        return m_strNotifSubjectName;
    }

    public enum FTNotifStateENUM {
        ARRIVAL, DEPARTURE
    }
}