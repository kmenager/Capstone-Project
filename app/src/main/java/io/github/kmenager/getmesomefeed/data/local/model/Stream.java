package io.github.kmenager.getmesomefeed.data.local.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representing a stream composed with a list of item
 */
public class Stream {

    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("items")
    public List<Item> items = new ArrayList<>();


}
