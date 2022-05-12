package com.my.app.glassapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AnnealedTable {

    @PrimaryKey(autoGenerate = true)
    private int annealed_id;
    @ColumnInfo(name = "Standard")
    String annealed_standard;
    @ColumnInfo(name = "Thickness")
    String annealed_thickness;
    @ColumnInfo(name = "Material")
    String annealed_materialDetails;
    @ColumnInfo(name = "Width")
    String annealed_glassWidth;
    @ColumnInfo(name = "Height")
    String annealed_glassHeight;
    @ColumnInfo(name = "Quantity")
    String annealed_quantity;
    @ColumnInfo(name = "Note")
    private String annealed_note;
    @ColumnInfo(name = "ImagePath")
    private String annealed_path;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    byte[] annealed_image;

    public String getAnnealed_path() {
        return annealed_path;
    }

    public void setAnnealed_path(String annealed_path) {
        this.annealed_path = annealed_path;
    }

    public int getAnnealed_id() {
        return annealed_id;
    }

    public void setAnnealed_id(int annealed_id) {
        this.annealed_id = annealed_id;
    }

    public String getAnnealed_standard() {
        return annealed_standard;
    }

    public void setAnnealed_standard(String annealed_standard) {
        this.annealed_standard = annealed_standard;
    }

    public String getAnnealed_thickness() {
        return annealed_thickness;
    }

    public void setAnnealed_thickness(String annealed_thickness) {
        this.annealed_thickness = annealed_thickness;
    }

    public String getAnnealed_materialDetails() {
        return annealed_materialDetails;
    }

    public void setAnnealed_materialDetails(String annealed_materialDetails) {
        this.annealed_materialDetails = annealed_materialDetails;
    }

    public String getAnnealed_glassWidth() {
        return annealed_glassWidth;
    }

    public void setAnnealed_glassWidth(String annealed_glassWidth) {
        this.annealed_glassWidth = annealed_glassWidth;
    }

    public String getAnnealed_glassHeight() {
        return annealed_glassHeight;
    }

    public void setAnnealed_glassHeight(String annealed_glassHeight) {
        this.annealed_glassHeight = annealed_glassHeight;
    }

    public String getAnnealed_quantity() {
        return annealed_quantity;
    }

    public void setAnnealed_quantity(String annealed_quantity) {
        this.annealed_quantity = annealed_quantity;
    }

    public String getAnnealed_note() {
        return annealed_note;
    }

    public void setAnnealed_note(String annealed_note) {
        this.annealed_note = annealed_note;
    }

    public byte[] getAnnealed_image() {
        return annealed_image;
    }

    public void setAnnealed_image(byte[] annealed_image) {
        this.annealed_image = annealed_image;
    }
}
