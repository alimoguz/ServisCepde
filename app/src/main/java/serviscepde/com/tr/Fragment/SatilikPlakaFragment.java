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
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import serviscepde.com.tr.App;
import serviscepde.com.tr.DownloadClass;
import serviscepde.com.tr.GalleryActivity;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.IlanEkle.EkleResponse;
import serviscepde.com.tr.Models.IlanEkle.EkleResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.ImageCompressor;
import serviscepde.com.tr.Utils.OnCompressTaskCompleted;
import serviscepde.com.tr.Utils.Utils;


public class SatilikPlakaFragment extends Fragment {


    View generalView;

    private LinearLayout linSatilikPlakaIptal;
    private TextView txtSatilikPlakaGonder;
    private RelativeLayout relSatilikPlakaFirstPhoto,relSatilikPlakaSecondPhoto,relSatilikPlakaLastPhoto;
    private ImageView imgSatilikPlakaFirstPhoto,imgSatilikPlakaFirstPhotoChange,imgSatilikPlakaSecondPhoto,imgSatilikPlakaSecondPhotoChange , imgSatilikPlakaLastPhoto , imgSatilikPlakaLastChange ;
    private TextInputEditText edtSatilikPlakaBaslik,edtSatilikPlakaFiyat,edtSatilikPlakaAciklama,edtSatilikPlakaPlaka;
    private AutoCompleteTextView autoCompleteSatilikPlakail,autoCompleteSatilikPlakailce;

    private String baslik,aciklama,fiyat,plaka;
    private List<City> sehirler = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private String cityId;
    private String townId;
    private ArrayList<String> townNames  = new ArrayList<>();

    private Context ctx;
    private String userToken;
    private ArrayList<String> photos = new ArrayList<>();
    private String[] imageArray;
    private SweetAlertDialog emptyDialog;
    private SweetAlertDialog pDialog;
    private OnCompressTaskCompleted onCompressTaskCompleted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.satilik_plaka_fragment, container, false);

        generalView = rootView;

        ctx = generalView.getContext();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        linSatilikPlakaIptal = generalView.findViewById(R.id.linSatilikPlakaIptal);

        txtSatilikPlakaGonder = generalView.findViewById(R.id.txtSatilikPlakaGonder);

        relSatilikPlakaFirstPhoto = generalView.findViewById(R.id.relSatilikPlakaFirstPhoto);
        relSatilikPlakaSecondPhoto = generalView.findViewById(R.id.relSatilikPlakaSecondPhoto);
        relSatilikPlakaLastPhoto = generalView.findViewById(R.id.relSatilikPlakaLastPhoto);

        imgSatilikPlakaFirstPhoto = generalView.findViewById(R.id.imgSatilikPlakaFirstPhoto);
        imgSatilikPlakaFirstPhotoChange = generalView.findViewById(R.id.imgSatilikPlakaFirstPhotoChange);
        imgSatilikPlakaSecondPhoto = generalView.findViewById(R.id.imgSatilikPlakaSecondPhoto);
        imgSatilikPlakaSecondPhotoChange = generalView.findViewById(R.id.imgSatilikPlakaSecondPhotoChange);
        imgSatilikPlakaLastPhoto = generalView.findViewById(R.id.imgSatilikPlakaLastPhoto);
        imgSatilikPlakaLastChange = generalView.findViewById(R.id.imgSatilikPlakaLastChange);

        edtSatilikPlakaBaslik = generalView.findViewById(R.id.edtSatilikPlakaBaslik);
        edtSatilikPlakaFiyat = generalView.findViewById(R.id.edtSatilikPlakaFiyat);
        edtSatilikPlakaAciklama = generalView.findViewById(R.id.edtSatilikPlakaAciklama);
        edtSatilikPlakaPlaka = generalView.findViewById(R.id.edtSatilikPlakaPlaka);

        autoCompleteSatilikPlakail = generalView.findViewById(R.id.autoCompleteSatilikPlakail);
        autoCompleteSatilikPlakailce = generalView.findViewById(R.id.autoCompleteSatilikPlakailce);

        pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f1a400"));
        pDialog.setTitleText("Lütfen Bekleyiniz");
        pDialog.setCancelable(false);

        linSatilikPlakaIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx , MainActivity.class);
                startActivity(intent);
            }
        });

        relSatilikPlakaFirstPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgSatilikPlakaFirstPhoto);
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

        relSatilikPlakaSecondPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgSatilikPlakaSecondPhoto);
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

        relSatilikPlakaLastPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgSatilikPlakaLastPhoto);
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

        Utils.setAutoCompleteAdapter(autoCompleteSatilikPlakail , cityNames ,ctx);

        autoCompleteSatilikPlakail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cityId = parent.getItemAtPosition(position).toString();
                cityId = DownloadClass.getCityIdWithName(cityId);
                Log.i("SelectedIlId" , cityId);

                townNames = DownloadClass.getTownNames(cityId);
                autoCompleteSatilikPlakailce.setText(townNames.get(0));
                Utils.setAutoCompleteAdapter(autoCompleteSatilikPlakailce , townNames , ctx);
            }
        });

        autoCompleteSatilikPlakailce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                townId = parent.getItemAtPosition(position).toString();
                townId = DownloadClass.getTownIdWithTownName(townId , cityId);
                Log.i("SelectedIlceId" , townId);
            }
        });

        txtSatilikPlakaGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog.show();

                txtSatilikPlakaGonder.setClickable(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtSatilikPlakaGonder.setClickable(true);
                    }
                },3000);

                baslik = edtSatilikPlakaBaslik.getText().toString();
                fiyat = edtSatilikPlakaFiyat.getText().toString();
                aciklama = edtSatilikPlakaAciklama.getText().toString();
                plaka = edtSatilikPlakaPlaka.getText().toString();

                cityId = DownloadClass.getCityIdWithName(autoCompleteSatilikPlakail.getText().toString());
                townId = DownloadClass.getTownIdWithTownName(autoCompleteSatilikPlakailce.getText().toString() , cityId);



                if (baslik.isEmpty() || cityId.isEmpty() || townId.isEmpty() || plaka.isEmpty() || aciklama.isEmpty())
                {
                    pDialog.dismiss();
                    emptyDialog = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                    emptyDialog.setTitleText("* ile belirtilen tüm alanlar doldurulmalıdır");
                    emptyDialog.show();
                }

                else
                {
                    if(photos.size() != 0)
                    {
                        new ImageCompressor(photos, ctx, new OnCompressTaskCompleted() {
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

        hashMap1.put("Tipi" , "8");
        hashMap1.put("Baslik" , baslik);
        hashMap1.put("ilanCity" , cityId);
        hashMap1.put("ilanSemtleri" , townId);
        hashMap1.put("Ucret" , fiyat);
        hashMap1.put("file" , imageArray);
        hashMap1.put("ilanAciklamasi" , aciklama);
        hashMap1.put("Plaka" , plaka);

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
                    Glide.with(ctx).load(imageList.get(0)).into(imgSatilikPlakaFirstPhoto);
                    imgSatilikPlakaFirstPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
                else if(resultCode == 200){
                    Glide.with(ctx).load(imageList.get(0)).into(imgSatilikPlakaSecondPhoto);
                    imgSatilikPlakaSecondPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
                else if(resultCode == 300){
                    Glide.with(ctx).load(imageList.get(0)).into(imgSatilikPlakaLastPhoto);
                    imgSatilikPlakaLastChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}