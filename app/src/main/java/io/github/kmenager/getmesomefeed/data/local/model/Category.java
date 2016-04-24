package io.github.kmenager.getmesomefeed.data.local.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kevinmenager on 19/04/16.
 */
public class Category {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("label")
    @Expose
    public String label;
}
