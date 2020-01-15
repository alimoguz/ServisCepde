package serviscepde.com.tr;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;


import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Retrofit;
import serviscepde.com.tr.Fragment.IseAracFragment;
import serviscepde.com.tr.Fragment.LoginFragment;
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
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if(manager.getBackStackEntryCount() > 2)
        {
            manager.popBackStackImmediate();
        }
        else
        {
            System.exit(0);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        if (isTesting) {
            Intent test = new Intent(this, TestActivity.class);
            startActivity(test);
        }



        Bundle quit = this.getIntent().getExtras();
        if (quit != null)
        {
            String tmp = quit.getString("quit" , "No");
            if(tmp.equals("Yes"))
            {
                this.finishAffinity();
                quit.remove("quit");
            }
            if(tmp.equals("No"))
            {

            }

        }

        handleIntent(getIntent());

        DownloadClass.downloadAllVariables();
        fragSplash = findViewById(R.id.fragSplash);
        ctx = getApplicationContext();
        setNotification();
        sharedPref = getBaseContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        videoFragment = new VideoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragSplash, videoFragment, "videoTag");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();


        ilces = new ArrayList<>();





        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }
    private void handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null){
            String recipeId = appLinkData.getLastPathSegment();
            Uri appData = Uri.parse("content://com.recipe_app/recipe/").buildUpon()
                    .appendPath(recipeId).build();
            Intent splash = new Intent(this , SplashActivity.class);
            startActivity(splash);
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
    }
}
