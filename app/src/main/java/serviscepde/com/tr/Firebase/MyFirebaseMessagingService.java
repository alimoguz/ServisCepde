package serviscepde.com.tr.Firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String TAG = "tagggMyFirebaseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        String topic = remoteMessage.getData().get("topic");
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("body");

        if(!message.isEmpty() && message != null){
            NotificationHelper notificationHelper = new NotificationHelper(this);
            notificationHelper.createNotification(0 , title, message);
        }

    }



}