package serviscepde.com.tr;

import android.app.Activity;
import androidx.annotation.NonNull;


import com.facebook.FacebookSdk;
import com.google.android.gms.ads.AdListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.Fragment.AddFragment;
import serviscepde.com.tr.Fragment.HomeFragment;
import serviscepde.com.tr.Fragment.NotificationFragment;
import serviscepde.com.tr.Fragment.ProfileFragment;
import serviscepde.com.tr.Fragment.SearchFragment;
import serviscepde.com.tr.Fragment.İletisimFragment;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.Models.Response.ResponseDetail;
import serviscepde.com.tr.Utils.Utils;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;

import static serviscepde.com.tr.App.TAG;

public class MainActivity extends AppCompatActivity {

    public static  RelativeLayout relHeader;
    FrameLayout fragMain;
    public static BottomNavigationView bottomNav;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private AddFragment addFragment;
    private NotificationFragment notificationFragment;
    private ProfileFragment profileFragment;
    private İletisimFragment iletisimFragment;
    public static Activity act;
    private String userToken;

    private ImageView imgIconIletisim;

    public static FragmentTransaction fragmentTransaction;
    public static FragmentManager fragmentManager;
    public static int count = 0;
    private static TextView badgeText;
    public static InterstitialAd interstitialAd;
    public static boolean adShown = false;

    @Override
    public void onBackPressed() {

        FragmentManager manager = getSupportFragmentManager();

        if(manager.getBackStackEntryCount() > 1)
        {
            manager.popBackStackImmediate();
        }
        else
        {
            Bundle quit = new Bundle();
            quit.putString("quit" , "Yes");
            Intent quitIntent = new Intent(this , SplashActivity.class);
            quitIntent.putExtras(quit);
            startActivity(quitIntent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        act = this;

        hideSoftKeyboard();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragMain = findViewById(R.id.fragMain);
        bottomNav = findViewById(R.id.bottomNav);
        relHeader = findViewById(R.id.relHeader);
        imgIconIletisim = findViewById(R.id.imgIconIletisim);

        relHeader.setVisibility(View.VISIBLE);
        bottomNav.setVisibility(View.VISIBLE);

        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNav.getChildAt(0);
        View view = bottomNavigationMenuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) view;

        View badge = LayoutInflater.from(getApplicationContext()).inflate(R.layout.badge, itemView, true);
        badgeText = badge.findViewById(R.id.notification_badge);

        SharedPreferences sharedPref = this.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        HashMap<String , String> hashMap = new HashMap<>();

        hashMap.put("Token" , userToken);

        if(userToken.equals("0"))
        {

        }
        else
        {
            getNotifications(hashMap);
        }

        loadFragment(new HomeFragment());

        iletisimFragment = new İletisimFragment();

        imgIconIletisim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(iletisimFragment);
            }
        });

        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        interstitialAd = new InterstitialAd(MainActivity.this);
        interstitialAd.setAdUnitId("ca-app-pub-9098556749113718/6566705672");
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


    }

    public static void getNotifications(HashMap<String , String> hashMap) {
        Call<BaseResponse> call = App.getApiService().getBildirimler(hashMap);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                ResponseDetail responseDetail = response.body().getResponseDetail();
                String token = responseDetail.getResult();
                JSONObject jsonObject = Utils.jwtToJsonObject(token);

                try {
                    if(jsonObject.get("OutPutMessage") instanceof  JSONObject)
                    {
                        Log.i(TAG, "onResponse: " + " Json Object Geldi");


                        count = 0;
                        for (int i = 0; i < jsonObject.getJSONObject("OutPutMessage").getJSONArray("Data").length(); i++){

                            JSONObject tmp = jsonObject.getJSONObject("OutPutMessage").getJSONArray("Data").getJSONObject(i);
                            int x;

                            String Status = tmp.getString("Status");

                            x = Integer.parseInt(Status);

                            if(x == 0)
                            {
                                ++count;
                            }
                        }


                        setBadgeCount(count);


                    }

                    if(jsonObject.get("OutPutMessage") instanceof JSONArray)
                    {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {


                case R.id.nav_home:

                    homeFragment = new HomeFragment();
                    loadFragment(homeFragment);
                    return true;

                case R.id.nav_search:


                    searchFragment = new SearchFragment();
                    loadFragment(searchFragment);
                    return true;

                case R.id.nav_add:

                    addFragment = new AddFragment();
                    loadFragment(addFragment);
                    return true;

                case R.id.nav_notification:

                    notificationFragment = new NotificationFragment();
                    loadFragment(notificationFragment);
                    return true;

                case R.id.nav_user:

                    profileFragment = new ProfileFragment();
                    loadFragment(profileFragment);
                    return true;
            }
            return false;
        }
    };


    private void loadFragment(Fragment fragment) {      //fragmentlarımızı çağırdığımız fonksiyon

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragMain, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void setBadgeCount(int count) {

        String notification = Integer.toString(count);
        if(count == 0 || count < 0)
        {
            badgeText.setVisibility(View.GONE);
        }
        else
        {
            badgeText.setVisibility(View.VISIBLE);
            badgeText.setText(notification);
        }
    }
}
