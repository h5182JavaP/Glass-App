package com.my.app.glassapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SGUTable {

    @PrimaryKey(autoGenerate = true)
    private int sgu_id;
    @ColumnInfo(name = "Standard")
    String sgu_standard;
    @ColumnInfo(name = "Thickness")
    String sgu_thickness;
    @ColumnInfo(name = "Material")
    String sgu_materialDetails;
    @ColumnInfo(name = "Width")
    String sgu_glassWidth;
    @ColumnInfo(name = "Height")
    String sgu_glassHeight;
    @ColumnInfo(name = "Quantity")
    String sgu_quantity;
    @ColumnInfo(name = "Note")
    private String sgu_note;
    @ColumnInfo(name = "ImagePath")
    private String sgu_path;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    byte[] sgu_image = null;

    public String getSgu_path() {
        return sgu_path;
    }

    public void setSgu_path(String sgu_path) {
        this.sgu_path = sgu_path;
    }

    public byte[] getSgu_image() {
        return sgu_image;
    }

    public void setSgu_image(byte[] sgu_image) {
        this.sgu_image = sgu_image;
    }

    public int getSgu_id() {
        return sgu_id;
    }

    public void setSgu_id(int sgu_id) {
        this.sgu_id = sgu_id;
    }

    public String getSgu_standard() {
        return sgu_standard;
    }

    public void setSgu_standard(String sgu_standard) {
        this.sgu_standard = sgu_standard;
    }

    public String getSgu_thickness() {
        return sgu_thickness;
    }

    public void setSgu_thickness(String sgu_thickness) {
        this.sgu_thickness = sgu_thickness;
    }

    public String getSgu_materialDetails() {
        return sgu_materialDetails;
    }

    public void setSgu_materialDetails(String sgu_materialDetails) {
        this.sgu_materialDetails = sgu_materialDetails;
    }

    public String getSgu_glassWidth() {
        return sgu_glassWidth;
    }

    public void setSgu_glassWidth(String sgu_glassWidth) {
        this.sgu_glassWidth = sgu_glassWidth;
    }

    public String getSgu_glassHeight() {
        return sgu_glassHeight;
    }

    public void setSgu_glassHeight(String sgu_glassHeight) {
        this.sgu_glassHeight = sgu_glassHeight;
    }

    public String getSgu_quantity() {
        return sgu_quantity;
    }

    public void setSgu_quantity(String sgu_quantity) {
        this.sgu_quantity = sgu_quantity;
    }

    public String getSgu_note() {
        return sgu_note;
    }

    public void setSgu_note(String sgu_note) {
        this.sgu_note = sgu_note;
    }
}
