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
    private  boolean isEmailValid,isPasswordMatch;

    List<String> sehirler = new ArrayList<>();
    List<String> ilceler= new ArrayList<>();

    JSONObject jsonObjectIl;
    JSONObject jsonObjectIlce;

    int ilCount = 1;
    int ilçeCount = 1;

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

        Call<SehirResponse> sehirResponseCall = App.getApiService().getSehirler();

        sehirResponseCall.enqueue(new Callback<SehirResponse>() {
            @Override
            public void onResponse(Call<SehirResponse> call, Response<SehirResponse> response) {

                SehirResponseDetail sehirResponseDetail = response.body().getSehirResponseDetail();

                String token = sehirResponseDetail.getResult();

                jsonObjectIl = Utils.jwtToJsonObject(token);

                try {

                    for(int i = 1; i <= jsonObjectIl.getJSONObject("OutPutMessage").getJSONObject("Data").length(); i++)
                    {
                        Log.i("JSONObject" , jsonObjectIl.getJSONObject("OutPutMessage").getJSONObject("Data").get("1").toString() );
                        String sehir = jsonObjectIl.getJSONObject("OutPutMessage").getJSONObject("Data").getString(String.valueOf(i));
                        sehirler.add(sehir);
                    }

                    //Log.i(App.TAG, jsonObjectIl.getJSONObject("OutPutMessage").getJSONObject("Data").getString("1"));

                    Log.i(App.TAG, String.valueOf(sehirler.size()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SehirResponse> call, Throwable t) {

            }
        });

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        ctx,
                        R.layout.dropdown_item,
                        sehirler);

        autoCompleteIl.setAdapter(adapter);

        ArrayAdapter<String> Turadapter =
                new ArrayAdapter<>(
                        ctx,
                        R.layout.dropdown_item,
                        tür);

        autoCompleteKullaniciTuru.setAdapter(Turadapter);


        autoCompleteKullaniciTuru.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                kullanıcıType = parent.getItemAtPosition(position).toString();
            }
        });





        autoCompleteIl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                il = parent.getItemAtPosition(position).toString();

                Log.i("SelectedIl", parent.getItemAtPosition(position).toString());

                try {

                    for(int i = 1; i < jsonObjectIl.getJSONObject("OutPutMessage").getJSONObject("Data").length(); i++)
                    {
                        if( parent.getItemAtPosition(position).toString().equals(jsonObjectIl.getJSONObject("OutPutMessage").getJSONObject("Data").getString(String.valueOf(i))))
                        {
                            break;
                        }

                        ++ilCount;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.i("Şehir Id" ,  " " + ilCount);

                Call<IlceResponse> ilceResponseCall = App.getApiService().getIlceler();

                ilceResponseCall.enqueue(new Callback<IlceResponse>() {
                    @Override
                    public void onResponse(Call<IlceResponse> call, Response<IlceResponse> response) {


                        IlceResponseDetail ilceResponseDetail = response.body().getIlceResponseDetail();

                        String token2 = ilceResponseDetail.getResult();

                        jsonObjectIlce = Utils.jwtToJsonObject(token2);

                        try {

                            Log.i("İlçeler" ,jsonObjectIlce.getJSONObject("OutPutMessage").getJSONObject("Data").getJSONObject(String.valueOf(ilCount)).toString() );

                            Iterator<String> keys = jsonObjectIlce.getJSONObject("OutPutMessage").getJSONObject("Data").getJSONObject(String.valueOf(ilCount)).keys();

                            while(keys.hasNext())

                            {
                                String key = keys.next();
                                String ilce = jsonObjectIlce.getJSONObject("OutPutMessage").getJSONObject("Data").getJSONObject(String.valueOf(ilCount)).getString(String.valueOf(key));
                                Log.i("İlçe" , ilce);
                                ilceler.add(ilce);
                            }

                            ArrayAdapter<String> ilceAdapter =
                                    new ArrayAdapter<>(
                                            ctx,
                                            R.layout.dropdown_item,
                                            ilceler);

                            autoCompleteIlce.setAdapter(ilceAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<IlceResponse> call, Throwable t) {

                    }
                });

            }
        });

        autoCompleteIlce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ilçe = parent.getItemAtPosition(position).toString();

                Log.i("SelectedIlçe" , ilçe);


                try {
                    Iterator<String> keysilce = jsonObjectIlce.getJSONObject("OutPutMessage").getJSONObject("Data").getJSONObject(String.valueOf(ilCount)).keys();

                    while(keysilce.hasNext())

                    {
                        String key = keysilce.next();
                        Log.i("Key" , " " + key);

                        if( parent.getItemAtPosition(position).toString().equals(jsonObjectIlce.getJSONObject("OutPutMessage").getJSONObject("Data").getJSONObject(String.valueOf(ilCount)).getString(key)))
                        {
                            ilçeCount = Integer.parseInt(key);
                            break;
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("IDLer" , " " + ilCount + " " + ilçeCount );

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

                isEmailValid = Utils.isValidEmailAddress(email);

                if (!isEmailValid)
                {
                    emailAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    emailAlert.setTitleText("Lütfen geçerli bir e-mail adresi girin");
                    emailAlert.show();
                }

                isPasswordMatch = sifre.equals(sifreTekrar);

                if(!isPasswordMatch)
                {
                    sifreAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    sifreAlert.setTitleText("Şifreler eşleşmiyor");
                    sifreAlert.show();
                }


                if(!ad.isEmpty() &&  !soyad.isEmpty() && !telefon.isEmpty() && !sifre.isEmpty() && isEmailValid && isPasswordMatch && sifre.length() > 6)
                {
                    HashMap<String , HashMap<String , String>> node = new HashMap<>();
                    HashMap<String , String> body = new HashMap<>();

                    body.put("MeType" , kullanıcıType);
                    body.put("UserName" , ad);
                    body.put("SurName" , soyad);
                    body.put("Email" , email);
                    body.put("Password" , sifre);
                    body.put("GSM" , telefon);
                    body.put("CityID" , String.valueOf(ilCount));
                    body.put("TownID" , String.valueOf(ilçeCount));


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
