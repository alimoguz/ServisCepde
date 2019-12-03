package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import serviscepde.com.tr.App;
import serviscepde.com.tr.DownloadClass;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.MarkaModel;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;


public class FiltreFragment extends Fragment {


    View generalView;
    private ArrayList<String> kategoriler = new ArrayList<>();

    private ImageView imgType;

    private AppCompatSpinner spinKategoriTip;

    private AutoCompleteTextView acFiltreIl,acFiltreIlce,acFiltreMarka,acFiltreModel,acFiltreKapasite,acFiltreAracDurum,acFiltreKullanilabilirKapasiteler
            ,acFiltreServisBaslamaIl,acFiltreServisBaslamaIlce,acFiltreServisBitisIl,acFiltreServisBitisIlce;

    private TextInputEditText edtFiltreMinYil,edtFiltreMaxYil,edtFiltreMinTecrube,edtFiltreMaxTecrube,edtFiltreMinYas,edtFiltreMaxYas;
    private LinearLayout linFiltreler;
    private TextView txtFiltreUygula;

    private Context ctx;
    private ArrayAdapter<String> kategoriAdapter;

    private List<City> sehirler = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private List<String> marka = new ArrayList<>();
    private List<String> model = new ArrayList<>();
    private String cityId,baslamaCityId,bitisCityId;
    private String townId,baslamaTownId,bitisTownId;
    private ArrayList<String> townNames , baslamaTownNames , bitisTownNames = new ArrayList<>();

    private String userToken;
    private String acIL,acIlce,acMarka,acModel,acKapasite,acAracDurum,acKullanabildiginizKapasiteler,acServisBaslamaIl,acServisBaslamaIlce,acServisBitisIl,acServisBitisIlce;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.filtre_fragment, container, false);

        generalView = rootView;
        ctx = generalView.getContext();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);



        imgType = generalView.findViewById(R.id.imgType);
        spinKategoriTip = generalView.findViewById(R.id.spinKategoriTip);
        linFiltreler = generalView.findViewById(R.id.linFiltreler);

        acFiltreIl = generalView.findViewById(R.id.acFiltreIl);
        acFiltreIlce = generalView.findViewById(R.id.acFiltreIlce);
        acFiltreMarka = generalView.findViewById(R.id.acFiltreMarka);
        acFiltreModel = generalView.findViewById(R.id.acFiltreModel);
        acFiltreKapasite = generalView.findViewById(R.id.acFiltreKapasite);
        acFiltreAracDurum = generalView.findViewById(R.id.acFiltreAracDurum);
        acFiltreKullanilabilirKapasiteler = generalView.findViewById(R.id.acFiltreKullanilabilirKapasiteler);
        acFiltreServisBaslamaIl = generalView.findViewById(R.id.acFiltreServisBaslamaIl);
        acFiltreServisBaslamaIlce = generalView.findViewById(R.id.acFiltreServisBaslamaIlce);
        acFiltreServisBitisIl = generalView.findViewById(R.id.acFiltreServisBitisIl);
        acFiltreServisBitisIlce = generalView.findViewById(R.id.acFiltreServisBitisIlce);

        edtFiltreMinYil = generalView.findViewById(R.id.edtFiltreMinYil);
        edtFiltreMaxYil = generalView.findViewById(R.id.edtFiltreMaxYil);
        edtFiltreMinTecrube = generalView.findViewById(R.id.edtFiltreMinTecrube);
        edtFiltreMaxTecrube = generalView.findViewById(R.id.edtFiltreMaxTecrube);
        edtFiltreMinYas = generalView.findViewById(R.id.edtFiltreMinYas);
        edtFiltreMaxYas = generalView.findViewById(R.id.edtFiltreMaxYas);

        txtFiltreUygula = generalView.findViewById(R.id.txtFiltreUygula);

        kategoriler.add("Tüm Kategoriler");
        kategoriler.add("İşime araç arıyorum");
        kategoriler.add("Aracıma iş arıyorum");
        kategoriler.add("Aracıma şoför arıyorum");
        kategoriler.add("Şoförüm iş arıyorum");
        kategoriler.add("Satılık araç");
        kategoriler.add("Kiralık araç");
        kategoriler.add("Yedek parça");


        kategoriAdapter = new ArrayAdapter<>(ctx , R.layout.support_simple_spinner_dropdown_item , kategoriler);
        spinKategoriTip.setAdapter(kategoriAdapter);

        sehirler = DownloadClass.getCities();
        cityNames = DownloadClass.getCityNames();
        marka = DownloadClass.getMarkaNames();

        Utils.setAutoCompleteAdapter(acFiltreIl , cityNames ,ctx);
        Utils.setAutoCompleteAdapter(acFiltreServisBaslamaIl , cityNames ,ctx);
        Utils.setAutoCompleteAdapter(acFiltreServisBitisIl , cityNames ,ctx);
        Utils.setAutoCompleteAdapter(acFiltreMarka , marka ,ctx);
        Utils.setAutoCompleteAdapter(acFiltreKapasite , App.getKapasite() , ctx);
        Utils.setAutoCompleteAdapter(acFiltreKullanilabilirKapasiteler , App.getKapasite() , ctx);
        Utils.setAutoCompleteAdapter(acFiltreAracDurum , App.getDurumu() , ctx);

        acFiltreMarka.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                acMarka = parent.getItemAtPosition(position).toString();
                acMarka = DownloadClass.getMarkaIdWithName(acMarka);
                Log.i("SelectedMarkaId" , acMarka);

                model = DownloadClass.getModelNames(acMarka);
                Utils.setAutoCompleteAdapter(acFiltreModel , model , ctx);
            }
        });
        acFiltreModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                acModel = parent.getItemAtPosition(position).toString();
                acModel = DownloadClass.getModelIdWithName(acModel);
                Log.i("SelectedModelId" , acModel);
            }
        });
        acFiltreIl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cityId = parent.getItemAtPosition(position).toString();
                cityId = DownloadClass.getCityIdWithName(cityId);
                Log.i("SelectedIl" , cityId);

                townNames = DownloadClass.getTownNames(cityId);
                Utils.setAutoCompleteAdapter(acFiltreIlce , townNames , ctx);
            }
        });
        acFiltreIlce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                townId = parent.getItemAtPosition(position).toString();
                townId = DownloadClass.getTownIdWithTownName(townId , cityId);
                Log.i("SelectedIlce" , townId);
            }
        });
        acFiltreServisBaslamaIl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                baslamaCityId = parent.getItemAtPosition(position).toString();
                baslamaCityId = DownloadClass.getCityIdWithName(baslamaCityId);
                Log.i("SelectedBaslamaId" , baslamaCityId);

                baslamaTownNames = DownloadClass.getTownNames(baslamaCityId);
                Utils.setAutoCompleteAdapter(acFiltreServisBaslamaIlce , baslamaTownNames , ctx);

            }
        });
        acFiltreServisBaslamaIlce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                baslamaTownId = parent.getItemAtPosition(position).toString();
                baslamaTownId = DownloadClass.getTownIdWithTownName(baslamaTownId , baslamaCityId);
                Log.i("SelectedBaslamaIlce" , baslamaTownId);

            }
        });
        acFiltreServisBitisIl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                bitisCityId = parent.getItemAtPosition(position).toString();
                bitisCityId = DownloadClass.getCityIdWithName(bitisCityId);
                Log.i("SelectedBitisIl" , bitisCityId);

                bitisTownNames = DownloadClass.getTownNames(bitisCityId);
                Utils.setAutoCompleteAdapter(acFiltreServisBitisIlce ,bitisTownNames , ctx);

            }
        });
        acFiltreServisBitisIlce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                bitisTownId = parent.getItemAtPosition(position).toString();
                bitisTownId = DownloadClass.getTownIdWithTownName(bitisTownId , bitisCityId);
                Log.i("SelectedBitisIlce" , bitisTownId);

            }
        });
        acFiltreKapasite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                acKapasite = String.valueOf(position + 1);
                Log.i("Kapasite" , acKapasite);
            }
        });
        acFiltreAracDurum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                acAracDurum = String.valueOf(position +1);
                Log.i("AraçDurum" , acAracDurum);
            }
        });
        acFiltreKullanilabilirKapasiteler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                acKullanabildiginizKapasiteler = String.valueOf(position + 1);
                Log.i("KullanabildiğiKapasite" , acKullanabildiginizKapasiteler);
            }
        });



        spinKategoriTip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int kategoriID = parent.getSelectedItemPosition();

                Log.i("ItemId" , " " + parent.getSelectedItemPosition());

                if(kategoriID == 0)
                {
                    linFiltreler.setVisibility(View.GONE);
                    Glide.with(ctx).load(R.drawable.icon_tum_kategori).into(imgType);


                }
                if(kategoriID == 1)
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    Glide.with(ctx).load(R.drawable.isime_arac).into(imgType);
                    loadIseArac();

                }
                if(kategoriID == 2)
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    Glide.with(ctx).load(R.drawable.aracima_is).into(imgType);
                    loadAracaIs();

                }
                if(kategoriID == 3)
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    Glide.with(ctx).load(R.drawable.aracima_sofor).into(imgType);
                    loadAracaSofor();

                }
                if(kategoriID == 4)
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    Glide.with(ctx).load(R.drawable.sofurm_is).into(imgType);
                    loadSoforeIs();

                }
                if(kategoriID == 5)
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    Glide.with(ctx).load(R.drawable.satilik_arac).into(imgType);
                }

                if(kategoriID == 6)
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    Glide.with(ctx).load(R.drawable.kiralik_arac).into(imgType);
                }

                if(kategoriID == 7)
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    Glide.with(ctx).load(R.drawable.yedek_parca).into(imgType);
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*

        İşeAraç;
        Marka;
        Model;
        MinimumYıl;
        MaximumYıl;
        Kapasite;
        ServisBaşlamaİli;
        ServisBaşlamaİlçe;
        ServisBitişİli;
        ServisBitişİlçe;

        AracaIs;
        Marka;
        Model;
        MinimumYıl;
        MaximumYıl;
        AraçDurumu;
        ServisBaşlamaİli;
        ServisBaşlamaİlçe;

        AracaSofor;
        Kapasite;
        ServisBaşlamaİli;
        ServisBaşlamaİlçe;
        ServisBitişİli;
        ServisBitişİlçe;

        Toplam;
        Marka;
        Model;
        MinimumYıl;
        MaximumYıl;
        Kapasite;
        AraçDurumu;
        KullanabildiğinizKapasiteler;
        MinimumTecrübe;
        MaximumTEcrübe;
        ServisBaşlamaİli;
        ServisBaşlamaİlçe;
        ServisBitişİli;
        ServisBitişİlçe;
        MinimumYaş;
        MaximumYaş;

        SoforeIs;
        KullanabildiğiKapasiteler;
        MinimumTecrübe;
        MaximumTecrübe;
        ServisBaşlamaİli;
        ServisBaşlamaİlçe;
        MinimumYaş;
        MaximumYaş;
        */
        return rootView;
    }

    private void loadSoforeIs() {
        acFiltreMarka.setVisibility(View.GONE);
        acFiltreModel.setVisibility(View.GONE);
        edtFiltreMinYil.setVisibility(View.GONE);
        edtFiltreMaxYil.setVisibility(View.GONE);
        acFiltreKapasite.setVisibility(View.GONE);
        acFiltreAracDurum.setVisibility(View.GONE);
        acFiltreServisBitisIl.setVisibility(View.GONE);
        acFiltreServisBitisIlce.setVisibility(View.GONE);
    }

    private void loadAracaSofor() {
        acFiltreMarka.setVisibility(View.GONE);
        acFiltreModel.setVisibility(View.GONE);
        edtFiltreMinYil.setVisibility(View.GONE);
        edtFiltreMaxYil.setVisibility(View.GONE);
        acFiltreAracDurum.setVisibility(View.GONE);
        acFiltreKullanilabilirKapasiteler.setVisibility(View.GONE);
        edtFiltreMinTecrube.setVisibility(View.GONE);
        edtFiltreMaxTecrube.setVisibility(View.GONE);
        edtFiltreMinYas.setVisibility(View.GONE);
        edtFiltreMaxYas.setVisibility(View.GONE);
    }

    private void loadAracaIs() {

        acFiltreKapasite.setVisibility(View.GONE);
        acFiltreKullanilabilirKapasiteler.setVisibility(View.GONE);
        edtFiltreMinTecrube.setVisibility(View.GONE);
        edtFiltreMaxTecrube.setVisibility(View.GONE);
        acFiltreServisBitisIl.setVisibility(View.GONE);
        acFiltreServisBitisIlce.setVisibility(View.GONE);
        edtFiltreMinYas.setVisibility(View.GONE);
        edtFiltreMaxYas.setVisibility(View.GONE);
    }

    private void loadIseArac() {

        acFiltreAracDurum.setVisibility(View.GONE);
        acFiltreKullanilabilirKapasiteler.setVisibility(View.GONE);
        edtFiltreMinTecrube.setVisibility(View.GONE);
        edtFiltreMaxTecrube.setVisibility(View.GONE);
        edtFiltreMinYas.setVisibility(View.GONE);
        edtFiltreMaxYas.setVisibility(View.GONE);


    }
}