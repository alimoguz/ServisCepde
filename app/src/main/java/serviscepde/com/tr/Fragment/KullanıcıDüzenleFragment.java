package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.App;
import serviscepde.com.tr.DownloadClass;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.Models.Response.ResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;


public class KullanıcıDüzenleFragment extends Fragment {


    View generalView;

    private AutoCompleteTextView aCDuzenleKullaniciTuru,autoCompleteIlDuzenle,autoCompleteIlceDuzenle;
    private EditText edtDuzenleKayitAd,edtDuzenleKayitSoyad,edtKayitEmailDuzenle,edtKayitSifreDuzenle;
    private MaskedEditText edtKayitTelefonDuzenle;
    private TextView txtDuzenle;
    private String ad,soyad,email,telefon,sifre,sifreTekrar;
    private String cityId,townId,kullaniciType;
    private ArrayList<String> townNames = new ArrayList<>();


    private Context ctx;
    private String userToken;
    private String [] kullaniciTur = {"Proje Yöneticisi" , "Şoför" , "Araç Sahibi" , "Diğer"};

    private List<City> sehirler = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private SweetAlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.kullanici_duzenle, container, false);

        generalView = rootView;
        ctx = generalView.getContext();

        aCDuzenleKullaniciTuru = generalView.findViewById(R.id.aCDuzenleKullaniciTuru);
        autoCompleteIlDuzenle = generalView.findViewById(R.id.autoCompleteIlDuzenle);
        autoCompleteIlceDuzenle = generalView.findViewById(R.id.autoCompleteIlceDuzenle);

        txtDuzenle = generalView.findViewById(R.id.txtDuzenle);

        edtDuzenleKayitAd = generalView.findViewById(R.id.edtDuzenleKayitAd);
        edtDuzenleKayitSoyad = generalView.findViewById(R.id.edtDuzenleKayitSoyad);
        edtKayitEmailDuzenle = generalView.findViewById(R.id.edtKayitEmailDuzenle);
        edtKayitTelefonDuzenle = generalView.findViewById(R.id.edtKayitTelefonDuzenle);
        edtKayitSifreDuzenle = generalView.findViewById(R.id.edtKayitSifreDuzenle);

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        sehirler = DownloadClass.getCities();
        cityNames = DownloadClass.getCityNames();

        ArrayAdapter<String> Turadapter = new ArrayAdapter<>(ctx, R.layout.dropdown_item, kullaniciTur);
        aCDuzenleKullaniciTuru.setAdapter(Turadapter);

        Utils.setAutoCompleteAdapter(autoCompleteIlDuzenle , cityNames , ctx);

        autoCompleteIlDuzenle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cityId = parent.getItemAtPosition(position).toString();
                cityId = DownloadClass.getCityIdWithName(cityId);
                Log.i("SelectedCityId" , cityId);

                townNames = DownloadClass.getTownNames(cityId);
                Utils.setAutoCompleteAdapter(autoCompleteIlceDuzenle , townNames , ctx);

            }
        });

        autoCompleteIlceDuzenle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                townId = parent.getItemAtPosition(position).toString();
                townId = DownloadClass.getTownIdWithTownName(cityId , townId);
                Log.i("SelectedIlceId" , townId);
            }
        });

        aCDuzenleKullaniciTuru.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                kullaniciType = String.valueOf(position + 1);

            }
        });


        HashMap<String , String> kullanici = new HashMap<>();
        kullanici.put("Token" , userToken);
        Call<BaseResponse> kullaniciBilgi = App.getApiService().kullaniciBilgileri(kullanici);
        kullaniciBilgi.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                ResponseDetail detail = response.body().getResponseDetail();
                String token = detail.getResult();

                JSONObject kullaniciInfo = Utils.jwtToJsonObject(token);


                try {
                    JSONObject tmp = kullaniciInfo.getJSONObject("OutPutMessage").getJSONObject("Data");

                    edtDuzenleKayitAd.setText(tmp.getString("UserName"));
                    edtDuzenleKayitSoyad.setText(tmp.getString("SurName"));
                    edtKayitEmailDuzenle.setText(tmp.getString("Email"));
                    edtKayitTelefonDuzenle.setText(tmp.getString("GSM"));
                    aCDuzenleKullaniciTuru.setText(setKullaniciTur(tmp.getString("MeType")));
                    autoCompleteIlDuzenle.setText(Utils.getSehirAdi(tmp.getString("CityID")));
                    autoCompleteIlceDuzenle.setText(DownloadClass.getTownNameWithId(tmp.getString("TownID")));




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });

        txtDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ad = edtDuzenleKayitAd.getText().toString();
                soyad = edtDuzenleKayitSoyad.getText().toString();
                email = edtKayitEmailDuzenle.getText().toString();
                telefon = edtKayitTelefonDuzenle.getText().toString();
                sifre = edtKayitSifreDuzenle.getText().toString();

                SweetAlertDialog kullaniciAlert;

                HashMap<String , Object> hashMap = new HashMap<>();
                HashMap<String , String> hashMap1 = new HashMap<>();

                if(ad.isEmpty() || soyad.isEmpty())
                {
                    kullaniciAlert = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                    kullaniciAlert.setTitleText("* ile belirtilen alanlar boş bırakılamaz");
                    kullaniciAlert.show();
                }

                hashMap1.put("UserName" , ad);
                hashMap1.put("SurName" , soyad);
                hashMap1.put("Email" , email);

                hashMap1.put("GSM" , telefon);
                hashMap1.put("CityID" , cityId);
                hashMap1.put("TownID" , townId);

                hashMap.put("param" , hashMap1);
                hashMap.put("Token" , userToken);

                Call<BaseResponse> call = App.getApiService().kullaniciDuzenle(hashMap);
                call.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {

                    }
                });





            }
        });












        return rootView;
    }

    private String setKullaniciTur(String MeType)
    {
        if(MeType.equals("1"))
        {
            return "Proje Yöneticisi";
        }
        if(MeType.equals("2"))
        {
            return "Şoför";
        }
        if(MeType.equals("3"))
        {
            return "Şoför";
        }

        return "";

    }
}