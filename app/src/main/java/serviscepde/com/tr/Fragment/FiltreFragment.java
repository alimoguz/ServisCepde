package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Dialogs.DialogFiltreAd;
import serviscepde.com.tr.Dialogs.DialogFiltreKaydet;
import serviscepde.com.tr.DownloadClass;
import serviscepde.com.tr.FilterResultActivity;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.Kapasite;
import serviscepde.com.tr.Models.MotorGuc;
import serviscepde.com.tr.Models.MotorHacim;
import serviscepde.com.tr.R;
import serviscepde.com.tr.SplashActivity;
import serviscepde.com.tr.Utils.Utils;


public class FiltreFragment extends Fragment {


    View generalView;
    private ArrayList<String> kategoriler = new ArrayList<>();

    private ImageView imgType;

    private AppCompatSpinner spinKategoriTip;

    private AutoCompleteTextView acFiltreIl,acFiltreIlce,acFiltreMarka,acFiltreModel,acFiltreKapasite,acFiltreAracDurum,acFiltreServisBaslamaIl,acFiltreServisBaslamaIlce
            ,acFiltreServisBitisIl,acFiltreServisBitisIlce;

    private MultiAutoCompleteTextView acFiltreKullanilabilirKapasiteler;

    private TextInputEditText edtFiltreMinYil,edtFiltreMaxYil,edtFiltreMinTecrube,edtFiltreMaxTecrube,edtFiltreMinYas,edtFiltreMaxYas;
    private String minYil,maxYil,minTecrube,maxTecrube,minYas,maxYas,minKM,maxKM = null;
    private LinearLayout linFiltreler;
    private TextView txtFiltreUygula;

    private LinearLayout linFiltreMarka,linFiltreModel,linFiltreMinYil,linFiltreMaxYil,linFiltreKapasite,linFiltreAracDurum,
            linFiltreKullanabildiginizKapasiteler,linFiltreMinTecrube,linFiltreMaxTecrube,linFiltreServisBaslaIl,linFiltreServisBaslaIlce,
            linFiltreServisBitisIl,linFiltreServisBitisIlce,linFiltreMinYas,linFiltreMaxYas;

    private LinearLayout linFiltreMinKM,linFiltreMaxKM,linFiltreMotorHacim,linFiltreMotorGuc,linFiltreSatici,linFiltreKasko,linFiltreDurum;
    private TextInputEditText edtFiltreMinKM,edtFiltreMaxKM;
    private AutoCompleteTextView acFiltreMotorHacim,acFiltreMotorGuc,acFiltreSatici,acFiltreKasko,acFiltreDurum;

    private Context ctx;
    private ArrayAdapter<String> kategoriAdapter;

    private List<City> sehirler = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private List<String> marka = new ArrayList<>();
    private List<String> model = new ArrayList<>();
    private String cityId,baslamaCityId,bitisCityId = null;
    private String townId,baslamaTownId,bitisTownId = null;
    private ArrayList<String> townNames , baslamaTownNames , bitisTownNames = new ArrayList<>();
    private static String searchText,orderBY;
    private String [] multipleSearch = {"Baslik" , "ilanAciklamasi"};
    private static String categoryFromOut;

    private String userToken;
    private String acMarka,acModel,acKapasite,acAracDurum,acKullanabildiginizKapasiteler,acMotorHacim,acMotorGuc,acSatici,acKasko,acDurum = null;

    private List<Kapasite> kapasites = new ArrayList<>();
    private List<String> kapasiteNames = new ArrayList<>();
    private List<MotorGuc> gucs = new ArrayList<>();
    private List<String> gucNames = new ArrayList<>();
    private List<MotorHacim> hacims = new ArrayList<>();
    private List<String> hacimNames = new ArrayList<>();


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
        acFiltreMotorHacim = generalView.findViewById(R.id.acFiltreMotorHacim);
        acFiltreMotorGuc = generalView.findViewById(R.id.acFiltreMotorGuc);
        acFiltreSatici = generalView.findViewById(R.id.acFiltreSatici);
        acFiltreKasko = generalView.findViewById(R.id.acFiltreKasko);
        acFiltreDurum = generalView.findViewById(R.id.acFiltreDurum);


        linFiltreMarka = generalView.findViewById(R.id.linFiltreMarka);
        linFiltreModel = generalView.findViewById(R.id.linFiltreModel);
        linFiltreMinYil = generalView.findViewById(R.id.linFiltreMinYil);
        linFiltreMaxYil = generalView.findViewById(R.id.linFiltreMaxYil);
        linFiltreKapasite = generalView.findViewById(R.id.linFiltreKapasite);
        linFiltreAracDurum = generalView.findViewById(R.id.linFiltreAracDurum);
        linFiltreKullanabildiginizKapasiteler = generalView.findViewById(R.id.linFiltreKullanabildiginizKapasiteler);
        linFiltreMinTecrube = generalView.findViewById(R.id.linFiltreMinTecrube);
        linFiltreMaxTecrube = generalView.findViewById(R.id.linFiltreMaxTecrube);
        linFiltreServisBaslaIl = generalView.findViewById(R.id.linFiltreServisBaslaIl);
        linFiltreServisBaslaIlce = generalView.findViewById(R.id.linFiltreServisBaslaIlce);
        linFiltreServisBitisIl = generalView.findViewById(R.id.linFiltreServisBitisIl);
        linFiltreServisBitisIlce = generalView.findViewById(R.id.linFiltreServisBitisIlce);
        linFiltreMinYas = generalView.findViewById(R.id.linFiltreMinYas);
        linFiltreMaxYas = generalView.findViewById(R.id.linFiltreMaxYas);
        linFiltreMinKM = generalView.findViewById(R.id.linFiltreMinKM);
        linFiltreMaxKM = generalView.findViewById(R.id.linFiltreMaxKM);
        linFiltreMotorHacim = generalView.findViewById(R.id.linFiltreMotorHacim);
        linFiltreMotorGuc = generalView.findViewById(R.id.linFiltreMotorGuc);
        linFiltreSatici = generalView.findViewById(R.id.linFiltreSatici);
        linFiltreKasko = generalView.findViewById(R.id.linFiltreKasko);
        linFiltreDurum = generalView.findViewById(R.id.linFiltreDurum);



        edtFiltreMinYil = generalView.findViewById(R.id.edtFiltreMinYil);
        edtFiltreMaxYil = generalView.findViewById(R.id.edtFiltreMaxYil);
        edtFiltreMinTecrube = generalView.findViewById(R.id.edtFiltreMinTecrube);
        edtFiltreMaxTecrube = generalView.findViewById(R.id.edtFiltreMaxTecrube);
        edtFiltreMinYas = generalView.findViewById(R.id.edtFiltreMinYas);
        edtFiltreMaxYas = generalView.findViewById(R.id.edtFiltreMaxYas);
        edtFiltreMinKM = generalView.findViewById(R.id.edtFiltreMinKM);
        edtFiltreMaxKM = generalView.findViewById(R.id.edtFiltreMaxKM);


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

        Bundle fromSearchFragment = this.getArguments();

        if(fromSearchFragment != null)
        {
            if(fromSearchFragment.getString("searchText") != null)
            {
                searchText = fromSearchFragment.getString("searchText");
            }
            if(fromSearchFragment.getString("orderBY") != null)
            {
                orderBY = fromSearchFragment.getString("orderBY");
            }
            if(fromSearchFragment.getString("selectedCategory") != null)
            {
                categoryFromOut = fromSearchFragment.getString("selectedCategory");
                Log.i("SelectedCategoryFromOut" , categoryFromOut);


                if(categoryFromOut.equals("1"))
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    openEverything();
                    loadAracaIs();
                    spinKategoriTip.setSelection(2);
                    Log.i("SpinPosition" , " " + spinKategoriTip.getSelectedItemPosition() );

                }
                if(categoryFromOut.equals("2"))
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    openEverything();
                    loadAracaSofor();
                    spinKategoriTip.setSelection(3);
                    Log.i("SpinPosition" , " " + spinKategoriTip.getSelectedItemPosition() );
                }
                if(categoryFromOut.equals("3"))
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    openEverything();
                    loadIseArac();
                    spinKategoriTip.setSelection(1);
                    Log.i("SpinPosition" , " " + spinKategoriTip.getSelectedItemPosition() );
                }
                if(categoryFromOut.equals("4"))
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    openEverything();
                    loadSoforeIs();
                    spinKategoriTip.setSelection(4);
                    Log.i("SpinPosition" , " " + spinKategoriTip.getSelectedItemPosition() );
                }
                if(categoryFromOut.equals("5"))
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    openEverything();
                    loadSatilikArac();
                    spinKategoriTip.setSelection(5);
                    Log.i("SpinPosition" , " " + spinKategoriTip.getSelectedItemPosition() );
                }
                if(categoryFromOut.equals("6"))
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    openEverything();
                    loadKiralikArac();
                    spinKategoriTip.setSelection(6);
                    Log.i("SpinPosition" , " " + spinKategoriTip.getSelectedItemPosition() );
                }
                if(categoryFromOut.equals("7"))
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    openEverything();
                    loadYedekParca();
                    spinKategoriTip.setSelection(7);
                    Log.i("SpinPosition" , " " + spinKategoriTip.getSelectedItemPosition() );
                }
            }
        }




        sehirler = DownloadClass.getCities();
        cityNames = DownloadClass.getCityNames();
        marka = DownloadClass.getMarkaNames();
        kapasites = DownloadClass.getKapasite();
        kapasiteNames = DownloadClass.getKapasiteNames();
        gucs = DownloadClass.getMotorGuc();
        gucNames = DownloadClass.getGucNames();
        hacims = DownloadClass.getMotorHacim();
        hacimNames = DownloadClass.getHacimNames();


        Utils.setAutoCompleteAdapter(acFiltreIl , cityNames ,ctx);
        Utils.setAutoCompleteAdapter(acFiltreServisBaslamaIl , cityNames ,ctx);
        Utils.setAutoCompleteAdapter(acFiltreServisBitisIl , cityNames ,ctx);
        Utils.setAutoCompleteAdapter(acFiltreMarka , marka ,ctx);
        Utils.setAutoCompleteAdapter(acFiltreKapasite , kapasiteNames , ctx);
        Utils.setAutoCompleteAdapter(acFiltreKullanilabilirKapasiteler , kapasiteNames , ctx);
        Utils.setAutoCompleteAdapter(acFiltreAracDurum , App.getAracDurumu(), ctx);
        Utils.setAutoCompleteAdapter(acFiltreMotorHacim , hacimNames , ctx);
        Utils.setAutoCompleteAdapter(acFiltreMotorGuc , gucNames , ctx);
        Utils.setAutoCompleteAdapter(acFiltreSatici , App.getKimden() , ctx);
        Utils.setAutoCompleteAdapter(acFiltreKasko , App.getKaskoTuru() , ctx);
        Utils.setAutoCompleteAdapter(acFiltreDurum , App.getDurumu() , ctx);

        acFiltreDurum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                acDurum = String.valueOf(position + 1);
            }
        });
        acFiltreKasko.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                acKasko = String.valueOf(position + 1);
            }
        });
        acFiltreMotorHacim.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                acMotorHacim = parent.getItemAtPosition(position).toString();
                acMotorHacim = DownloadClass.getHacimIdWithName(acMotorHacim);
            }
        });
        acFiltreMotorGuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                acMotorGuc = parent.getItemAtPosition(position).toString();
                acMotorGuc = DownloadClass.getGucIdWithName(acMotorGuc);
            }
        });
        acFiltreSatici.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                acSatici = String.valueOf(position + 1);
            }
        });
        acFiltreMarka.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                acMarka = parent.getItemAtPosition(position).toString();
                acMarka = DownloadClass.getMarkaIdWithName(acMarka);
                Log.i("SelectedMarkaId" , acMarka);

                model = DownloadClass.getModelNames(acMarka);
                Utils.setAutoCompleteAdapter(acFiltreModel , model , ctx);
                acFiltreModel.setText(model.get(0));
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
                acFiltreIlce.setText(townNames.get(0));
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
                acFiltreServisBaslamaIlce.setText(baslamaTownNames.get(0));
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
                acFiltreServisBitisIlce.setText(bitisTownNames.get(0));
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
                acKapasite = parent.getItemAtPosition(position).toString();
                acKapasite = DownloadClass.getKapasiteIdWithName(acKapasite);
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
                String tmp = DownloadClass.getKapasiteIdWithName(parent.getItemAtPosition(position).toString()).concat(",");
                acKullanabildiginizKapasiteler = acKullanabildiginizKapasiteler.concat(tmp);
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
                    openEverything();
                    Glide.with(ctx).load(R.drawable.isime_arac).into(imgType);
                    loadIseArac();

                }
                if(kategoriID == 2)
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    openEverything();
                    Glide.with(ctx).load(R.drawable.aracima_is).into(imgType);
                    loadAracaIs();

                }
                if(kategoriID == 3)
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    openEverything();
                    Glide.with(ctx).load(R.drawable.aracima_sofor).into(imgType);
                    loadAracaSofor();

                }
                if(kategoriID == 4)
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    openEverything();
                    Glide.with(ctx).load(R.drawable.sofurm_is).into(imgType);
                    loadSoforeIs();

                }
                if(kategoriID == 5)
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    openEverything();
                    Glide.with(ctx).load(R.drawable.satilik_arac).into(imgType);
                    loadSatilikArac();
                }
                if(kategoriID == 6)
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    openEverything();
                    Glide.with(ctx).load(R.drawable.kiralik_arac).into(imgType);
                    loadKiralikArac();
                }
                if(kategoriID == 7)
                {
                    linFiltreler.setVisibility(View.VISIBLE);
                    openEverything();
                    Glide.with(ctx).load(R.drawable.yedek_parca).into(imgType);
                    loadYedekParca();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        txtFiltreUygula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("OnClick" , "Filtre");

                int selectedCategory = spinKategoriTip.getSelectedItemPosition();

                Log.i("SelectedCategory"  ,  String.valueOf(selectedCategory));

                minYil = edtFiltreMinYil.getText().toString();
                maxYil = edtFiltreMaxYil.getText().toString();
                minTecrube = edtFiltreMinTecrube.getText().toString();
                maxTecrube = edtFiltreMaxTecrube.getText().toString();
                minYas = edtFiltreMinYas.getText().toString();
                maxYas = edtFiltreMaxYas.getText().toString();
                minKM = edtFiltreMinKM.getText().toString();
                maxKM = edtFiltreMaxKM.getText().toString();

                HashMap<String , Object> param = new HashMap<>();

                if(selectedCategory != 0)
                {
                    param.put("Tipi" , selectedCategory);
                }

                param.put("start" , 0);
                param.put("func" , "Ilanlar");
                param.put("b1" , "Filtrele");


                if(searchText != null)
                {
                    Log.i("searchText" , searchText);
                    param.put("Search" , searchText);
                    param.put("MultipleSearch" , multipleSearch );
                }
                if(orderBY != null)
                {
                    Log.i("orderBY" , orderBY);
                    param.put("OrderBy" , orderBY);
                }

                if(cityId != null)
                {
                    Log.i("Seçilenİl" ,  cityId);
                    param.put("ilanCity" , cityId);
                }
                if(townId != null)
                {
                    Log.i("Seçilenİlçe" , townId);
                    param.put("ilanSemtleri" , townId);
                }
                if(baslamaCityId != null)
                {
                    Log.i("Başlamaİli" , baslamaCityId);
                    param.put("ServiseBaslamaCity" , baslamaCityId);
                }
                if(baslamaTownId != null)
                {
                    Log.i("Başlamaİlçe" , baslamaTownId);
                    param.put("ServiseBaslamaSemtleri" , baslamaTownId);
                }
                if(bitisCityId != null)
                {
                    Log.i("Bitişİl" , bitisCityId);
                    param.put("ServisBitisCity" , bitisCityId);
                }
                if(bitisTownId != null)
                {
                    Log.i("Bitişİlçe" , bitisTownId);
                    param.put("ServisBitisSemtleri" , bitisTownId);
                }
                if(acMarka != null)
                {
                    Log.i("Marka" , acMarka);
                    param.put("AracMarkasi" , acMarka);
                }
                if(acModel != null)
                {
                    Log.i("Model" , acModel);
                    param.put("AracModeli" , acModel);
                }
                if(acKapasite != null)
                {
                    Log.i("Kapasite" , acKapasite);
                    param.put("AracKapasitesi" , acKapasite);
                }
                if(acAracDurum != null)
                {
                    Log.i("AraçDurumu" , acAracDurum);
                    param.put("AracDurumu" , acAracDurum);
                }
                if(acKullanabildiginizKapasiteler != null)
                {
                    Log.i("Kapasiteler" , acKullanabildiginizKapasiteler);
                    acKullanabildiginizKapasiteler = Utils.trimmer(acKullanabildiginizKapasiteler);
                    param.put("KullanabildiginizKapasiteler" , acKullanabildiginizKapasiteler);
                }
                if(acMotorHacim != null)
                {
                    Log.i("MotorHacmi" , acMotorHacim);
                    param.put("MotorHacmi" , acMotorHacim);
                }
                if(acMotorGuc != null)
                {
                    Log.i("MotorGuc" , acMotorGuc);
                    param.put("MotorGucu" , acMotorGuc);
                }
                if(acSatici != null)
                {
                    Log.i("Satıcı" , acSatici);
                    param.put("Kimden" , acSatici);
                }
                if(!minYil.isEmpty())
                {
                    Log.i("MinYıl" , minYil);
                    param.put("AracYiliMin" , minYil);
                }
                if(!maxYil.isEmpty())
                {
                    Log.i("maxYil" , maxYil);
                    param.put("AracYiliMax" , maxYil);
                }
                if(!minTecrube.isEmpty())
                {
                    Log.i("minTecrube" , minTecrube);
                    param.put("TecrubeMin" , minTecrube);
                }
                if(!maxTecrube.isEmpty())
                {
                    Log.i("maxTecrube" , maxTecrube);
                    param.put("TecrubeMax" , maxTecrube);
                }
                if(!minKM.isEmpty())
                {
                    Log.i("MinKm" , minKM);
                    param.put("ToplamKMMin" , minKM);
                }
                if(!maxKM.isEmpty())
                {
                    Log.i("maxKM" , maxKM);
                    param.put("ToplamKMMax" , maxKM);
                }
                if(!minYas.isEmpty())
                {
                    Log.i("minYas" , minYas);
                    param.put("YasinizMin" , minYas);
                }
                if(!maxYas.isEmpty())
                {
                    Log.i("maxYas" , maxYas);
                    param.put("YasinizMax" , maxYas);
                }
                if(acKasko != null)
                {
                    Log.i("Kasko" , acKasko);
                    param.put("Kasko" ,acKasko );
                }
                if(acDurum != null)
                {
                    Log.i("YedekParçaDurum" , acDurum);
                    param.put("YedekParcaDurum" ,acDurum );
                }

                final DialogFiltreKaydet diaKaydet = new DialogFiltreKaydet(MainActivity.act);
                diaKaydet.setCancelable(false);
                diaKaydet.setCanceledOnTouchOutside(false);
                diaKaydet.show();

                Switch saveSwitch = diaKaydet.findViewById(R.id.switchFiltre);
                TextView tamam = diaKaydet.findViewById(R.id.txtFiltreKayitTamam);


                tamam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isSaved = saveSwitch.isChecked();
                        Log.i("switchState" , " " + isSaved);

                        if(isSaved)
                        {
                            diaKaydet.dismiss();
                            final DialogFiltreAd dialogFiltreAd = new DialogFiltreAd(MainActivity.act);
                            dialogFiltreAd.setCancelable(false);
                            dialogFiltreAd.setCanceledOnTouchOutside(false);
                            dialogFiltreAd.show();

                            EditText et = dialogFiltreAd.findViewById(R.id.edtFiltreAd);
                            TextView tamam = dialogFiltreAd.findViewById(R.id.txtFiltreKayıtAdTamam);
                            TextView iptal = dialogFiltreAd.findViewById(R.id.txtFiltreKayıtAdIptal);

                            iptal.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    param.put("IsSaved" , 0);
                                    param.put("SavedName" , null);
                                    openFilterResult(param);
                                }
                            });

                            tamam.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String filtreAd = et.getText().toString();

                                    if(filtreAd.isEmpty())
                                    {
                                        SweetAlertDialog adAlert;
                                        adAlert = new SweetAlertDialog(ctx , SweetAlertDialog.WARNING_TYPE);
                                        adAlert.setTitleText("İsim boş bırakılamaz !");
                                        adAlert.show();
                                    }
                                    else
                                    {
                                        param.put("IsSaved" , 1);
                                        param.put("SavedName" , filtreAd);
                                        openFilterResult(param);
                                    }

                                }
                            });

                        }
                        else
                        {
                            diaKaydet.dismiss();
                            param.put("IsSaved" , 0);
                            param.put("SavedName" , null);
                            openFilterResult(param);
                        }

                    }
                });


            }
        });


        return rootView;
    }

    private void openFilterResult(HashMap param) {

        Intent filterResult = new Intent(ctx , FilterResultActivity.class);
        filterResult.putExtra("paramHash" , param);
        startActivity(filterResult);


    }

    private void loadYedekParca() {

        linFiltreMarka.setVisibility(View.GONE);
        linFiltreModel.setVisibility(View.GONE);
        linFiltreMinYil.setVisibility(View.GONE);
        linFiltreMaxYil.setVisibility(View.GONE);
        linFiltreKapasite.setVisibility(View.GONE);
        linFiltreAracDurum.setVisibility(View.GONE);
        linFiltreKullanabildiginizKapasiteler.setVisibility(View.GONE);
        linFiltreMinTecrube.setVisibility(View.GONE);
        linFiltreMaxTecrube.setVisibility(View.GONE);
        linFiltreServisBaslaIl.setVisibility(View.GONE);
        linFiltreServisBaslaIlce.setVisibility(View.GONE);
        linFiltreServisBitisIl.setVisibility(View.GONE);
        linFiltreServisBitisIlce.setVisibility(View.GONE);
        linFiltreMinYas.setVisibility(View.GONE);
        linFiltreMaxYas.setVisibility(View.GONE);
        linFiltreMinKM.setVisibility(View.GONE);
        linFiltreMaxKM.setVisibility(View.GONE);
        linFiltreMotorHacim.setVisibility(View.GONE);
        linFiltreMotorGuc.setVisibility(View.GONE);
        linFiltreSatici.setVisibility(View.GONE);
        linFiltreKasko.setVisibility(View.GONE);

    }

    private void loadKiralikArac() {

        linFiltreAracDurum.setVisibility(View.GONE);
        linFiltreKullanabildiginizKapasiteler.setVisibility(View.GONE);
        linFiltreMinTecrube.setVisibility(View.GONE);
        linFiltreMaxTecrube.setVisibility(View.GONE);
        linFiltreServisBaslaIl.setVisibility(View.GONE);
        linFiltreServisBaslaIlce.setVisibility(View.GONE);
        linFiltreServisBitisIl.setVisibility(View.GONE);
        linFiltreServisBitisIlce.setVisibility(View.GONE);
        linFiltreMinYas.setVisibility(View.GONE);
        linFiltreMaxYas.setVisibility(View.GONE);
        linFiltreMinKM.setVisibility(View.GONE);
        linFiltreMaxKM.setVisibility(View.GONE);
        linFiltreMotorHacim.setVisibility(View.GONE);
        linFiltreMotorGuc.setVisibility(View.GONE);
        linFiltreSatici.setVisibility(View.GONE);
        linFiltreDurum.setVisibility(View.GONE);
        linFiltreKasko.setVisibility(View.GONE);

    }

    private void loadSatilikArac() {

        linFiltreAracDurum.setVisibility(View.GONE);
        linFiltreKullanabildiginizKapasiteler.setVisibility(View.GONE);
        linFiltreMinTecrube.setVisibility(View.GONE);
        linFiltreMaxTecrube.setVisibility(View.GONE);
        linFiltreServisBaslaIl.setVisibility(View.GONE);
        linFiltreServisBaslaIlce.setVisibility(View.GONE);
        linFiltreServisBitisIl.setVisibility(View.GONE);
        linFiltreServisBitisIlce.setVisibility(View.GONE);
        linFiltreMinYas.setVisibility(View.GONE);
        linFiltreMaxYas.setVisibility(View.GONE);
        linFiltreKasko.setVisibility(View.GONE);
        linFiltreDurum.setVisibility(View.GONE);


    }

    private void loadSoforeIs() {

        linFiltreMarka.setVisibility(View.GONE);
        linFiltreModel.setVisibility(View.GONE);
        linFiltreMinYil.setVisibility(View.GONE);
        linFiltreMaxYil.setVisibility(View.GONE);
        linFiltreKapasite.setVisibility(View.GONE);
        linFiltreAracDurum.setVisibility(View.GONE);
        linFiltreServisBitisIl.setVisibility(View.GONE);
        linFiltreServisBitisIlce.setVisibility(View.GONE);
        linFiltreMinKM.setVisibility(View.GONE);
        linFiltreMaxKM.setVisibility(View.GONE);
        linFiltreMotorHacim.setVisibility(View.GONE);
        linFiltreMotorGuc.setVisibility(View.GONE);
        linFiltreSatici.setVisibility(View.GONE);
        linFiltreKasko.setVisibility(View.GONE);
        linFiltreDurum.setVisibility(View.GONE);


    }

    private void loadAracaSofor() {
        linFiltreMarka.setVisibility(View.GONE);
        linFiltreModel.setVisibility(View.GONE);
        linFiltreMinYil.setVisibility(View.GONE);
        linFiltreMaxYil.setVisibility(View.GONE);
        linFiltreAracDurum.setVisibility(View.GONE);
        linFiltreKullanabildiginizKapasiteler.setVisibility(View.GONE);
        linFiltreMinTecrube.setVisibility(View.GONE);
        linFiltreMaxTecrube.setVisibility(View.GONE);
        linFiltreMinYas.setVisibility(View.GONE);
        linFiltreMaxYas.setVisibility(View.GONE);
        linFiltreMinKM.setVisibility(View.GONE);
        linFiltreMaxKM.setVisibility(View.GONE);
        linFiltreMotorHacim.setVisibility(View.GONE);
        linFiltreMotorGuc.setVisibility(View.GONE);
        linFiltreSatici.setVisibility(View.GONE);
        linFiltreKasko.setVisibility(View.GONE);
        linFiltreDurum.setVisibility(View.GONE);

    }

    private void loadAracaIs() {

        linFiltreKapasite.setVisibility(View.GONE);
        linFiltreKullanabildiginizKapasiteler.setVisibility(View.GONE);
        linFiltreMinTecrube.setVisibility(View.GONE);
        linFiltreMaxTecrube.setVisibility(View.GONE);
        linFiltreServisBitisIl.setVisibility(View.GONE);
        linFiltreServisBitisIlce.setVisibility(View.GONE);
        linFiltreMinYas.setVisibility(View.GONE);
        linFiltreMaxYas.setVisibility(View.GONE);
        linFiltreMinKM.setVisibility(View.GONE);
        linFiltreMaxKM.setVisibility(View.GONE);
        linFiltreMotorHacim.setVisibility(View.GONE);
        linFiltreMotorGuc.setVisibility(View.GONE);
        linFiltreSatici.setVisibility(View.GONE);
        linFiltreKasko.setVisibility(View.GONE);
        linFiltreDurum.setVisibility(View.GONE);


    }

    private void loadIseArac() {

        linFiltreAracDurum.setVisibility(View.GONE);
        linFiltreKullanabildiginizKapasiteler.setVisibility(View.GONE);
        linFiltreMinTecrube.setVisibility(View.GONE);
        linFiltreMaxTecrube.setVisibility(View.GONE);
        linFiltreMinYas.setVisibility(View.GONE);
        linFiltreMaxYas.setVisibility(View.GONE);
        linFiltreMinKM.setVisibility(View.GONE);
        linFiltreMaxKM.setVisibility(View.GONE);
        linFiltreMotorHacim.setVisibility(View.GONE);
        linFiltreMotorGuc.setVisibility(View.GONE);
        linFiltreSatici.setVisibility(View.GONE);
        linFiltreKasko.setVisibility(View.GONE);
        linFiltreDurum.setVisibility(View.GONE);

    }

    private void openEverything() {

        linFiltreMarka.setVisibility(View.VISIBLE);
        linFiltreModel.setVisibility(View.VISIBLE);
        linFiltreMinYil.setVisibility(View.VISIBLE);
        linFiltreMaxYil.setVisibility(View.VISIBLE);
        linFiltreKapasite.setVisibility(View.VISIBLE);
        linFiltreAracDurum.setVisibility(View.VISIBLE);
        linFiltreKullanabildiginizKapasiteler.setVisibility(View.VISIBLE);
        linFiltreMinTecrube.setVisibility(View.VISIBLE);
        linFiltreMaxTecrube.setVisibility(View.VISIBLE);
        linFiltreServisBaslaIl.setVisibility(View.VISIBLE);
        linFiltreServisBaslaIlce.setVisibility(View.VISIBLE);
        linFiltreServisBitisIl.setVisibility(View.VISIBLE);
        linFiltreServisBitisIlce.setVisibility(View.VISIBLE);
        linFiltreMinYas.setVisibility(View.VISIBLE);
        linFiltreMaxYas.setVisibility(View.VISIBLE);
        linFiltreMinKM.setVisibility(View.VISIBLE);
        linFiltreMaxKM.setVisibility(View.VISIBLE);
        linFiltreMotorHacim.setVisibility(View.VISIBLE);
        linFiltreMotorGuc.setVisibility(View.VISIBLE);
        linFiltreSatici.setVisibility(View.VISIBLE);
        linFiltreKasko.setVisibility(View.VISIBLE);
        linFiltreDurum.setVisibility(View.VISIBLE);



    }
}