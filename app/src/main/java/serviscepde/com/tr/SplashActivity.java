package serviscepde.com.tr;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;


import com.google.firebase.messaging.FirebaseMessaging;

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

    List<Ilce> ilces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        fragSplash = findViewById(R.id.fragSplash);

        sharedPref = getBaseContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        ilces = new ArrayList<>();

        setNotification();



        /*Call<IlceResponse> ilceResponseCall = App.getApiService().getIlceler();
        ilceResponseCall.enqueue(new Callback<IlceResponse>() {
            @Override
            public void onResponse(Call<IlceResponse> call, Response<IlceResponse> response) {
                IlceResponseDetail ilceResponseDetail = response.body().getIlceResponseDetail();
                String token2 = ilceResponseDetail.getResult();
                JSONObject jsonObjectIlce = Utils.jwtToJsonObject(token2);

                try {
                    JSONObject withCityId = jsonObjectIlce.getJSONObject("OutPutMessage").getJSONObject("Data");

                    Iterator<String> keys = withCityId.keys();

                    while (keys.hasNext())
                    {
                        String cityId = keys.next();

                        JSONObject ilceler = withCityId.getJSONObject(cityId);

                        Iterator<String> keyilceler = ilceler.keys();

                        while (keyilceler.hasNext())
                        {
                            String ilceId = keyilceler.next();
                            String ilceName = ilceler.getString(ilceId);

                            Ilce ilce = new Ilce(cityId , ilceId , ilceName);
                            ilces.add(ilce);
                        }


                    }

                    setilceler();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<IlceResponse> call, Throwable t) {

            }
        });*/


        if(isTesting)
        {
            /*ıseAracFragment = new IseAracFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragSplash,ıseAracFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();*/

            Intent test = new Intent(this , TestActivity.class);
            startActivity(test);
        }

        else
        {
            videoFragment = new VideoFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragSplash,videoFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();

        }








    }

    private void setilceler() {
        for(Ilce tmp : ilces)
        {
            Log.i("ılceler" , tmp.getCityID() + ""  + tmp.getIlceID() + " " + tmp.getIlceName());
        }
    }


    private void setNotification() {
        FirebaseMessaging.getInstance().subscribeToTopic("serviscepde-adnroid1-app");


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
        }
    }
}
