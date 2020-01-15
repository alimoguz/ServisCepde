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
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;

import me.abhinay.input.CurrencyEditText;
import serviscepde.com.tr.App;
import serviscepde.com.tr.DownloadClass;
import serviscepde.com.tr.GalleryActivity;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.IlanEkle.EkleResponse;
import serviscepde.com.tr.Models.IlanEkle.EkleResponseDetail;
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
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class YedekParcaFragment extends Fragment {


    View generalView;

    private LinearLayout linYedekParcaIptal;

    private RelativeLayout relYedekParcaFirstPhoto,relYedekParcaSecondPhoto,relYedekParcaLastPhoto;

    private ImageView imgYedekParcaFirstPhoto,imgYedekParcaFirstPhotoChange,imgYedekParcaSecondPhoto,imgYedekParcaSecondPhotoChange,imgYedekParcaLastPhoto,imgYedekParcaLastChange;

    private TextInputEditText edtYedekParcaBaslik,edtYedekParcaAciklama,edtYedekParcaMarka;

    private CurrencyEditText edtYedekParcaFiyat;

    private AutoCompleteTextView autoCompleteYedekParcail,autoCompleteYedekParcailce,autoCompleteYedekParcaDurumu;

    private Switch switchYedekParcaCikmaParca;
    private TextView txtYedekParcaGonder;

    private ArrayList<String> photos = new ArrayList<>();
    private String imageString,userToken;
    private String baslik,fiyat,aciklama,marka;
    private String actvDurum;

    private SweetAlertDialog emptyDialog;
    private String[] imageArray;

    private List<City> sehirler = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private String cityId;
    private String townId;
    private ArrayList<String> townNames  = new ArrayList<>();

    private SweetAlertDialog pDialog;

    private Context ctx;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.yedek_parca_fragment, container, false);

        generalView = rootView;

        ctx = generalView.getContext();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        autoCompleteYedekParcail = generalView.findViewById(R.id.autoCompleteYedekParcail);
        autoCompleteYedekParcailce = generalView.findViewById(R.id.autoCompleteYedekParcailce);
        autoCompleteYedekParcaDurumu = generalView.findViewById(R.id.autoCompleteYedekParcaDurumu);



        edtYedekParcaBaslik = generalView.findViewById(R.id.edtYedekParcaBaslik);
        edtYedekParcaFiyat = generalView.findViewById(R.id.edtYedekParcaFiyat);
        edtYedekParcaAciklama = generalView.findViewById(R.id.edtYedekParcaAciklama);
        edtYedekParcaMarka = generalView.findViewById(R.id.edtYedekParcaMarka);


        imgYedekParcaFirstPhoto = generalView.findViewById(R.id.imgYedekParcaFirstPhoto);
        imgYedekParcaFirstPhotoChange = generalView.findViewById(R.id.imgYedekParcaFirstPhotoChange);
        imgYedekParcaSecondPhoto = generalView.findViewById(R.id.imgYedekParcaSecondPhoto);
        imgYedekParcaSecondPhotoChange = generalView.findViewById(R.id.imgYedekParcaSecondPhotoChange);
        imgYedekParcaLastPhoto = generalView.findViewById(R.id.imgYedekParcaLastPhoto);
        imgYedekParcaLastChange = generalView.findViewById(R.id.imgYedekParcaLastChange);


        relYedekParcaFirstPhoto = generalView.findViewById(R.id.relYedekParcaFirstPhoto);
        relYedekParcaSecondPhoto = generalView.findViewById(R.id.relYedekParcaSecondPhoto);
        relYedekParcaLastPhoto = generalView.findViewById(R.id.relYedekParcaLastPhoto);

        switchYedekParcaCikmaParca = generalView.findViewById(R.id.switchYedekParcaCikmaParca);
        linYedekParcaIptal = generalView.findViewById(R.id.linYedekParcaIptal);
        txtYedekParcaGonder = generalView.findViewById(R.id.txtYedekParcaGonder);

        pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f1a400"));
        pDialog.setTitleText("Lütfen Bekleyiniz");
        pDialog.setCancelable(false);

        if(photos.size() == 1)
        {
            String img1 = photos.get(0);
            Glide.with(ctx).load(img1).into(imgYedekParcaFirstPhoto);
            imgYedekParcaFirstPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 2)
        {
            String img2 = photos.get(0);
            String img3 = photos.get(1);

            Glide.with(ctx).load(img2).into(imgYedekParcaFirstPhoto);
            Glide.with(ctx).load(img3).into(imgYedekParcaSecondPhoto);
            imgYedekParcaFirstPhotoChange.setVisibility(View.INVISIBLE);
            imgYedekParcaSecondPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 3)
        {
            String img4 = photos.get(0);
            String img5 = photos.get(1);
            String img6 = photos.get(2);

            Glide.with(ctx).load(img4).into(imgYedekParcaFirstPhoto);
            Glide.with(ctx).load(img5).into(imgYedekParcaSecondPhoto);
            Glide.with(ctx).load(img6).into(imgYedekParcaLastPhoto);

            imgYedekParcaFirstPhotoChange.setVisibility(View.INVISIBLE);
            imgYedekParcaSecondPhotoChange.setVisibility(View.INVISIBLE);
            imgYedekParcaLastChange.setVisibility(View.INVISIBLE);
        }

        linYedekParcaIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx , MainActivity.class);
                startActivity(intent);
            }
        });

        relYedekParcaFirstPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgYedekParcaFirstPhoto);
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

        relYedekParcaSecondPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgYedekParcaSecondPhoto);
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

        relYedekParcaLastPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgYedekParcaLastPhoto);
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



        Utils.setAutoCompleteAdapter(autoCompleteYedekParcail, cityNames , ctx);
        autoCompleteYedekParcaDurumu.setAdapter(null);
        Utils.setAutoCompleteAdapter(autoCompleteYedekParcaDurumu , App.getDurumu() , ctx);

        autoCompleteYedekParcail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cityId = parent.getItemAtPosition(position).toString();
                cityId = DownloadClass.getCityIdWithName(cityId);
                Log.i("SelectedIlId" , cityId);

                townNames = DownloadClass.getTownNames(cityId);
                autoCompleteYedekParcailce.setText(townNames.get(0));
                Utils.setAutoCompleteAdapter(autoCompleteYedekParcailce , townNames , ctx);
            }
        });

        autoCompleteYedekParcailce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                townId = parent.getItemAtPosition(position).toString();
                townId = DownloadClass.getTownIdWithTownName(townId , cityId);
                Log.i("SelectedIlceId" , townId);
            }
        });

        autoCompleteYedekParcaDurumu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actvDurum = String.valueOf(position + 1);
            }
        });


        txtYedekParcaGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtYedekParcaGonder.setClickable(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtYedekParcaGonder.setClickable(true);
                    }
                },3000);

                boolean isFiyatNull = edtYedekParcaFiyat.getText().toString().isEmpty();
                if(isFiyatNull)
                {
                    fiyat = "0";
                }
                else
                {
                    fiyat = String.valueOf(edtYedekParcaFiyat.getCleanDoubleValue());
                }

                baslik = edtYedekParcaBaslik.getText().toString();
                aciklama = edtYedekParcaAciklama.getText().toString();
                marka = edtYedekParcaMarka.getText().toString();

                cityId = DownloadClass.getCityIdWithName(autoCompleteYedekParcail.getText().toString());
                townId = DownloadClass.getTownIdWithTownName(autoCompleteYedekParcailce.getText().toString() , cityId);
                actvDurum = DownloadClass.getDurumu(autoCompleteYedekParcaDurumu.getText().toString());

                if (baslik.isEmpty()  || aciklama.isEmpty() || marka.isEmpty() || cityId.isEmpty() || townId.isEmpty() || actvDurum.isEmpty())
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
        String s = "0";

        if(switchYedekParcaCikmaParca.isChecked())
        {
            s = "1";
        }
        HashMap<String , Object> hashMap = new HashMap<>();
        HashMap<String , Object> hashMap1 = new HashMap<>();

        hashMap1.put("Tipi" , "7");
        hashMap1.put("Baslik" , baslik);
        hashMap1.put("ilanCity" , cityId);
        hashMap1.put("ilanSemtleri" , townId);
        hashMap1.put("ParcaMarkasi" , marka);
        hashMap1.put("Ucret" , fiyat);
        hashMap1.put("file" , imageArray);
        hashMap1.put("ilanAciklamasi" , aciklama);
        hashMap1.put("YedekParcaDurum" , actvDurum);
        hashMap1.put("CikmaYedekParca" , s);

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
                    Glide.with(ctx).load(imageList.get(0)).into(imgYedekParcaFirstPhoto);
                    imgYedekParcaFirstPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
                else if(resultCode == 200){
                    Glide.with(ctx).load(imageList.get(0)).into(imgYedekParcaSecondPhoto);
                    imgYedekParcaSecondPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
                else if(resultCode == 300){
                    Glide.with(ctx).load(imageList.get(0)).into(imgYedekParcaLastPhoto);
                    imgYedekParcaLastChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}