package serviscepde.com.tr.Fragment;

import android.app.ActivityManager;
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
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.yigitserin.currencyedittext.CurrencyEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.ACTIVITY_SERVICE;


public class SoforeIsFragment extends Fragment {


    View generalView;

    private LinearLayout linSoforeIsIptal;

    private RelativeLayout relSoforeIsFirstPhoto,relSoforeIsSecondPhoto,relSoforeIsLastPhoto;

    private ImageView imgSoforeIsFirstPhoto,imgSoforeIsFirstPhotoChange,imgSoforeIsSecondPhoto,imgSoforeIsSecondPhotoChange,imgSoforeIsLastPhoto,imgSoforeIsLastChange;

    private TextInputEditText edtSoforeIsBaslik,edtSoforeIsAciklama,edtSoforeIsTecrube,edtSoforeIsServisBaslamaSaati,edtSoforeIsYas,edtSoforeIsBelgeler;

    private CurrencyEditText edtSoforeIsFiyat;

    private AutoCompleteTextView autoCompleteSoforeIsil,autoCompleteSoforeIsilce,autoCompleteSoforeIsServisBaslamaili,autoCompleteSoforeIsServiseBaslamailce;

    private MultiAutoCompleteTextView autoCompleteSoforeIsKapasite,autoCompleteSoforeIsEhliyet;

    private Switch switchSoforeIsSrc;
    private String switchState = "0";

    private TextView txtSoforeIsGonder;

    private final static Calendar takvim = Calendar.getInstance();
    private int saat = takvim.get(Calendar.HOUR_OF_DAY);
    private int dakika = takvim.get(Calendar.MINUTE);

    private ArrayList<String> photos = new ArrayList<>();

    private String baslik,fiyat,aciklama,tecrube,serviseBaslamaSaati,yas,belgeler;
    private String actvKapasite,actvEhliyet;

    private SweetAlertDialog emptyDialog;

    private String userToken;
    private String [] imageArray;

    private Context ctx;

    private List<City> sehirler = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private String cityId,baslamaCityId;
    private String townId,baslamaTownId;
    private ArrayList<String> townNames , baslamaTownNames  = new ArrayList<>();
    private List<Kapasite> kapasites = new ArrayList<>();
    private List<String> kapasiteNames = new ArrayList<>();
    private List<String> ehliyetList = new ArrayList<>();
    private SweetAlertDialog pDialog;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.sofore_is_fragment, container, false);


        generalView = rootView;

        ctx = generalView.getContext();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");

        edtSoforeIsBaslik = generalView.findViewById(R.id.edtSoforeIsBaslik);
        edtSoforeIsFiyat = generalView.findViewById(R.id.edtSoforeIsFiyat);
        edtSoforeIsFiyat.setLocale(new Locale("tr","TR"));
        edtSoforeIsFiyat.setDecimalDigits(2);
        edtSoforeIsFiyat.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        edtSoforeIsAciklama = generalView.findViewById(R.id.edtSoforeIsAciklama);
        edtSoforeIsTecrube = generalView.findViewById(R.id.edtSoforeIsTecrube);
        edtSoforeIsServisBaslamaSaati = generalView.findViewById(R.id.edtSoforeIsServisBaslamaSaati);
        edtSoforeIsYas = generalView.findViewById(R.id.edtSoforeIsYas);
        edtSoforeIsBelgeler = generalView.findViewById(R.id.edtSoforeIsBelgeler);


        autoCompleteSoforeIsil = generalView.findViewById(R.id.autoCompleteSoforeIsil);
        autoCompleteSoforeIsilce = generalView.findViewById(R.id.autoCompleteSoforeIsilce);
        autoCompleteSoforeIsKapasite = generalView.findViewById(R.id.autoCompleteSoforeIsKapasite);
        autoCompleteSoforeIsServisBaslamaili = generalView.findViewById(R.id.autoCompleteSoforeIsServisBaslamaili);
        autoCompleteSoforeIsServiseBaslamailce = generalView.findViewById(R.id.autoCompleteSoforeIsServiseBaslamailce);
        autoCompleteSoforeIsEhliyet = generalView.findViewById(R.id.autoCompleteSoforeIsEhliyet);


        relSoforeIsFirstPhoto = generalView.findViewById(R.id.relSoforeIsFirstPhoto);
        relSoforeIsSecondPhoto = generalView.findViewById(R.id.relSoforeIsSecondPhoto);
        relSoforeIsLastPhoto = generalView.findViewById(R.id.relSoforeIsLastPhoto);


        imgSoforeIsFirstPhoto = generalView.findViewById(R.id.imgSoforeIsFirstPhoto);
        imgSoforeIsFirstPhotoChange = generalView.findViewById(R.id.imgSoforeIsFirstPhotoChange);
        imgSoforeIsSecondPhoto = generalView.findViewById(R.id.imgSoforeIsSecondPhoto);
        imgSoforeIsSecondPhotoChange = generalView.findViewById(R.id.imgSoforeIsSecondPhotoChange);
        imgSoforeIsLastPhoto = generalView.findViewById(R.id.imgSoforeIsLastPhoto);
        imgSoforeIsLastChange = generalView.findViewById(R.id.imgSoforeIsLastChange);

        linSoforeIsIptal = generalView.findViewById(R.id.linSoforeIsIptal);
        switchSoforeIsSrc = generalView.findViewById(R.id.switchSoforeIsSrc);
        txtSoforeIsGonder = generalView.findViewById(R.id.txtSoforeIsGonder);

        pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f1a400"));
        pDialog.setTitleText("Lütfen Bekleyiniz");
        pDialog.setCancelable(false);

        if(photos.size() == 1)
        {
            String img1 = photos.get(0);
            Glide.with(ctx).load(img1).into(imgSoforeIsFirstPhoto);
            imgSoforeIsFirstPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 2)
        {
            String img2 = photos.get(0);
            String img3 = photos.get(1);

            Glide.with(ctx).load(img2).into(imgSoforeIsFirstPhoto);
            Glide.with(ctx).load(img3).into(imgSoforeIsSecondPhoto);
            imgSoforeIsSecondPhotoChange.setVisibility(View.INVISIBLE);
            imgSoforeIsFirstPhotoChange.setVisibility(View.INVISIBLE);

        }

        if(photos.size() == 3)
        {
            String img4 = photos.get(0);
            String img5 = photos.get(1);
            String img6 = photos.get(2);

            Glide.with(ctx).load(img4).into(imgSoforeIsFirstPhoto);
            Glide.with(ctx).load(img5).into(imgSoforeIsSecondPhoto);
            Glide.with(ctx).load(img6).into(imgSoforeIsLastPhoto);

            imgSoforeIsFirstPhotoChange.setVisibility(View.INVISIBLE);
            imgSoforeIsLastChange.setVisibility(View.INVISIBLE);
            imgSoforeIsSecondPhotoChange.setVisibility(View.INVISIBLE);
        }

        linSoforeIsIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx , MainActivity.class);
                startActivity(intent);
            }
        });

        relSoforeIsFirstPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgSoforeIsFirstPhoto);
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

        relSoforeIsSecondPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgSoforeIsSecondPhoto);
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

        relSoforeIsLastPhoto.setOnClickListener(new View.OnClickListener() {
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

                        Glide.with(ctx).load(R.drawable.empty_image).into(imgSoforeIsLastPhoto);
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
        ehliyetList = DownloadClass.getEhliyetList();



        Utils.setAutoCompleteAdapter(autoCompleteSoforeIsil , cityNames , ctx);
        Utils.setAutoCompleteAdapter(autoCompleteSoforeIsServisBaslamaili , cityNames , ctx);


        autoCompleteSoforeIsil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cityId = parent.getItemAtPosition(position).toString();
                cityId = DownloadClass.getCityIdWithName(cityId);
                Log.i("SelectedIlId" , cityId);

                townNames = DownloadClass.getTownNames(cityId);
                autoCompleteSoforeIsilce.setText(townNames.get(0));
                Utils.setAutoCompleteAdapter(autoCompleteSoforeIsilce , townNames , ctx);
            }
        });

        autoCompleteSoforeIsilce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                townId = parent.getItemAtPosition(position).toString();
                townId = DownloadClass.getTownIdWithTownName(townId , cityId);
                Log.i("SelectedIlceId" , townId);

            }
        });
        autoCompleteSoforeIsServisBaslamaili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                baslamaCityId = parent.getItemAtPosition(position).toString();
                baslamaCityId = DownloadClass.getCityIdWithName(baslamaCityId);
                Log.i("ServisBaslamaId" , baslamaCityId);

                baslamaTownNames = DownloadClass.getTownNames(baslamaCityId);
                autoCompleteSoforeIsServiseBaslamailce.setText(baslamaTownNames.get(0));
                Utils.setAutoCompleteAdapter(autoCompleteSoforeIsServiseBaslamailce , baslamaTownNames , ctx);
            }
        });

        autoCompleteSoforeIsServiseBaslamailce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                baslamaTownId = parent.getItemAtPosition(position).toString();
                baslamaTownId = DownloadClass.getTownIdWithTownName(baslamaTownId , baslamaCityId);
                Log.i("ServisBaslamaIlceId" , baslamaTownId);

            }
        });



        ArrayAdapter<String> ehliyet = new ArrayAdapter<>(ctx, R.layout.dropdown_item, ehliyetList);
        autoCompleteSoforeIsEhliyet.setAdapter(ehliyet);
        autoCompleteSoforeIsEhliyet.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        actvEhliyet = "";
        autoCompleteSoforeIsEhliyet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String ehliyetText = App.returnEhliyet(String.valueOf(position + 1));

                String tmp = ehliyetText.concat(",");
                actvEhliyet = actvEhliyet.concat(tmp);
                Log.i("Kapasite" , actvEhliyet);

            }
        });






        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, R.layout.dropdown_item, kapasiteNames);

        autoCompleteSoforeIsKapasite.setAdapter(adapter);
        autoCompleteSoforeIsKapasite.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        actvKapasite = "";
        autoCompleteSoforeIsKapasite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String kapasiteText = App.returnKapasite(String.valueOf(position + 1));


                String tmp = kapasiteText.concat(",");
                actvKapasite = actvKapasite.concat(tmp);
                Log.i("Kapasite" , actvKapasite);

            }
        });



        edtSoforeIsServisBaslamaSaati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.openTimeDialog(edtSoforeIsServisBaslamaSaati , ctx);
            }
        });

        txtSoforeIsGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog.show();

                txtSoforeIsGonder.setClickable(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtSoforeIsGonder.setClickable(true);
                    }
                },3000);

                boolean isFiyatNull = edtSoforeIsFiyat.getText().toString().isEmpty();
                if(isFiyatNull)
                {
                    fiyat = "0";
                }
                else
                {
                    fiyat = edtSoforeIsFiyat.getText().toString();

                    if(fiyat.contains(","))
                    {
                        String newFiyat = fiyat.substring(fiyat.indexOf(","));

                        if(newFiyat.length() == 2)
                        {
                            fiyat = fiyat.concat("0");
                        }
                    }
                    if(!fiyat.contains(","))
                    {
                        fiyat = fiyat.concat(",00");
                    }
                }

                baslik = edtSoforeIsBaslik.getText().toString();
                aciklama = edtSoforeIsAciklama.getText().toString();
                tecrube = edtSoforeIsTecrube.getText().toString();
                serviseBaslamaSaati = edtSoforeIsServisBaslamaSaati.getText().toString();
                yas = edtSoforeIsYas.getText().toString();
                belgeler = edtSoforeIsBelgeler.getText().toString();

                cityId = DownloadClass.getCityIdWithName(autoCompleteSoforeIsil.getText().toString());
                townId = DownloadClass.getTownIdWithTownName(autoCompleteSoforeIsilce.getText().toString() , cityId);
                baslamaCityId = DownloadClass.getCityIdWithName(autoCompleteSoforeIsServisBaslamaili.getText().toString());
                baslamaTownId = DownloadClass.getTownIdWithTownName(autoCompleteSoforeIsServiseBaslamailce.getText().toString() , baslamaCityId);


                if( baslik.isEmpty()  || aciklama.isEmpty() || tecrube.isEmpty() || serviseBaslamaSaati.isEmpty() || actvEhliyet.isEmpty() || yas.isEmpty() || belgeler.isEmpty() || cityId.isEmpty() || townId.isEmpty() || baslamaCityId.isEmpty() || baslamaTownId.isEmpty() || actvKapasite.isEmpty() )
                {
                    pDialog.dismiss();
                    emptyDialog = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                    emptyDialog.setTitleText("* ile belirtilen tüm alanlar doldurulmalıdır");
                    emptyDialog.show();
                }

                else
                {
                    if(switchSoforeIsSrc.isChecked())
                    {
                        switchState = "1";
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
        actvKapasite = Utils.trimmer(actvKapasite);
        actvEhliyet = Utils.trimmer(actvEhliyet);

        HashMap<String , Object> hashMap = new HashMap<>();
        HashMap<String , Object> hashMap1 = new HashMap<>();

        hashMap1.put("Tipi" , "4");
        hashMap1.put("Baslik" , baslik);
        hashMap1.put("ilanCity" , cityId);
        hashMap1.put("ilanSemtleri" , townId);
        hashMap1.put("KullanabildiginizKapasiteler" , actvKapasite);
        hashMap1.put("ServiseBaslamaCity" , baslamaCityId);
        hashMap1.put("ServiseBaslamaSemtleri" , baslamaTownId);
        hashMap1.put("ServiseBaslamaSaati" , serviseBaslamaSaati);
        hashMap1.put("Ucret" , fiyat);
        hashMap1.put("ilanAciklamasi" , aciklama);
        hashMap1.put("file" , imageArray);
        hashMap1.put("Tecrube" , tecrube);
        hashMap1.put("Ehliyetiniz" , actvEhliyet);
        hashMap1.put("Yasiniz" , yas);
        hashMap1.put("Belgeler" , belgeler);

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
                        ilanHata.setTitleText("Bir sorunla karşılaştık lütfen daha sonra tekrar deneyin");
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
                Toast toast = Toast.makeText(ctx,"Fotoğraflar yüklenemedi",Toast.LENGTH_SHORT);
                toast.show();
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
                    Glide.with(ctx).load(imageList.get(0)).into(imgSoforeIsFirstPhoto);
                    imgSoforeIsFirstPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
                else if(resultCode == 200){
                    Glide.with(ctx).load(imageList.get(0)).into(imgSoforeIsSecondPhoto);
                    imgSoforeIsSecondPhotoChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
                else if(resultCode == 300){
                    Glide.with(ctx).load(imageList.get(0)).into(imgSoforeIsLastPhoto);
                    imgSoforeIsLastChange.setVisibility(View.INVISIBLE);
                    photos.add(imageList.get(0));

                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}