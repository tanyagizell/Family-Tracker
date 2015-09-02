package il.ac.huji.familytracker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Omer on 30/08/2015.
 */
public class FamilyMember implements Parcelable {
    public static final Parcelable.Creator<FamilyMember> CREATOR = new Parcelable.Creator<FamilyMember>() {
        @Override
        public FamilyMember createFromParcel(Parcel source) {
            return new FamilyMember(source);
        }

        @Override
        public FamilyMember[] newArray(int size) {
            return new FamilyMember[size];
        }
    };
    private String m_strName;
    private String m_strPhoneNumber;
    private String m_strEmail;
    private String m_strLatestCoord;
    private int m_nMemberDBId;
    private int m_nFamilyId;
    //TODO list of locations
    private Boolean m_blnIsEmptyContainer;

    public FamilyMember()
    {
        m_blnIsEmptyContainer = true;
    }

    public FamilyMember(String p_strName, String p_strPhone, String p_strEmail, int p_nFamilyId, int p_nMemberId) {
        constructedFamilyMemberCtorLogic(p_strName, p_strPhone, p_strEmail, p_nFamilyId, p_nMemberId);

    }

    public FamilyMember(Parcel source) {
        //extracting fields
        String strName = source.readString();
        String strEmail = source.readString();
        String strPhone = source.readString();
        String strCoord = source.readString();
        int nFamilyId = source.readInt();
        int nMemberId = source.readInt();

        //constructing family member using the logic of the ctor used to create a family member
        // that has it's data already in the app
        constructedFamilyMemberCtorLogic(strName, strPhone, strEmail, nFamilyId, nMemberId);
        setLatestCoord(strCoord);

    }

    private void constructedFamilyMemberCtorLogic(String p_strName, String p_strPhone, String p_strEmail, int p_nFamilyId, int p_nMemberId) {
        m_blnIsEmptyContainer = false;
        m_strName = p_strName;
        m_strPhoneNumber = p_strPhone;
        m_strEmail = p_strEmail;
        m_nFamilyId = p_nFamilyId;
        m_nMemberDBId = p_nMemberId;
    }

    public String getName() {
        return m_strName;
    }

    public void setName(String m_strName) {
        this.m_strName = m_strName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //order of elements written to parcel ,important to remember fo unwraping the parcel
        //1.name
        //2.email
        //3.phone number
        //4.latest coordination recorded
        //5 family db id
        //6 member db id
        //more fields may be saved in the future for the proper communication with the service
        dest.writeString(m_strName);
        dest.writeString(m_strEmail);
        dest.writeString(m_strPhoneNumber);
        dest.writeString(m_strLatestCoord);
        dest.writeInt(m_nFamilyId);
        dest.writeInt(m_nMemberDBId);

    }

    public int getFamilyId() {
        return m_nFamilyId;
    }
}
