package il.ac.huji.familytracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/***************************************************************************************
 * Created by Tanyagizell on 23/07/2015.
 *
 * This class represents a family object.
 ****************************************************************************************/
public class Family implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Family> CREATOR = new Parcelable.Creator<Family>() {
        @Override
        public Family createFromParcel(Parcel in) {
            return new Family(in);
        }

        @Override
        public Family[] newArray(int size) {
            return new Family[size];
        }
    };
    ArrayList<FamilyMember> _familyMembers;
    private String _familyName;
    private int _DBFamilyID;

    public Family(String name, int id) {
        _familyName = name;
        _DBFamilyID = id;
    }

    protected Family(Parcel in) {
        if (in.readByte() == 0x01) {
            _familyMembers = new ArrayList<>();
            in.readList(_familyMembers, FamilyMember.class.getClassLoader());
        } else {
            _familyMembers = null;
        }
        _familyName = in.readString();
        _DBFamilyID = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (_familyMembers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(_familyMembers);
        }
        dest.writeString(_familyName);
        dest.writeInt(_DBFamilyID);
    }
}