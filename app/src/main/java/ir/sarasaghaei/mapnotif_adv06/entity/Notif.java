package ir.sarasaghaei.mapnotif_adv06.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Notif {

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    private int id_notif;

    @ColumnInfo
    private String title_notif;

    @ColumnInfo
    private String detail_notif;

    @ColumnInfo
    private  int flag_notif;

    @ColumnInfo
    private String latitude;

    @ColumnInfo
    private String longitude;


    @Ignore
    public Notif(int id_notif, String title_notif, String detail_notif, int flag_notif,
                    String latitude, String longitude) {
        this.id_notif = id_notif;
        this.title_notif = title_notif;
        this.detail_notif = detail_notif;
        this.flag_notif = flag_notif;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public Notif(int id_notif, String title_notif, int flag_notif) {
        this.id_notif = id_notif;
        this.title_notif = title_notif;
        this.flag_notif = flag_notif;
    }

    public int getId_notif() {
        return id_notif;
    }

    public void setId_notif(int id_notif) {
        this.id_notif = id_notif;
    }

    public String getTitle_notif() {
        return title_notif;
    }

    public void setTitle_notif(String title_notif) {
        this.title_notif = title_notif;
    }

    public String getDetail_notif() {
        return detail_notif;
    }

    public void setDetail_notif(String detail_notif) {
        this.detail_notif = detail_notif;
    }

    public int getFlag_notif() {
        return flag_notif;
    }

    public void setFlag_notif(int flag_notif) {
        this.flag_notif = flag_notif;
    }
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
