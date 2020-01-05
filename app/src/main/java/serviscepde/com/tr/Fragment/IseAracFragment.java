package serviscepde.com.tr.Fragment;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import serviscepde.com.tr.DownloadClass;
import serviscepde.com.tr.GalleryActivity;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.IlanEkle.EkleResponse;
import serviscepde.com.tr.Models.IlanEkle.EkleResponseDetail;
import serviscepde.com.tr.Models.Ilce;
import serviscepde.com.tr.Models.Kapasite;
import serviscepde.com.tr.Models.Sehirler.SehirResponse;
import serviscepde.com.tr.Models.Sehirler.SehirResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
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
    private String [] imageArray;
    private SweetAlertDialog pDialog;

    private String switchStates="";
    private String baslik,fiyat,aciklama,yil,servisBaslamaSaati,servisBitisSaati,firmaGirisSaati,firmaCikisSaati,toplamKM,gunSayisi, imageString = null;
    private String actvIseAracMarka,actvIseAracModel,actvIseAracKapasite;

    private SweetAlertDialog emptyDialog;

    private List<Kapasite> kapasites = new ArrayList<>();
    private List<String> kapasiteNames = new ArrayList<>();


    final static  Calendar takvim = Calendar.getInstance();
    private int saat = takvim.get(Calendar.HOUR_OF_DAY);
    private int dakika = takvim.get(Calendar.MINUTE);

    private List<City> sehirler = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private List<String> marka = new ArrayList<>();
    private List<String> model = new ArrayList<>();
    private String cityId,baslamaCityId,bitisCityId;
    private String townId,baslamaTownId,bitisTownId;
    private ArrayList<String> townNames , baslamaTownNames , bitisTownNames = new ArrayList<>();

    private String userToken;

    public static Activity act;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ise_arac_fragment, container, false);


        generalView = rootView;
        act = getActivity();
        ctx = generalView.getContext();


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

        pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f1a400"));
        pDialog.setTitleText("Lütfen Bekleyiniz");
        pDialog.setCancelable(false);

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
                        Intent intent = new Intent(getContext(), GalleryActivity.class);
                        intent.putExtra("position",1);
                        startActivityForResult(intent, 6614);
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

                        Intent intent = new Intent(getContext(), GalleryActivity.class);
                        intent.putExtra("position",2);
                        startActivityForResult(intent, 6614);
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

                        Intent intent = new Intent(getContext(), GalleryActivity.class);
                        intent.putExtra("position",3);
                        startActivityForResult(intent, 6614);

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


        sehirler = DownloadClass.getCities();
        cityNames = DownloadClass.getCityNames();
        marka = DownloadClass.getMarkaNames();
        kapasites = DownloadClass.getKapasite();
        kapasiteNames = DownloadClass.getKapasiteNames();


        setAutoCompleteAdapter(autoCompleteIseAracil , cityNames);
        setAutoCompleteAdapter(autoCompleteIseAracServisBaslamaili , cityNames);
        setAutoCompleteAdapter(autoCompleteIseAracServisBitisili , cityNames);
        setAutoCompleteAdapter(autoCompleteIseAracKapasite , kapasiteNames);
        setAutoCompleteAdapter(autoCompleteIseAracMarka , marka);

        autoCompleteIseAracMarka.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvIseAracMarka = parent.getItemAtPosition(position).toString();
                actvIseAracMarka = DownloadClass.getMarkaIdWithName(actvIseAracMarka);
                Log.i("SelectedMarkaId" , actvIseAracMarka);

                model = DownloadClass.getModelNames(actvIseAracMarka);
                setAutoCompleteAdapter(autoCompleteIseAracModel , model);
                autoCompleteIseAracModel.setText("");
            }
        });

        autoCompleteIseAracModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvIseAracModel = parent.getItemAtPosition(position).toString();
                actvIseAracModel = DownloadClass.getModelIdWithName(actvIseAracModel);

            }
        });


        autoCompleteIseAracil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityId = parent.getItemAtPosition(position).toString();
                cityId = DownloadClass.getCityIdWithName(cityId);
                Log.i("SelectedIl" , cityId);

                townNames = DownloadClass.getTownNames(cityId);
                autoCompleteIseAracilce.setText(townNames.get(0));
                setAutoCompleteAdapter(autoCompleteIseAracilce , townNames);
            }
        });

        autoCompleteIseAracilce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                townId = parent.getItemAtPosition(position).toString();
                townId = DownloadClass.getTownIdWithTownName(townId , cityId);
                Log.i("SelectedIlce" , townId);
            }
        });


        autoCompleteIseAracServisBaslamaili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                baslamaCityId = parent.getItemAtPosition(position).toString();
                baslamaCityId = DownloadClass.getCityIdWithName(baslamaCityId);
                Log.i("SelectedBaslamaId" , baslamaCityId);

                baslamaTownNames = DownloadClass.getTownNames(baslamaCityId);
                autoCompleteIseAracServiseBaslamailce.setText(baslamaTownNames.get(0));
                setAutoCompleteAdapter(autoCompleteIseAracServiseBaslamailce , baslamaTownNames);
            }
        });

        autoCompleteIseAracServiseBaslamailce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                baslamaTownId = parent.getItemAtPosition(position).toString();
                baslamaTownId = DownloadClass.getTownIdWithTownName(baslamaTownId , baslamaCityId);

                Log.i("SelectedBaslamaIlce" , baslamaTownId);
            }
        });

        autoCompleteIseAracServisBitisili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                bitisCityId = parent.getItemAtPosition(position).toString();
                bitisCityId = DownloadClass.getCityIdWithName(bitisCityId);
                Log.i("SelectedBitisIl" , bitisCityId);

                bitisTownNames = DownloadClass.getTownNames(bitisCityId);
                autoCompleteIseAracServisBitisilce.setText(bitisTownNames.get(0));
                setAutoCompleteAdapter(autoCompleteIseAracServisBitisilce , bitisTownNames);
            }
        });

        autoCompleteIseAracServisBitisilce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                bitisTownId = parent.getItemAtPosition(position).toString();
                bitisTownId = DownloadClass.getTownIdWithTownName(bitisTownId , bitisCityId);
                Log.i("SelectedBitisIlce" , bitisTownId);

            }
        });

        autoCompleteIseAracKapasite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvIseAracKapasite = parent.getItemAtPosition(position).toString();
                actvIseAracKapasite = DownloadClass.getKapasiteIdWithName(actvIseAracKapasite);
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

                txtIseAracGonder.setClickable(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtIseAracGonder.setClickable(true);
                    }
                },3000);


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

                if(switchStates != null)
                {
                    switchStates = Utils.SwitchTrimmer(switchStates);
                    Log.i("SwitchStates" ,switchStates);
                }


                if(photos.size() != 0)
                {
                    ArrayList<String> base64Photo = Utils.pathToBase64(photos);
                    imageArray = new String[base64Photo.size()];

                    for(int i = 0; i < base64Photo.size(); i++)
                    {
                        imageArray[i] = base64Photo.get(i);
                    }

                }


                if(baslik.isEmpty()  || aciklama.isEmpty() || yil.isEmpty() || servisBaslamaSaati.isEmpty() || servisBitisSaati.isEmpty() || firmaGirisSaati.isEmpty() || firmaCikisSaati.isEmpty() || toplamKM.isEmpty() || gunSayisi.isEmpty() || cityId.isEmpty() || autoCompleteIseAracilce.getText().toString().isEmpty() || actvIseAracKapasite.isEmpty() || actvIseAracMarka.isEmpty() || baslamaCityId.isEmpty() || autoCompleteIseAracServisBitisilce.getText().toString().isEmpty() || bitisCityId.isEmpty() || autoCompleteIseAracServiseBaslamailce.getText().toString().isEmpty())
                {

                    emptyDialog = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                    emptyDialog.setTitleText("* ile belirtilen tüm alanlar doldurulmalıdır");
                    emptyDialog.show();

                }

                else
                {
                    HashMap<String , Object> hashMap = new HashMap<>();
                    HashMap<String , Object> hashMap1 = new HashMap<>();

                    pDialog.show();

                    hashMap1.put("Tipi" , "3");
                    hashMap1.put("Baslik" , baslik);
                    hashMap1.put("ilanCity" , cityId);
                    hashMap1.put("ilanSemtleri" , DownloadClass.getTownIdWithTownName(autoCompleteIseAracilce.getText().toString() , cityId));
                    hashMap1.put("AracMarkasi" , actvIseAracMarka);
                    hashMap1.put("AracModeli" , actvIseAracModel);
                    hashMap1.put("AracYili" , yil);
                    hashMap1.put("AracKapasitesi" , actvIseAracKapasite);
                    hashMap1.put("AracOzellikleri" , switchStates);
                    hashMap1.put("ServiseBaslamaCity" , baslamaCityId);
                    hashMap1.put("ServiseBaslamaSemtleri" , DownloadClass.getTownIdWithTownName(autoCompleteIseAracServiseBaslamailce.getText().toString() , baslamaCityId));
                    hashMap1.put("ServiseBaslamaSaati" , servisBaslamaSaati);
                    hashMap1.put("FirmayaGirisSaati" , firmaGirisSaati);
                    hashMap1.put("FirmadanCikisSaati" , firmaCikisSaati);
                    hashMap1.put("ServisBitisSaati" , servisBitisSaati);
                    hashMap1.put("ServisBitisCity" , bitisCityId);
                    hashMap1.put("ServisBitisSemtleri" , DownloadClass.getTownIdWithTownName(autoCompleteIseAracServisBitisilce.getText().toString() , bitisCityId));
                    hashMap1.put("ToplamKM" , toplamKM);
                    hashMap1.put("CalisilacakGunSayisi" , gunSayisi);
                    hashMap1.put("Ucret" , fiyat);
                    hashMap1.put("ilanAciklamasi" , aciklama);
                    hashMap1.put("file" , imageArray);

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
                                    pDialog.dismiss();
                                    ilanOnay = new SweetAlertDialog(ctx , SweetAlertDialog.NORMAL_TYPE);
                                    ilanOnay.setTitleText(ekleResponse.getJSONObject("OutPutMessage").getString("Message"));
                                    ilanOnay.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            Intent main = new Intent(ctx , MainActivity.class);
                                            startActivity(main);
                                            getActivity().finish();
                                        }
                                    });
                                    ilanOnay.show();
                                }

                                else
                                {
                                    pDialog.dismiss();
                                    ilanHata = new SweetAlertDialog(ctx , SweetAlertDialog.ERROR_TYPE);
                                    ilanHata.setTitleText("Bir hata oluştu lütfen daha sonra tekrar deneyin");
                                    ilanHata.show();
                                }

                            } catch (JSONException e) {
                                pDialog.dismiss();
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<EkleResponse> call, Throwable t) {

                            pDialog.dismiss();
                            SweetAlertDialog ilanHata;
                            ilanHata = new SweetAlertDialog(ctx , SweetAlertDialog.ERROR_TYPE);
                            ilanHata.setTitleText("Bir şeyler yanlış gitti lütfen daha sonra tekrar deneyin");
                            ilanHata.show();

                        }
                    });

                    switchStates ="";
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 6614){
            if(data != null)
            {
                ArrayList<String> imageList = data.getStringArrayListExtra("imageList");
                if(resultCode == 100){
                    Glide.with(ctx).load(imageList.get(0)).into(imgIseAracFirstPhoto);
                    imgIseAracFirstPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));
                }
                else if(resultCode == 200){
                    Glide.with(ctx).load(imageList.get(0)).into(imgIseAracSecondPhoto);
                    imgIseAracSecondPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
                else if(resultCode == 300){
                    Glide.with(ctx).load(imageList.get(0)).into(imgIseAracLastPhoto);
                    imgIseAracFirstLastChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}