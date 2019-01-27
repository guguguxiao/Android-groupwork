package com.homework.grop.group_homework.network;

import com.google.gson.annotations.SerializedName;
import com.homework.grop.group_homework.Feed;

import java.util.List;

public class FeedResponse {


    @SerializedName("success") private boolean success;
    @SerializedName("feeds") private List<Feed> feeds;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    @Override public String toString() {
        return "FeedResponse{" +
                " success=" + success +
                ", feeds=" + feeds +
                '}';
    }
}
