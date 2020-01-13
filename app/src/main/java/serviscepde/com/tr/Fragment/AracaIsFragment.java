package serviscepde.com.tr.Fragment;

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

import serviscepde.com.tr.App;
import serviscepde.com.tr.DownloadClass;
import serviscepde.com.tr.GalleryActivity;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.IlanEkle.EkleResponse;
import serviscepde.com.tr.Models.IlanEkle.EkleResponseDetail;
import serviscepde.com.tr.Models.Kapasite;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.ImageCompression;
import serviscepde.com.tr.Utils.ImageCompressor;
import serviscepde.com.tr.Utils.OnCompressTaskCompleted;
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


public class AracaIsFragment extends Fragment {


    View generalView;

    private Switch switchAracaIsOkulTasiti,switchAracaIsTurizmPaketi,switchAracaIsKlima,switchAracaIsDeriDoseme,switchAracaIsTribunTavan,switchAracaIsYatarKoltuk;

    private TextInputEditText edtAracaIsBaslik,edtAracaIsFiyat,edtAracaIsAciklama,edtAracaIsYil,edtAracaIsServisBaslamaSaati,edtAracaIsServisBitisSaati,
            edtAracaIsTecrube,edtAracaIsPlaka,edtAracaIsReferans,edtAracaIsToplamKM,edtAracaIsCalisilacakGunSayisi;


    private AutoCompleteTextView autoCompleteAracaIsil,autoCompleteAracaIsilce,autoCompleteAracaIsMarka,autoCompleteAracaIsModel,autoCompleteAracaIsKapasite,autoCompleteAracaIsServisBaslamaili,
            autoCompleteAracaIsServiseBaslamailce,autoCompleteAracaIsAracDurumu;

    private RelativeLayout relAracaIsFirstPhoto,relAracaIsSecondPhoto,relAracaIsLastPhoto;

    private ImageView imgAracaIsFirstPhoto,imgAracaIsFirstPhotoChange,imgAracaIsSecondPhoto,imgAracaIsSecondPhotoChange,imgAracaIsLastPhoto,imgAracaIsLastChange;

    private LinearLayout linAracaIsIptal;

    private ArrayList<String> photos = new ArrayList<>();

    private String baslik,fiyat,aciklama,yil,servisBaslamaSaati,servisBitisSaati, tecrube,plaka,referans,toplamKM,gunSayisi,imagesString;

    private String actvMarka,actvModel,actvKapasite,actvAracDurumu;

    private TextView txtAracaIsGonder;
    private Context ctx;
    private SweetAlertDialog emptyDialog;

    private String switchStates=null;
    private List<String> aracDurumu = new ArrayList<>();

    private final static Calendar takvim = Calendar.getInstance();
    private int saat = takvim.get(Calendar.HOUR_OF_DAY);
    private int dakika = takvim.get(Calendar.MINUTE);

    private SweetAlertDialog pDialog;

    private String userToken;

    private List<City> sehirler = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private List<String> marka = new ArrayList<>();
    private List<String> model = new ArrayList<>();
    private ArrayList<String> townNames , baslamaTownNames  = new ArrayList<>();
    private List<Kapasite> kapasites = new ArrayList<>();
    private List<String> kapasiteNames = new ArrayList<>();
    private List<String> emptyList = new ArrayList<>();

    private String cityId,baslamaCityId;
    private String townId,baslamaTownId;
    private String [] imageArray;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.araca_is_fragment, container, false);


        generalView = rootView;

        ctx = generalView.getContext();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        edtAracaIsBaslik = generalView.findViewById(R.id.edtAracaIsBaslik);
        edtAracaIsFiyat = generalView.findViewById(R.id.edtAracaIsFiyat);
        edtAracaIsAciklama = generalView.findViewById(R.id.edtAracaIsAciklama);
        edtAracaIsYil = generalView.findViewById(R.id.edtAracaIsYil);
        edtAracaIsServisBaslamaSaati = generalView.findViewById(R.id.edtAracaIsServisBaslamaSaati);
        edtAracaIsServisBitisSaati = generalView.findViewById(R.id.edtAracaIsServisBitisSaati);
        edtAracaIsTecrube = generalView.findViewById(R.id.edtAracaIsTecrube);
        edtAracaIsPlaka = generalView.findViewById(R.id.edtAracaIsPlaka);
        edtAracaIsReferans = generalView.findViewById(R.id.edtAracaIsReferans);
        edtAracaIsToplamKM = generalView.findViewById(R.id.edtAracaIsToplamKM);
        edtAracaIsCalisilacakGunSayisi = generalView.findViewById(R.id.edtAracaIsCalisilacakGunSayisi);


        autoCompleteAracaIsil = generalView.findViewById(R.id.autoCompleteAracaIsil);
        autoCompleteAracaIsilce = generalView.findViewById(R.id.autoCompleteAracaIsilce);
        autoCompleteAracaIsMarka = generalView.findViewById(R.id.autoCompleteAracaIsMarka);
        autoCompleteAracaIsModel = generalView.findViewById(R.id.autoCompleteAracaIsModel);
        autoCompleteAracaIsKapasite = generalView.findViewById(R.id.autoCompleteAracaIsKapasite);
        autoCompleteAracaIsServisBaslamaili = generalView.findViewById(R.id.autoCompleteAracaIsServisBaslamaili);
        autoCompleteAracaIsServiseBaslamailce = generalView.findViewById(R.id.autoCompleteAracaIsServiseBaslamailce);
        autoCompleteAracaIsAracDurumu = generalView.findViewById(R.id.autoCompleteAracaIsAracDurumu);


        switchAracaIsOkulTasiti = generalView.findViewById(R.id.switchAracaIsOkulTasiti);
        switchAracaIsTurizmPaketi = generalView.findViewById(R.id.switchAracaIsTurizmPaketi);
        switchAracaIsKlima = generalView.findViewById(R.id.switchAracaIsKlima);
        switchAracaIsDeriDoseme = generalView.findViewById(R.id.switchAracaIsDeriDoseme);
        switchAracaIsTribunTavan = generalView.findViewById(R.id.switchAracaIsTribunTavan);
        switchAracaIsYatarKoltuk = generalView.findViewById(R.id.switchAracaIsYatarKoltuk);


        relAracaIsFirstPhoto = generalView.findViewById(R.id.relAracaIsFirstPhoto);
        relAracaIsSecondPhoto = generalView.findViewById(R.id.relAracaIsSecondPhoto);
        relAracaIsLastPhoto = generalView.findViewById(R.id.relAracaIsLastPhoto);


        imgAracaIsFirstPhoto = generalView.findViewById(R.id.imgAracaIsFirstPhoto);
        imgAracaIsFirstPhotoChange = generalView.findViewById(R.id.imgAracaIsFirstPhotoChange);
        imgAracaIsSecondPhoto = generalView.findViewById(R.id.imgAracaIsSecondPhoto);
        imgAracaIsSecondPhotoChange = generalView.findViewById(R.id.imgAracaIsSecondPhotoChange);
        imgAracaIsLastPhoto = generalView.findViewById(R.id.imgAracaIsLastPhoto);
        imgAracaIsLastChange = generalView.findViewById(R.id.imgAracaIsLastChange);


        linAracaIsIptal = generalView.findViewById(R.id.linAracaIsIptal);
        txtAracaIsGonder = generalView.findViewById(R.id.txtAracaIsGonder);

        pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f1a400"));
        pDialog.setTitleText("Lütfen Bekleyiniz");
        pDialog.setCancelable(false);


        aracDurumu.add("Aracım Tamamen Boşta");
        aracDurumu.add("Aracıma Ek İş Arıyorum");


        if(photos.size() == 1)
        {
            String img1 = photos.get(0);
            Glide.with(ctx).load(img1).into(imgAracaIsFirstPhoto);
            imgAracaIsFirstPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 2)
        {
            String img2 = photos.get(0);
            String img3 = photos.get(1);

            Glide.with(ctx).load(img2).into(imgAracaIsFirstPhoto);
            Glide.with(ctx).load(img3).into(imgAracaIsSecondPhoto);
            imgAracaIsFirstPhotoChange.setVisibility(View.INVISIBLE);
            imgAracaIsSecondPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 3)
        {
            String img4 = photos.get(0);
            String img5 = photos.get(1);
            String img6 = photos.get(2);

            Glide.with(ctx).load(img4).into(imgAracaIsFirstPhoto);
            Glide.with(ctx).load(img5).into(imgAracaIsSecondPhoto);
            Glide.with(ctx).load(img6).into(imgAracaIsLastPhoto);

            imgAracaIsFirstPhotoChange.setVisibility(View.INVISIBLE);
            imgAracaIsSecondPhotoChange.setVisibility(View.INVISIBLE);
            imgAracaIsLastChange.setVisibility(View.INVISIBLE);
        }


        linAracaIsIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx , MainActivity.class);
                startActivity(intent);

            }
        });





        relAracaIsFirstPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgAracaIsFirstPhoto);
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

        relAracaIsSecondPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgAracaIsSecondPhoto);
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

        relAracaIsLastPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgAracaIsLastPhoto);
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

        Utils.setAutoCompleteAdapter(autoCompleteAracaIsAracDurumu , emptyList , ctx);


        Utils.setAutoCompleteAdapter(autoCompleteAracaIsil , cityNames ,ctx);
        Utils.setAutoCompleteAdapter(autoCompleteAracaIsServisBaslamaili , cityNames  ,ctx);
        Utils.setAutoCompleteAdapter(autoCompleteAracaIsKapasite , kapasiteNames , ctx);
        Utils.setAutoCompleteAdapter(autoCompleteAracaIsAracDurumu , aracDurumu , ctx);
        Utils.setAutoCompleteAdapter(autoCompleteAracaIsMarka , marka , ctx);


        autoCompleteAracaIsMarka.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvMarka = parent.getItemAtPosition(position).toString();
                actvMarka = DownloadClass.getMarkaIdWithName(actvMarka);
                Log.i("SelectedMarkaId" , actvMarka);

                model = DownloadClass.getModelNames(actvMarka);
                Utils.setAutoCompleteAdapter(autoCompleteAracaIsModel , model , ctx);
                autoCompleteAracaIsModel.setText("");

            }
        });

        autoCompleteAracaIsModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvModel = parent.getItemAtPosition(position).toString();
                actvModel = DownloadClass.getModelIdWithName(actvModel);

            }
        });

        autoCompleteAracaIsil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cityId = parent.getItemAtPosition(position).toString();
                cityId = DownloadClass.getCityIdWithName(cityId);
                Log.i("SelectedCity" , cityId);

                townNames = DownloadClass.getTownNames(cityId);
                autoCompleteAracaIsilce.setText(townNames.get(0));
                Utils.setAutoCompleteAdapter(autoCompleteAracaIsilce , townNames ,ctx);
            }
        });

        autoCompleteAracaIsilce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                townId = parent.getItemAtPosition(position).toString();
                townId = DownloadClass.getTownIdWithTownName(townId , cityId);
                Log.i("SelectedIlceId" , townId);
            }
        });

        autoCompleteAracaIsServisBaslamaili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                baslamaCityId = parent.getItemAtPosition(position).toString();
                baslamaCityId = DownloadClass.getCityIdWithName(baslamaCityId);
                Log.i("BaslamaCityId" , baslamaCityId);

                baslamaTownNames = DownloadClass.getTownNames(baslamaCityId);
                autoCompleteAracaIsServiseBaslamailce.setText(baslamaTownNames.get(0));
                Utils.setAutoCompleteAdapter(autoCompleteAracaIsServiseBaslamailce , baslamaTownNames , ctx);
            }
        });

        autoCompleteAracaIsServiseBaslamailce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                baslamaTownId = parent.getItemAtPosition(position).toString();
                baslamaTownId = DownloadClass.getTownIdWithTownName(baslamaTownId , baslamaCityId);

                Log.i("SelectedBaslamaIlce" , baslamaTownId);

            }
        });

        autoCompleteAracaIsKapasite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvKapasite = parent.getItemAtPosition(position).toString();
                actvKapasite = DownloadClass.getKapasiteIdWithName(actvKapasite);
                Log.i("SelectedKapasite" , actvKapasite);
            }
        });

        autoCompleteAracaIsAracDurumu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvAracDurumu = String.valueOf(position + 1);
                Log.i("SelectedDurum" , actvAracDurumu);

            }
        });

        edtAracaIsServisBaslamaSaati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog tpd = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        edtAracaIsServisBaslamaSaati.setText(hourOfDay + ":" + minute);

                    }
                },saat,dakika,true);

                tpd.setButton(TimePickerDialog.BUTTON_POSITIVE , "Seç" , tpd);
                tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE , "İptal" , tpd);
                tpd.show();

            }
        });

        edtAracaIsServisBitisSaati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog tpd = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        edtAracaIsServisBitisSaati.setText(hourOfDay + ":" + minute);

                    }
                },saat,dakika,true);

                tpd.setButton(TimePickerDialog.BUTTON_POSITIVE , "Seç" , tpd);
                tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE , "İptal" , tpd);
                tpd.show();

            }
        });


        txtAracaIsGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                txtAracaIsGonder.setClickable(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtAracaIsGonder.setClickable(true);
                    }
                },3000);

                baslik = edtAracaIsBaslik.getText().toString();
                fiyat = edtAracaIsFiyat.getText().toString();
                aciklama = edtAracaIsAciklama.getText().toString();
                yil = edtAracaIsYil.getText().toString();
                servisBaslamaSaati = edtAracaIsServisBaslamaSaati.getText().toString();
                servisBitisSaati = edtAracaIsServisBitisSaati.getText().toString();
                tecrube = edtAracaIsTecrube.getText().toString();
                plaka = edtAracaIsPlaka.getText().toString();
                referans = edtAracaIsPlaka.getText().toString();
                toplamKM = edtAracaIsToplamKM.getText().toString();
                gunSayisi = edtAracaIsCalisilacakGunSayisi.getText().toString();


                cityId = DownloadClass.getCityIdWithName(autoCompleteAracaIsil.getText().toString());
                townId = DownloadClass.getTownIdWithTownName(autoCompleteAracaIsilce.getText().toString() , cityId);
                baslamaCityId = DownloadClass.getCityIdWithName(autoCompleteAracaIsServisBaslamaili.getText().toString());
                baslamaTownId = DownloadClass.getTownIdWithTownName(autoCompleteAracaIsServiseBaslamailce.getText().toString() , baslamaCityId);
                actvMarka = DownloadClass.getMarkaIdWithName(autoCompleteAracaIsMarka.getText().toString());
                actvModel = DownloadClass.getModelIdWithName(autoCompleteAracaIsModel.getText().toString());
                actvKapasite = DownloadClass.getKapasiteIdWithName(autoCompleteAracaIsKapasite.getText().toString());
                actvAracDurumu = DownloadClass.getAracDurumuWithText(autoCompleteAracaIsAracDurumu.getText().toString());



                if(baslik.isEmpty() || aciklama.isEmpty() || yil.isEmpty() || servisBaslamaSaati.isEmpty() || servisBitisSaati.isEmpty() || tecrube.isEmpty() || plaka.isEmpty() || referans.isEmpty() || toplamKM.isEmpty() || gunSayisi.isEmpty() || cityId.isEmpty() || townId.isEmpty() || actvMarka.isEmpty() || actvModel.isEmpty() ||  actvKapasite.isEmpty() || baslamaCityId.isEmpty() || baslamaTownId.isEmpty() || actvAracDurumu.isEmpty())
                {
                    emptyDialog = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                    emptyDialog.setTitleText("* ile belirtilen tüm alanlar doldurulmalıdır");
                    emptyDialog.show();
                }

                else
                {
                    pDialog.show();


                    Log.i("YouMadeIt" , "Here");
                    if(switchAracaIsOkulTasiti.isChecked())
                    {
                        switchStates = "1|";
                    }
                    if(switchAracaIsTurizmPaketi.isChecked())
                    {
                        if(switchStates !=  null)
                        {
                            switchStates = switchStates.concat("2|");
                        }
                        else
                        {
                            switchStates = "2|";
                        }

                    }
                    if(switchAracaIsKlima.isChecked())
                    {
                        if(switchStates !=  null)
                        {
                            switchStates = switchStates.concat("3|");
                        }
                        else
                        {
                            switchStates = "3|";
                        }

                    }
                    if(switchAracaIsDeriDoseme.isChecked())
                    {
                        if(switchStates !=  null)
                        {
                            switchStates = switchStates.concat("4|");
                        }
                        else
                        {
                            switchStates = "4|";
                        }
                    }
                    if(switchAracaIsTribunTavan.isChecked())
                    {
                        if(switchStates !=  null)
                        {
                            switchStates = switchStates.concat("5|");
                        }
                        else
                        {
                            switchStates = "5|";
                        }
                    }
                    if(switchAracaIsYatarKoltuk.isChecked())
                    {
                        if(switchStates !=  null)
                        {
                            switchStates = switchStates.concat("6");
                        }
                        else
                        {
                            switchStates = "6";
                        }
                    }

                    if(switchStates != null)
                    {
                        switchStates = Utils.SwitchTrimmer(switchStates);
                    }

                    if(photos.size() != 0)
                    {
                        new ImageCompression(photos, ctx, new OnCompressTaskCompleted() {
                            @Override
                            public void onCompressTaskCompleted(ArrayList<String> compressedUris) {
                                new ImageCompressor(compressedUris, ctx, new OnCompressTaskCompleted() {
                                    @Override
                                    public void onCompressTaskCompleted(ArrayList<String> base64Photo) {
                                        imageArray = new String[base64Photo.size()];
                                        for(int i = 0; i < base64Photo.size(); i++)
                                        {
                                            imageArray[i] = base64Photo.get(i);
                                        }

                                        load();
                                    }
                                }).execute();
                            }
                        }).execute();
                    }
                    else{
                        load();
                    }
                }
            }
        });

        return rootView;
    }

    private void load() {
        HashMap<String , Object> hashMap = new HashMap<>();
        HashMap<String , Object> hashMap1 = new HashMap<>();

        hashMap1.put("Tipi" , "1");
        hashMap1.put("Baslik" , baslik);
        hashMap1.put("ilanCity" , cityId);
        hashMap1.put("ilanSemtleri" , townId);
        hashMap1.put("AracMarkasi" , actvMarka);
        hashMap1.put("AracModeli" , actvModel);
        hashMap1.put("AracYili" , yil);
        hashMap1.put("AracKapasitesi" , actvKapasite);
        hashMap1.put("AracOzellikleri" , switchStates);
        hashMap1.put("ServiseBaslamaCity" , baslamaCityId);
        hashMap1.put("ServiseBaslamaSemtleri" , baslamaTownId);
        hashMap1.put("ServiseBaslamaSaati" , servisBaslamaSaati);
        hashMap1.put("ServisBitisSaati" , servisBitisSaati);
        hashMap1.put("ToplamKM" , toplamKM);
        hashMap1.put("CalisilacakGunSayisi" , gunSayisi);
        hashMap1.put("Ucret" , fiyat);
        hashMap1.put("ilanAciklamasi" , aciklama);
        hashMap1.put("file" , imageArray);
        hashMap1.put("Tecrube" , tecrube);
        hashMap1.put("Plaka" , plaka);
        hashMap1.put("Referanslar" , referans);
        hashMap1.put("AracDurumu" , actvAracDurumu);

        hashMap.put("Token" , userToken);
        hashMap.put("param" , hashMap1);

        Log.i("hashMap" , hashMap.toString());


        Call<EkleResponse> ilanEkle = App.getApiService().ilanEkle(hashMap);

        ilanEkle.enqueue(new Callback<EkleResponse>() {

            @Override
            public void onResponse(Call<EkleResponse> call, Response<EkleResponse> response) {

                EkleResponseDetail detail = response.body().getDetail();

                Log.i("Response" , detail.toString());

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
                Log.i("Failure" , t.getMessage());
            }
        });
        switchStates = null;
        photos.clear();
        imageArray = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 6614){

            if(data != null)
            {
                ArrayList<String> imageList = data.getStringArrayListExtra("imageList");
                if(resultCode == 100){
                    Glide.with(ctx).load(imageList.get(0)).into(imgAracaIsFirstPhoto);
                    imgAracaIsFirstPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));
                }
                else if(resultCode == 200){
                    Glide.with(ctx).load(imageList.get(0)).into(imgAracaIsSecondPhoto);
                    imgAracaIsSecondPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
                else if(resultCode == 300){
                    Glide.with(ctx).load(imageList.get(0)).into(imgAracaIsLastPhoto);
                    imgAracaIsLastChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}