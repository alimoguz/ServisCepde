package serviscepde.com.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.Ilce;
import serviscepde.com.tr.Models.Ilceler.IlceResponse;
import serviscepde.com.tr.Models.Ilceler.IlceResponseDetail;

import serviscepde.com.tr.R;

import serviscepde.com.tr.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    String[] COUNTRIES = new String[] {"Item 1", "Item 2", "Item 3", "Item 4"};
    Context ctx;

    List<String> sehirler = new ArrayList<>();
    List<String> ilceler= new ArrayList<>();

    JSONObject jsonObjectIl;
    JSONObject jsonObjectIlce;

    int count = 1;

    List<Ilce> ilces;
    List<City> tmpCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        ilces = new ArrayList<>();
        tmpCity = new ArrayList<>();



        Call<IlceResponse> ilceResponseCall = App.getApiService().getIlceler();
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
        });

        /*Call<SehirResponse> sehirResponseCall = App.getApiService().getSehirler();
        sehirResponseCall.enqueue(new Callback<SehirResponse>() {
            @Override
            public void onResponse(Call<SehirResponse> call, Response<SehirResponse> response) {


                SehirResponseDetail sehirResponseDetail = response.body().getSehirResponseDetail();
                String token = sehirResponseDetail.getResult();
                JSONObject jsonObjectIl = Utils.jwtToJsonObject(token);

                try {

                    JSONObject cities = jsonObjectIl.getJSONObject("OutPutMessage").getJSONObject("Data");

                    for(int i = 0; i < cities.length(); i++)
                    {

                        Iterator<String> keys = cities.keys();
                        while (keys.hasNext())
                        {
                            String cityID = keys.next();
                            String cityName = cities.get(cityID).toString();
                            Log.i("CityNameAndId" , cityID + "" + cityName);

                            City city = new City(cityID,cityName);
                            tmpCity.add(city);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("jsonArray" , e.getMessage());
                }


            }

            @Override
            public void onFailure(Call<SehirResponse> call, Throwable t) {

            }
        });*/








    }

    private void setilceler() {
        for(Ilce tmp : ilces)
        {
            Log.i("ılceler" , tmp.getCityID() + ""  + tmp.getIlceID() + " " + tmp.getIlceName());
        }
    }
}
