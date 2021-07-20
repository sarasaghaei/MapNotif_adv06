package ir.sarasaghaei.mapnotif_adv06.service;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.HashMap;
import java.util.Map;
import ir.sarasaghaei.mapnotif_adv06.Const;
import static ir.sarasaghaei.mapnotif_adv06.Const.TAG;
import ir.sarasaghaei.mapnotif_adv06.MainActivity;
import ir.sarasaghaei.mapnotif_adv06.R;
import ir.sarasaghaei.mapnotif_adv06.entity.Notif;
import ir.sarasaghaei.mapnotif_adv06.room.AppDatabase;

public class FirebaseMessage extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String latitude = null;
        String longitude = null;

        //get data in Nofitication
        Map<String,String> map = new HashMap<>();
        if (remoteMessage.getData().size() > 0){
            latitude = remoteMessage.getData().get(Const.LATITUDE);
            longitude = remoteMessage.getData().get(Const.LONGITUDE);

            map.put(Const.LATITUDE,latitude);
            map.put(Const.LONGITUDE,longitude);
        }


        //Insert Notification in database for read OR unread Notification
        Long insertNotif = AppDatabase.getInstance(getApplicationContext())
                .getNotifDAO()
                .insert(new Notif(0,title,body,0 , latitude ,longitude));

        //get max id and send to detailActivity for get record in database and show item
        int maxid = AppDatabase.getInstance(getApplicationContext()).getNotifDAO().getmaxId();
        Log.e(TAG, "maxId" + maxid);
        map.put(Const.MAX_ID,String.valueOf(maxid));


        createNotification(title,body,map);

    }

    private void createNotification(String title, String body,Map<String,String> map){
        NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setSmallIcon(R.drawable.ic_baseline_notifications_24);

        //sending data to notif fargmnet when on click notificaton
        Intent Notifintent = new Intent(getApplicationContext(), MainActivity.class);
        Notifintent.putExtra(Const.TITLE,title);
        Notifintent.putExtra(Const.BODY,body);
        for(Map.Entry<String,String> item :map.entrySet()){
            Notifintent.putExtra(item.getKey(), item.getValue());
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,Notifintent,0);
        builder.setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("id","name",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId("id");
        }

        manager.notify(1,builder.build());

    }
}
