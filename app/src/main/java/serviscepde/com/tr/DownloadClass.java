package serviscepde.com.tr;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.Ilce;
import serviscepde.com.tr.Models.Ilceler.IlceResponse;
import serviscepde.com.tr.Models.Ilceler.IlceResponseDetail;
import serviscepde.com.tr.Models.Kapasite;
import serviscepde.com.tr.Models.MarkaModel;
import serviscepde.com.tr.Models.MotorGuc;
import serviscepde.com.tr.Models.MotorHacim;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.Models.Response.ResponseDetail;
import serviscepde.com.tr.Models.Sehirler.SehirResponse;
import serviscepde.com.tr.Models.Sehirler.SehirResponseDetail;
import serviscepde.com.tr.Utils.Utils;

public class DownloadClass {
    public static ArrayList<City> cities = new ArrayList<>();
    public static ArrayList<Ilce> towns = new ArrayList<>();
    public static ArrayList<MarkaModel> markaModelsArray = new ArrayList<>();
    public static ArrayList<Kapasite> kapasiteList =  new ArrayList<>();
    public static ArrayList<MotorGuc> motorGucList =  new ArrayList<>();
    public static ArrayList<MotorHacim> motorHacimList = new ArrayList<>();

    public static void downloadAllVariables() {
        downloadCities();
        downloadTowns();
        downloadMarkaModel();
        downloadKapasite();
        downloadGuc();
        downloadHacim();
    }

    private static void downloadHacim() {

        motorHacimList = new ArrayList<>();
        Call<BaseResponse> hacimCall = App.getApiService().getMotorHacim();
        hacimCall.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                ResponseDetail detail = response.body().getResponseDetail();
                String token = detail.getResult();
                JSONObject tmp = Utils.jwtToJsonObject(token);

                try {
                    JSONObject motorHacim = tmp.getJSONObject("OutPutMessage").getJSONObject("Data");

                    for(int i = 1; i <= motorHacim.length(); i++)
                    {
                        String ID = String.valueOf(i);
                        String hacim = motorHacim.getString(String.valueOf(i));

                        MotorHacim tmpHacim = new MotorHacim(ID , hacim);
                        motorHacimList.add(tmpHacim);
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

    private static void downloadGuc() {

        motorGucList = new ArrayList<>();

        Call<BaseResponse> gucCall = App.getApiService().getMotorGucu();
        gucCall.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                ResponseDetail detail = response.body().getResponseDetail();
                String token = detail.getResult();
                JSONObject motorGuc = Utils.jwtToJsonObject(token);


                try {
                    JSONObject motorGucListe = motorGuc.getJSONObject("OutPutMessage").getJSONObject("Data");

                    for(int i = 1; i <= motorGucListe.length(); i++)
                    {
                        String ID = String.valueOf(i);
                        String guc = motorGucListe.getString(String.valueOf(i));

                        MotorGuc motorGuc1 = new MotorGuc(ID , guc);
                        motorGucList.add(motorGuc1);

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

    private static void downloadMarkaModel() {
        markaModelsArray = new ArrayList<>();
        Call<BaseResponse> sehirResponseCall = App.getApiService().getMarkaModel();
        sehirResponseCall.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {


                ResponseDetail sehirResponseDetail = response.body().getResponseDetail();
                String token = sehirResponseDetail.getResult();
                JSONObject jsonObjectIl = Utils.jwtToJsonObject(token);

                try {
                    JSONObject markaModels = jsonObjectIl.getJSONObject("OutPutMessage").getJSONObject("Data");
                    HashMap<String, Object> hashMap = new Gson().fromJson(markaModels.toString(), HashMap.class);

                    Iterator<String> temp = hashMap.keySet().iterator();
                    while(temp.hasNext()){
                        String parentId  = temp.next();
                        JSONObject object = (JSONObject) markaModels.get(parentId);
                        HashMap<String, Object> hashMapValue = new Gson().fromJson(object.toString(), HashMap.class);
                        Iterator<String> temp1 = hashMapValue.keySet().iterator();
                        while(temp1.hasNext()){
                            String id  = temp1.next();
                            String name = object.get(id).toString();

                            Log.i("%%%", "onResponse: %%%"+parentId + "----" + id + "----" + name);
                            MarkaModel a = new MarkaModel(id , name , parentId);
                            markaModelsArray.add(a);
                        }
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

    public static void downloadTowns() {
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
                            towns.add(ilce);
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<IlceResponse> call, Throwable t) {

            }
        });
    }

    public static void downloadCities() {
        Call<SehirResponse> sehirResponseCall = App.getApiService().getSehirler();
        sehirResponseCall.enqueue(new Callback<SehirResponse>() {
            @Override
            public void onResponse(Call<SehirResponse> call, Response<SehirResponse> response) {


                SehirResponseDetail sehirResponseDetail = response.body().getSehirResponseDetail();
                String token = sehirResponseDetail.getResult();
                JSONObject jsonObjectIl = Utils.jwtToJsonObject(token);

                try {

                    for(int i = 1; i <= jsonObjectIl.getJSONObject("OutPutMessage").getJSONObject("Data").length(); i++)
                    {
                        String ID = String.valueOf(i);
                        String cityName = jsonObjectIl.getJSONObject("OutPutMessage").getJSONObject("Data").getString(String.valueOf(i));

                        City city = new City(ID,cityName);
                        cities.add(city);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SehirResponse> call, Throwable t) {

            }
        });
    }

    private static void downloadKapasite() {

        kapasiteList = new ArrayList<>();
        Call<BaseResponse> kapasiteCall = App.getApiService().getKapasite();
        kapasiteCall.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                ResponseDetail detail = response.body().getResponseDetail();
                String token = detail.getResult();
                JSONObject kapasite = Utils.jwtToJsonObject(token);


                try {
                    JSONObject kapasiteler = kapasite.getJSONObject("OutPutMessage").getJSONObject("Data");

                    for(int i = 1; i <= kapasiteler.length(); i++)
                    {

                        String ID = String.valueOf(i);
                        String capacity = kapasiteler.getString(String.valueOf(i));

                        Kapasite kapasite1 = new Kapasite(ID, capacity);
                        kapasiteList.add(kapasite1);
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

    public static ArrayList<MotorHacim> getMotorHacim()
    {
        return motorHacimList;
    }

    public static ArrayList<String> getHacimNames()
    {
        ArrayList<String> hacimNames = new ArrayList<>();
        for(MotorHacim motorHacim : motorHacimList)
        {
            hacimNames.add(motorHacim.getMotorHacim());
        }

        return hacimNames;
    }

    public static String getHacimIdWithName(String hacimName)
    {
        for(MotorHacim motorHacim : motorHacimList)
        {
            if(motorHacim.getMotorHacim().equals(hacimName))
            {
                return motorHacim.getID();
            }
        }
        return "";
    }

    public static ArrayList<MotorGuc> getMotorGuc()
    {
        return motorGucList;
    }

    public static ArrayList<String> getGucNames()
    {
        ArrayList<String>  gucNames = new ArrayList<>();
        for(MotorGuc motorGuc : motorGucList)
        {
            gucNames.add(motorGuc.getMotorGuc());
        }
        return gucNames;
    }

    public static String getGucIdWithName(String motorGucu)
    {
        for(MotorGuc motorGuc : motorGucList)
        {
            if(motorGuc.getMotorGuc().equals(motorGucu))
            {
                return motorGuc.getID();
            }
        }

        return "";
    }


    public static ArrayList<Kapasite> getKapasite()
    {
        return kapasiteList;
    }

    public static ArrayList<String> getKapasiteNames()
    {
        ArrayList<String> kapasiteNames = new ArrayList<>();
        for(Kapasite kapasite : kapasiteList)
        {
            kapasiteNames.add(kapasite.getCapacity());
        }
        return  kapasiteNames;
    }

    public static String getKapasiteIdWithName(String kapasiteName)
    {
        for(Kapasite kapasite : kapasiteList)
        {
            if(kapasite.getCapacity().equals(kapasiteName))
            {
                return kapasite.getID();
            }
        }

        return "";
    }

    public static ArrayList<City> getCities() {
        return cities;
    }

    public static ArrayList<Ilce> getTowns(String cityId) {
        ArrayList<Ilce> cityTowns = new ArrayList<>();
        for(Ilce ilce: towns){
            if(ilce.getCityID().equals(cityId)){
                cityTowns.add(ilce);
            }
        }
        return cityTowns;
    }

    public static ArrayList<String> getCityNames(){
        ArrayList<String> cityNames = new ArrayList<>();
        for(City city: cities){
            cityNames.add(city.getCityName());
        }

        return cityNames;
    }

    public static String getCityIdWithName(String cityName){
        for(City city: cities){
            if(city.getCityName().equals(cityName)){
                return city.getID();
            }
        }
        return "";
    }

    public static ArrayList<String> getTownNames(String cityId) {
        ArrayList<String> temp = new ArrayList<>();
        for(Ilce ilceler: towns){
            if(ilceler.getCityID().equals(cityId)){
                temp.add(ilceler.getIlceName());
            }
        }
        return temp;
    }

    public static String getTownIdWithTownName(String tempTownName, String selectedCityId) {
        for(Ilce ilce: towns){
            if(ilce.getCityID().equals(selectedCityId) && ilce.getIlceName().equals(tempTownName)){
                return ilce.getIlceID();
            }
        }
        return "";
    }

    public static String getTownNameWithId(String townID)
    {
        for(Ilce ılce : towns)
        {
            if(ılce.getIlceID().equals(townID))
            {
                return ılce.getIlceName();
            }

        }

        return  "";


    }

    public static ArrayList<String> getMarkaNames(){
        ArrayList<String> temp = new ArrayList<>();
        for(MarkaModel markaModel: markaModelsArray){
            if(markaModel.getParentId().equals("0")){
                temp.add(markaModel.getName());
            }
        }
        return  temp;
    }

    public static ArrayList<String> getModelNames(String parentId){
        ArrayList<String> temp = new ArrayList<>();
        for(MarkaModel markaModel: markaModelsArray){
            if(markaModel.getParentId().equals(parentId)){
                temp.add(markaModel.getName());
            }
        }
        return  temp;
    }

    public static String getMarkaIdWithName(String markaName){
        for(MarkaModel markaModel: markaModelsArray){
            if(markaModel.getName().equals(markaName)){
                return markaModel.getId();
            }
        }
        return  "";
    }

    public static String getModelIdWithName(String modelName){
        for(MarkaModel markaModel: markaModelsArray){
            if(markaModel.getName().equals(modelName)){
                return markaModel.getId();
            }
        }
        return  "";
    }
}
