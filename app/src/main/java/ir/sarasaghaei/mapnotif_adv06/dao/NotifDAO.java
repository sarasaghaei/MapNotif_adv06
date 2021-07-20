package ir.sarasaghaei.mapnotif_adv06.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ir.sarasaghaei.mapnotif_adv06.Const;
import ir.sarasaghaei.mapnotif_adv06.entity.Notif;

@Dao
public interface NotifDAO {


    @Insert
    Long insert(Notif notif);

    @Query("select * from Notif")
    List<Notif> getall();

    @Query("select * from Notif where id_notif = :id_notif")
    Notif getbyid (int id_notif);

    @Query("update Notif set flag_notif = :flag_notif where id_notif = :id_notif")
    int update_flagNotif(int id_notif, int flag_notif);

    @Query("select MAX(id_notif) from Notif")
    int getmaxId();

    @Query("select count (*) from Notif where flag_notif = 0")
    int getCountunreadNotif();

    @Query("select * from (select * from Notif ORDER BY id_notif DESC ) ORDER BY flag_notif ASC ")
    List<Notif> getNotif_orderby();

}
