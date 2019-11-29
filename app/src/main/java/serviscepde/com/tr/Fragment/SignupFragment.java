package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import serviscepde.com.tr.App;
import serviscepde.com.tr.DownloadClass;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.Ilce;
import serviscepde.com.tr.Models.Ilceler.IlceResponse;
import serviscepde.com.tr.Models.Ilceler.IlceResponseDetail;
import serviscepde.com.tr.Models.Sehirler.SehirResponse;
import serviscepde.com.tr.Models.Sehirler.SehirResponseDetail;
import serviscepde.com.tr.Models.UserRegister.UserRegisterResponse;
import serviscepde.com.tr.Models.UserRegister.UserRegisterResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static serviscepde.com.tr.App.TAG;

public class SignupFragment extends Fragment {

    View generalView;

    String [] tür = {"Proje Yöneticisi" , "Şoför" , "Diğer"};

    private  EditText edtKayitAd,edtKayitEmail,edtKayitTelefon,edtKayitSifre,edtKayitSifreTekrar,edtKayitSoyad;
    private TextView txtKayitOlSon;

    private  AutoCompleteTextView autoCompleteIl,autoCompleteIlce,autoCompleteKullaniciTuru;

    private  SweetAlertDialog adAlert, soyadAlert,emailAlert,telefonAlert,sifreAlert,servisAlert;
    private String ad,soyad,email,telefon,sifre,sifreTekrar,il,ilçe,kullanıcıType;
    private boolean isEmailValid = true;
    private boolean isPasswordMatch;

    List<City> sehirler = new ArrayList<>();
    List<String > cityNames = new ArrayList<>();
    List<String> ilceler= new ArrayList<>();

    JSONObject jsonObjectIl;
    JSONObject jsonObjectIlce;

    int ilCount = 1;
    int ilçeCount = 1;

    String SelectedCityId = "";
    String SelectedTownId = "";

    private Context ctx;

    private boolean isEligible = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.signup_fragment , container , false);

        generalView = rootView;

        edtKayitAd = generalView.findViewById(R.id.edtKayitAd);
        edtKayitSoyad = generalView.findViewById(R.id.edtKayitSoyad);
        edtKayitEmail = generalView.findViewById(R.id.edtKayitEmail);
        edtKayitTelefon = generalView.findViewById(R.id.edtKayitTelefon);
        edtKayitSifre = generalView.findViewById(R.id.edtKayitSifre);
        edtKayitSifreTekrar = generalView.findViewById(R.id.edtKayitSifreTekrar);

        txtKayitOlSon = generalView.findViewById(R.id.txtKayitOlSon);

        autoCompleteIl = generalView.findViewById(R.id.autoCompleteIl);
        autoCompleteIlce = generalView.findViewById(R.id.autoCompleteIlce);
        autoCompleteKullaniciTuru = generalView.findViewById(R.id.autoCompleteKullaniciTuru);

        ctx = generalView.getContext();



        ArrayAdapter<String> Turadapter = new ArrayAdapter<>(ctx, R.layout.dropdown_item, tür);
        autoCompleteKullaniciTuru.setAdapter(Turadapter);


        autoCompleteKullaniciTuru.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                kullanıcıType = parent.getItemAtPosition(position).toString();
            }
        });



        sehirler = DownloadClass.getCities();
        cityNames = DownloadClass.getCityNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, R.layout.dropdown_item, cityNames);

        autoCompleteIl.setAdapter(adapter);



        autoCompleteIl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tempCityName = parent.getItemAtPosition(position).toString();

                SelectedCityId = DownloadClass.getCityIdWithName(tempCityName);
                ArrayList<String> cityTownNames = DownloadClass.getTownNames(SelectedCityId);

                ArrayAdapter<String> ilceAdapter = new ArrayAdapter<>(ctx, R.layout.dropdown_item, cityTownNames);

                autoCompleteIlce.setAdapter(ilceAdapter);
            }
        });

        autoCompleteIlce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tempTownName = parent.getItemAtPosition(position).toString();
                SelectedTownId = DownloadClass.getTownIdWithTownName(tempTownName, SelectedCityId);

            }
        });


        txtKayitOlSon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ad = edtKayitAd.getText().toString();
                soyad = edtKayitSoyad.getText().toString();
                email = edtKayitEmail.getText().toString();
                telefon = edtKayitTelefon.getText().toString();
                sifre = edtKayitSifre.getText().toString();
                sifreTekrar = edtKayitSifreTekrar.getText().toString();

                if(ad.isEmpty())
                {
                    adAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    adAlert.setTitleText("Ad boş bırakılamaz");
                    adAlert.show();
                }

                if(soyad.isEmpty())
                {
                    soyadAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    soyadAlert.setTitleText("Soyad boş bırakılamaz");
                    soyadAlert.show();
                }

                if(telefon.isEmpty())
                {
                    telefonAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    telefonAlert.setTitleText("Telefon boş bırakılamaz");
                    telefonAlert.show();
                }


                if(sifre.isEmpty())
                {
                    sifreAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    sifreAlert.setTitleText("Şifre boş bırakılamaz");
                    sifreAlert.show();
                }

                if(!sifre.isEmpty() && sifre.length() < 6)
                {
                    sifreAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    sifreAlert.setTitleText("Şifre 6 haneden daha kısa olamaz");
                    sifreAlert.show();
                }

                if(!email.isEmpty())
                {
                    isEmailValid = Utils.isValidEmailAddress(email);
                    if (!isEmailValid)
                    {
                        emailAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                        emailAlert.setTitleText("Lütfen geçerli bir e-mail adresi girin");
                        emailAlert.show();
                    }
                }

                isPasswordMatch = sifre.equals(sifreTekrar);
                if(!isPasswordMatch)
                {
                    sifreAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    sifreAlert.setTitleText("Şifreler eşleşmiyor");
                    sifreAlert.show();
                }


                if(!ad.isEmpty() &&  !soyad.isEmpty() && !telefon.isEmpty() && !sifre.isEmpty() && isEmailValid && isPasswordMatch && sifre.length() >= 6)
                {

                    HashMap<String , HashMap<String , String>> node = new HashMap<>();
                    HashMap<String , String> body = new HashMap<>();

                    body.put("MeType" , kullanıcıType);
                    body.put("UserName" , ad);
                    body.put("SurName" , soyad);
                    body.put("Email" , email);
                    body.put("Password" , sifre);
                    body.put("GSM" , telefon);
                    body.put("CityID" , SelectedCityId);
                    body.put("TownID" , SelectedTownId);


                    node.put("param" , body);
                    Call<UserRegisterResponse> userRegisterResponseCall = App.getApiService().getRegister(node);
                    userRegisterResponseCall.enqueue(new Callback<UserRegisterResponse>() {
                        @Override
                        public void onResponse(Call<UserRegisterResponse> call, Response<UserRegisterResponse> response) {

                            UserRegisterResponseDetail userRegisterResponseDetail = response.body().getUserRegisterResponseDetail();
                            String token = userRegisterResponseDetail.getResult();

                            Log.i(TAG, "onResponse: " + token);

                            JSONObject jsonObject = Utils.jwtToJsonObject(token);
                            Log.i(TAG, "onResponse: " + jsonObject.toString());

                            try {
                                Log.i(TAG, "onResponse: " + jsonObject.getJSONArray("errorOther"));


                                if(jsonObject.get("OutPutMessage") instanceof  JSONObject)
                                {

                                    int status = jsonObject.getJSONObject("OutPutMessage").getInt("Status");
                                    if(status == 200)
                                    {
                                        servisAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.NORMAL_TYPE);
                                        servisAlert.setTitleText(jsonObject.getJSONObject("OutPutMessage").getString("SuccessMessage"));
                                        servisAlert.show();
                                    }
                                }

                                if(jsonObject.get("OutPutMessage") instanceof JSONArray)
                                {

                                    if(jsonObject.getJSONArray("errorOther") != null)
                                    {
                                        servisAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                                        servisAlert.setTitleText(jsonObject.getJSONArray("errorOther").getString(0));
                                        servisAlert.show();
                                    }

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(Call<UserRegisterResponse> call, Throwable t) {

                        }
                    });


                }



            }
        });


        return  rootView;
    }
}
