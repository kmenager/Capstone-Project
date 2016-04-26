package io.github.kmenager.getmesomefeed.data.local.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("picture")
    @Expose
    public String picture;

    @SerializedName("locale")
    @Expose
    public String locale;
}
