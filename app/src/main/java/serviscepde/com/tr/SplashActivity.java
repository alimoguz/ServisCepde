package serviscepde.com.tr;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;


import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Retrofit;
import serviscepde.com.tr.Fragment.IseAracFragment;
import serviscepde.com.tr.Fragment.VideoFragment;
import serviscepde.com.tr.Models.Ilce;


import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    FrameLayout fragSplash;
    VideoFragment videoFragment;
    IseAracFragment ıseAracFragment;

    Boolean isTesting = false;

    List<Ilce> ilceListesi = new ArrayList<>();

    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;

    private static String isLogged;

    List<Ilce> ilces;
    private Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);


        /*throw new RuntimeException("Crash");*/


        DownloadClass.downloadAllVariables();

        fragSplash = findViewById(R.id.fragSplash);

        ctx = getApplicationContext();

        sharedPref = getBaseContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        videoFragment = new VideoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragSplash,videoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();

        /*sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        isLogged = sharedPref.getString("Loggedin" , "0");

        if(!isLogged.equals("0"))
        {
            Intent intentMain = new Intent(this , MainActivity.class);
            startActivity(intentMain);
        }

        if(isLogged.equals("0"))
        {
            videoFragment = new VideoFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragSplash,videoFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }*/

        ilces = new ArrayList<>();

        setNotification();


        if(isTesting)
        {
            Intent test = new Intent(this , TestActivity.class);
            startActivity(test);
        }

    }


    private void setNotification() {
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("serviscepde_android_app_new");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("channel", "channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("serviscepde_android_app_new");

/*
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
            builder.setTitle(bundle.getString("title", ""));
            builder.setMessage(bundle.getString("message"));
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }*/
    }
}
