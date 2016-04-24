package io.github.kmenager.getmesomefeed.data.local.model.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import io.github.kmenager.getmesomefeed.data.local.model.ItemView;

/**
 * Model of item saved in database
 */
public class ItemDatabase implements ItemView, Parcelable {
    
    public int id;
    public String remoteId;
    public Date date;
    public String contentOffline;
    public String contentFull;
    public String title;
    public String summary;
    public String urlImage;
    public String urlArticle;

    public ItemDatabase() {
    }

    protected ItemDatabase(Parcel in) {
        id = in.readInt();
        remoteId = in.readString();
        contentOffline = in.readString();
        contentFull = in.readString();
        title = in.readString();
        summary = in.readString();
        urlImage = in.readString();
        urlArticle = in.readString();
    }

    public static final Creator<ItemDatabase> CREATOR = new Creator<ItemDatabase>() {
        @Override
        public ItemDatabase createFromParcel(Parcel in) {
            return new ItemDatabase(in);
        }

        @Override
        public ItemDatabase[] newArray(int size) {
            return new ItemDatabase[size];
        }
    };

    @Override
    public String getId() {
        return remoteId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getUrlImage() {
        return urlImage;
    }

    @Override
    public String getUrlArticle() {
        return urlArticle;
    }

    @Override
    public String getContent() {
        return contentOffline;
    }

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(remoteId);
        dest.writeString(contentOffline);
        dest.writeString(contentFull);
        dest.writeString(title);
        dest.writeString(summary);
        dest.writeString(urlImage);
        dest.writeString(urlArticle);
    }
}
