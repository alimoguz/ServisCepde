package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;

import me.abhinay.input.CurrencyEditText;
import me.abhinay.input.CurrencySymbols;
import serviscepde.com.tr.App;
import serviscepde.com.tr.DownloadClass;
import serviscepde.com.tr.GalleryActivity;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.IlanEkle.EkleResponse;
import serviscepde.com.tr.Models.IlanEkle.EkleResponseDetail;
import serviscepde.com.tr.Models.Kapasite;
import serviscepde.com.tr.Models.Sehirler.SehirResponse;
import serviscepde.com.tr.Models.Sehirler.SehirResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.ImageCompression;
import serviscepde.com.tr.Utils.ImageCompressor;
import serviscepde.com.tr.Utils.OnCompressTaskCompleted;
import serviscepde.com.tr.Utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

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


public class KiralikAracFragment extends Fragment {


    View generalView;

    private LinearLayout linKiralikAracIptal;

    private RelativeLayout relKiralikAracLastPhoto,relKiralikAracSecondPhoto,relKiralikAracFirstPhoto;

    private ImageView imgKiralikAracLastChange,imgKiralikAracLastPhoto,imgKiralikAracSecondPhotoChange,imgKiralikAracSecondPhoto,imgKiralikAracFirstPhotoChange,imgKiralikAracFirstPhoto;

    private TextInputEditText edtKiralikAracHaftalikFiyat,edtKiralikAracAracYili,edtKiralikAracAciklama,edtKiralikAracBaslik;

    private CurrencyEditText edtKiralikAracFiyat,edtKiralikAracAylikFiyat;

    private AutoCompleteTextView autoCompleteKiralikAracKasko,autoCompleteKiralikAracKapasite,autoCompleteKiralikAracYakitTipi,autoCompleteKiralikAracVitesTipi,
            autoCompleteKiralikAracModel,autoCompleteKiralikAracMarka,autoCompleteKiralikAracilce,autoCompleteKiralikAracil;
    private TextView txtKiralikAracGonder;

    private ArrayList<String> photos = new ArrayList<>();
    private List<City> tmpCity = new ArrayList<>();
    private List<String> sehirListesi = new ArrayList<>();

    private String baslik,fiyat,aciklama,yil,aylikFiyat,imageString,userToken;
    private String actvKasko,actvKapasite,actvModel,actvMarka;

    private SweetAlertDialog emptyDialog;

    private String [] imageArray;

    private List<City> sehirler = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private List<String> marka = new ArrayList<>();
    private List<String> model = new ArrayList<>();
    private String cityId;
    private String townId;
    private ArrayList<String> townNames = new ArrayList<>();
    private ArrayList<String> kaskoList = new ArrayList<>();

    private List<Kapasite> kapasites = new ArrayList<>();
    private List<String> kapasiteNames = new ArrayList<>();
    private SweetAlertDialog pDialog;

    private Context ctx;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.kiralik_arac_fragment, container, false);

        photos = getArguments().getStringArrayList("photoList");

        generalView = rootView;

        ctx = generalView.getContext();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        autoCompleteKiralikAracKasko = generalView.findViewById(R.id.autoCompleteKiralikAracKasko);
        autoCompleteKiralikAracKapasite = generalView.findViewById(R.id.autoCompleteKiralikAracKapasite);
        autoCompleteKiralikAracModel = generalView.findViewById(R.id.autoCompleteKiralikAracModel);
        autoCompleteKiralikAracMarka = generalView.findViewById(R.id.autoCompleteKiralikAracMarka);
        autoCompleteKiralikAracilce = generalView.findViewById(R.id.autoCompleteKiralikAracilce);
        autoCompleteKiralikAracil = generalView.findViewById(R.id.autoCompleteKiralikAracil);


        edtKiralikAracAylikFiyat = generalView.findViewById(R.id.edtKiralikAracAylikFiyat);
        edtKiralikAracAracYili = generalView.findViewById(R.id.edtKiralikAracAracYili);
        edtKiralikAracAciklama = generalView.findViewById(R.id.edtKiralikAracAciklama);
        edtKiralikAracFiyat = generalView.findViewById(R.id.edtKiralikAracFiyat);
        edtKiralikAracFiyat.setCurrency(CurrencySymbols.NONE);
        edtKiralikAracBaslik = generalView.findViewById(R.id.edtKiralikAracBaslik);


        imgKiralikAracLastChange = generalView.findViewById(R.id.imgKiralikAracLastChange);
        imgKiralikAracLastPhoto = generalView.findViewById(R.id.imgKiralikAracLastPhoto);
        imgKiralikAracSecondPhotoChange = generalView.findViewById(R.id.imgKiralikAracSecondPhotoChange);
        imgKiralikAracSecondPhoto = generalView.findViewById(R.id.imgKiralikAracSecondPhoto);
        imgKiralikAracFirstPhotoChange = generalView.findViewById(R.id.imgKiralikAracFirstPhotoChange);
        imgKiralikAracFirstPhoto = generalView.findViewById(R.id.imgKiralikAracFirstPhoto);


        relKiralikAracLastPhoto = generalView.findViewById(R.id.relKiralikAracLastPhoto);
        relKiralikAracSecondPhoto = generalView.findViewById(R.id.relKiralikAracSecondPhoto);
        relKiralikAracFirstPhoto = generalView.findViewById(R.id.relKiralikAracFirstPhoto);

        linKiralikAracIptal = generalView.findViewById(R.id.linKiralikAracIptal);
        txtKiralikAracGonder = generalView.findViewById(R.id.txtKiralikAracGonder);

        pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f1a400"));
        pDialog.setTitleText("Lütfen Bekleyiniz");
        pDialog.setCancelable(false);


        if(photos.size() == 1)
        {
            String img1 = photos.get(0);
            Glide.with(ctx).load(img1).into(imgKiralikAracFirstPhoto);
            imgKiralikAracFirstPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 2)
        {
            String img2 = photos.get(0);
            String img3 = photos.get(1);

            Glide.with(ctx).load(img2).into(imgKiralikAracFirstPhoto);
            Glide.with(ctx).load(img3).into(imgKiralikAracSecondPhoto);
            imgKiralikAracSecondPhotoChange.setVisibility(View.INVISIBLE);
            imgKiralikAracFirstPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 3)
        {
            String img4 = photos.get(0);
            String img5 = photos.get(1);
            String img6 = photos.get(2);

            Glide.with(ctx).load(img4).into(imgKiralikAracFirstPhoto);
            Glide.with(ctx).load(img5).into(imgKiralikAracSecondPhoto);
            Glide.with(ctx).load(img6).into(imgKiralikAracLastPhoto);

            imgKiralikAracLastChange.setVisibility(View.INVISIBLE);
            imgKiralikAracSecondPhotoChange.setVisibility(View.INVISIBLE);
            imgKiralikAracFirstPhotoChange.setVisibility(View.INVISIBLE);
        }

        linKiralikAracIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx , MainActivity.class);
                startActivity(intent);
            }
        });

        relKiralikAracFirstPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgKiralikAracFirstPhoto);
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

        relKiralikAracSecondPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgKiralikAracSecondPhoto);
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

        relKiralikAracLastPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgKiralikAracLastPhoto);
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
        kaskoList = DownloadClass.getKaskoList();

        Utils.setAutoCompleteAdapter(autoCompleteKiralikAracil , cityNames , ctx );
        Utils.setAutoCompleteAdapter(autoCompleteKiralikAracKapasite , kapasiteNames , ctx);
        Utils.setAutoCompleteAdapter(autoCompleteKiralikAracKasko, kaskoList , ctx);
        Utils.setAutoCompleteAdapter(autoCompleteKiralikAracMarka , marka , ctx);


        autoCompleteKiralikAracMarka.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvMarka = parent.getItemAtPosition(position).toString();
                actvMarka = DownloadClass.getMarkaIdWithName(actvMarka);
                Log.i("SelectedMarkaId" , actvMarka);

                model = DownloadClass.getModelNames(actvMarka);
                Utils.setAutoCompleteAdapter(autoCompleteKiralikAracModel , model , ctx);
                autoCompleteKiralikAracModel.setText("");
            }
        });

        autoCompleteKiralikAracModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvModel = parent.getItemAtPosition(position).toString();
                actvModel = DownloadClass.getModelIdWithName(actvModel);


            }
        });

        autoCompleteKiralikAracil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityId = parent.getItemAtPosition(position).toString();
                cityId = DownloadClass.getCityIdWithName(cityId);
                Log.i("SelectedIlId" , cityId);

                townNames = DownloadClass.getTownNames(cityId);
                autoCompleteKiralikAracilce.setText(townNames.get(0));
                Utils.setAutoCompleteAdapter(autoCompleteKiralikAracilce , townNames , ctx);
            }
        });


        autoCompleteKiralikAracilce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                townId = parent.getItemAtPosition(position).toString();
                townId = DownloadClass.getTownIdWithTownName(townId , cityId);
                Log.i("SelectedIlceId" , townId);

            }
        });

        autoCompleteKiralikAracKapasite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvKapasite = parent.getItemAtPosition(position).toString();
                actvKapasite = DownloadClass.getKapasiteIdWithName(actvKapasite);
            }
        });

        autoCompleteKiralikAracKasko.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvKasko = String.valueOf(position + 1);
            }
        });




        txtKiralikAracGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtKiralikAracGonder.setClickable(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtKiralikAracGonder.setClickable(true);
                    }
                },3000);


                boolean isFiyatNull = edtKiralikAracFiyat.getText().toString().isEmpty();
                if(isFiyatNull)
                {
                    fiyat = "0";
                }
                else
                {
                    fiyat = String.valueOf(edtKiralikAracFiyat.getCleanDoubleValue());
                }


                boolean isAylikNull = edtKiralikAracAylikFiyat.getText().toString().isEmpty();
                if(isAylikNull)
                {
                    aylikFiyat = "0";
                }
                else
                {
                    aylikFiyat = String.valueOf(edtKiralikAracAylikFiyat.getCleanDoubleValue());
                }

                baslik = edtKiralikAracBaslik.getText().toString();
                aciklama = edtKiralikAracAciklama.getText().toString();
                yil = edtKiralikAracAracYili.getText().toString();

                cityId = DownloadClass.getCityIdWithName(autoCompleteKiralikAracil.getText().toString());
                townId = DownloadClass.getTownIdWithTownName(autoCompleteKiralikAracilce.getText().toString() , cityId);
                actvMarka = DownloadClass.getMarkaIdWithName(autoCompleteKiralikAracMarka.getText().toString());
                actvModel = DownloadClass.getModelIdWithName(autoCompleteKiralikAracModel.getText().toString());
                actvKapasite = DownloadClass.getKapasiteIdWithName(autoCompleteKiralikAracKapasite.getText().toString());
                actvKasko = DownloadClass.getKaskoWithText(autoCompleteKiralikAracKasko.getText().toString());

                if(baslik.isEmpty()  || aciklama.isEmpty() || yil.isEmpty() || actvKasko.isEmpty() || actvKapasite.isEmpty() || actvModel.isEmpty() || actvMarka.isEmpty() || cityId.isEmpty() || townId.isEmpty())
                {
                    emptyDialog = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                    emptyDialog.setTitleText("* ile belirtilen tüm alanlar doldurulmalıdır");
                    emptyDialog.show();

                }
                else
                {
                    pDialog.show();

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

        hashMap1.put("Tipi" , "6");
        hashMap1.put("Baslik" , baslik);
        hashMap1.put("ilanCity" , cityId);
        hashMap1.put("ilanSemtleri" , townId);
        hashMap1.put("AracMarkasi" , actvMarka);
        hashMap1.put("AracModeli" , actvModel);
        hashMap1.put("AracYili" , yil);
        hashMap1.put("AracKapasitesi" , actvKapasite);
        hashMap1.put("Ucret" , fiyat);
        hashMap1.put("file" , imageArray);
        hashMap1.put("AylikFiyat" , aylikFiyat);
        hashMap1.put("Kasko" , actvKasko);
        hashMap1.put("ilanAciklamasi" , aciklama);

        hashMap.put("Token" , userToken);
        hashMap.put("param" , hashMap1);

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


                photos.clear();

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

                    if(ekleResponse.getJSONObject("errorEmpty") != null)
                    {
                        pDialog.dismiss();

                        ilanHata = new SweetAlertDialog(ctx , SweetAlertDialog.ERROR_TYPE);
                        ilanHata.setTitleText(ekleResponse.getJSONObject("errorEmpty").toString());
                        ilanHata.show();
                    }
                    if(ekleResponse.getJSONObject("errorOther") !=null)
                    {
                        pDialog.dismiss();
                        ilanHata = new SweetAlertDialog(ctx , SweetAlertDialog.ERROR_TYPE);
                        ilanHata.setTitleText(ekleResponse.getJSONObject("errorOther").toString());
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
                    Glide.with(ctx).load(imageList.get(0)).into(imgKiralikAracFirstPhoto);
                    imgKiralikAracFirstPhotoChange.setVisibility(View.INVISIBLE);
                }
                else if(resultCode == 200){
                    Glide.with(ctx).load(imageList.get(0)).into(imgKiralikAracSecondPhoto);
                    imgKiralikAracSecondPhotoChange.setVisibility(View.INVISIBLE);
                }
                else if(resultCode == 300){
                    Glide.with(ctx).load(imageList.get(0)).into(imgKiralikAracLastPhoto);
                    imgKiralikAracLastChange.setVisibility(View.INVISIBLE);
                }
            }


        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}