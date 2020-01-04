package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import serviscepde.com.tr.App;
import serviscepde.com.tr.DownloadClass;
import serviscepde.com.tr.GalleryActivity;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.IlanEkle.EkleResponse;
import serviscepde.com.tr.Models.IlanEkle.EkleResponseDetail;
import serviscepde.com.tr.Models.Ilce;
import serviscepde.com.tr.Models.Kapasite;
import serviscepde.com.tr.Models.MotorGuc;
import serviscepde.com.tr.Models.MotorHacim;
import serviscepde.com.tr.Models.Sehirler.SehirResponse;
import serviscepde.com.tr.Models.Sehirler.SehirResponseDetail;
import serviscepde.com.tr.R;
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


public class SatilikAracFragment extends Fragment {


    View generalView;

    private LinearLayout linSatilikAracIptal;

    private RelativeLayout relSatilikAracFirstPhoto,relSatilikAracSecondPhoto,relSatilikAracLastPhoto;

    private ImageView imgSatilikAracFirstPhoto,imgSatilikAracFirstPhotoChange,imgSatilikAracSecondPhoto,imgSatilikAracSecondPhotoChange,imgSatilikAracLastPhoto,imgSatilikAracLastChange;

    private TextInputEditText edtSatilikAracBaslik,edtSatilikAracFiyat,edtSatilikAracAciklama,edtSatilikAracAracYili,edtSatilikAracKM;

    private AutoCompleteTextView autoCompleteSatilikAracil,autoCompleteSatilikAracilce,autoCompleteSatilikAracMarka,autoCompleteSatilikAracModel,autoCompleteSatilikAracAltModel
            ,autoCompleteSatilikAracMotorHacmi,autoCompleteSatilikAracMotorGucu,autoCompleteSatilikAracKapasite,autoCompleteSatilikAracKimden;

    private TextView txtSatilikAracGonder;

    private ArrayList<String> photos = new ArrayList<>();
    private String []imageArray;

    private String baslik,fiyat,aciklama,yil,km,userToken,imagesString;
    private String actvMarka,actvModel,actvAltModel,actvMotorHacmi,actvMotorGucu,actvKapasite,actvKimden;

    private SweetAlertDialog emptyDialog;

    private Context ctx;


    private List<City> sehirler = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private List<String> marka = new ArrayList<>();
    private List<String> model = new ArrayList<>();
    private String cityId;
    private String townId;
    private ArrayList<String> townNames = new ArrayList<>();

    private List<Kapasite> kapasites = new ArrayList<>();
    private List<String> kapasiteNames = new ArrayList<>();
    private List<MotorGuc> gucs = new ArrayList<>();
    private List<String> gucNames = new ArrayList<>();
    private List<MotorHacim> hacims = new ArrayList<>();
    private List<String> hacimNames = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.satilik_arac_fragment, container, false);


        generalView = rootView;

        ctx = generalView.getContext();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        autoCompleteSatilikAracil = generalView.findViewById(R.id.autoCompleteSatilikAracil);
        autoCompleteSatilikAracilce = generalView.findViewById(R.id.autoCompleteSatilikAracilce);
        autoCompleteSatilikAracMarka = generalView.findViewById(R.id.autoCompleteSatilikAracMarka);
        autoCompleteSatilikAracModel = generalView.findViewById(R.id.autoCompleteSatilikAracModel);
        autoCompleteSatilikAracAltModel = generalView.findViewById(R.id.autoCompleteSatilikAracAltModel);
        autoCompleteSatilikAracMotorHacmi = generalView.findViewById(R.id.autoCompleteSatilikAracMotorHacmi);
        autoCompleteSatilikAracMotorGucu = generalView.findViewById(R.id.autoCompleteSatilikAracMotorGucu);
        autoCompleteSatilikAracKapasite = generalView.findViewById(R.id.autoCompleteSatilikAracKapasite);
        autoCompleteSatilikAracKimden = generalView.findViewById(R.id.autoCompleteSatilikAracKimden);


        edtSatilikAracBaslik = generalView.findViewById(R.id.edtSatilikAracBaslik);
        edtSatilikAracFiyat = generalView.findViewById(R.id.edtSatilikAracFiyat);
        edtSatilikAracAciklama = generalView.findViewById(R.id.edtSatilikAracAciklama);
        edtSatilikAracAracYili = generalView.findViewById(R.id.edtSatilikAracAracYili);
        edtSatilikAracKM = generalView.findViewById(R.id.edtSatilikAracKM);


        imgSatilikAracFirstPhoto = generalView.findViewById(R.id.imgSatilikAracFirstPhoto);
        imgSatilikAracFirstPhotoChange = generalView.findViewById(R.id.imgSatilikAracFirstPhotoChange);
        imgSatilikAracSecondPhoto = generalView.findViewById(R.id.imgSatilikAracSecondPhoto);
        imgSatilikAracSecondPhotoChange = generalView.findViewById(R.id.imgSatilikAracSecondPhotoChange);
        imgSatilikAracLastPhoto = generalView.findViewById(R.id.imgSatilikAracLastPhoto);
        imgSatilikAracLastChange = generalView.findViewById(R.id.imgSatilikAracLastChange);


        relSatilikAracFirstPhoto = generalView.findViewById(R.id.relSatilikAracFirstPhoto);
        relSatilikAracSecondPhoto = generalView.findViewById(R.id.relSatilikAracSecondPhoto);
        relSatilikAracLastPhoto = generalView.findViewById(R.id.relSatilikAracLastPhoto);


        linSatilikAracIptal = generalView.findViewById(R.id.linSatilikAracIptal);
        txtSatilikAracGonder = generalView.findViewById(R.id.txtSatilikAracGonder);

        if(photos.size() == 1)
        {
            String img1 = photos.get(0);
            Glide.with(ctx).load(img1).into(imgSatilikAracFirstPhoto);
            imgSatilikAracFirstPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 2)
        {
            String img2 = photos.get(0);
            String img3 = photos.get(1);

            Glide.with(ctx).load(img2).into(imgSatilikAracFirstPhoto);
            Glide.with(ctx).load(img3).into(imgSatilikAracSecondPhoto);
            imgSatilikAracSecondPhotoChange.setVisibility(View.INVISIBLE);
            imgSatilikAracFirstPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 3)
        {
            String img4 = photos.get(0);
            String img5 = photos.get(1);
            String img6 = photos.get(2);

            Glide.with(ctx).load(img4).into(imgSatilikAracFirstPhoto);
            Glide.with(ctx).load(img5).into(imgSatilikAracSecondPhoto);
            Glide.with(ctx).load(img6).into(imgSatilikAracLastPhoto);

            imgSatilikAracLastChange.setVisibility(View.INVISIBLE);
            imgSatilikAracSecondPhotoChange.setVisibility(View.INVISIBLE);
            imgSatilikAracFirstPhotoChange.setVisibility(View.INVISIBLE);
        }

        linSatilikAracIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx , MainActivity.class);
                startActivity(intent);
            }
        });

        relSatilikAracFirstPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgSatilikAracFirstPhoto);
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
        relSatilikAracSecondPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgSatilikAracSecondPhoto);
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
        relSatilikAracLastPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgSatilikAracLastPhoto);
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
        gucs = DownloadClass.getMotorGuc();
        gucNames = DownloadClass.getGucNames();
        hacims = DownloadClass.getMotorHacim();
        hacimNames = DownloadClass.getHacimNames();



        Utils.setAutoCompleteAdapter(autoCompleteSatilikAracil , cityNames , ctx);
        Utils.setAutoCompleteAdapter(autoCompleteSatilikAracKapasite , kapasiteNames ,ctx);
        Utils.setAutoCompleteAdapter(autoCompleteSatilikAracMotorGucu , gucNames ,ctx);
        Utils.setAutoCompleteAdapter(autoCompleteSatilikAracMotorHacmi , hacimNames ,ctx);
        Utils.setAutoCompleteAdapter(autoCompleteSatilikAracKimden ,App.getKimden() ,ctx);
        Utils.setAutoCompleteAdapter(autoCompleteSatilikAracMarka , marka  , ctx);


        autoCompleteSatilikAracMarka.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvMarka = parent.getItemAtPosition(position).toString();
                actvMarka = DownloadClass.getMarkaIdWithName(actvMarka);
                Log.i("SelectedMarkaId" , actvMarka);

                model = DownloadClass.getModelNames(actvMarka);
                Utils.setAutoCompleteAdapter(autoCompleteSatilikAracModel , model , ctx);
                autoCompleteSatilikAracModel.setText("");
            }
        });

        autoCompleteSatilikAracModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvModel = parent.getItemAtPosition(position).toString();
                actvModel = DownloadClass.getModelIdWithName(actvModel);
            }
        });

        autoCompleteSatilikAracil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cityId = parent.getItemAtPosition(position).toString();
                cityId = DownloadClass.getCityIdWithName(cityId);
                Log.i("SelectedCityId" , cityId);

                townNames = DownloadClass.getTownNames(cityId);
                autoCompleteSatilikAracilce.setText(townNames.get(0));
                Utils.setAutoCompleteAdapter(autoCompleteSatilikAracilce , townNames , ctx);
            }
        });

        autoCompleteSatilikAracilce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                townId = parent.getItemAtPosition(position).toString();
                townId = DownloadClass.getTownIdWithTownName(townId , cityId);
                Log.i("SelectedIlceId" , townId);


            }
        });

        autoCompleteSatilikAracKapasite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actvKapasite = parent.getItemAtPosition(position).toString();
                actvKapasite = DownloadClass.getKapasiteIdWithName(actvKapasite);
            }
        });
        autoCompleteSatilikAracMotorGucu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvMotorGucu = parent.getItemAtPosition(position).toString();
                actvMotorGucu = DownloadClass.getGucIdWithName(actvMotorGucu);
                Log.i("MotorGucu" , actvMotorGucu);
            }
        });
        autoCompleteSatilikAracMotorHacmi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actvMotorHacmi = parent.getItemAtPosition(position).toString();
                actvMotorHacmi = DownloadClass.getHacimIdWithName(actvMotorHacmi);
                Log.i("MotorHacim" , actvMotorHacmi);
            }
        });
        autoCompleteSatilikAracKimden.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actvKimden = String.valueOf(position + 1);
            }
        });


        txtSatilikAracGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtSatilikAracGonder.setClickable(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtSatilikAracGonder.setClickable(true);
                    }
                },3000);

                baslik = edtSatilikAracBaslik.getText().toString();
                fiyat = edtSatilikAracFiyat.getText().toString();
                aciklama = edtSatilikAracAciklama.getText().toString();
                yil = edtSatilikAracAracYili.getText().toString();
                km = edtSatilikAracKM.getText().toString();


                if(baslik.isEmpty()  || aciklama.isEmpty() || yil.isEmpty() || km.isEmpty() || cityId.isEmpty() || autoCompleteSatilikAracilce.getText().toString().isEmpty() || actvMarka.isEmpty() || actvModel.isEmpty() || actvMotorHacmi.isEmpty() || actvMotorGucu.isEmpty() || actvKapasite.isEmpty() || actvKimden.isEmpty())
                {
                    emptyDialog = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                    emptyDialog.setTitleText("* ile belirtilen tüm alanlar doldurulmalıdır");
                    emptyDialog.show();
                }

                else
                {

                    if(photos.size() != 0)
                    {
                        ArrayList<String> base64Photo = Utils.pathToBase64(photos);
                        imageArray = new String[base64Photo.size()];

                        for(int i = 0; i < base64Photo.size(); i++)
                        {
                            imageArray[i] = base64Photo.get(i);
                        }

                    }


                    HashMap<String , Object> hashMap = new HashMap<>();
                    HashMap<String , Object> hashMap1 = new HashMap<>();

                    hashMap1.put("Tipi" , "5");
                    hashMap1.put("Baslik" , baslik);
                    hashMap1.put("Ucret" , fiyat);
                    hashMap1.put("ilanAciklamasi" , aciklama);
                    hashMap1.put("AracYili" , yil);
                    hashMap1.put("ToplamKM" , km);
                    hashMap1.put("ilanCity" , cityId);
                    hashMap1.put("ilanSemtleri" , DownloadClass.getTownIdWithTownName(autoCompleteSatilikAracilce.getText().toString() , cityId));
                    hashMap1.put("AracMarkasi" , actvMarka);
                    hashMap1.put("AracModeli" , actvModel);
                    hashMap1.put("MotorHacmi" , actvMotorHacmi);
                    hashMap1.put("MotorGucu" , actvMotorGucu);
                    hashMap1.put("AracKapasitesi" , actvKapasite);
                    hashMap1.put("file" , imageArray);
                    hashMap1.put("Kimden" , actvKimden);
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
                                    ilanHata = new SweetAlertDialog(ctx , SweetAlertDialog.ERROR_TYPE);
                                    ilanHata.setTitleText("Bir sorunla karşılaştık lütfen daha sonra tekrar deneyin");
                                    ilanHata.show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }

                        @Override
                        public void onFailure(Call<EkleResponse> call, Throwable t) {

                            Log.i("Failure" , t.getMessage());

                        }
                    });
                }

            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if(requestCode == 6614){
            if(data != null)
            {
                ArrayList<String> imageList = data.getStringArrayListExtra("imageList");
                if(resultCode == 100){
                    Glide.with(ctx).load(imageList.get(0)).into(imgSatilikAracFirstPhoto);
                    imgSatilikAracFirstPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
                else if(resultCode == 200){
                    Glide.with(ctx).load(imageList.get(0)).into(imgSatilikAracSecondPhoto);
                    imgSatilikAracSecondPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
                else if(resultCode == 300){
                    Glide.with(ctx).load(imageList.get(0)).into(imgSatilikAracLastPhoto);
                    imgSatilikAracLastChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}