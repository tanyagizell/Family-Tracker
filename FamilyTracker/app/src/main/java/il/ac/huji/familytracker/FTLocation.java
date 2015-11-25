package il.ac.huji.familytracker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tanyagizell on 23/07/2015.
 */
public class FTLocation implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FTLocation> CREATOR = new Parcelable.Creator<FTLocation>() {
        @Override
        public FTLocation createFromParcel(Parcel in) {
            return new FTLocation(in);
        }

        @Override
        public FTLocation[] newArray(int size) {
            return new FTLocation[size];
        }
    };
    private String m_strCoord;
    private String m_strLocName;
    private String m_strAddr;
    private int m_nLocDBId;
    private int m_FamilyId;

    /*
     * constructor for an empty location with only an ID
     */
    public FTLocation(int id,int familyID){
        this.m_nLocDBId = id;
        this.m_FamilyId = familyID;
        //set all the rest of the parameters with default valies
        this.m_strCoord = "";
        this.m_strLocName = "";
        this.m_strAddr = "";

    }

    public FTLocation(String m_strCoord, String m_strLocName, String m_strAddr,int m_nLocDBId, int m_FamilyId) {
        this.m_strCoord = m_strCoord;
        this.m_strLocName = m_strLocName;
        this.m_strAddr = m_strAddr;
        this.m_nLocDBId = m_nLocDBId;
        this.m_FamilyId = m_FamilyId;
    }

    protected FTLocation(Parcel in) {
        m_strCoord = in.readString();
        m_strLocName = in.readString();
        m_strAddr = in.readString();
        m_nLocDBId = in.readInt();
        m_FamilyId = in.readInt();
    }

//    public FTLocation(String strCoord, StringstrLocName, int nLocId, nFamilyId);

    public String getLocationCoordinates() {
        return m_strCoord;
    }

    public String getLocationName() {
        return m_strLocName;
    }

    public String getLocationAddr() {
        return m_strAddr;
    }

    public int getFamilyId(){return m_FamilyId;}

    public void setLocationAddr(String Address) {
        m_strAddr = Address;
    }
    public void setLocationCoordinates(String p_strCoord) {
         m_strCoord = p_strCoord;
    }
    public void setLocationName(String name){m_strLocName = name;}



    public int getID() {
        return m_nLocDBId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_strCoord);
        dest.writeString(m_strLocName);
        dest.writeString(m_strAddr);
        dest.writeInt(m_nLocDBId);
        dest.writeInt(m_FamilyId);
    }
}