package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.Adapter.ViewPagerAdapter;
import serviscepde.com.tr.App;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.IlanDetay.IlanDetayResponse;
import serviscepde.com.tr.Models.IlanDetay.IlanDetayResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.SplashActivity;
import serviscepde.com.tr.Utils.Utils;


public class IlanDetayFragment extends Fragment {


    View generalView;
    private ViewPager viewPagerPhotos;
    private ImageView imgNoPhoto;
    Context ctx;
    ArrayList<String> photos = new ArrayList<>();

    private ExtendedFloatingActionButton fabIcon;
    private TextView txtIlanDetayAciklama,txtFiyat,txtIlanSahibi,txtIlanTamKonum,txtIlanNo,txtIlanTarih,txtAracMarkasi,txtAracModel,txtAracAltModel,txtAracYil,txtAracKapasite
            ,txtServisBaslamaSaat,txtServisBitisSaat,txtServisBaslamaKonum,txtServisBitisKonum,txtFirmaGirisSaat,txtFirmaCikisSaat,txtToplamKM,txtGunSayisi,txtMotorHacim,
            txtMotorGuc,txtKimden,txtAracDurumu,txtTecrube,txtPlaka,txtReferans,txtUcretBeklentisi,txtKapasiteler,txtYas,txtEhliyet,txtSrc,txtAylikFiyati,txtHaftalikFiyati,
            txtVitesTipi,txtYakitTipi,txtKaskoDurum,txtParcaMarkasi,txtCikmaYedekParca,txtYedekParcaDurumu,txtAracOzellikler,txtBelgeler;

    private String Aciklama,Fiyat,IlanSahibi,Konum,IlanNo,IlanTarih,AracMarkasi,AracModel,AracAltModel,AracYil,AracKapasite
            ,ServisBaslamaSaat,ServisBitisSaat,ServisBaslamaKonum,ServisBitisKonum,FirmaGirisSaat,FirmaCikisSaat,ToplamKM,GunSayisi,MotorHacim,
            MotorGuc,Kimden,AracDurumu,Tecrube,Plaka,Referans,UcretBeklentisi,Kapasiteler,Yas,Ehliyet,Src,AylikFiyati,HaftalikFiyati,
            VitesTipi,YakitTipi,KaskoDurum,ParcaMarkasi,CikmaYedekParca,YedekParcaDurumu,Ozellikler,Belgeler;


    private LinearLayout linMarka,linModel,linAltModel,linYil,linKapasite,linBaslamaSaat,linBitisSaat,linBaslamaKonum,linBitisKonum,linFirmaGiris,linFirmaCikis,linToplamKM,
            linGunSayisi,linMotorHacim,linMotorGuc,linKimden,linAracDurum,linTecrube,linPlaka,linReferans,linUcretBeklentisi,linKapasiteler,linYas,linEhliyet,linSrc,
            linAylikFiyat,linHaftalikFiyat,linVitesTipi,linYakitTipi,linKaskoDurum,linParcaMarkasi,linCikmaYedekParca,linYedekParcaDurumu,linAracOzellikleri,linBelgeler;

    private String userToken,Resimler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ilan_detay_fragment, container, false);

        String ilanID = getArguments().getString("ilanID");
        Log.i("Fragment" , ilanID);

        MainActivity.bottomNav.setVisibility(View.GONE);
        MainActivity.relHeader.setVisibility(View.GONE);

        generalView = rootView;
        ctx = generalView.getContext();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");

        txtIlanDetayAciklama = generalView.findViewById(R.id.txtIlanDetayAciklama);
        txtFiyat = generalView.findViewById(R.id.txtFiyat);
        txtIlanSahibi = generalView.findViewById(R.id.txtIlanSahibi);
        txtIlanTamKonum = generalView.findViewById(R.id.txtIlanTamKonum);
        txtIlanNo = generalView.findViewById(R.id.txtIlanNo);
        txtIlanTarih = generalView.findViewById(R.id.txtIlanTarih);
        txtAracMarkasi = generalView.findViewById(R.id.txtAracMarkasi);
        txtAracModel = generalView.findViewById(R.id.txtAracModel);
        txtAracAltModel = generalView.findViewById(R.id.txtAracAltModel);
        txtAracYil = generalView.findViewById(R.id.txtAracYil);
        txtAracKapasite = generalView.findViewById(R.id.txtAracKapasite);
        txtServisBaslamaSaat = generalView.findViewById(R.id.txtServisBaslamaSaat);
        txtServisBitisSaat = generalView.findViewById(R.id.txtServisBitisSaat);
        txtServisBaslamaKonum = generalView.findViewById(R.id.txtServisBaslamaKonum);
        txtServisBitisKonum = generalView.findViewById(R.id.txtServisBitisKonum);
        txtFirmaGirisSaat = generalView.findViewById(R.id.txtFirmaGirisSaat);
        txtFirmaCikisSaat = generalView.findViewById(R.id.txtFirmaCikisSaat);
        txtToplamKM = generalView.findViewById(R.id.txtToplamKM);
        txtGunSayisi = generalView.findViewById(R.id.txtGunSayisi);
        txtMotorHacim = generalView.findViewById(R.id.txtMotorHacim);
        txtMotorGuc = generalView.findViewById(R.id.txtMotorGuc);
        txtKimden = generalView.findViewById(R.id.txtKimden);
        txtAracDurumu = generalView.findViewById(R.id.txtAracDurumu);
        txtTecrube = generalView.findViewById(R.id.txtTecrube);
        txtPlaka = generalView.findViewById(R.id.txtPlaka);
        txtReferans = generalView.findViewById(R.id.txtReferans);
        txtUcretBeklentisi = generalView.findViewById(R.id.txtUcretBeklentisi);
        txtKapasiteler = generalView.findViewById(R.id.txtKapasiteler);
        txtYas = generalView.findViewById(R.id.txtYas);
        txtEhliyet = generalView.findViewById(R.id.txtEhliyet);
        txtSrc = generalView.findViewById(R.id.txtSrc);
        txtAylikFiyati = generalView.findViewById(R.id.txtAylikFiyati);
        txtHaftalikFiyati = generalView.findViewById(R.id.txtHaftalikFiyati);
        txtVitesTipi = generalView.findViewById(R.id.txtVitesTipi);
        txtYakitTipi = generalView.findViewById(R.id.txtYakitTipi);
        txtKaskoDurum = generalView.findViewById(R.id.txtKaskoDurum);
        txtParcaMarkasi = generalView.findViewById(R.id.txtParcaMarkasi);
        txtCikmaYedekParca = generalView.findViewById(R.id.txtCikmaYedekParca);
        txtYedekParcaDurumu = generalView.findViewById(R.id.txtYedekParcaDurumu);
        txtAracOzellikler = generalView.findViewById(R.id.txtAracOzellikler);
        txtBelgeler = generalView.findViewById(R.id.txtBelgeler);


        linMarka = generalView.findViewById(R.id.linMarka);
        linModel = generalView.findViewById(R.id.linModel);
        linAltModel = generalView.findViewById(R.id.linAltModel);
        linYil = generalView.findViewById(R.id.linYil);
        linKapasite = generalView.findViewById(R.id.linKapasite);
        linBaslamaSaat = generalView.findViewById(R.id.linBaslamaSaat);
        linBitisSaat = generalView.findViewById(R.id.linBitisSaat);
        linBaslamaKonum = generalView.findViewById(R.id.linBaslamaKonum);
        linBitisKonum = generalView.findViewById(R.id.linBitisKonum);
        linFirmaGiris = generalView.findViewById(R.id.linFirmaGiris);
        linFirmaCikis = generalView.findViewById(R.id.linFirmaCikis);
        linToplamKM = generalView.findViewById(R.id.linToplamKM);
        linGunSayisi = generalView.findViewById(R.id.linGunSayisi);
        linMotorHacim = generalView.findViewById(R.id.linMotorHacim);
        linMotorGuc = generalView.findViewById(R.id.linMotorGuc);
        linKimden = generalView.findViewById(R.id.linKimden);
        linAracDurum = generalView.findViewById(R.id.linAracDurum);
        linTecrube = generalView.findViewById(R.id.linTecrube);
        linPlaka = generalView.findViewById(R.id.linPlaka);
        linReferans = generalView.findViewById(R.id.linReferans);
        linUcretBeklentisi = generalView.findViewById(R.id.linUcretBeklentisi);
        linKapasiteler = generalView.findViewById(R.id.linKapasiteler);
        linYas = generalView.findViewById(R.id.linYas);
        linEhliyet = generalView.findViewById(R.id.linEhliyet);
        linSrc = generalView.findViewById(R.id.linSrc);
        linAylikFiyat = generalView.findViewById(R.id.linAylikFiyat);
        linHaftalikFiyat = generalView.findViewById(R.id.linHaftalikFiyat);
        linVitesTipi = generalView.findViewById(R.id.linVitesTipi);
        linYakitTipi = generalView.findViewById(R.id.linYakitTipi);
        linKaskoDurum = generalView.findViewById(R.id.linKaskoDurum);
        linParcaMarkasi = generalView.findViewById(R.id.linParcaMarkasi);
        linCikmaYedekParca = generalView.findViewById(R.id.linCikmaYedekParca);
        linYedekParcaDurumu = generalView.findViewById(R.id.linYedekParcaDurumu);
        linAracOzellikleri = generalView.findViewById(R.id.linAracOzellikleri);
        linBelgeler = generalView.findViewById(R.id.linBelgeler);

        viewPagerPhotos = generalView.findViewById(R.id.viewPagerPhotos);
        imgNoPhoto = generalView.findViewById(R.id.imgNoPhoto);
        fabIcon = generalView.findViewById(R.id.fabIcon);




        HashMap<String , Object> hashMap = new HashMap<>();
        HashMap<String , String> hashMap1 = new HashMap<>();

        hashMap1.put("ID" , ilanID);

        hashMap.put("param" , hashMap1);
        hashMap.put("Token" , userToken);


        Call<IlanDetayResponse> ilanDetayResponseCal = App.getApiService().ilanDetay( hashMap);

        ilanDetayResponseCal.enqueue(new Callback<IlanDetayResponse>() {
            @Override
            public void onResponse(Call<IlanDetayResponse> call, Response<IlanDetayResponse> response) {

                IlanDetayResponseDetail detail = response.body().getDetail();
                String token = detail.getResult();

                JSONObject ilan = Utils.jwtToJsonObject(token);


                try {
                    JSONObject ilanDetay = ilan.getJSONObject("OutPutMessage").getJSONObject("Data");

                    if(ilanDetay.has("ilanAciklamasi"))
                    {
                        Aciklama = ilanDetay.getString("ilanAciklamasi");
                        Log.i("Açıklama" , Aciklama);
                    }
                    else
                    {
                        Aciklama = "-";
                        Log.i("Açıklama" , Aciklama);
                    }

                    if(ilanDetay.has("Ucret"))
                    {
                        Fiyat = ilanDetay.getString("Ucret");
                        Log.i("Fiyat" , Fiyat);
                    }
                    else
                    {
                        Fiyat = "-";
                        Log.i("Fiyat" , Fiyat);
                    }

                    if(ilanDetay.getJSONObject("Users").has("UserName") && ilanDetay.getJSONObject("Users").has("SurName"))
                    {
                        IlanSahibi = ilanDetay.getJSONObject("Users").getString("UserName").concat(" ").concat(ilanDetay.getJSONObject("Users").getString("SurName"));
                        Log.i("IlanSahibi" , IlanSahibi);
                    }
                    else
                    {
                        IlanSahibi = "-";
                        Log.i("IlanSahibi" , IlanSahibi);
                    }

                    if(ilanDetay.has("ilanCityText") && ilanDetay.has("ilanSemtleriText"))
                    {
                        Konum = ilanDetay.getString("ilanCityText").concat("/").concat(ilanDetay.getString("ilanSemtleriText"));
                        Log.i("Konum" , Konum);
                    }
                    else
                    {
                        Konum = "-";
                        Log.i("Konum" , Konum);
                    }

                    if(ilanDetay.has("ID"))
                    {
                        IlanNo = ilanDetay.getString("ID");
                        Log.i("IlanNo" , IlanNo);
                    }
                    else
                    {
                        IlanNo = "-";
                        Log.i("IlanNo" , IlanNo);
                    }

                    if(ilanDetay.has("create_at"))
                    {
                        String tmp2 = ilanDetay.getString("create_at");
                        String [] tmp = tmp2.split("\\+s");
                        IlanTarih = tmp[0];
                        Log.i("IlanTarih" , IlanTarih);
                    }
                    else
                    {
                        IlanTarih = "-";
                        Log.i("IlanTarih" , IlanTarih);
                    }

                    if(ilanDetay.has("AracMarkasiText"))
                    {
                        AracMarkasi = ilanDetay.getString("AracMarkasiText");
                        Log.i("AracMarkasi" , AracMarkasi);
                    }
                    else
                    {
                        AracMarkasi = "-";
                        Log.i("AracMarkasi" , AracMarkasi);
                    }

                    if(ilanDetay.has("AracModeliText"))
                    {
                        AracModel = ilanDetay.getString("AracModeliText");
                        Log.i("AracModel" , AracModel);
                    }
                    else
                    {
                        AracModel = "-";
                        Log.i("AracModel" , AracModel);
                    }

                    if(ilanDetay.has("AracSubModeli"))
                    {
                        AracAltModel = ilanDetay.getString("AracSubModeli");
                        Log.i("AracAltModel" , AracAltModel);
                    }
                    else
                    {
                        AracAltModel = "-";
                        Log.i("AracAltModel" , AracAltModel);
                    }

                    if(ilanDetay.has("AracYili"))
                    {
                        AracYil = ilanDetay.getString("AracYili");
                        Log.i("AracYil" , AracYil);
                    }
                    else
                    {
                        AracYil = "-";
                        Log.i("AracYil" , AracYil);
                    }

                    if(ilanDetay.has("AracKapasiteText"))
                    {
                        AracKapasite = ilanDetay.getString("AracKapasiteText");
                        Log.i("AracKapasite" , AracKapasite);
                    }
                    else
                    {
                        AracKapasite = "-";
                        Log.i("AracKapasite" , AracKapasite);
                    }

                    if(ilanDetay.has("ServiseBaslamaSaati"))
                    {
                        ServisBaslamaSaat = ilanDetay.getString("ServiseBaslamaSaati");
                        Log.i("ServisBaslamaSaat" , ServisBaslamaSaat);
                    }
                    else
                    {
                        ServisBaslamaSaat = "-";
                        Log.i("ServisBaslamaSaat" , ServisBaslamaSaat);
                    }

                    if(ilanDetay.has("ServisBitisSaati"))
                    {
                        ServisBitisSaat = ilanDetay.getString("ServisBitisSaati");
                        Log.i("ServisBitisSaat" , ServisBitisSaat);
                    }
                    else
                    {
                        ServisBitisSaat = "-";
                        Log.i("ServisBitisSaat" , ServisBitisSaat);
                    }

                    if(ilanDetay.has("ServiseBaslamaCityText") && ilanDetay.has("ServiseBaslamaSemtleriText"))
                    {
                        ServisBaslamaKonum = ilanDetay.getString("ServiseBaslamaCityText").concat("/").concat(ilanDetay.getString("ServiseBaslamaSemtleriText"));
                        Log.i("ServisBaslamaKonum" , ServisBaslamaKonum);
                    }
                    else
                    {
                        ServisBaslamaKonum = "-";
                        Log.i("ServisBaslamaKonum" , ServisBaslamaKonum);
                    }

                    if(ilanDetay.has("ServisBitisCityText") && ilanDetay.has("ServisBitisSemtleriText"))
                    {
                        ServisBitisKonum = ilanDetay.getString("ServisBitisCityText").concat("/").concat(ilanDetay.getString("ServisBitisSemtleriText"));
                        Log.i("ServisBitisKonum" , ServisBitisKonum);
                    }
                    else
                    {
                        ServisBitisKonum = "-";
                        Log.i("ServisBitisKonum" , ServisBitisKonum);
                    }

                    if(ilanDetay.has("FirmayaGirisSaati"))
                    {
                        FirmaGirisSaat = ilanDetay.getString("FirmayaGirisSaati");
                        Log.i("FirmaGirisSaat" , FirmaGirisSaat);
                    }
                    else
                    {
                        FirmaGirisSaat = "-";
                        Log.i("FirmaGirisSaat" , FirmaGirisSaat);
                    }

                    if(ilanDetay.has("FirmadanCikisSaati"))
                    {
                        FirmaCikisSaat = ilanDetay.getString("FirmadanCikisSaati");
                        Log.i("FirmaCikisSaat" , FirmaCikisSaat);
                    }
                    else
                    {
                        FirmaCikisSaat = "-";
                        Log.i("FirmaCikisSaat" , FirmaCikisSaat);
                    }

                    if(ilanDetay.has("ToplamKM"))
                    {
                        ToplamKM = ilanDetay.getString("ToplamKM");
                        Log.i("ToplamKM" , ToplamKM);
                    }
                    else
                    {
                        ToplamKM = "-";
                        Log.i("ToplamKM" , ToplamKM);
                    }

                    if(ilanDetay.has("CalisilacakGunSayisi"))
                    {
                        GunSayisi = ilanDetay.getString("CalisilacakGunSayisi");
                        Log.i("GunSayisi" , GunSayisi);
                    }
                    else
                    {
                        GunSayisi = "-";
                        Log.i("GunSayisi" , GunSayisi);
                    }

                    if(ilanDetay.has("MotorHacmiText"))
                    {
                        MotorHacim = ilanDetay.getString("MotorHacmiText");
                        Log.i("MotorHacim" , MotorHacim);
                    }
                    else
                    {
                        MotorHacim = "-";
                        Log.i("MotorHacim" , MotorHacim);
                    }

                    if(ilanDetay.has("MotorGucuText"))
                    {
                        MotorGuc = ilanDetay.getString("MotorGucuText");
                        Log.i("MotorGuc" , MotorGuc);
                    }
                    else
                    {
                        MotorGuc = "-";
                        Log.i("MotorGuc" , MotorGuc);
                    }

                    if(ilanDetay.has("Kimden"))
                    {
                        Kimden = ilanDetay.getString("Kimden");
                        Log.i("Kimden" , Kimden);
                    }
                    else
                    {
                        Kimden = "-";
                        Log.i("Kimden" , Kimden);
                    }

                    if(ilanDetay.has("AracDurumu"))
                    {
                        AracDurumu = ilanDetay.getString("AracDurumu");
                        Log.i("AracDurumu" , AracDurumu);
                    }
                    else
                    {
                        AracDurumu = "-";
                        Log.i("AracDurumu" , AracDurumu);
                    }

                    if(ilanDetay.has("Tecrube"))
                    {
                        Tecrube = ilanDetay.getString("Tecrube");
                        Log.i("Tecrube" , Tecrube);
                    }
                    else
                    {
                        Tecrube = "-";
                        Log.i("Tecrube" , Tecrube);
                    }

                    if(ilanDetay.has("Plaka"))
                    {
                        Plaka = ilanDetay.getString("Plaka");
                        Log.i("Plaka" , Plaka);
                    }
                    else
                    {
                        Plaka = "-";
                        Log.i("Plaka" , Plaka);
                    }

                    if(ilanDetay.has("Referanslar"))
                    {
                        Referans = ilanDetay.getString("Referanslar");
                        Log.i("Referans" , Referans);
                    }
                    else
                    {
                        Referans = "-";
                        Log.i("Referans" , Referans);
                    }

                    if(ilanDetay.has("UcretBeklentisi"))
                    {
                        UcretBeklentisi = ilanDetay.getString("UcretBeklentisi");
                        Log.i("UcretBeklentisi" , UcretBeklentisi);
                    }
                    else
                    {
                        UcretBeklentisi = "-";
                        Log.i("UcretBeklentisi" , UcretBeklentisi);
                    }

                    if(ilanDetay.has("KullanabildiginizKapasiteler"))
                    {
                        Kapasiteler = ilanDetay.getString("KullanabildiginizKapasiteler");
                        Log.i("Kapasiteler" , Kapasiteler);
                    }
                    else
                    {
                        Kapasiteler = "-";
                        Log.i("Kapasiteler" , Kapasiteler);
                    }

                    if(ilanDetay.has("Yasiniz"))
                    {
                        Yas = ilanDetay.getString("Yasiniz");
                        Log.i("Yas" , Yas);
                    }
                    else
                    {
                        Yas = "-";
                        Log.i("Yas" , Yas);
                    }

                    if(ilanDetay.has("Ehliyetiniz"))
                    {
                        Ehliyet = ilanDetay.getString("Ehliyetiniz");
                        Log.i("Ehliyet" , Ehliyet);
                    }
                    else
                    {
                        Ehliyet = "-";
                        Log.i("Ehliyet" , Ehliyet);
                    }

                    if(ilanDetay.has("SRC"))
                    {
                        Src = ilanDetay.getString("SRC");
                        Log.i("Src" , Src);
                    }
                    else
                    {
                        Src = "-";
                        Log.i("Src" , Src);
                    }

                    if(ilanDetay.has("AylikFiyat"))
                    {
                        AylikFiyati = ilanDetay.getString("AylikFiyat");
                        Log.i("AylikFiyati" , AylikFiyati);
                    }
                    else
                    {
                        AylikFiyati = "-";
                        Log.i("AylikFiyati" , AylikFiyati);
                    }

                    if(ilanDetay.has("HaftalikFiyat"))
                    {
                        HaftalikFiyati = ilanDetay.getString("HaftalikFiyat");
                        Log.i("HaftalikFiyati" , HaftalikFiyati);
                    }
                    else
                    {
                        HaftalikFiyati = "-";
                        Log.i("HaftalikFiyati" , HaftalikFiyati);
                    }

                    if(ilanDetay.has("VitesTipi"))
                    {
                        VitesTipi = ilanDetay.getString("VitesTipi");
                        Log.i("VitesTipi" , VitesTipi);
                    }
                    else
                    {
                        VitesTipi = "-";
                        Log.i("VitesTipi" , VitesTipi);
                    }

                    if(ilanDetay.has("YakitTipi"))
                    {
                        YakitTipi = ilanDetay.getString("YakitTipi");
                        Log.i("YakitTipi" , YakitTipi);
                    }
                    else
                    {
                        YakitTipi = "-";
                        Log.i("YakitTipi" , YakitTipi);
                    }

                    if(ilanDetay.has("Kasko"))
                    {
                        KaskoDurum = ilanDetay.getString("Kasko");
                        Log.i("KaskoDurum" , KaskoDurum);
                    }
                    else
                    {
                        KaskoDurum = "-";
                        Log.i("KaskoDurum" , KaskoDurum);
                    }

                    if(ilanDetay.has("ParcaMarkasi"))
                    {
                        ParcaMarkasi = ilanDetay.getString("ParcaMarkasi");
                        Log.i("ParcaMarkasi" , ParcaMarkasi);
                    }
                    else
                    {
                        ParcaMarkasi = "-";
                        Log.i("ParcaMarkasi" , ParcaMarkasi);
                    }

                    if(ilanDetay.has("CikmaYedekParca"))
                    {
                        CikmaYedekParca = ilanDetay.getString("CikmaYedekParca");
                        Log.i("CikmaYedekParca" , CikmaYedekParca);
                    }
                    else
                    {
                        CikmaYedekParca = "-";
                        Log.i("CikmaYedekParca" , CikmaYedekParca);
                    }

                    if(ilanDetay.has("YedekParcaDurum"))
                    {
                        YedekParcaDurumu = ilanDetay.getString("YedekParcaDurum");
                        Log.i("YedekParcaDurumu" , YedekParcaDurumu);
                    }
                    else
                    {
                        YedekParcaDurumu = "-";
                        Log.i("YedekParcaDurumu" , YedekParcaDurumu);
                    }

                    if(ilanDetay.has("AracOzellikleriText"))
                    {
                        Ozellikler = ilanDetay.getString("AracOzellikleriText");
                        Log.i("Ozellikler" , Ozellikler);
                    }
                    else
                    {
                        Ozellikler = "-";
                        Log.i("Ozellikler" , Ozellikler);
                    }

                    if(ilanDetay.has("Belgeler"))
                    {
                        Belgeler = ilanDetay.getString("Belgeler");
                        Log.i("Belgeler" , Belgeler);
                    }
                    else
                    {
                        Belgeler = "-";
                        Log.i("Belgeler" , Belgeler);
                    }

                    if(ilanDetay.has("Resimler"))
                    {
                        Resimler = ilanDetay.getString("Resimler");
                        Log.i("Resimler" , Resimler);

                        if(Resimler.contains("|"))
                        {
                            String [] images = Resimler.split(Pattern.quote("|"));
                            ViewPagerAdapter adapter = new ViewPagerAdapter(ctx ,images);
                            viewPagerPhotos.setAdapter(adapter);
                        }
                        else
                        {
                            String [] images = new String[1];
                            images[0] = Resimler;
                            ViewPagerAdapter adapter = new ViewPagerAdapter(ctx ,images);
                            viewPagerPhotos.setAdapter(adapter);
                        }

                    }
                    else
                    {
                        Resimler = "-";
                        Log.i("Resimler" , Resimler);
                        viewPagerPhotos.setVisibility(View.INVISIBLE);
                        imgNoPhoto.setVisibility(View.VISIBLE);
                    }

                    String ilanKategori = ilanDetay.getString("Tipi");

                    if(ilanKategori.equals("1"))
                    {
                        aracaIsYukle();
                    }
                    if(ilanKategori.equals("2"))
                    {
                        aracaSoforYukle();
                    }
                    if(ilanKategori.equals("3"))
                    {
                        iseAracYukle();
                    }
                    if(ilanKategori.equals("4"))
                    {
                        soforeIsYukle();
                    }
                    if(ilanKategori.equals("5"))
                    {
                        satilikAracYukle();
                    }
                    if(ilanKategori.equals("6"))
                    {
                        kiralikAracYukle();
                    }
                    if(ilanKategori.equals("7"))
                    {
                        yedekParcaYukle();
                    }




                    /*Aciklama = ilanDetay.getString("ilanAciklamasi");
                    Fiyat = ilanDetay.getString("Ucret");
                    IlanSahibi = ilanDetay.getJSONObject("Users").getString("UserName").concat(" ").concat(ilanDetay.getJSONObject("Users").getString("SurName"));
                    Konum = ilanDetay.getString("ilanCityText").concat("/").concat(ilanDetay.getString("ilanSemtleriText"));
                    IlanNo = ilanDetay.getString("ID");
                    IlanTarih = ilanDetay.getString("create_at");
                    AracMarkasi = ilanDetay.getString("AracMarkasiText");
                    AracModel = ilanDetay.getString("AracModeliText");
                    AracAltModel = ilanDetay.getString("AracSubModeli");
                    AracYil = ilanDetay.getString("AracYili");
                    AracKapasite = ilanDetay.getString("AracKapasiteText");
                    ServisBaslamaSaat = ilanDetay.getString("ServiseBaslamaSaati");
                    ServisBitisSaat = ilanDetay.getString("ServisBitisSaati");
                    ServisBaslamaKonum = ilanDetay.getString("ServiseBaslamaCityText").concat("/").concat(ilanDetay.getString("ServiseBaslamaSemtleriText"));
                    ServisBitisKonum = ilanDetay.getString("ServisBitisCityText").concat("/").concat(ilanDetay.getString("ServisBitisSemtleriText"));
                    FirmaGirisSaat = ilanDetay.getString("FirmayaGirisSaati");
                    FirmaCikisSaat = ilanDetay.getString("FirmadanCikisSaati");
                    ToplamKM = ilanDetay.getString("ToplamKM");
                    GunSayisi = ilanDetay.getString("CalisilacakGunSayisi");
                    MotorHacim = ilanDetay.getString("MotorHacmiText");
                    MotorGuc = ilanDetay.getString("MotorGucuText");
                    Kimden = ilanDetay.getString("Kimden");
                    AracDurumu = ilanDetay.getString("AracDurumu");
                    Tecrube = ilanDetay.getString("Tecrube");
                    Plaka = ilanDetay.getString("Plaka");
                    Referans = ilanDetay.getString("Referanslar");
                    UcretBeklentisi = ilanDetay.getString("UcretBeklentisi");
                    Kapasiteler = ilanDetay.getString("KullanabildiginizKapasiteler");
                    Yas = ilanDetay.getString("Yasiniz");
                    Ehliyet = ilanDetay.getString("Ehliyetiniz");
                    Src = ilanDetay.getString("SRC");
                    AylikFiyati = ilanDetay.getString("AylikFiyat");
                    HaftalikFiyati = ilanDetay.getString("HaftalikFiyat");
                    VitesTipi = ilanDetay.getString("VitesTipi");
                    YakitTipi = ilanDetay.getString("YakitTipi");
                    KaskoDurum = ilanDetay.getString("Kasko");
                    ParcaMarkasi = ilanDetay.getString("ParcaMarkasi");
                    CikmaYedekParca = ilanDetay.getString("CikmaYedekParca");
                    YedekParcaDurumu = ilanDetay.getString("YedekParcaDurum");*/



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.i("Başardın" , "Başarılı");
            }

            @Override
            public void onFailure(Call<IlanDetayResponse> call, Throwable t) {

                Log.i("Error" , t.getMessage());

            }
        });

        return rootView;
    }

    private void iseAracYukle()
    {
        txtIlanNo.setText(IlanNo);
        txtIlanSahibi.setText(IlanSahibi);
        txtIlanTarih.setText(IlanTarih);
        txtIlanTamKonum.setText(Konum);
        txtIlanDetayAciklama.setText(Aciklama);
        txtAracMarkasi.setText(AracMarkasi);
        txtAracModel.setText(AracModel);
        txtAracYil.setText(AracYil);
        txtAracKapasite.setText(AracKapasite);
        txtAracOzellikler.setText(Ozellikler);
        txtServisBaslamaKonum.setText(ServisBaslamaKonum);
        txtServisBaslamaSaat.setText(ServisBaslamaSaat);
        txtFirmaGirisSaat.setText(FirmaGirisSaat);
        txtFirmaCikisSaat.setText(FirmaCikisSaat);
        txtServisBitisSaat.setText(ServisBitisSaat);
        txtServisBitisKonum.setText(ServisBitisKonum);
        txtToplamKM.setText(ToplamKM);
        txtGunSayisi.setText(GunSayisi);
        txtFiyat.setText(Fiyat);

        linAltModel.setVisibility(View.GONE);
        linMotorHacim.setVisibility(View.GONE);
        linMotorGuc.setVisibility(View.GONE);
        linKimden.setVisibility(View.GONE);
        linAracDurum.setVisibility(View.GONE);
        linTecrube.setVisibility(View.GONE);
        linPlaka.setVisibility(View.GONE);
        linReferans.setVisibility(View.GONE);
        linUcretBeklentisi.setVisibility(View.GONE);
        linKapasiteler.setVisibility(View.GONE);
        linYas.setVisibility(View.GONE);
        linEhliyet.setVisibility(View.GONE);
        linAylikFiyat.setVisibility(View.GONE);
        linHaftalikFiyat.setVisibility(View.GONE);
        linVitesTipi.setVisibility(View.GONE);
        linYakitTipi.setVisibility(View.GONE);
        linKaskoDurum.setVisibility(View.GONE);
        linParcaMarkasi.setVisibility(View.GONE);
        linCikmaYedekParca.setVisibility(View.GONE);
        linYedekParcaDurumu.setVisibility(View.GONE);
        linBelgeler.setVisibility(View.GONE);


    }

    private void aracaIsYukle()
    {
        txtIlanNo.setText(IlanNo);
        txtIlanSahibi.setText(IlanSahibi);
        txtIlanTarih.setText(IlanTarih);
        txtIlanTamKonum.setText(Konum);
        txtAracMarkasi.setText(AracMarkasi);
        txtAracModel.setText(AracModel);
        txtAracYil.setText(AracYil);
        txtAracKapasite.setText(AracKapasite);
        txtAracOzellikler.setText(Ozellikler);
        txtServisBaslamaKonum.setText(ServisBaslamaKonum);
        txtServisBaslamaSaat.setText(ServisBaslamaSaat);
        txtServisBitisSaat.setText(ServisBitisSaat);
        txtToplamKM.setText(ToplamKM);
        txtGunSayisi.setText(GunSayisi);
        txtIlanDetayAciklama.setText(Aciklama);
        txtFiyat.setText(Fiyat);
        txtTecrube.setText(Tecrube);
        txtPlaka.setText(Plaka);
        txtReferans.setText(Referans);
        txtAracDurumu.setText(AracDurumu);


        linYedekParcaDurumu.setVisibility(View.GONE);
        linCikmaYedekParca.setVisibility(View.GONE);
        linParcaMarkasi.setVisibility(View.GONE);
        linKaskoDurum.setVisibility(View.GONE);
        linYakitTipi.setVisibility(View.GONE);
        linVitesTipi.setVisibility(View.GONE);
        linHaftalikFiyat.setVisibility(View.GONE);
        linAylikFiyat.setVisibility(View.GONE);
        linSrc.setVisibility(View.GONE);
        linEhliyet.setVisibility(View.GONE);
        linYas.setVisibility(View.GONE);
        linKapasiteler.setVisibility(View.GONE);
        linUcretBeklentisi.setVisibility(View.GONE);
        linKimden.setVisibility(View.GONE);
        linMotorGuc.setVisibility(View.GONE);
        linMotorHacim.setVisibility(View.GONE);
        linFirmaCikis.setVisibility(View.GONE);
        linFirmaGiris.setVisibility(View.GONE);
        linBitisKonum.setVisibility(View.GONE);
        linAltModel.setVisibility(View.GONE);
        linBelgeler.setVisibility(View.GONE);
    }

    private void aracaSoforYukle()
    {

        txtIlanNo.setText(IlanNo);
        txtIlanSahibi.setText(IlanSahibi);
        txtIlanTarih.setText(IlanTarih);
        txtIlanDetayAciklama.setText(Aciklama);
        txtIlanTamKonum.setText(Konum);
        txtAracKapasite.setText(AracKapasite);
        txtServisBaslamaKonum.setText(ServisBaslamaKonum);
        txtServisBaslamaSaat.setText(ServisBaslamaSaat);
        txtServisBitisSaat.setText(ServisBitisSaat);
        txtServisBitisKonum.setText(ServisBitisKonum);
        txtFirmaGirisSaat.setText(FirmaGirisSaat);
        txtFirmaCikisSaat.setText(FirmaCikisSaat);
        txtFiyat.setText(Fiyat);
        txtGunSayisi.setText(GunSayisi);
        txtTecrube.setText(Tecrube);


        linMarka.setVisibility(View.GONE);
        linModel.setVisibility(View.GONE);
        linAltModel.setVisibility(View.GONE);
        linYil.setVisibility(View.GONE);
        linMotorHacim.setVisibility(View.GONE);
        linMotorGuc.setVisibility(View.GONE);
        linKimden.setVisibility(View.GONE);
        linAracDurum.setVisibility(View.GONE);
        linPlaka.setVisibility(View.GONE);
        linReferans.setVisibility(View.GONE);
        linUcretBeklentisi.setVisibility(View.GONE);
        linKapasiteler.setVisibility(View.GONE);
        linYas.setVisibility(View.GONE);
        linEhliyet.setVisibility(View.GONE);
        linSrc.setVisibility(View.GONE);
        linAylikFiyat.setVisibility(View.GONE);
        linHaftalikFiyat.setVisibility(View.GONE);
        linVitesTipi.setVisibility(View.GONE);
        linYakitTipi.setVisibility(View.GONE);
        linKaskoDurum.setVisibility(View.GONE);
        linParcaMarkasi.setVisibility(View.GONE);
        linCikmaYedekParca.setVisibility(View.GONE);
        linYedekParcaDurumu.setVisibility(View.GONE);
        linAracOzellikleri.setVisibility(View.GONE);
        linBelgeler.setVisibility(View.GONE);


    }

    private void soforeIsYukle()
    {
        txtIlanNo.setText(IlanNo);
        txtIlanSahibi.setText(IlanSahibi);
        txtIlanTarih.setText(IlanTarih);
        txtIlanTamKonum.setText(Konum);
        txtIlanDetayAciklama.setText(Aciklama);
        txtYas.setText(Yas);
        txtFiyat.setText(Fiyat);
        txtKapasiteler.setText(Kapasiteler);
        txtServisBaslamaKonum.setText(ServisBaslamaKonum);
        txtServisBaslamaSaat.setText(ServisBaslamaSaat);
        txtTecrube.setText(Tecrube);
        txtEhliyet.setText(Ehliyet);
        txtBelgeler.setText(Belgeler);


        linMarka.setVisibility(View.GONE);
        linModel.setVisibility(View.GONE);
        linAltModel.setVisibility(View.GONE);
        linYil.setVisibility(View.GONE);
        linKapasite.setVisibility(View.GONE);
        linBitisKonum.setVisibility(View.GONE);
        linFirmaGiris.setVisibility(View.GONE);
        linFirmaCikis.setVisibility(View.GONE);
        linToplamKM.setVisibility(View.GONE);
        linGunSayisi.setVisibility(View.GONE);
        linMotorHacim.setVisibility(View.GONE);
        linMotorGuc.setVisibility(View.GONE);
        linKimden.setVisibility(View.GONE);
        linAracDurum.setVisibility(View.GONE);
        linPlaka.setVisibility(View.GONE);
        linReferans.setVisibility(View.GONE);
        linUcretBeklentisi.setVisibility(View.GONE);
        linSrc.setVisibility(View.GONE);
        linAylikFiyat.setVisibility(View.GONE);
        linHaftalikFiyat.setVisibility(View.GONE);
        linVitesTipi.setVisibility(View.GONE);
        linYakitTipi.setVisibility(View.GONE);
        linKaskoDurum.setVisibility(View.GONE);
        linParcaMarkasi.setVisibility(View.GONE);
        linCikmaYedekParca.setVisibility(View.GONE);
        linYedekParcaDurumu.setVisibility(View.GONE);
        linAracOzellikleri.setVisibility(View.GONE);


    }

    private void satilikAracYukle()
    {
        txtIlanNo.setText(IlanNo);
        txtIlanSahibi.setText(IlanSahibi);
        txtIlanTarih.setText(IlanTarih);
        txtFiyat.setText(Fiyat);
        txtIlanTamKonum.setText(Konum);
        txtIlanDetayAciklama.setText(Aciklama);
        txtAracMarkasi.setText(AracMarkasi);
        txtAracModel.setText(AracModel);
        txtAracAltModel.setText(AracAltModel);
        txtAracYil.setText(AracYil);
        txtMotorGuc.setText(MotorGuc);
        txtMotorHacim.setText(MotorHacim);
        txtAracKapasite.setText(AracKapasite);
        txtToplamKM.setText(ToplamKM);
        txtKimden.setText(Kimden);


        linBaslamaSaat.setVisibility(View.GONE);
        linBitisSaat.setVisibility(View.GONE);
        linBaslamaKonum.setVisibility(View.GONE);
        linBitisKonum.setVisibility(View.GONE);
        linFirmaGiris.setVisibility(View.GONE);
        linFirmaCikis.setVisibility(View.GONE);
        linGunSayisi.setVisibility(View.GONE);
        linAracDurum.setVisibility(View.GONE);
        linTecrube.setVisibility(View.GONE);
        linPlaka.setVisibility(View.GONE);
        linReferans.setVisibility(View.GONE);
        linUcretBeklentisi.setVisibility(View.GONE);
        linKapasiteler.setVisibility(View.GONE);
        linYas.setVisibility(View.GONE);
        linEhliyet.setVisibility(View.GONE);
        linSrc.setVisibility(View.GONE);
        linAylikFiyat.setVisibility(View.GONE);
        linHaftalikFiyat.setVisibility(View.GONE);
        linVitesTipi.setVisibility(View.GONE);
        linYakitTipi.setVisibility(View.GONE);
        linKaskoDurum.setVisibility(View.GONE);
        linParcaMarkasi.setVisibility(View.GONE);
        linCikmaYedekParca.setVisibility(View.GONE);
        linYedekParcaDurumu.setVisibility(View.GONE);
        linAracOzellikleri.setVisibility(View.GONE);
        linBelgeler.setVisibility(View.GONE);

    }

    private void kiralikAracYukle()
    {
        txtIlanNo.setText(IlanNo);
        txtIlanSahibi.setText(IlanSahibi);
        txtIlanTarih.setText(IlanTarih);
        txtFiyat.setText(Fiyat);
        txtIlanTamKonum.setText(Konum);
        txtIlanDetayAciklama.setText(Aciklama);
        txtAracMarkasi.setText(AracMarkasi);
        txtAracModel.setText(AracModel);
        txtAracKapasite.setText(AracKapasite);
        txtAracYil.setText(AracYil);
        txtHaftalikFiyati.setText(HaftalikFiyati);
        txtAylikFiyati.setText(AylikFiyati);
        txtVitesTipi.setText(VitesTipi);
        txtYakitTipi.setText(YakitTipi);
        txtKaskoDurum.setText(KaskoDurum);


        linAltModel.setVisibility(View.GONE);
        linYil.setVisibility(View.GONE);
        linBaslamaSaat.setVisibility(View.GONE);
        linBitisSaat.setVisibility(View.GONE);
        linBaslamaKonum.setVisibility(View.GONE);
        linBitisKonum.setVisibility(View.GONE);
        linFirmaGiris.setVisibility(View.GONE);
        linFirmaCikis.setVisibility(View.GONE);
        linToplamKM.setVisibility(View.GONE);
        linGunSayisi.setVisibility(View.GONE);
        linMotorHacim.setVisibility(View.GONE);
        linMotorGuc.setVisibility(View.GONE);
        linKimden.setVisibility(View.GONE);
        linAracDurum.setVisibility(View.GONE);
        linTecrube.setVisibility(View.GONE);
        linPlaka.setVisibility(View.GONE);
        linReferans.setVisibility(View.GONE);
        linUcretBeklentisi.setVisibility(View.GONE);
        linKapasiteler.setVisibility(View.GONE);
        linYas.setVisibility(View.GONE);
        linEhliyet.setVisibility(View.GONE);
        linSrc.setVisibility(View.GONE);
        linParcaMarkasi.setVisibility(View.GONE);
        linCikmaYedekParca.setVisibility(View.GONE);
        linYedekParcaDurumu.setVisibility(View.GONE);
        linAracOzellikleri.setVisibility(View.GONE);
        linBelgeler.setVisibility(View.GONE);

    }

    private void yedekParcaYukle()
    {

        txtIlanNo.setText(IlanNo);
        txtIlanSahibi.setText(IlanSahibi);
        txtIlanTarih.setText(IlanTarih);
        txtFiyat.setText(Fiyat);
        txtIlanTamKonum.setText(Konum);
        txtYedekParcaDurumu.setText(YedekParcaDurumu);
        txtParcaMarkasi.setText(ParcaMarkasi);

        linMarka.setVisibility(View.GONE);
        linModel.setVisibility(View.GONE);
        linAltModel.setVisibility(View.GONE);
        linYil.setVisibility(View.GONE);
        linKapasite.setVisibility(View.GONE);
        linBaslamaSaat.setVisibility(View.GONE);
        linBitisSaat.setVisibility(View.GONE);
        linBaslamaKonum.setVisibility(View.GONE);
        linBitisKonum.setVisibility(View.GONE);
        linFirmaGiris.setVisibility(View.GONE);
        linFirmaCikis.setVisibility(View.GONE);
        linToplamKM.setVisibility(View.GONE);
        linGunSayisi.setVisibility(View.GONE);
        linMotorHacim.setVisibility(View.GONE);
        linMotorGuc.setVisibility(View.GONE);
        linKimden.setVisibility(View.GONE);
        linAracDurum.setVisibility(View.GONE);
        linTecrube.setVisibility(View.GONE);
        linPlaka.setVisibility(View.GONE);
        linReferans.setVisibility(View.GONE);
        linUcretBeklentisi.setVisibility(View.GONE);
        linKapasiteler.setVisibility(View.GONE);
        linYas.setVisibility(View.GONE);
        linEhliyet.setVisibility(View.GONE);
        linSrc.setVisibility(View.GONE);
        linAylikFiyat.setVisibility(View.GONE);
        linHaftalikFiyat.setVisibility(View.GONE);
        linVitesTipi.setVisibility(View.GONE);
        linYakitTipi.setVisibility(View.GONE);
        linKaskoDurum.setVisibility(View.GONE);
        linAracOzellikleri.setVisibility(View.GONE);
        linBelgeler.setVisibility(View.GONE);

    }
}