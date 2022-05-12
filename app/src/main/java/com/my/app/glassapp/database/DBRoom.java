package com.my.app.glassapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.my.app.glassapp.model.AnnealedTable;
import com.my.app.glassapp.model.DGUTable;
import com.my.app.glassapp.model.LaminatedDGUTable;
import com.my.app.glassapp.model.LaminationTable;
import com.my.app.glassapp.model.SGUTable;

@Database(entities = {SGUTable.class, DGUTable.class, LaminationTable.class, AnnealedTable.class, LaminatedDGUTable.class}, version = 2, exportSchema = false)
public abstract class DBRoom extends RoomDatabase {

    public static DBRoom database;

    public static DBRoom getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, DBRoom.class, "DutchGlass")
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }

   public abstract DBDao dao();
}
