package serviscepde.com.tr.Firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import serviscepde.com.tr.Fragment.NotificationFragment;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.R;

public class MyMessagingService extends FirebaseMessagingService {

    String TAG = "tagggg";
    String type = "";
    String CHANNEL = "channel";
    NotificationManager notificationManager;

    String id;
    String body;
    String title;
    String link;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        SharedPreferences sharedPref = this.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        String userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        HashMap<String , String> hashMap = new HashMap<>();

        hashMap.put("Token" , userToken);

        MainActivity.getNotifications(hashMap);


        id = remoteMessage.getData().get("id");
        title = remoteMessage.getData().get("title");
        body = remoteMessage.getData().get("body");
        link = remoteMessage.getData().get("link");



        if(remoteMessage.getData().size() > 0){
            type = "json";
            sendNotification(remoteMessage.getData().toString());
        }
        if(remoteMessage.getNotification() != null){
            type = "message";
            sendNotification(remoteMessage.getNotification().getBody());

        }
    }

    private void sendNotification(String body) {
        String CHANNEL_ID = CHANNEL;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.YELLOW);
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("link",link);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, CHANNEL);
        notifBuilder.setContentTitle(title);
        Log.i(TAG, "sendNotification: %%%%"+ title);
        if(title == null || title == ""){
            notifBuilder.setContentTitle("Yeni bildiriminiz var");
        }
        notifBuilder.setContentText(this.body);
        if(this.body == null){
            notifBuilder.setContentText("Görüntülemek için tıklayınız.");
        }
        Uri sounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notifBuilder.setSound(sounduri);
        notifBuilder.setSmallIcon(R.drawable.servis_logo);
        notifBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.servis_logo));
        notifBuilder.setAutoCancel(true);
        notifBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notifBuilder.build());
    }
}
