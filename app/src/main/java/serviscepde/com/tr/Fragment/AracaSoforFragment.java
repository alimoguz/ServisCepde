package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import serviscepde.com.tr.Models.Sehirler.SehirResponse;
import serviscepde.com.tr.Models.Sehirler.SehirResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AracaSoforFragment extends Fragment {


    View generalView;

    private TextInputEditText edtAracaSoforBaslik,edtAracaSoforFiyat,edtAracaSoforAciklama,edtAracaSoforServisBaslamaSaati,edtAracaSoforServisBitisSaati,
            edtAracaSoforFirmaGirisSaati,edtAracaSoforFirmadanCikisSaati,edtAracaSoforTecrube,edtAracaSoforCalisilacakGunSayisi;

    private AutoCompleteTextView autoCompleteAracaSoforil,autoCompleteAracaSoforilce,autoCompleteAracaSoforKapasite,autoCompleteAracaSoforServisBaslamaili,
            autoCompleteAracaSoforServiseBaslamailce,autoCompleteAracaSoforServisBitisili,autoCompleteAracaSoforServisBitisilce;

    private RelativeLayout relAracaSoforFirstPhoto,relAracaSoforSecondPhoto,relAracaSoforLastPhoto;
    private ImageView imgAracaSoforFirstPhoto,imgAracaSoforFirstPhotoChange,imgAracaSoforSecondPhoto,imgAracaSoforSecondPhotoChange,imgAracaSoforLastPhoto,imgAracaSoforLastChange;
    private LinearLayout linAracaSoforIptal;
    private TextView txtAracaSoforGonder;

    private ArrayList<String> photos = new ArrayList<>();
    private String baslik,fiyat,aciklama,servisBaslamaSaati,servisBitisSaati,firmaGirisSaati,firmadanCikisSaati,tecrube,gun,imagesString;
    private String actvil,actvilce,actvKapasite,actvServiseBaslamaili,actvServiseBaslamailce,actvServisBitisili,actvServisBitisilce;
    private Context ctx;

    private List<City> tmpCity = new ArrayList<>();
    private List<String> sehirListesi = new ArrayList<>();

    private SweetAlertDialog emptyDialog;

    private String userToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.araca_sofor_fragment, container, false);

        photos = getArguments().getStringArrayList("photoList");

        generalView = rootView;

        ctx = generalView.getContext();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");

        edtAracaSoforBaslik = generalView.findViewById(R.id.edtAracaSoforBaslik);
        edtAracaSoforFiyat = generalView.findViewById(R.id.edtAracaSoforFiyat);
        edtAracaSoforAciklama = generalView.findViewById(R.id.edtAracaSoforAciklama);
        edtAracaSoforServisBaslamaSaati = generalView.findViewById(R.id.edtAracaSoforServisBaslamaSaati);
        edtAracaSoforServisBitisSaati = generalView.findViewById(R.id.edtAracaSoforServisBitisSaati);
        edtAracaSoforFirmaGirisSaati = generalView.findViewById(R.id.edtAracaSoforFirmaGirisSaati);
        edtAracaSoforFirmadanCikisSaati = generalView.findViewById(R.id.edtAracaSoforFirmadanCikisSaati);
        edtAracaSoforTecrube = generalView.findViewById(R.id.edtAracaSoforTecrube);
        edtAracaSoforCalisilacakGunSayisi = generalView.findViewById(R.id.edtAracaSoforCalisilacakGunSayisi);


        autoCompleteAracaSoforil = generalView.findViewById(R.id.autoCompleteAracaSoforil);
        autoCompleteAracaSoforilce = generalView.findViewById(R.id.autoCompleteAracaSoforilce);
        autoCompleteAracaSoforKapasite = generalView.findViewById(R.id.autoCompleteAracaSoforKapasite);
        autoCompleteAracaSoforServisBaslamaili = generalView.findViewById(R.id.autoCompleteAracaSoforServisBaslamaili);
        autoCompleteAracaSoforServiseBaslamailce = generalView.findViewById(R.id.autoCompleteAracaSoforServiseBaslamailce);
        autoCompleteAracaSoforServisBitisili = generalView.findViewById(R.id.autoCompleteAracaSoforServisBitisili);
        autoCompleteAracaSoforServisBitisilce = generalView.findViewById(R.id.autoCompleteAracaSoforServisBitisilce);


        relAracaSoforFirstPhoto = generalView.findViewById(R.id.relAracaSoforFirstPhoto);
        relAracaSoforSecondPhoto = generalView.findViewById(R.id.relAracaSoforSecondPhoto);
        relAracaSoforLastPhoto = generalView.findViewById(R.id.relAracaSoforLastPhoto);


        imgAracaSoforFirstPhoto = generalView.findViewById(R.id.imgAracaSoforFirstPhoto);
        imgAracaSoforFirstPhotoChange = generalView.findViewById(R.id.imgAracaSoforFirstPhotoChange);
        imgAracaSoforSecondPhoto = generalView.findViewById(R.id.imgAracaSoforSecondPhoto);
        imgAracaSoforSecondPhotoChange = generalView.findViewById(R.id.imgAracaSoforSecondPhotoChange);
        imgAracaSoforLastPhoto = generalView.findViewById(R.id.imgAracaSoforLastPhoto);
        imgAracaSoforLastChange = generalView.findViewById(R.id.imgAracaSoforLastChange);


        linAracaSoforIptal = generalView.findViewById(R.id.linAracaSoforIptal);
        txtAracaSoforGonder = generalView.findViewById(R.id.txtAracaSoforGonder);

        if(photos.size() == 1)
        {
            String img1 = photos.get(0);
            Glide.with(ctx).load(img1).into(imgAracaSoforFirstPhoto);
            imgAracaSoforFirstPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 2)
        {
            String img2 = photos.get(0);
            String img3 = photos.get(1);

            Glide.with(ctx).load(img2).into(imgAracaSoforFirstPhoto);
            Glide.with(ctx).load(img3).into(imgAracaSoforSecondPhoto);
            imgAracaSoforFirstPhotoChange.setVisibility(View.INVISIBLE);
            imgAracaSoforSecondPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 3)
        {
            String img4 = photos.get(0);
            String img5 = photos.get(1);
            String img6 = photos.get(2);

            Glide.with(ctx).load(img4).into(imgAracaSoforFirstPhoto);
            Glide.with(ctx).load(img5).into(imgAracaSoforSecondPhoto);
            Glide.with(ctx).load(img6).into(imgAracaSoforLastPhoto);

            imgAracaSoforFirstPhotoChange.setVisibility(View.INVISIBLE);
            imgAracaSoforLastChange.setVisibility(View.INVISIBLE);
            imgAracaSoforSecondPhotoChange.setVisibility(View.INVISIBLE);
        }

        

        linAracaSoforIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx , MainActivity.class);
                startActivity(intent);
            }
        });

        relAracaSoforFirstPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgAracaSoforFirstPhoto);
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

        relAracaSoforSecondPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgAracaSoforSecondPhoto);
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

        relAracaSoforLastPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgAracaSoforLastPhoto);
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

        Utils.setAutoCompleteAdapter(autoCompleteAracaSoforil , sehirListesi , ctx);
        Utils.setAutoCompleteAdapter(autoCompleteAracaSoforServisBaslamaili , sehirListesi ,ctx);
        Utils.setAutoCompleteAdapter(autoCompleteAracaSoforServisBitisili , sehirListesi ,ctx);
        Utils.setAutoCompleteAdapter(autoCompleteAracaSoforKapasite , App.getKapasite(),ctx);

        autoCompleteAracaSoforil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvil = String.valueOf(position + 1);

            }
        });

        autoCompleteAracaSoforServisBaslamaili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvServiseBaslamaili = String.valueOf(position + 1);

            }
        });
        autoCompleteAracaSoforServisBitisili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvServisBitisili = String.valueOf(position + 1);

            }
        });
        autoCompleteAracaSoforKapasite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvKapasite = String.valueOf(position + 1);

            }
        });


        edtAracaSoforServisBaslamaSaati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.openTimeDialog(edtAracaSoforServisBaslamaSaati , ctx);
            }
        });

        edtAracaSoforServisBitisSaati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.openTimeDialog(edtAracaSoforServisBitisSaati , ctx);
            }
        });

        edtAracaSoforFirmaGirisSaati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.openTimeDialog(edtAracaSoforFirmaGirisSaati , ctx);
            }
        });

        edtAracaSoforFirmadanCikisSaati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.openTimeDialog(edtAracaSoforFirmadanCikisSaati , ctx);
            }
        });


        txtAracaSoforGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baslik = edtAracaSoforBaslik.getText().toString();
                fiyat = edtAracaSoforFiyat.getText().toString();
                aciklama = edtAracaSoforAciklama.getText().toString();
                servisBaslamaSaati = edtAracaSoforServisBaslamaSaati.getText().toString();
                servisBitisSaati = edtAracaSoforServisBitisSaati.getText().toString();
                firmaGirisSaati = edtAracaSoforFirmaGirisSaati.getText().toString();
                firmadanCikisSaati = edtAracaSoforFirmadanCikisSaati.getText().toString();
                tecrube = edtAracaSoforTecrube.getText().toString();
                gun = edtAracaSoforCalisilacakGunSayisi.getText().toString();


               if(baslik.isEmpty() || fiyat.isEmpty() || aciklama.isEmpty() || servisBaslamaSaati.isEmpty() || servisBitisSaati.isEmpty() || firmaGirisSaati.isEmpty() ||
                       firmadanCikisSaati.isEmpty() || tecrube.isEmpty() || gun.isEmpty() ||  actvil.isEmpty() || actvilce.isEmpty() || actvKapasite.isEmpty() || actvServiseBaslamaili.isEmpty() ||
                       actvServiseBaslamailce.isEmpty() || actvServisBitisili.isEmpty() || actvServisBitisilce.isEmpty())
               {
                   emptyDialog = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                   emptyDialog.setTitleText("* ile belirtilen tüm alanlar doldurulmalıdır");
                   emptyDialog.show();

               }

               else
               {
                   ArrayList<String> base64Photo = Utils.pathToBase64(photos);
                   imagesString = Utils.imageToString(base64Photo);
                   imagesString = Utils.trimmer(imagesString);

                   Log.i("İmages" , imagesString);

                   HashMap<String , Object> hashMap = new HashMap<>();
                   HashMap<String , String> hashMap1 = new HashMap<>();

                   hashMap1.put("Tipi" , "2");
                   hashMap1.put("Baslik" , baslik);
                   hashMap1.put("ilanCity" , actvil);
                   hashMap1.put("ilanSemtleri" , "10");
                   hashMap1.put("AracKapasitesi" , actvKapasite);
                   hashMap1.put("ServiseBaslamaCity" , actvServiseBaslamaili);
                   hashMap1.put("ServiseBaslamaSemtleri" , "10");
                   hashMap1.put("ServiseBaslamaSaati" , servisBaslamaSaati);
                   hashMap1.put("ServisBitisSaati" , servisBitisSaati);
                   hashMap1.put("FirmayaGiriSaati" , firmaGirisSaati);
                   hashMap1.put("FirmadanCikisSaati" , firmadanCikisSaati);
                   hashMap1.put("CalisilacakGunSayisi" , gun);
                   hashMap1.put("Ucret" , fiyat);
                   hashMap1.put("ilanAciklamasi" , aciklama);
                   hashMap1.put("file" , imagesString);
                   hashMap1.put("Tecrube" , tecrube);
                   hashMap1.put("ServisBitisCity" , actvServisBitisilce);
                   hashMap1.put("ServisBitisSemtleri" , "10");


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