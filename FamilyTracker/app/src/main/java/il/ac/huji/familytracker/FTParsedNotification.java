package il.ac.huji.familytracker;

import java.util.ArrayList;

/**
 * Created by Omer on 20/09/2015.
 */
public class FTParsedNotification {
    private enmNotificationTypes m_enmNotifType;
    ;
    private ArrayList<Object> m_arrobjNotifArgs;

    public FTParsedNotification(enmNotificationTypes p_enmType) {
        m_enmNotifType = p_enmType;
    }

    public ArrayList<Object> getNotificationsArgs() {
        return m_arrobjNotifArgs;
    }

    public void setNotificationsArgs(ArrayList<Object> m_arrobjNotifArgs) {
        this.m_arrobjNotifArgs = m_arrobjNotifArgs;
    }

    public enmNotificationTypes getNotificationType() {
        return m_enmNotifType;
    }

    /**
     * ******************
     * **** constants *****
     * ******************
     */

    public enum enmNotificationTypes {
        CREATE_GEOFENCE, GEOFENCE_ALERT, CURRENT_LOC_REQUEST, CURRENT_LOC_RESPONSE
    }


}
