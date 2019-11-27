package serviscepde.com.tr.Fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import serviscepde.com.tr.App;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.IlanEkle.EkleResponse;
import serviscepde.com.tr.Models.IlanEkle.EkleResponseDetail;
import serviscepde.com.tr.Models.Ilce;
import serviscepde.com.tr.Models.Sehirler.SehirResponse;
import serviscepde.com.tr.Models.Sehirler.SehirResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IseAracFragment extends Fragment {


    View generalView;


    private  TextInputEditText edtIseAracBaslik,edtIseAracFiyat,edtIseAracAciklama,edtIseAracYil,edtIseAracServisBaslamaSaati,edtIseAracServisBitisSaati,
            edtIseAracFirmaGirisSaati,edtIseAracFirmadanCikisSaati,edtIseAracToplamKM,edtIseAracCalisilacakGunSayisi;


    private  AutoCompleteTextView autoCompleteIseAracil,autoCompleteIseAracilce,autoCompleteIseAracMarka,autoCompleteIseAracModel,autoCompleteIseAracKapasite,autoCompleteIseAracServisBaslamaili,
            autoCompleteIseAracServiseBaslamailce,autoCompleteIseAracServisBitisili,autoCompleteIseAracServisBitisilce;

    private Switch switchIseAracOkulTasiti,switchIseAracTurizmPaketi,switchIseAracKlima,switchIseAracDeriDoseme,switchIseAracTribunTavan,switchIseAracYatarKoltuk;

    private  RelativeLayout relIseAracFirstPhoto,relIseAracSecondPhoto,relIseAracLastPhoto;

    private ImageView imgIseAracFirstPhoto,imgIseAracFirstPhotoChange,imgIseAracSecondPhoto,imgIseAracSecondPhotoChange,imgIseAracLastPhoto,imgIseAracFirstLastChange;

    private TextView txtIseAracGonder;

    private LinearLayout linIseAracIptal;

    private ArrayList<String> photos = new ArrayList<>();

    public  Context ctx;

    private String switchStates = "";
    private String baslik,fiyat,aciklama,yil,servisBaslamaSaati,servisBitisSaati,firmaGirisSaati,firmaCikisSaati,toplamKM,gunSayisi, imageString;
    private String actvIseAracil,actvIseAracilce,actvIseAracMarka,actvIseAracModel,actvIseAracKapasite,actvIseAracServisBaslamaili,
            actvIseAracServiseBaslamailce,actvIseAracServisBitisili,actvIseAracServisBitisilce;

    private SweetAlertDialog emptyDialog;

    private List<City> tmpCity = new ArrayList<>();
    private List<String> sehirListesi = new ArrayList<>();


    final static  Calendar takvim = Calendar.getInstance();
    private int saat = takvim.get(Calendar.HOUR_OF_DAY);
    private int dakika = takvim.get(Calendar.MINUTE);

    private String userToken;
    List<Ilce> ilces;
    List<Ilce> ilcesList;

    List<String> tmp2 = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ise_arac_fragment, container, false);

        photos = getArguments().getStringArrayList("photoList");

        Log.i("Photos" ," " + photos.size() + "photosPath" + photos.toString());

        generalView = rootView;

        ctx = generalView.getContext();

        ilces = new ArrayList<>();
        ilcesList = new ArrayList<>();


        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);


        edtIseAracBaslik = generalView.findViewById(R.id.edtIseAracBaslik);
        edtIseAracFiyat = generalView.findViewById(R.id.edtIseAracFiyat);
        edtIseAracAciklama = generalView.findViewById(R.id.edtIseAracAciklama);
        edtIseAracYil = generalView.findViewById(R.id.edtIseAracYil);
        edtIseAracServisBaslamaSaati = generalView.findViewById(R.id.edtIseAracServisBaslamaSaati);
        edtIseAracServisBitisSaati = generalView.findViewById(R.id.edtIseAracServisBitisSaati);
        edtIseAracFirmaGirisSaati = generalView.findViewById(R.id.edtIseAracFirmaGirisSaati);
        edtIseAracFirmadanCikisSaati = generalView.findViewById(R.id.edtIseAracFirmadanCikisSaati);
        edtIseAracToplamKM = generalView.findViewById(R.id.edtIseAracToplamKM);
        edtIseAracCalisilacakGunSayisi = generalView.findViewById(R.id.edtIseAracCalisilacakGunSayisi);


        autoCompleteIseAracil = generalView.findViewById(R.id.autoCompleteIseAracil);
        autoCompleteIseAracilce = generalView.findViewById(R.id.autoCompleteIseAracilce);
        autoCompleteIseAracMarka = generalView.findViewById(R.id.autoCompleteIseAracMarka);
        autoCompleteIseAracModel = generalView.findViewById(R.id.autoCompleteIseAracModel);
        autoCompleteIseAracKapasite = generalView.findViewById(R.id.autoCompleteIseAracKapasite);
        autoCompleteIseAracServisBaslamaili = generalView.findViewById(R.id.autoCompleteIseAracServisBaslamaili);
        autoCompleteIseAracServiseBaslamailce = generalView.findViewById(R.id.autoCompleteIseAracServiseBaslamailce);
        autoCompleteIseAracServisBitisili = generalView.findViewById(R.id.autoCompleteIseAracServisBitisili);
        autoCompleteIseAracServisBitisilce = generalView.findViewById(R.id.autoCompleteIseAracServisBitisilce);


        switchIseAracOkulTasiti = generalView.findViewById(R.id.switchIseAracOkulTasiti);
        switchIseAracTurizmPaketi = generalView.findViewById(R.id.switchIseAracTurizmPaketi);
        switchIseAracKlima = generalView.findViewById(R.id.switchIseAracKlima);
        switchIseAracDeriDoseme = generalView.findViewById(R.id.switchIseAracDeriDoseme);
        switchIseAracTribunTavan = generalView.findViewById(R.id.switchIseAracTribunTavan);
        switchIseAracYatarKoltuk = generalView.findViewById(R.id.switchIseAracYatarKoltuk);


        relIseAracFirstPhoto = generalView.findViewById(R.id.relIseAracFirstPhoto);
        relIseAracSecondPhoto = generalView.findViewById(R.id.relIseAracSecondPhoto);
        relIseAracLastPhoto = generalView.findViewById(R.id.relIseAracLastPhoto);


        imgIseAracFirstPhoto  = generalView.findViewById(R.id.imgIseAracFirstPhoto);
        imgIseAracFirstPhotoChange  = generalView.findViewById(R.id.imgIseAracFirstPhotoChange);
        imgIseAracSecondPhoto  = generalView.findViewById(R.id.imgIseAracSecondPhoto);
        imgIseAracSecondPhotoChange  = generalView.findViewById(R.id.imgIseAracSecondPhotoChange);
        imgIseAracLastPhoto  = generalView.findViewById(R.id.imgIseAracLastPhoto);
        imgIseAracFirstLastChange  = generalView.findViewById(R.id.imgIseAracLastChange);

        linIseAracIptal  = generalView.findViewById(R.id.linIseAracIptal);
        txtIseAracGonder  = generalView.findViewById(R.id.txtIseAracGonder);

        linIseAracIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx , MainActivity.class);
                startActivity(intent);
            }
        });


        if(photos.size() == 1)
        {
            String img1 = photos.get(0);
            Glide.with(ctx).load(img1).into(imgIseAracFirstPhoto);
            imgIseAracFirstPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 2)
        {
            String img2 = photos.get(0);
            String img3 = photos.get(1);

            Glide.with(ctx).load(img2).into(imgIseAracFirstPhoto);
            Glide.with(ctx).load(img3).into(imgIseAracSecondPhoto);
            imgIseAracFirstPhotoChange.setVisibility(View.INVISIBLE);
            imgIseAracSecondPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 3)
        {
            String img4 = photos.get(0);
            String img5 = photos.get(1);
            String img6 = photos.get(2);

            Glide.with(ctx).load(img4).into(imgIseAracFirstPhoto);
            Glide.with(ctx).load(img5).into(imgIseAracSecondPhoto);
            Glide.with(ctx).load(img6).into(imgIseAracLastPhoto);

            imgIseAracFirstPhotoChange.setVisibility(View.INVISIBLE);
            imgIseAracSecondPhotoChange.setVisibility(View.INVISIBLE);
            imgIseAracFirstLastChange.setVisibility(View.INVISIBLE);
        }

        relIseAracFirstPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.i("RelPhoto" , "onSuccess");

                SweetAlertDialog firstPhoto;

                firstPhoto = new SweetAlertDialog(ctx , SweetAlertDialog.NORMAL_TYPE);
                firstPhoto.setTitle("Düzenle");
                firstPhoto.setConfirmButton("Değiştir", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        ImagePicker.create(getActivity())
                                .returnMode(ReturnMode.GALLERY_ONLY)
                                .returnMode(ReturnMode.CAMERA_ONLY)
                                .folderMode(true)
                                .toolbarFolderTitle("Folder")
                                .includeVideo(false)
                                .multi()
                                .limit(1)
                                .start();

                        firstPhoto.dismiss();

                    }
                });
                firstPhoto.setCancelButton("Sil", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgIseAracFirstPhoto);
                        firstPhoto.dismiss();
                    }
                });
                firstPhoto.setNeutralButton("İptal", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        firstPhoto.dismiss();
                    }
                });
                firstPhoto.show();

            }
        });

        relIseAracSecondPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog secondPhoto;

                secondPhoto = new SweetAlertDialog(ctx , SweetAlertDialog.NORMAL_TYPE);
                secondPhoto.setTitle("Düzenle");
                secondPhoto.setConfirmButton("Değiştir", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        ImagePicker.create(getActivity())
                                .returnMode(ReturnMode.GALLERY_ONLY)
                                .returnMode(ReturnMode.CAMERA_ONLY)
                                .folderMode(true)
                                .toolbarFolderTitle("Folder")
                                .includeVideo(false)
                                .multi()
                                .limit(1)
                                .start();

                        secondPhoto.dismiss();

                    }
                });
                secondPhoto.setCancelButton("Sil", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgIseAracFirstPhoto);
                        secondPhoto.dismiss();
                    }
                });
                secondPhoto.setNeutralButton("İptal", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        secondPhoto.dismiss();
                    }
                });
                secondPhoto.show();


            }
        });

        relIseAracLastPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog lastPhoto;

                lastPhoto = new SweetAlertDialog(ctx , SweetAlertDialog.NORMAL_TYPE);
                lastPhoto.setTitle("Düzenle");
                lastPhoto.setConfirmButton("Değiştir", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        ImagePicker.create(getActivity())
                                .returnMode(ReturnMode.GALLERY_ONLY)
                                .returnMode(ReturnMode.CAMERA_ONLY)
                                .folderMode(true)
                                .toolbarFolderTitle("Folder")
                                .includeVideo(false)
                                .multi()
                                .limit(1)
                                .start();

                        lastPhoto.dismiss();

                    }
                });
                lastPhoto.setCancelButton("Sil", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgIseAracFirstPhoto);
                        lastPhoto.dismiss();
                    }
                });
                lastPhoto.setNeutralButton("İptal", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        lastPhoto.dismiss();
                    }
                });
                lastPhoto.show();


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

                for (int i = 0;  i < tmpCity.size(); i++)
                {
                    String tmp = tmpCity.get(i).getCityName();
                    sehirListesi.add(tmp);
                }

            }

            @Override
            public void onFailure(Call<SehirResponse> call, Throwable t) {

            }
        });


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

                    //setilceler();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<IlceResponse> call, Throwable t) {

            }
        });*/

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
                        tmpCity.add(city);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                for (int i = 0;  i < tmpCity.size(); i++)
                {
                    String tmp = tmpCity.get(i).getCityName();
                    sehirListesi.add(tmp);
                }

            }

            @Override
            public void onFailure(Call<SehirResponse> call, Throwable t) {

            }
        });




        setAutoCompleteAdapter(autoCompleteIseAracil , sehirListesi);
        setAutoCompleteAdapter(autoCompleteIseAracServisBaslamaili , sehirListesi);
        setAutoCompleteAdapter(autoCompleteIseAracServisBitisili , sehirListesi);
        setAutoCompleteAdapter(autoCompleteIseAracKapasite , App.getKapasite());




        autoCompleteIseAracil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvIseAracil = String.valueOf(position + 1);
                Log.i("SelectedItem" , actvIseAracil);

            }
        });


        autoCompleteIseAracServisBaslamaili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvIseAracServisBaslamaili = String.valueOf(position + 1);
                Log.i("SelectedItem" , actvIseAracServisBaslamaili);
            }
        });

        autoCompleteIseAracServisBitisili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvIseAracServisBitisili = String.valueOf(position + 1);
            }
        });

        autoCompleteIseAracKapasite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvIseAracKapasite = String.valueOf(position + 1);
            }
        });

        edtIseAracServisBaslamaSaati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog tpd = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        edtIseAracServisBaslamaSaati.setText(hourOfDay + ":" + minute);

                    }
                },saat,dakika,true);

                tpd.setButton(TimePickerDialog.BUTTON_POSITIVE , "Seç" , tpd);
                tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE , "İptal" , tpd);
                tpd.show();
            }
        });

        edtIseAracServisBitisSaati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog tpd = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        edtIseAracServisBitisSaati.setText(hourOfDay + ":" + minute);

                    }
                },saat,dakika,true);

                tpd.setButton(TimePickerDialog.BUTTON_POSITIVE , "Seç" , tpd);
                tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE , "İptal" , tpd);
                tpd.show();

            }
        });

        edtIseAracFirmaGirisSaati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog tpd = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        edtIseAracFirmaGirisSaati.setText(hourOfDay + ":" + minute);

                    }
                },saat,dakika,true);

                tpd.setButton(TimePickerDialog.BUTTON_POSITIVE , "Seç" , tpd);
                tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE , "İptal" , tpd);
                tpd.show();
            }
        });

        edtIseAracFirmadanCikisSaati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog tpd = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        edtIseAracFirmadanCikisSaati.setText(hourOfDay + ":" + minute);

                    }
                },saat,dakika,true);

                tpd.setButton(TimePickerDialog.BUTTON_POSITIVE , "Seç" , tpd);
                tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE , "İptal" , tpd);
                tpd.show();
            }
        });





        txtIseAracGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baslik = edtIseAracBaslik.getText().toString();
                fiyat = edtIseAracFiyat.getText().toString();
                aciklama = edtIseAracAciklama.getText().toString();
                yil = edtIseAracYil.getText().toString();
                servisBaslamaSaati = edtIseAracServisBaslamaSaati.getText().toString();
                servisBitisSaati = edtIseAracServisBitisSaati.getText().toString();
                firmaGirisSaati = edtIseAracFirmaGirisSaati.getText().toString();
                firmaCikisSaati = edtIseAracFirmadanCikisSaati.getText().toString();
                toplamKM = edtIseAracToplamKM.getText().toString();
                gunSayisi = edtIseAracCalisilacakGunSayisi.getText().toString();

                if(switchIseAracOkulTasiti.isChecked())
                {
                    switchStates = "1|";
                }
                if(switchIseAracTurizmPaketi.isChecked())
                {
                    switchStates = switchStates.concat("2").concat("|");
                }
                if(switchIseAracKlima.isChecked())
                {
                    switchStates = switchStates.concat("3").concat("|");
                }
                if(switchIseAracDeriDoseme.isChecked())
                {
                    switchStates = switchStates.concat("4").concat("|");
                }
                if(switchIseAracTribunTavan.isChecked())
                {
                    switchStates = switchStates.concat("5").concat("|");
                }
                if(switchIseAracYatarKoltuk.isChecked())
                {
                    switchStates = switchStates.concat("6");
                }

                switchStates = Utils.trimmer(switchStates);

                ArrayList<String> base64Photo = Utils.pathToBase64(photos);
                imageString = Utils.imageToString(base64Photo);
                imageString = Utils.trimmer(imageString);







                Log.i("SwitchStates" ,switchStates);


                if(baslik.isEmpty() || fiyat.isEmpty() || aciklama.isEmpty() || yil.isEmpty() || servisBaslamaSaati.isEmpty()
                        || servisBitisSaati.isEmpty() || firmaGirisSaati.isEmpty() || firmaCikisSaati.isEmpty() || toplamKM.isEmpty() || gunSayisi.isEmpty() ||
                        actvIseAracil.isEmpty() || actvIseAracilce.isEmpty() || actvIseAracKapasite.isEmpty() || actvIseAracMarka.isEmpty() || actvIseAracModel.isEmpty()
                        || actvIseAracServisBaslamaili.isEmpty() || actvIseAracServisBitisilce.isEmpty() || actvIseAracServisBitisili.isEmpty() || actvIseAracServiseBaslamailce.isEmpty())
                {

                    emptyDialog = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                    emptyDialog.setTitleText("* ile belirtilen tüm alanlar doldurulmalıdır");
                    emptyDialog.show();

                }

                else
                {
                    HashMap<String , Object> hashMap = new HashMap<>();
                    HashMap<String , String> hashMap1 = new HashMap<>();

                    hashMap1.put("Tipi" , "3");
                    hashMap1.put("Baslik" , baslik);
                    hashMap1.put("ilanCity" , actvIseAracil);
                    hashMap1.put("ilanSemtleri" , "10");
                    hashMap1.put("AracMarkasi" , "3");
                    hashMap1.put("AracModeli" , "4");
                    hashMap1.put("AracYili" , yil);
                    hashMap1.put("AracKapasitesi" , actvIseAracKapasite);
                    hashMap1.put("AracOzellikleri" , switchStates);
                    hashMap1.put("ServiseBaslamaCity" , actvIseAracServisBaslamaili);
                    hashMap1.put("ServiseBaslamaSemtleri" , "10");
                    hashMap1.put("ServiseBaslamaSaati" , servisBaslamaSaati);
                    hashMap1.put("FirmayaGiriSaati" , firmaGirisSaati);
                    hashMap1.put("FirmadanCikisSaati" , firmaCikisSaati);
                    hashMap1.put("ServisBitisSaati" , servisBitisSaati);
                    hashMap1.put("ServisBitisCity" , actvIseAracServisBitisili);
                    hashMap1.put("ServisBitisSemtleri" , "10");
                    hashMap1.put("ToplamKM" , toplamKM);
                    hashMap1.put("CalisilacakGunSayisi" , gunSayisi);
                    hashMap1.put("Ucret" , fiyat);
                    hashMap1.put("ilanAciklamasi" , aciklama);
                    hashMap1.put("file" , imageString);

                    hashMap.put("Token" , userToken);
                    hashMap.put("param" , hashMap1);


                    Call<EkleResponse> ilanEkle = App.getApiService().ilanEkle(hashMap);
                    ilanEkle.enqueue(new Callback<EkleResponse>() {

                        @Override
                        public void onResponse(Call<EkleResponse> call, Response<EkleResponse> response) {

                            EkleResponseDetail detail = response.body().getDetail();

                            String token = detail.getResult();

                            JSONObject ekleResponse = Utils.jwtToJsonObject(token);
                            SweetAlertDialog ilanHata;
                            SweetAlertDialog ilanOnay;

                            try {

                                if( ekleResponse.getJSONObject("OutPutMessage").getInt("Status") == 200)
                                {
                                    ilanOnay = new SweetAlertDialog(ctx , SweetAlertDialog.NORMAL_TYPE);
                                    ilanOnay.setTitleText(ekleResponse.getJSONObject("OutPutMessage").getString("Message"));
                                    ilanOnay.show();
                                }

                                if(ekleResponse.getJSONObject("errorEmpty") != null)
                                {

                                    ilanHata = new SweetAlertDialog(ctx , SweetAlertDialog.ERROR_TYPE);
                                    ilanHata.setTitleText(ekleResponse.getJSONObject("errorEmpty").toString());
                                    ilanHata.show();
                                }
                                if(ekleResponse.getJSONObject("errorOther") !=null)
                                {
                                    ilanHata = new SweetAlertDialog(ctx , SweetAlertDialog.ERROR_TYPE);
                                    ilanHata.setTitleText(ekleResponse.getJSONObject("errorOther").toString());
                                    ilanHata.show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<EkleResponse> call, Throwable t) {

                            SweetAlertDialog ilanHata;
                            ilanHata = new SweetAlertDialog(ctx , SweetAlertDialog.ERROR_TYPE);
                            ilanHata.setTitleText("Bir şeyler yanlış gitti lütfen daha sonra tekrar deneyin");
                            ilanHata.show();

                        }
                    });

                    switchStates = "";
                    photos.clear();

                }
            }
        });

        return rootView;
    }

    private void setAutoCompleteAdapter(AutoCompleteTextView autoCompleteTextView , List<String> list)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, R.layout.dropdown_item, list);
        autoCompleteTextView.setAdapter(adapter);
    }









}