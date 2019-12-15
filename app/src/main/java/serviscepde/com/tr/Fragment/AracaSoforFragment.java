package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

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
import serviscepde.com.tr.Models.Kapasite;
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
    private String actvKapasite;
    private Context ctx;


    private SweetAlertDialog emptyDialog;

    private String userToken;
    private String [] imageArray;

    private List<City> sehirler = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private List<Kapasite> kapasites = new ArrayList<>();
    private List<String> kapasiteNames = new ArrayList<>();
    private String cityId,baslamaCityId,bitisCityId;
    private String townId,baslamaTownId,bitisTownId;
    private ArrayList<String> townNames , baslamaTownNames , bitisTownNames = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.araca_sofor_fragment, container, false);


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

                        Intent intent = new Intent(getContext(), GalleryActivity.class);
                        intent.putExtra("position",1);
                        startActivityForResult(intent, 6614);
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

                        Intent intent = new Intent(getContext(), GalleryActivity.class);
                        intent.putExtra("position",2);
                        startActivityForResult(intent, 6614);

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

                        Intent intent = new Intent(getContext(), GalleryActivity.class);
                        intent.putExtra("position",3);
                        startActivityForResult(intent, 6614);

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

        sehirler = DownloadClass.getCities();
        cityNames = DownloadClass.getCityNames();
        kapasites = DownloadClass.getKapasite();
        kapasiteNames = DownloadClass.getKapasiteNames();


        Utils.setAutoCompleteAdapter(autoCompleteAracaSoforil , cityNames , ctx);
        Utils.setAutoCompleteAdapter(autoCompleteAracaSoforServisBaslamaili , cityNames ,ctx);
        Utils.setAutoCompleteAdapter(autoCompleteAracaSoforServisBitisili , cityNames ,ctx);
        Utils.setAutoCompleteAdapter(autoCompleteAracaSoforKapasite , kapasiteNames ,ctx);

        autoCompleteAracaSoforil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cityId = parent.getItemAtPosition(position).toString();
                cityId = DownloadClass.getCityIdWithName(cityId);
                Log.i("selectedIlId" , cityId);

                townNames = DownloadClass.getTownNames(cityId);
                Utils.setAutoCompleteAdapter(autoCompleteAracaSoforilce , townNames , ctx);

            }
        });

        autoCompleteAracaSoforilce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                townId = parent.getItemAtPosition(position).toString();
                townId = DownloadClass.getTownIdWithTownName(townId,cityId);
                Log.i("SelectedIlce" , townId);

            }
        });

        autoCompleteAracaSoforServisBaslamaili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                baslamaCityId = parent.getItemAtPosition(position).toString();
                baslamaCityId = DownloadClass.getCityIdWithName(baslamaCityId);
                Log.i("selectedIlId" , baslamaCityId);

                baslamaTownNames = DownloadClass.getTownNames(baslamaCityId);
                Utils.setAutoCompleteAdapter(autoCompleteAracaSoforServiseBaslamailce , baslamaTownNames , ctx);

            }
        });


        autoCompleteAracaSoforServiseBaslamailce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                baslamaTownId = parent.getItemAtPosition(position).toString();
                baslamaTownId = DownloadClass.getTownIdWithTownName(baslamaTownId , baslamaCityId);

                Log.i("SelectedBaslamaIlce" , baslamaTownId);
            }
        });


        autoCompleteAracaSoforServisBitisili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                bitisCityId = parent.getItemAtPosition(position).toString();
                bitisCityId = DownloadClass.getCityIdWithName(bitisCityId);
                Log.i("SelectedBitisIl" , bitisCityId);

                bitisTownNames = DownloadClass.getTownNames(bitisCityId);
                Utils.setAutoCompleteAdapter(autoCompleteAracaSoforServisBitisilce , bitisTownNames , ctx);

            }
        });

        autoCompleteAracaSoforServisBitisilce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                bitisTownId = parent.getItemAtPosition(position).toString();
                bitisTownId = DownloadClass.getTownIdWithTownName(bitisTownId , bitisCityId);
                Log.i("ServisBitisilceId" , bitisTownId);
            }
        });
        autoCompleteAracaSoforKapasite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvKapasite = parent.getItemAtPosition(position).toString();
                actvKapasite = DownloadClass.getKapasiteIdWithName(actvKapasite);

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

                txtAracaSoforGonder.setClickable(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtAracaSoforGonder.setClickable(true);
                    }
                },3000);

                baslik = edtAracaSoforBaslik.getText().toString();
                fiyat = edtAracaSoforFiyat.getText().toString();
                aciklama = edtAracaSoforAciklama.getText().toString();
                servisBaslamaSaati = edtAracaSoforServisBaslamaSaati.getText().toString();
                servisBitisSaati = edtAracaSoforServisBitisSaati.getText().toString();
                firmaGirisSaati = edtAracaSoforFirmaGirisSaati.getText().toString();
                firmadanCikisSaati = edtAracaSoforFirmadanCikisSaati.getText().toString();
                tecrube = edtAracaSoforTecrube.getText().toString();
                gun = edtAracaSoforCalisilacakGunSayisi.getText().toString();


               if(baslik.isEmpty()  || aciklama.isEmpty() || servisBaslamaSaati.isEmpty() || servisBitisSaati.isEmpty() || firmaGirisSaati.isEmpty() ||
                       firmadanCikisSaati.isEmpty() || tecrube.isEmpty() || gun.isEmpty() ||  cityId.isEmpty() || townId.isEmpty() || actvKapasite.isEmpty() || baslamaCityId.isEmpty() ||
                       baslamaTownId.isEmpty() || bitisCityId.isEmpty() || bitisTownId.isEmpty())
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

                   hashMap1.put("Tipi" , "2");
                   hashMap1.put("Baslik" , baslik);
                   hashMap1.put("ilanCity" , cityId);
                   hashMap1.put("ilanSemtleri" , townId);
                   hashMap1.put("AracKapasitesi" , actvKapasite);
                   hashMap1.put("ServiseBaslamaCity" , baslamaCityId);
                   hashMap1.put("ServiseBaslamaSemtleri" , baslamaTownId);
                   hashMap1.put("ServiseBaslamaSaati" , servisBaslamaSaati);
                   hashMap1.put("ServisBitisSaati" , servisBitisSaati);
                   hashMap1.put("FirmayaGiriSaati" , firmaGirisSaati);
                   hashMap1.put("FirmadanCikisSaati" , firmadanCikisSaati);
                   hashMap1.put("CalisilacakGunSayisi" , gun);
                   hashMap1.put("Ucret" , fiyat);
                   hashMap1.put("ilanAciklamasi" , aciklama);
                   hashMap1.put("file" , imageArray);
                   hashMap1.put("Tecrube" , tecrube);
                   hashMap1.put("ServisBitisCity" , bitisCityId);
                   hashMap1.put("ServisBitisSemtleri" , bitisTownId);


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
                                   ilanHata.setTitleText("Bir hata oluştu lütfen daha sonra tekrar deneyin");
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
                    Glide.with(ctx).load(imageList.get(0)).into(imgAracaSoforFirstPhoto);
                    imgAracaSoforFirstPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));
                }
                else if(resultCode == 200){
                    Glide.with(ctx).load(imageList.get(0)).into(imgAracaSoforSecondPhoto);
                    imgAracaSoforSecondPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
                else if(resultCode == 300){
                    Glide.with(ctx).load(imageList.get(0)).into(imgAracaSoforLastPhoto);
                    imgAracaSoforLastChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}