package io.github.kmenager.getmesomefeed.data.local.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kevinmenager on 26/04/16.
 */
public class Origin implements Parcelable {

    @SerializedName("title")
    public String title;

    public Origin() {
    }

    protected Origin(Parcel in) {
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Origin> CREATOR = new Creator<Origin>() {
        @Override
        public Origin createFromParcel(Parcel in) {
            return new Origin(in);
        }

        @Override
        public Origin[] newArray(int size) {
            return new Origin[size];
        }
    };
}
