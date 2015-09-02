package il.ac.huji.familytracker;

/**
 * Created by Tanyagizell on 23/07/2015.
 */
public class FTLocation {
    private String m_strCoord;
    private String m_strLocName;
    private int m_nLocDBId;

    public FTLocation(String m_strCoord, String m_strLocName, int m_nLocDBId) {
        this.m_strCoord = m_strCoord;
        this.m_strLocName = m_strLocName;
        this.m_nLocDBId = m_nLocDBId;
    }

    public String getLocationCoordinates() {
        return m_strCoord;
    }

    public String getLocationName() {
        return m_strLocName;
    }

    public int getLocDBId() {
        return m_nLocDBId;
    }
}
