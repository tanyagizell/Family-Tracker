package il.ac.huji.familytracker;

import java.util.Date;

/**
 * Created by Omer on 01/09/2015.
 */
public class FTNotification {
    public static final String DB_REP_NOTIF_TYPE_ARRIVED = "ARRIVED";
    public static final String DB_REP_NOTIF_TYPE_DEFARTED = "DEPARTED";
    private FTNotifStateENUM m_enmType;
    private Date m_dtTimeStamp;
    private String m_strXCoord;
    private String m_strYCoord;
    private int m_nIdInNotifMgr;

    public FTNotification(String p_strType, String p_strTimeStamp, String p_strCoord, int p_nNotifMgrId) {
        m_enmType = p_strType.equals(DB_REP_NOTIF_TYPE_ARRIVED) ? FTNotifStateENUM.ARRIVAL : FTNotifStateENUM.DEPARTURE;
        //TODO check what parsing is needed
        m_dtTimeStamp = convertPushDateToAppDate(p_strTimeStamp);
        AssignXYCoords(p_strCoord);
        m_nIdInNotifMgr = p_nNotifMgrId;
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
    public String toString() {
        //TODO create string representation for log view
        return null;
    }


    public enum FTNotifStateENUM {
        ARRIVAL, DEPARTURE
    }
}
