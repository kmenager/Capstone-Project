package io.github.kmenager.getmesomefeed.data.local.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Model representing an alternate reference to original article url
 */
public class Alternate implements Parcelable {

    @SerializedName("href")
    public String urlArticle;

    public Alternate() {
    }

    protected Alternate(Parcel in) {
        urlArticle = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(urlArticle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Alternate> CREATOR = new Creator<Alternate>() {
        @Override
        public Alternate createFromParcel(Parcel in) {
            return new Alternate(in);
        }

        @Override
        public Alternate[] newArray(int size) {
            return new Alternate[size];
        }
    };
}
