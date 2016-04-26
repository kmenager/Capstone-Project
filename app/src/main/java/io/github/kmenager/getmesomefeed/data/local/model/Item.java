package io.github.kmenager.getmesomefeed.data.local.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representing a item from a stream
 */
public class Item implements Parcelable, ItemView {

    @SerializedName("id")
    public String id;

    @SerializedName("content")
    public Content content;

    @SerializedName("summary")
    public Summary summary;

    @SerializedName("title")
    public String title;

    @SerializedName("published")
    public long date;

    @SerializedName("alternate")
    public List<Alternate> alternate = new ArrayList<>();

    @SerializedName("visual")
    public Visual visual;

    @SerializedName("origin")
    public Origin origin;

    public Item() {
    }

    protected Item(Parcel in) {
        id = in.readString();
        content = in.readParcelable(Content.class.getClassLoader());
        summary = in.readParcelable(Summary.class.getClassLoader());
        title = in.readString();
        date = in.readLong();
        alternate = in.createTypedArrayList(Alternate.CREATOR);
        visual = in.readParcelable(Visual.class.getClassLoader());
        origin = in.readParcelable(Origin.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(content, flags);
        dest.writeParcelable(summary, flags);
        dest.writeString(title);
        dest.writeLong(date);
        dest.writeTypedList(alternate);
        dest.writeParcelable(visual, flags);
        dest.writeParcelable(origin, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getUrlImage() {
        return visual != null ? visual.urlImage : null;
    }

    @Override
    public String getUrlArticle() {
        return (alternate != null && alternate.size() > 0) ? alternate.get(0).urlArticle : null;
    }

    @Override
    public String getContent() {
        return content != null ? content.content : null;
    }

    @Override
    public String getSummary() {
        return summary != null ? summary.content : null;
    }
}
