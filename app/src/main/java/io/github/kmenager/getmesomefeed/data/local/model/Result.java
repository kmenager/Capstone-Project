package io.github.kmenager.getmesomefeed.data.local.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Model representing a search result
 */
public class Result implements Parcelable {

    @SerializedName("feedId")
    public String feedId;

    @SerializedName("title")
    public String title;

    @SerializedName("iconUrl")
    public String iconUrl;

    @SerializedName("visualUrl")
    public String visualUrl;

    @SerializedName("subscribers")
    public int subscribers;

    public boolean isSaved;

    public Result() {
    }

    protected Result(Parcel in) {
        feedId = in.readString();
        title = in.readString();
        iconUrl = in.readString();
        visualUrl = in.readString();
        subscribers = in.readInt();
        isSaved = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(feedId);
        dest.writeString(title);
        dest.writeString(iconUrl);
        dest.writeString(visualUrl);
        dest.writeInt(subscribers);
        dest.writeByte((byte) (isSaved ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}
