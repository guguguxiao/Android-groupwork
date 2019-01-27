package com.homework.grop.group_homework.network;

import com.google.gson.annotations.SerializedName;


public class PostVideoResponse {

    @SerializedName("url") private String url;
    @SerializedName("success") private boolean success;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override public String toString() {
        return "PostVideoResponse{" +
                "playUrl='" + url + '\'' +
                ", success=" + success +
                '}';
    }
}
