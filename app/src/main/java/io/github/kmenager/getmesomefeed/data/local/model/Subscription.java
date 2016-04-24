package io.github.kmenager.getmesomefeed.data.local.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kevinmenager on 19/04/16.
 */
public class Subscription {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("iconUrl")
    @Expose
    public String iconUrl;

    @SerializedName("visualUrl")
    @Expose
    public String visualUrl;
}
