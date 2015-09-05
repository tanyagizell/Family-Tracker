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
    private int m_nLocDBId;
    private int m_FamilyId;

    public FTLocation(String m_strCoord, String m_strLocName, int m_nLocDBId, int m_FamilyId) {
        this.m_strCoord = m_strCoord;
        this.m_strLocName = m_strLocName;
        this.m_nLocDBId = m_nLocDBId;
        this.m_FamilyId = m_FamilyId;
    }

    protected FTLocation(Parcel in) {
        m_strCoord = in.readString();
        m_strLocName = in.readString();
        m_nLocDBId = in.readInt();
        m_FamilyId = in.readInt();
    }

    public String getLocationCoordinates() {
        return m_strCoord;
    }

    public String getLocationName() {
        return m_strLocName;
    }

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
        dest.writeInt(m_nLocDBId);
        dest.writeInt(m_FamilyId);
    }
}