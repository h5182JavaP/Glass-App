package com.my.app.glassapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.my.app.glassapp.model.AnnealedTable;
import com.my.app.glassapp.model.DGUTable;
import com.my.app.glassapp.model.LaminatedDGUTable;
import com.my.app.glassapp.model.LaminationTable;
import com.my.app.glassapp.model.SGUTable;

import java.util.List;

@Dao
public interface DBDao {

    @Insert
    void insertSGUData(SGUTable table);

    @Insert
    void insertDGUData(DGUTable table);

    @Insert
    void insertLaminationData(LaminationTable table);

    @Insert
    void insertAnnealedData(AnnealedTable table);

    @Insert
    void insertLaminatedDGUData(LaminatedDGUTable table);

    @Query("select * from sgutable limit 500")
    List<SGUTable> getSGUData();

    @Query("select * from dgutable limit 500")
    List<DGUTable> getDGUData();

    @Query("select * from laminationtable limit 500")
    List<LaminationTable> getLaminationData();

    @Query("select * from annealedtable limit 500")
    List<AnnealedTable> getAnnealedData();

    @Query("select * from LaminatedDGUTable limit 500")
    List<LaminatedDGUTable> getLDGUData();

    @Query("DELETE FROM sgutable")
    void deleteSguData();

    @Query("DELETE FROM dgutable")
    void deleteDguData();

    @Query("DELETE FROM laminationtable")
    void deleteLaminationData();

    @Query("DELETE FROM annealedtable")
    void deleteAnnealedData();

    @Query("DELETE FROM laminateddgutable")
    void deleteLDGUData();

    //    @Update
//    void update(SGUTable table);
    @Query("UPDATE sgutable SET Standard = :standard,Thickness = :thickness,Material = :material," +
            "Width = :width,Height = :height,Quantity = :quantity,Note = :note,ImagePath = :path,sgu_image = :img WHERE sgu_id = :id")
    void updateSGUTable(int id, String standard, String thickness, String material,
                        String width, String height, String quantity, String note,String path, byte[] img);

    @Query("UPDATE sgutable SET Standard = :standard,Thickness = :thickness,Material = :material," +
            "Width = :width,Height = :height,Quantity = :quantity,Note = :note WHERE sgu_id = :id")
    void updateSGUTable1(int id, String standard, String thickness, String material,
                         String width, String height, String quantity, String note);

    @Query("UPDATE sgutable SET sgu_image = :img,ImagePath = :path  WHERE sgu_id = :id")
    void updateSGUImage(int id, byte[] img,String path);

    @Query("UPDATE dgutable SET Standard = :standard,Glass1 = :glass1,Glass2 = :glass2," +
            "Gap = :gap,Width = :width,Height = :height,Quantity = :quantity,Note = :note ,ImagePath = :path ,dgu_image = :img WHERE dgu_id = :id")
    void updateDGUTable(int id, String standard, String glass1, String glass2,
                        String gap, String width, String height, String quantity, String note,String path, byte[] img);

    @Query("UPDATE dgutable SET Standard = :standard,Glass1 = :glass1,Glass2 = :glass2," +
            "Gap = :gap,Width = :width,Height = :height,Quantity = :quantity,Note = :note  WHERE dgu_id = :id")
    void updateDGUTable1(int id, String standard, String glass1, String glass2,
                         String gap, String width, String height, String quantity, String note);

    @Query("UPDATE dgutable SET dgu_image = :img,ImagePath = :path WHERE dgu_id = :id")
    void updateDGUImage(int id, byte[] img,String path);

    @Query("UPDATE laminationtable SET Standard = :standard,Glass1 = :glass1,pvb = :pvb,Glass2 = :glass2," +
            "Width = :width,Height = :height,Quantity = :quantity,Note = :note,ImagePath = :path,lamination_image = :img WHERE lamination_id = :id")
    void updateLaminationTable(int id, String standard, String glass1, String pvb, String glass2,
                               String width, String height, String quantity, String note,String path, byte[] img);

    @Query("UPDATE laminationtable SET Standard = :standard,Glass1 = :glass1,pvb = :pvb,Glass2 = :glass2," +
            "Width = :width,Height = :height,Quantity = :quantity,Note = :note WHERE lamination_id = :id")
    void updateLaminationTable1(int id, String standard, String glass1, String pvb, String glass2,
                                String width, String height, String quantity, String note);

    @Query("UPDATE laminationtable SET lamination_image = :img ,ImagePath = :path WHERE lamination_id = :id")
    void updateLaminationImage(int id, byte[] img,String path);

    @Query("UPDATE annealedtable SET Standard = :standard,Thickness = :thickness,Material = :material," +
            "Width = :width,Height = :height,Quantity = :quantity,Note = :note,ImagePath = :imgepath,annealed_image = :img WHERE annealed_id = :id")
    void updateAnnealedTable(int id, String standard, String thickness, String material,
                             String width, String height, String quantity, String note,String imgepath, byte[] img);

    @Query("UPDATE annealedtable SET Standard = :standard,Thickness = :thickness,Material = :material," +
            "Width = :width,Height = :height,Quantity = :quantity,Note = :note WHERE annealed_id = :id")
    void updateAnnealedTable1(int id, String standard, String thickness, String material,
                              String width, String height, String quantity, String note);

    @Query("UPDATE annealedtable SET annealed_image = :img,ImagePath = :path WHERE annealed_id = :id")
    void updateAnnealedImage(int id, byte[] img,String path);

    @Query("UPDATE laminateddgutable SET Standard = :standard,Glass1 = :glass1,Glass2 = :glass2,Glass3 = :glass3," +
            "Gap = :gap,pvb = :pvb,Width = :width,Height = :height,Quantity = :quantity,Note = :note ,ImagePath = :imgepath ,ldgu_image = :img WHERE ldgu_id = :id")
    void updateLDGUTable(int id, String standard, String glass1, String glass2, String glass3,
                         String gap,  String pvb, String width, String height, String quantity, String note, String imgepath, byte[] img);

    @Query("UPDATE laminateddgutable SET Standard = :standard,Glass1 = :glass1,Glass2 = :glass2,Glass3 = :glass3," +
            "Gap = :gap,pvb = :pvb,Width = :width,Height = :height,Quantity = :quantity,Note = :note  WHERE ldgu_id = :id")
    void updateLDGUTable1(int id, String standard, String glass1, String glass2, String glass3,
                          String gap, String pvb, String width, String height, String quantity, String note);

    @Query("UPDATE laminateddgutable SET ldgu_image = :img ,ImagePath = :path WHERE ldgu_id = :id")
    void updateLDGUImage(int id, byte[] img,String path);
}
