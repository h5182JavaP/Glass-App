package com.my.app.glassapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DGUTable {

    @PrimaryKey(autoGenerate = true)
    private int dgu_id;
    @ColumnInfo(name = "Standard")
    String dgu_standard;
    @ColumnInfo(name = "Glass1")
    String dgu_glass_1;
    @ColumnInfo(name = "Glass2")
    String dgu_glass_2;
    @ColumnInfo(name = "Gap")
    String dgu_gap;
    @ColumnInfo(name = "Width")
    String dgu_glassWidth;
    @ColumnInfo(name = "Height")
    String dgu_glassHeight;
    @ColumnInfo(name = "Quantity")
    String dgu_quantity;
    @ColumnInfo(name = "Note")
    String dgu_note;
    @ColumnInfo(name = "ImagePath")
    private String dgu_path;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    byte[] dgu_image;

    public String getDgu_path() {
        return dgu_path;
    }

    public void setDgu_path(String dgu_path) {
        this.dgu_path = dgu_path;
    }

    public byte[] getDgu_image() {
        return dgu_image;
    }

    public void setDgu_image(byte[] dgu_image) {
        this.dgu_image = dgu_image;
    }

    public int getDgu_id() {
        return dgu_id;
    }

    public void setDgu_id(int dgu_id) {
        this.dgu_id = dgu_id;
    }

    public String getDgu_standard() {
        return dgu_standard;
    }

    public void setDgu_standard(String dgu_standard) {
        this.dgu_standard = dgu_standard;
    }

    public String getDgu_glass_1() {
        return dgu_glass_1;
    }

    public void setDgu_glass_1(String dgu_glass_1) {
        this.dgu_glass_1 = dgu_glass_1;
    }

    public String getDgu_glass_2() {
        return dgu_glass_2;
    }

    public void setDgu_glass_2(String dgu_glass_2) {
        this.dgu_glass_2 = dgu_glass_2;
    }

    public String getDgu_gap() {
        return dgu_gap;
    }

    public void setDgu_gap(String dgu_gap) {
        this.dgu_gap = dgu_gap;
    }

    public String getDgu_glassWidth() {
        return dgu_glassWidth;
    }

    public void setDgu_glassWidth(String dgu_glassWidth) {
        this.dgu_glassWidth = dgu_glassWidth;
    }

    public String getDgu_glassHeight() {
        return dgu_glassHeight;
    }

    public void setDgu_glassHeight(String dgu_glassHeight) {
        this.dgu_glassHeight = dgu_glassHeight;
    }

    public String getDgu_quantity() {
        return dgu_quantity;
    }

    public void setDgu_quantity(String dgu_quantity) {
        this.dgu_quantity = dgu_quantity;
    }

    public String getDgu_note() {
        return dgu_note;
    }

    public void setDgu_note(String dgu_note) {
        this.dgu_note = dgu_note;
    }
}
