package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import serviscepde.com.tr.App;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.IlanEkle.EkleResponse;
import serviscepde.com.tr.Models.IlanEkle.EkleResponseDetail;
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


public class KiralikAracFragment extends Fragment {


    View generalView;

    private LinearLayout linKiralikAracIptal;

    private RelativeLayout relKiralikAracLastPhoto,relKiralikAracSecondPhoto,relKiralikAracFirstPhoto;

    private ImageView imgKiralikAracLastChange,imgKiralikAracLastPhoto,imgKiralikAracSecondPhotoChange,imgKiralikAracSecondPhoto,imgKiralikAracFirstPhotoChange,imgKiralikAracFirstPhoto;

    private TextInputEditText edtKiralikAracAylikFiyat,edtKiralikAracHaftalikFiyat,edtKiralikAracAracYili,edtKiralikAracAciklama,edtKiralikAracFiyat,edtKiralikAracBaslik;

    private AutoCompleteTextView autoCompleteKiralikAracKasko,autoCompleteKiralikAracKapasite,autoCompleteKiralikAracYakitTipi,autoCompleteKiralikAracVitesTipi,
            autoCompleteKiralikAracModel,autoCompleteKiralikAracMarka,autoCompleteKiralikAracilce,autoCompleteKiralikAracil;
    private TextView txtKiralikAracGonder;

    private ArrayList<String> photos = new ArrayList<>();
    private List<City> tmpCity = new ArrayList<>();
    private List<String> sehirListesi = new ArrayList<>();

    private String baslik,fiyat,aciklama,yil,haftalikFiyat,aylikFiyat,imageString,userToken;
    private String actvKasko,actvKapasite,actvYakitTipi,actvVitesTipi,actvModel,actvMarka,actvil,actvilce;

    private SweetAlertDialog emptyDialog;;

    Context ctx;

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
        autoCompleteKiralikAracYakitTipi = generalView.findViewById(R.id.autoCompleteKiralikAracYakitTipi);
        autoCompleteKiralikAracVitesTipi = generalView.findViewById(R.id.autoCompleteKiralikAracVitesTipi);
        autoCompleteKiralikAracModel = generalView.findViewById(R.id.autoCompleteKiralikAracModel);
        autoCompleteKiralikAracMarka = generalView.findViewById(R.id.autoCompleteKiralikAracMarka);
        autoCompleteKiralikAracilce = generalView.findViewById(R.id.autoCompleteKiralikAracilce);
        autoCompleteKiralikAracil = generalView.findViewById(R.id.autoCompleteKiralikAracil);


        edtKiralikAracAylikFiyat = generalView.findViewById(R.id.edtKiralikAracAylikFiyat);
        edtKiralikAracHaftalikFiyat = generalView.findViewById(R.id.edtKiralikAracHaftalikFiyat);
        edtKiralikAracAracYili = generalView.findViewById(R.id.edtKiralikAracAracYili);
        edtKiralikAracAciklama = generalView.findViewById(R.id.edtKiralikAracAciklama);
        edtKiralikAracFiyat = generalView.findViewById(R.id.edtKiralikAracFiyat);
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

        Call<SehirResponse> sehirResponseCall = App.getApiService().getSehirler();
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

        Utils.setAutoCompleteAdapter(autoCompleteKiralikAracil , sehirListesi , ctx );
        Utils.setAutoCompleteAdapter(autoCompleteKiralikAracKapasite , App.getKapasite() , ctx);
        Utils.setAutoCompleteAdapter(autoCompleteKiralikAracKasko, App.getKaskoTuru() , ctx);
        Utils.setAutoCompleteAdapter(autoCompleteKiralikAracVitesTipi , App.getVitesTipi() ,ctx);
        Utils.setAutoCompleteAdapter(autoCompleteKiralikAracYakitTipi , App.getYakitTipi() , ctx);

        autoCompleteKiralikAracil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvil = String.valueOf(position + 1);
            }
        });

        autoCompleteKiralikAracKapasite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvKapasite = String.valueOf(position + 1);
            }
        });

        autoCompleteKiralikAracKasko.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvKasko = String.valueOf(position + 1);
            }
        });

        autoCompleteKiralikAracVitesTipi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvVitesTipi = String.valueOf(position + 1);
            }
        });

        autoCompleteKiralikAracYakitTipi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvYakitTipi = String.valueOf(position + 1);
            }
        });

        txtKiralikAracGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baslik = edtKiralikAracBaslik.getText().toString();
                fiyat = edtKiralikAracFiyat.getText().toString();
                aciklama = edtKiralikAracAciklama.getText().toString();
                yil = edtKiralikAracAracYili.getText().toString();
                haftalikFiyat = edtKiralikAracHaftalikFiyat.getText().toString();
                aylikFiyat = edtKiralikAracAylikFiyat.getText().toString();

                if(baslik.isEmpty() || fiyat.isEmpty() || aciklama.isEmpty() || yil.isEmpty() || actvKasko.isEmpty() || actvKapasite.isEmpty() || actvYakitTipi.isEmpty() ||
                        actvVitesTipi.isEmpty() || actvModel.isEmpty() || actvMarka.isEmpty() || actvil.isEmpty() || actvilce.isEmpty())
                {
                    emptyDialog = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                    emptyDialog.setTitleText("* ile belirtilen tüm alanlar doldurulmalıdır");
                    emptyDialog.show();

                }
                else
                {

                    ArrayList<String> base64Photo = Utils.pathToBase64(photos);
                    imageString = Utils.imageToString(base64Photo);
                    imageString = Utils.trimmer(imageString);

                    HashMap<String , Object> hashMap = new HashMap<>();
                    HashMap<String , String> hashMap1 = new HashMap<>();

                    hashMap1.put("Tipi" , "6");
                    hashMap1.put("Baslik" , baslik);
                    hashMap1.put("ilanCity" , actvil);
                    hashMap1.put("ilanSemtleri" , "10");
                    hashMap1.put("AracMarkasi" , "3");
                    hashMap1.put("AracModeli" , "4");
                    hashMap1.put("AracYili" , yil);
                    hashMap1.put("AracKapasitesi" , actvKapasite);
                    hashMap1.put("Ucret" , fiyat);
                    hashMap1.put("file" , imageString);
                    hashMap1.put("HaftalikFiyat" , haftalikFiyat);
                    hashMap1.put("AylikFiyat" , aylikFiyat);
                    hashMap1.put("VitesTipi" , actvVitesTipi);
                    hashMap1.put("YakitTipi" , actvYakitTipi);
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

                            Log.i("Failure" , t.getMessage());

                        }
                    });

                }

            }
        });












        return rootView;
    }
}