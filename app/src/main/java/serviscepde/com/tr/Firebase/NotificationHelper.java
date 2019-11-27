package serviscepde.com.tr.Firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import serviscepde.com.tr.Fragment.NotificationFragment;
import serviscepde.com.tr.R;


public class NotificationHelper {

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    public NotificationHelper(Context context) {
        mContext = context;
    }

    /**
     * Create and push the notification
     */
    public void createNotification(int id, String title, String message) {


        /*Creates an explicit intent for an Activity in your app*/
        Intent resultIntent = new Intent(mContext , NotificationFragment.class);
        resultIntent.putExtra("notification",true);
        resultIntent.putExtra("notiId",id);
        resultIntent.putExtra("message",message);
        resultIntent.putExtra("title",title);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,id /* Request code */, resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_find);

        mBuilder = new NotificationCompat.Builder(mContext)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon_find)
                .setColor(Color.WHITE)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setLights(Color.RED, 3000, 3000)
                .setVibrate(new long[] { 1000, 1000})
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_ALL", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(id /* Request Code */, mBuilder.build());
    }
}


