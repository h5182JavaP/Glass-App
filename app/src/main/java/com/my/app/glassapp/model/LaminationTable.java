package com.my.app.glassapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LaminationTable {

    @PrimaryKey(autoGenerate = true)
    private int lamination_id;
    @ColumnInfo(name = "Standard")
    String lamination_standard;
    @ColumnInfo(name = "Glass1")
    String lamination_glass_1;
    @ColumnInfo(name = "Glass2")
    String lamination_glass_2;
    @ColumnInfo(name = "PVB")
    String lamination_pvb;
    @ColumnInfo(name = "Width")
    String lamination_glassWidth;
    @ColumnInfo(name = "Height")
    String lamination_glassHeight;
    @ColumnInfo(name = "Quantity")
    String lamination_quantity;
    @ColumnInfo(name = "Note")
    private String lamination_note;
    @ColumnInfo(name = "ImagePath")
    private String lamination_path;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    byte[] lamination_image;

    public String getLamination_path() {
        return lamination_path;
    }

    public void setLamination_path(String lamination_path) {
        this.lamination_path = lamination_path;
    }

    public int getLamination_id() {
        return lamination_id;
    }

    public void setLamination_id(int lamination_id) {
        this.lamination_id = lamination_id;
    }

    public String getLamination_standard() {
        return lamination_standard;
    }

    public void setLamination_standard(String lamination_standard) {
        this.lamination_standard = lamination_standard;
    }

    public String getLamination_glass_1() {
        return lamination_glass_1;
    }

    public void setLamination_glass_1(String lamination_glass_1) {
        this.lamination_glass_1 = lamination_glass_1;
    }

    public String getLamination_glass_2() {
        return lamination_glass_2;
    }

    public void setLamination_glass_2(String lamination_glass_2) {
        this.lamination_glass_2 = lamination_glass_2;
    }

    public String getLamination_pvb() {
        return lamination_pvb;
    }

    public void setLamination_pvb(String lamination_pvb) {
        this.lamination_pvb = lamination_pvb;
    }

    public String getLamination_glassWidth() {
        return lamination_glassWidth;
    }

    public void setLamination_glassWidth(String lamination_glassWidth) {
        this.lamination_glassWidth = lamination_glassWidth;
    }

    public String getLamination_glassHeight() {
        return lamination_glassHeight;
    }

    public void setLamination_glassHeight(String lamination_glassHeight) {
        this.lamination_glassHeight = lamination_glassHeight;
    }

    public String getLamination_quantity() {
        return lamination_quantity;
    }

    public void setLamination_quantity(String lamination_quantity) {
        this.lamination_quantity = lamination_quantity;
    }

    public String getLamination_note() {
        return lamination_note;
    }

    public void setLamination_note(String lamination_note) {
        this.lamination_note = lamination_note;
    }

    public byte[] getLamination_image() {
        return lamination_image;
    }

    public void setLamination_image(byte[] lamination_image) {
        this.lamination_image = lamination_image;
    }
}
