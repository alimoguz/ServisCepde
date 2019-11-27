package serviscepde.com.tr;

import android.app.Application;

import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Utils.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class App extends Application {

    private static String BASE_URL = "http://fatura-kes.site/servis/webServices/";
    public static  String TAG = "ServisCepde";
    public static String key = "23424234234456hjgfd";
    public static String IMAGE_URL = "http://fatura-kes.site/servis/webServices/Uploads/ilanlar/Big/";
    public static List<City> citiesList = new ArrayList<>();
    public static List<City> tmp = new ArrayList<>();




    public static ApiInterface getApiService()
    {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(App.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        ApiInterface apis = retrofit.create(ApiInterface.class);

        return apis;
    }

    public static List<String> kapasite = new ArrayList<>();

    public static List<String> getKapasite()
    {
        kapasite.add("9+1");
        kapasite.add("10+1");
        kapasite.add("11+1");
        kapasite.add("12+1");
        kapasite.add("13+1");
        kapasite.add("14+1");
        kapasite.add("15+1");
        kapasite.add("16+1");
        kapasite.add("17+1");
        kapasite.add("18+1");
        kapasite.add("19+1");
        kapasite.add("20+1");
        kapasite.add("21+1");
        kapasite.add("23+1");
        kapasite.add("27+1");
        kapasite.add("28+1");
        kapasite.add("29+1");
        kapasite.add("30+1");
        kapasite.add("31+1");
        kapasite.add("35+1");
        kapasite.add("45+1");
        kapasite.add("46+1");
        kapasite.add("54+1");
        kapasite.add("askıda (4+1)");
        kapasite.add("askıda (5+1)");
        kapasite.add("askıda (6+1)");
        kapasite.add("askıda (7+1)");
        kapasite.add("askıda (8+1)");
        kapasite.add("Şoför");

        return kapasite;

    }

    public static List<String> ehliyet = new ArrayList<>();
    public static List<String> getEhliyet()
    {
        ehliyet.add("M");
        ehliyet.add("A1");
        ehliyet.add("A2");
        ehliyet.add("A");
        ehliyet.add("B1");
        ehliyet.add("B");

        return  ehliyet;

    }

    public static List<String> motorGucu = new ArrayList<>();
    public static List<String> getMotorGucu()
    {
        motorGucu.add("100 hp'ye kadar");
        motorGucu.add("101 - 125hp");
        return motorGucu;

    }

    public static List<String> motorHacmi = new ArrayList<>();
    public static List<String> getMotorHacmi()
    {
        motorHacmi.add("1300 cm3'e kadar");
        motorHacmi.add("1301 - 1600 cm3");
        motorHacmi.add("1601 - 1800 cm3");
        motorHacmi.add("1801 - 2000 cm3");
        motorHacmi.add("2001 - 2500 cm3");
        motorHacmi.add("2501 - 3000 cm3");
        motorHacmi.add("3001 - 3500 cm3");
        motorHacmi.add("3501 - 4000 cm3");
        motorHacmi.add("4001 - 4500 cm3");
        motorHacmi.add("4501 - 5000 cm3");
        motorHacmi.add("5001 cm3 ve üzeri");
        return motorHacmi;
    }

    public  static List<String> kaskoTuru = new ArrayList<>();
    public  static List<String> getKaskoTuru()
    {
        kaskoTuru.add("Kaza");
        kaskoTuru.add("Bakım Onarım");
        kaskoTuru.add("Ferdi Kaza");
        kaskoTuru.add("Ek Sürücü Sigortası");
        kaskoTuru.add("Full Kasko");
        return kaskoTuru;


    }

    public static List<String> kimden = new ArrayList<>();
    public static List<String> getKimden()
    {
        kimden.add("Araç sahibinden");
        kimden.add("Galeriden");

        return kimden;
    }

    public static List<String> vitesTipi = new ArrayList<>();
    public static List<String> getVitesTipi()
    {

        vitesTipi.add("Otomatik");
        vitesTipi.add("Manuel");
        vitesTipi.add("Yarı Otomatik");

        return vitesTipi;
    }

    public static List<String> yakitTipi = new ArrayList<>();
    public static List<String> getYakitTipi()
    {
        yakitTipi.add("Benzin");
        yakitTipi.add("Dizel");
        yakitTipi.add("Hybrid");
        yakitTipi.add("Benzin + Dizel");

        return yakitTipi;
    }

    public static List<String> durumu = new ArrayList<>();
    public static List<String> getDurumu()
    {
        durumu.add("Sıfır");
        durumu.add("İkinci el");

        return durumu;
    }




}





