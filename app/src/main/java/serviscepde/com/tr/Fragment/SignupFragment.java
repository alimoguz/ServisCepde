package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import br.com.sapereaude.maskedEditText.MaskedEditText;
import serviscepde.com.tr.App;
import serviscepde.com.tr.DownloadClass;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.UserRegister.UserRegisterResponse;
import serviscepde.com.tr.Models.UserRegister.UserRegisterResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.SplashActivity;
import serviscepde.com.tr.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static serviscepde.com.tr.App.TAG;

public class SignupFragment extends Fragment {

    View generalView;

    private String [] kullaniciTur = {"Proje Yöneticisi" , "Şoför" , "Araç Sahibi" , "Diğer"};

    private  EditText edtKayitAd,edtKayitEmail,edtKayitSifre,edtKayitSifreTekrar,edtKayitSoyad;
    private MaskedEditText edtKayitTelefon;
    private TextView txtKayitOlSon,txtKullanim;
    private CheckBox checkBoxKullanim;

    private  AutoCompleteTextView autoCompleteIl,autoCompleteIlce,autoCompleteKullaniciTuru;

    private  SweetAlertDialog adAlert, soyadAlert,emailAlert,telefonAlert,sifreAlert,servisAlert,checkAlert,kullaniciAlert;
    private String ad,soyad,email,telefon,sifre,sifreTekrar,il,ilçe,kullanıcıType;
    private boolean isEmailValid = true;
    private boolean isPasswordMatch;

    List<City> sehirler = new ArrayList<>();
    List<String > cityNames = new ArrayList<>();
    List<String> ilceler= new ArrayList<>();

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
        txtKullanim = generalView.findViewById(R.id.txtKullanim);
        checkBoxKullanim = generalView.findViewById(R.id.checkBoxKullanim);

        autoCompleteIl = generalView.findViewById(R.id.autoCompleteIl);
        autoCompleteIlce = generalView.findViewById(R.id.autoCompleteIlce);
        autoCompleteKullaniciTuru = generalView.findViewById(R.id.autoCompleteKullaniciTuru);

        ctx = generalView.getContext();

        SweetAlertDialog pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f1a400"));
        pDialog.setTitleText("Lütfen Bekleyiniz");
        pDialog.setCancelable(false);



        ArrayAdapter<String> Turadapter = new ArrayAdapter<>(ctx, R.layout.dropdown_item, kullaniciTur);
        autoCompleteKullaniciTuru.setAdapter(Turadapter);


        kullanıcıType = "";
        autoCompleteKullaniciTuru.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                kullanıcıType = String.valueOf(position + 1);
            }
        });


        txtKullanim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://www.serviscepde.com/sozlesme/uyelik-sozlesmesi";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);



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
                autoCompleteIlce.setText(cityTownNames.get(0));

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
                boolean isAccepted = checkBoxKullanim.isChecked();

                if(kullanıcıType.isEmpty() || ad.isEmpty() || soyad.isEmpty() || telefon.isEmpty() || sifre.isEmpty())
                {
                    kullaniciAlert = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                    kullaniciAlert.setTitleText("* ile belirtilen alanlar boş bırakılamaz");
                    kullaniciAlert.show();

                    return;
                }

                if( sifre.length() < 6)
                {
                    kullaniciAlert = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                    kullaniciAlert.setTitleText("Şifre en az 6 karakter olmalı");
                    kullaniciAlert.show();

                    return;

                }

                if(!isAccepted)
                {
                    checkAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    checkAlert.setTitleText("Kullanım şartları kabul edilmeli!");
                    checkAlert.show();
                }

               /* if()
                {
                    adAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    adAlert.setTitleText("Ad boş bırakılamaz");
                    adAlert.show();
                }

                if()
                {
                    soyadAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    soyadAlert.setTitleText("Soyad boş bırakılamaz");
                    soyadAlert.show();
                }

                if()
                {
                    telefonAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    telefonAlert.setTitleText("Telefon boş bırakılamaz");
                    telefonAlert.show();
                }


                if()
                {
                    sifreAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    sifreAlert.setTitleText("Şifre boş bırakılamaz");
                    sifreAlert.show();
                }

                if()
                {
                    sifreAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    sifreAlert.setTitleText("Şifre 6 haneden daha kısa olamaz");
                    sifreAlert.show();
                }*/

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


                if(!ad.isEmpty() &&  !soyad.isEmpty() && !telefon.isEmpty() && !sifre.isEmpty() && isEmailValid && isPasswordMatch && sifre.length() >= 6 && isAccepted && !kullanıcıType.isEmpty())
                {

                    HashMap<String , HashMap<String , String>> node = new HashMap<>();
                    HashMap<String , String> body = new HashMap<>();
                    pDialog.show();

                    body.put("MeType" , kullanıcıType);
                    body.put("UserName" , ad);
                    body.put("SurName" , soyad);
                    body.put("Email" , email);
                    body.put("Password" , sifre);
                    body.put("GSM" , telefon);
                    body.put("CityID" , SelectedCityId);
                    body.put("TownID" , DownloadClass.getTownIdWithTownName(autoCompleteIlce.getText().toString() , SelectedCityId));


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
                                        pDialog.dismiss();
                                        servisAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.NORMAL_TYPE);
                                        servisAlert.setTitleText(jsonObject.getJSONObject("OutPutMessage").getString("SuccessMessage"));
                                        servisAlert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {

                                                FragmentManager manager = getFragmentManager();
                                                manager.popBackStackImmediate();

                                            }
                                        });


                                        servisAlert.show();
                                    }
                                }

                                if(jsonObject.get("OutPutMessage") instanceof JSONArray)
                                {
                                    pDialog.dismiss();

                                    if(jsonObject.getJSONArray("errorOther") != null)
                                    {
                                        servisAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                                        servisAlert.setTitleText(jsonObject.getJSONArray("errorOther").getString(0));
                                        servisAlert.show();
                                    }

                                }


                            } catch (JSONException e) {
                                pDialog.dismiss();
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(Call<UserRegisterResponse> call, Throwable t) {

                            pDialog.dismiss();

                        }
                    });


                }



            }
        });


        return  rootView;
    }
}
