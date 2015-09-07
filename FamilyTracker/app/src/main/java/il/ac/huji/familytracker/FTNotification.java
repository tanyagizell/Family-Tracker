package il.ac.huji.familytracker;

import android.os.Parcel;
import android.os.Parcelable;

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
    private String m_strXCoord;
    private String m_strYCoord;
    private int m_nIdInNotifMgr;
    private String m_strNotifSubjectName;
    //Possible member ,not necessarily initialized
    private String m_strNotifLocName;


    public FTNotification(String p_strType, String p_strTimeStamp, String p_strCoord, int p_nNotifMgrId, String p_strSubjectName) {
        m_enmType = p_strType.equals(DB_REP_NOTIF_TYPE_ARRIVED) ? FTNotifStateENUM.ARRIVAL : FTNotifStateENUM.DEPARTURE;
        //TODO check what parsing is needed
        m_dtTimeStamp = convertPushDateToAppDate(p_strTimeStamp);
        AssignXYCoords(p_strCoord);
        m_nIdInNotifMgr = p_nNotifMgrId;
        m_strNotifSubjectName = p_strSubjectName;
        m_strNotifLocName = "";
    }

    protected FTNotification(Parcel in) {
        m_enmType = (FTNotifStateENUM) in.readValue(FTNotifStateENUM.class.getClassLoader());
        long tmpM_dtTimeStamp = in.readLong();
        m_dtTimeStamp = tmpM_dtTimeStamp != -1 ? new Date(tmpM_dtTimeStamp) : null;
        m_strXCoord = in.readString();
        m_strYCoord = in.readString();
        m_nIdInNotifMgr = in.readInt();
        m_strNotifSubjectName = in.readString();
        m_strNotifLocName = in.readString();
    }

    public FTNotifStateENUM getType() {
        return m_enmType;
    }

    public Date getTimeStamp() {
        return m_dtTimeStamp;
    }

    public String getXCoord() {
        return m_strXCoord;
    }

    public String getYCoord() {
        return m_strYCoord;
    }

    public int getIdInNotifMgr() {
        return m_nIdInNotifMgr;
    }

    private void AssignXYCoords(String p_strCoord) {
        String[] arrstrSplitRes = p_strCoord.split(",");
        m_strXCoord = arrstrSplitRes[0];
        m_strYCoord = arrstrSplitRes[1];
    }

    private Date convertPushDateToAppDate(String p_strTimeStamp) {
        //TODO check matter
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(m_enmType);
        dest.writeLong(m_dtTimeStamp != null ? m_dtTimeStamp.getTime() : -1L);
        dest.writeString(m_strXCoord);
        dest.writeString(m_strYCoord);
        dest.writeInt(m_nIdInNotifMgr);
        dest.writeString(m_strNotifSubjectName);
    }

    public String getSubjectName() {
        return m_strNotifSubjectName;
    }

    public String getNotificationLocationName() {
        return m_strNotifLocName;
    }

    public void setNotificationLocationName(String m_strNotifLocName) {
        this.m_strNotifLocName = m_strNotifLocName;
    }

    public enum FTNotifStateENUM {
        ARRIVAL, DEPARTURE
    }
}