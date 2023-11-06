package com.uit.myairquality.Model;

import com.google.gson.annotations.SerializedName;

public class Asset {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("version")
    private String version;

    @SerializedName("platform")
    private String platform;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getPlatform() {
        return platform;
    }

    // Thêm getter và setter cho các trường dữ liệu khác nếu cần

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", platform='" + platform + '\'' +
                // Thêm các trường dữ liệu khác nếu cần
                '}';
    }
}
