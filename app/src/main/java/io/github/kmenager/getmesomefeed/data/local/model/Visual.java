package io.github.kmenager.getmesomefeed.data.local.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Model representing a visual url for stream
 */
public class Visual implements Parcelable {

    @SerializedName("url")
    public String urlImage;

    @SerializedName("contentType")
    public String contentType;

    public Visual() {
    }

    protected Visual(Parcel in) {
        urlImage = in.readString();
        contentType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(urlImage);
        dest.writeString(contentType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Visual> CREATOR = new Creator<Visual>() {
        @Override
        public Visual createFromParcel(Parcel in) {
            return new Visual(in);
        }

        @Override
        public Visual[] newArray(int size) {
            return new Visual[size];
        }
    };
}
