package ir.sarasaghaei.mapnotif_adv06.room;

import android.app.Application;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ir.sarasaghaei.mapnotif_adv06.Const;
import ir.sarasaghaei.mapnotif_adv06.dao.NotifDAO;
import ir.sarasaghaei.mapnotif_adv06.entity.Notif;

@Database(entities = {Notif.class} ,version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NotifDAO getNotifDAO();

    public static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room
                    .databaseBuilder(context, AppDatabase.class, Const.DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
