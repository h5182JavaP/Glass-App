package com.my.app.glassapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LaminatedDGUTable {

    @PrimaryKey(autoGenerate = true)
    private int ldgu_id;
    @ColumnInfo(name = "Standard")
    String ldgu_standard;
    @ColumnInfo(name = "Glass1")
    String ldgu_glass_1;
    @ColumnInfo(name = "Glass2")
    String ldgu_glass_2;
    @ColumnInfo(name = "Glass3")
    String ldgu_glass_3;
    @ColumnInfo(name = "Gap")
    String ldgu_gap;
    @ColumnInfo(name = "PVB")
    String ldgu_pvb;
    @ColumnInfo(name = "Width")
    String ldgu_glassWidth;
    @ColumnInfo(name = "Height")
    String ldgu_glassHeight;
    @ColumnInfo(name = "Quantity")
    String ldgu_quantity;
    @ColumnInfo(name = "Note")
    String ldgu_note;
    @ColumnInfo(name = "ImagePath")
    private String ldgu_path;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    byte[] ldgu_image;

    public String getLdgu_pvb() {
        return ldgu_pvb;
    }

    public void setLdgu_pvb(String ldgu_pvb) {
        this.ldgu_pvb = ldgu_pvb;
    }

    public String getLdgu_path() {
        return ldgu_path;
    }

    public void setLdgu_path(String ldgu_path) {
        this.ldgu_path = ldgu_path;
    }

    public byte[] getLdgu_image() {
        return ldgu_image;
    }

    public void setLdgu_image(byte[] ldgu_image) {
        this.ldgu_image = ldgu_image;
    }

    public int getLdgu_id() {
        return ldgu_id;
    }

    public void setLdgu_id(int ldgu_id) {
        this.ldgu_id = ldgu_id;
    }

    public String getLdgu_standard() {
        return ldgu_standard;
    }

    public void setLdgu_standard(String ldgu_standard) {
        this.ldgu_standard = ldgu_standard;
    }

    public String getLdgu_glass_1() {
        return ldgu_glass_1;
    }

    public void setLdgu_glass_1(String ldgu_glass_1) {
        this.ldgu_glass_1 = ldgu_glass_1;
    }

    public String getLdgu_glass_2() {
        return ldgu_glass_2;
    }

    public String getLdgu_glass_3() {
        return ldgu_glass_3;
    }

    public void setLdgu_glass_3(String ldgu_glass_3) {
        this.ldgu_glass_3 = ldgu_glass_3;
    }

    public void setLdgu_glass_2(String ldgu_glass_2) {
        this.ldgu_glass_2 = ldgu_glass_2;
    }

    public String getLdgu_gap() {
        return ldgu_gap;
    }

    public void setLdgu_gap(String ldgu_gap) {
        this.ldgu_gap = ldgu_gap;
    }

    public String getLdgu_glassWidth() {
        return ldgu_glassWidth;
    }

    public void setLdgu_glassWidth(String ldgu_glassWidth) {
        this.ldgu_glassWidth = ldgu_glassWidth;
    }

    public String getLdgu_glassHeight() {
        return ldgu_glassHeight;
    }

    public void setLdgu_glassHeight(String ldgu_glassHeight) {
        this.ldgu_glassHeight = ldgu_glassHeight;
    }

    public String getLdgu_quantity() {
        return ldgu_quantity;
    }

    public void setLdgu_quantity(String ldgu_quantity) {
        this.ldgu_quantity = ldgu_quantity;
    }

    public String getLdgu_note() {
        return ldgu_note;
    }

    public void setLdgu_note(String ldgu_note) {
        this.ldgu_note = ldgu_note;
    }
}
