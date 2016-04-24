package io.github.kmenager.getmesomefeed.data.local.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representing a search object with a list of result
 */
public class QueryFeed {

    @SerializedName("results")
    public List<Result> results = new ArrayList<>();

    @SerializedName("related")
    public List<String> related = new ArrayList<String>();
}
