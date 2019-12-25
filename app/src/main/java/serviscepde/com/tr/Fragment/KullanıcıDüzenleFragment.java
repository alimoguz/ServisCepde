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
    private List<String> tür = new ArrayList<>();

    private List<City> sehirler = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private ArrayList<String> NewtownNames = new ArrayList<>();
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

        HashMap<String , String> kullanici = DownloadClass.getActiveUser();
        Log.i("Test" , " " + kullanici.size());

        loadUserInfo(kullanici);

        sehirler = DownloadClass.getCities();
        cityNames = DownloadClass.getCityNames();

        tür.add("Proje Yöneticisi");
        tür.add("Şoför");
        tür.add("Araç Sahibi");
        tür.add("Diğer");



        Utils.setAutoCompleteAdapter(autoCompleteIlDuzenle , cityNames , ctx);
        Utils.setAutoCompleteAdapter(autoCompleteIlceDuzenle , DownloadClass.getTownNames(kullanici.get("CityID")) , ctx);
        Utils.setAutoCompleteAdapter(aCDuzenleKullaniciTuru , tür , ctx);

        autoCompleteIlDuzenle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cityId = parent.getItemAtPosition(position).toString();
                cityId = DownloadClass.getCityIdWithName(cityId);
                Log.i("SelectedCityId" , cityId);

                townNames = DownloadClass.getTownNames(cityId);
                autoCompleteIlceDuzenle.setText(townNames.get(0));
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

                else
                {
                    Log.i("xxxxTür" , aCDuzenleKullaniciTuru.getText().toString());
                    Log.i("xxxxİl" , autoCompleteIlDuzenle.getText().toString());
                    Log.i("xxxxİlçe" , autoCompleteIlceDuzenle.getText().toString());

                    String NewCityId = DownloadClass.getCityIdWithName(autoCompleteIlDuzenle.getText().toString());
                    String NewTownId = DownloadClass.getTownIdWithTownName(autoCompleteIlceDuzenle.getText().toString() , NewCityId);
                    String NewTur = getKullaniciTur(aCDuzenleKullaniciTuru.getText().toString());

                    hashMap1.put("UserName" , ad);
                    hashMap1.put("SurName" , soyad);
                    hashMap1.put("Email" , email);

                    hashMap1.put("CityID" , NewCityId);
                    hashMap1.put("TownID" , NewTownId);
                    hashMap1.put("GSM" , telefon);
                    hashMap1.put("MeType" , NewTur);

                    hashMap.put("param" , hashMap1);
                    hashMap.put("Token" , userToken);

                    Call<BaseResponse> call = App.getApiService().kullaniciDuzenle(hashMap);
                    call.enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                            ResponseDetail detail = response.body().getResponseDetail();
                            String token = detail.getResult();
                            JSONObject result = Utils.jwtToJsonObject(token);

                            try {
                                int status = result.getJSONObject("OutPutMessage").getInt("Status");

                                if(status == 200 || status == 500)
                                {
                                    dialog = new SweetAlertDialog(ctx , SweetAlertDialog.NORMAL_TYPE);
                                    dialog.setTitleText("Profiliniz başarıyla güncellendi");
                                    dialog.show();
                                }
                                else
                                {
                                    dialog = new SweetAlertDialog(ctx , SweetAlertDialog.NORMAL_TYPE);
                                    dialog.setTitleText("Bir hata oluştu daha sonra tekrar deneyiniz");
                                    dialog.show();
                                }

                            } catch (JSONException e) {

                                dialog = new SweetAlertDialog(ctx , SweetAlertDialog.NORMAL_TYPE);
                                dialog.setTitleText("Bir hata oluştu daha sonra tekrar deneyiniz");
                                dialog.show();
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {

                        }
                    });

                }
            }
        });

        return rootView;
    }

    private void loadUserInfo(HashMap<String , String> kullanici) {

        edtDuzenleKayitAd.setText(kullanici.get("UserName"));
        edtDuzenleKayitSoyad.setText(kullanici.get("SurName"));
        edtKayitEmailDuzenle.setText(kullanici.get("Email"));
        edtKayitTelefonDuzenle.setText(kullanici.get("GSM"));
        aCDuzenleKullaniciTuru.setText(setKullaniciTur(kullanici.get("MeType")));
        autoCompleteIlDuzenle.setText(Utils.getSehirAdi(kullanici.get("CityID")));
        autoCompleteIlceDuzenle.setText(DownloadClass.getTownNameWithId(kullanici.get("TownID")));

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
            return "Araç Sahibi";
        }
        if(MeType.equals("4"))
        {
            return "Diğer";
        }

        return "";
    }

    private String getKullaniciTur(String MeType)
    {
        if(MeType.equals("Proje Yöneticisi"))
        {
            return "1";
        }
        if(MeType.equals("Şoför"))
        {
            return "2";
        }
        if(MeType.equals("Araç Sahibi"))
        {
            return "3";
        }
        if(MeType.equals("Diğer"))
        {
            return "4";
        }

        return "";
    }
}