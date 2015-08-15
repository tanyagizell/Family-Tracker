package il.ac.huji.familytracker;

import java.util.Date;

/**
 * Created by Tanyagizell on 23/07/2015.
 *
 * This class represents a family member object
 *
 */
public class FamilyMember {


    private String m_strName;
    private String m_strPhoneNumber;
    private String m_strEmail;
    private String m_strLatestCoord;
    //TODO list of locations


    public FamilyMember(String p_strName, String p_strPhone, String p_strEmail)
    {
        m_strName = p_strName;
        m_strPhoneNumber = p_strPhone;
        m_strEmail = p_strEmail;
    }

    public String getName() {
        return m_strName;
    }

    public String getPhoneNumber() {
        return m_strPhoneNumber;
    }

    public String getEmail() {
        return m_strEmail;
    }

    public void setEmail(String p_strEmail) {
        this.m_strEmail = p_strEmail;
    }

    public String getLatestCoord() {
        //TODO add time stamp -possibility
        // or decide on a modus operndi r
        return m_strLatestCoord;
    }

    public void setLatestCoord(String m_strLatestCoord) {
        this.m_strLatestCoord = m_strLatestCoord;
    }
}
