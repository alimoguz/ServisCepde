package serviscepde.com.tr;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.Ilce;
import serviscepde.com.tr.Models.Ilceler.IlceResponse;
import serviscepde.com.tr.Models.Ilceler.IlceResponseDetail;
import serviscepde.com.tr.Models.MarkaModel;
import serviscepde.com.tr.Models.Sehirler.SehirResponse;
import serviscepde.com.tr.Models.Sehirler.SehirResponseDetail;
import serviscepde.com.tr.Utils.Utils;

public class DownloadClass {
    public static ArrayList<City> cities = new ArrayList<>();
    public static ArrayList<Ilce> towns = new ArrayList<>();
    public static ArrayList<MarkaModel> markaModels = new ArrayList<>();

    public static void downloadAllVariables() {
        downloadCities();
        downloadTowns();
        downloadMarkaModel();
    }

    private static void downloadMarkaModel() {

        MarkaModel a = new MarkaModel("1" , "BMC" , "0");
        MarkaModel a1 = new MarkaModel("3" , "Citroen" , "0");
        MarkaModel a2 = new MarkaModel("5" , "Diğer Markalar" , "0");
        MarkaModel a3 = new MarkaModel("7" , "Farketmez" , "0");
        MarkaModel a4 = new MarkaModel("8" , "Fiat" , "0");

        MarkaModel a5 = new MarkaModel("2" , "Levend" , "1");
        MarkaModel a6 = new MarkaModel("11" , "Levend 2" , "1");

        MarkaModel a7 = new MarkaModel("4" , "Jumper" , "3");

        MarkaModel a8 = new MarkaModel("6" , "Tüm Modeller" , "5");

        MarkaModel a9 = new MarkaModel("9" , "Diğer Modeller" , "8");
        MarkaModel a10 = new MarkaModel("10" , "Ducato" , "8");


        markaModels.add(a);
        markaModels.add(a1);
        markaModels.add(a2);
        markaModels.add(a3);
        markaModels.add(a4);
        markaModels.add(a5);
        markaModels.add(a6);
        markaModels.add(a7);
        markaModels.add(a8);
        markaModels.add(a9);
        markaModels.add(a10);


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

    public static ArrayList<String> getMarkaNames(){
        ArrayList<String> temp = new ArrayList<>();
        for(MarkaModel markaModel: markaModels){
            if(markaModel.getParentId().equals("0")){
                temp.add(markaModel.getName());
            }
        }
        return  temp;
    }

    public static ArrayList<String> getModelNames(String parentId){
        ArrayList<String> temp = new ArrayList<>();
        for(MarkaModel markaModel: markaModels){
            if(markaModel.getParentId().equals(parentId)){
                temp.add(markaModel.getName());
            }
        }
        return  temp;
    }

    public static String getMarkaIdWithName(String markaName){
        for(MarkaModel markaModel: markaModels){
            if(markaModel.getName().equals(markaName)){
                return markaModel.getId();
            }
        }
        return  "";
    }

    public static String getModelIdWithName(String modelName){
        for(MarkaModel markaModel: markaModels){
            if(markaModel.getName().equals(modelName)){
                return markaModel.getId();
            }
        }
        return  "";
    }
}
