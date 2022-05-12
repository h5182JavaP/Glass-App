package com.my.app.glassapp.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InquirySaveTable {
    @SerializedName("order[0][glass_type]")
    @Expose
    String data;
//    @SerializedName("unit")
//    @Expose
    String value;

    public InquirySaveTable() {
    }

    public InquirySaveTable(String data, String value) {
        this.data = data;
        this.value = value;
    }

    public InquirySaveTable(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
