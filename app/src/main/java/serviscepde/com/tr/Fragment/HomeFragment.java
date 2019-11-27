package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import serviscepde.com.tr.App;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.IlanKategori.IlanKategoriResponse;
import serviscepde.com.tr.Models.IlanKategori.IlanKategoriResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {


    View generalView;

    private ImageView imgFind;
    SearchFragment searchFragment;
    private IlanKategoriFragment ilanKategoriFragment;
    private IlanGuncelFragment ilanGuncelFragment;
    private IlanDetayFragment ilanDetayFragment;

    private LinearLayout linIsimeArac,linAracimaIs,linAracaSofor,linSoforeIs,linSatilikArac,linKiralikArac,linYedekParca;

    private LinearLayout linGuncel1,linGuncel2,linGuncel3;
    private ImageView imgIlanGuncel1Photo,imgIlanGuncel2Photo,imgIlanGuncel3Photo;
    private TextView txtIlanGuncel1Aciklama,txtIlanGuncel1Konum,txtIlanGuncel1Fiyat,txtIlanGuncel2Aciklama,txtIlanGuncel2Konum,txtIlanGuncel2Fiyat
            ,txtIlanGuncel3Aciklama,txtIlanGuncel3Konum,txtIlanGuncel3Fiyat;

    private TextView txtTumIlanlariGor;

    int selectedCategory;

    Context ctx;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_fragment , container , false);

        generalView = rootView;

        MainActivity.relHeader.setVisibility(View.VISIBLE);
        MainActivity.bottomNav.setVisibility(View.VISIBLE);

        ctx = generalView.getContext();

        imgFind = generalView.findViewById(R.id.imgFind);

        linIsimeArac = generalView.findViewById(R.id.linIsimeArac);
        linAracimaIs = generalView.findViewById(R.id.linAracimaIs);
        linAracaSofor = generalView.findViewById(R.id.linAracaSofor);
        linSoforeIs = generalView.findViewById(R.id.linSoforeIs);
        linSatilikArac = generalView.findViewById(R.id.linSatilikArac);
        linKiralikArac = generalView.findViewById(R.id.linKiralikArac);
        linYedekParca = generalView.findViewById(R.id.linYedekParca);

        linGuncel1 = generalView.findViewById(R.id.linGuncel1);
        linGuncel2 = generalView.findViewById(R.id.linGuncel2);
        linGuncel3 = generalView.findViewById(R.id.linGuncel3);

        imgIlanGuncel1Photo = generalView.findViewById(R.id.imgIlanGuncel1Photo);
        imgIlanGuncel2Photo = generalView.findViewById(R.id.imgIlanGuncel2Photo);
        imgIlanGuncel3Photo = generalView.findViewById(R.id.imgIlanGuncel3Photo);

        txtIlanGuncel1Aciklama = generalView.findViewById(R.id.txtIlanGuncel1Aciklama);
        txtIlanGuncel1Konum = generalView.findViewById(R.id.txtIlanGuncel1Konum);
        txtIlanGuncel1Fiyat = generalView.findViewById(R.id.txtIlanGuncel1Fiyat);
        txtIlanGuncel2Aciklama = generalView.findViewById(R.id.txtIlanGuncel2Aciklama);
        txtIlanGuncel2Konum = generalView.findViewById(R.id.txtIlanGuncel2Konum);
        txtIlanGuncel2Fiyat = generalView.findViewById(R.id.txtIlanGuncel2Fiyat);
        txtIlanGuncel3Aciklama = generalView.findViewById(R.id.txtIlanGuncel3Aciklama);
        txtIlanGuncel3Konum = generalView.findViewById(R.id.txtIlanGuncel3Konum);
        txtIlanGuncel3Fiyat = generalView.findViewById(R.id.txtIlanGuncel3Fiyat);


        txtTumIlanlariGor = generalView.findViewById(R.id.txtTumIlanlariGor);


        ilanKategoriFragment = new IlanKategoriFragment();
        ilanGuncelFragment = new IlanGuncelFragment();
        ilanDetayFragment = new IlanDetayFragment();

        Bundle categoryType = new Bundle();


        imgFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        txtTumIlanlariGor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(ilanGuncelFragment);
            }
        });


        linIsimeArac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 3;
                categoryType.putInt("selectedCategory" , selectedCategory);
                loadFragmentwithBundle(ilanKategoriFragment, categoryType);

            }
        });

        linAracimaIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 1;
                categoryType.putInt("selectedCategory" , selectedCategory);
                loadFragmentwithBundle(ilanKategoriFragment, categoryType);

            }
        });

        linAracaSofor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 2;
                categoryType.putInt("selectedCategory" , selectedCategory);
                loadFragmentwithBundle(ilanKategoriFragment, categoryType);

            }
        });

        linSoforeIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 4;
                categoryType.putInt("selectedCategory" , selectedCategory);
                loadFragmentwithBundle(ilanKategoriFragment, categoryType);

            }
        });

        linSatilikArac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 5;
                categoryType.putInt("selectedCategory" , selectedCategory);
                loadFragmentwithBundle(ilanKategoriFragment, categoryType);

            }
        });

        linKiralikArac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 6;
                categoryType.putInt("selectedCategory" , selectedCategory);
                loadFragmentwithBundle(ilanKategoriFragment, categoryType);

            }
        });

        linYedekParca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 7;
                categoryType.putInt("selectedCategory" , selectedCategory);
                loadFragmentwithBundle(ilanKategoriFragment, categoryType);
            }
        });

        HashMap<String , HashMap<String , Integer>> hashMap = new HashMap<>();
        HashMap<String , Integer> hashMap1 = new HashMap<>();

        hashMap1.put("start" , 0);
        hashMap1.put("limit" , 3);

        hashMap.put("param" , hashMap1);


        Call<IlanKategoriResponse> call = App.getApiService().getIlanbyKategori(hashMap);


        call.enqueue(new Callback<IlanKategoriResponse>() {
            @Override
            public void onResponse(Call<IlanKategoriResponse> call, Response<IlanKategoriResponse> response) {

                Log.i("ServisDurumu" , "Başarılı");

                IlanKategoriResponseDetail detail = response.body().getIlanKategoriResponseDetail();

                String token = detail.getResult();

                JSONObject ilanlar = Utils.jwtToJsonObject(token);

                try {

                    for(int i = 0; i < ilanlar.getJSONObject("OutPutMessage").getJSONArray("Data").length(); i++)
                    {
                        if(ilanlar.getJSONObject("OutPutMessage").getJSONArray("Data").length() == 0)
                        {
                            linGuncel1.setVisibility(View.GONE);
                            linGuncel2.setVisibility(View.GONE);
                            linGuncel3.setVisibility(View.GONE);
                        }

                        if(ilanlar.getJSONObject("OutPutMessage").getJSONArray("Data").length() == 1)
                        {
                            linGuncel2.setVisibility(View.GONE);
                            linGuncel3.setVisibility(View.GONE);
                        }
                        if(ilanlar.getJSONObject("OutPutMessage").getJSONArray("Data").length() == 2)
                        {
                            linGuncel3.setVisibility(View.GONE);
                        }



                        if(i == 0)
                        {
                            JSONObject tmp = ilanlar.getJSONObject("OutPutMessage").getJSONArray("Data").getJSONObject(i);

                            String ID = tmp.getString("ID");
                            String ilanCity = tmp.getString("ilanCity");
                            String Baslik = tmp.getString("Baslik");
                            String Ucret = null;
                            String Resimler = null;

                            if(tmp.has("Ucret"))
                            {
                                Ucret = tmp.getString("Ucret");
                            }
                            else
                            {
                                Log.i("Ilan Özet Bilgi" ,  "UcretYok");
                            }

                            if(tmp.has("Resimler"))
                            {
                                Resimler = tmp.getString("Resimler");
                            }
                            else
                            {
                                Log.i("Ilan Özet Bilgi" ,  "ResimYok");
                            }

                            Log.i("Ilan Özet Bilgi" , ID + "\t" + ilanCity + "\t" + Baslik + "\t" + Ucret + "\t" + Resimler);

                            txtIlanGuncel1Aciklama.setText(Baslik);

                            linGuncel1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Log.i("Bastın" , "test");

                                    openIlanDetay(ilanDetayFragment ,ID);

                                }
                            });

                            if(Resimler == null)
                            {
                                Glide.with(ctx).load(R.drawable.default_image).into(imgIlanGuncel1Photo);
                            }
                            else
                            {
                                if(Resimler.contains("|"))
                                {
                                    String [] images = Resimler.split(Pattern.quote("|"));
                                    Glide.with(ctx).load(App.IMAGE_URL + images[0]).into(imgIlanGuncel1Photo);
                                }
                                else
                                {
                                    Glide.with(ctx).load(App.IMAGE_URL + Resimler).into(imgIlanGuncel1Photo);
                                }
                            }

                            if(Ucret == null)
                            {
                                txtIlanGuncel1Fiyat.setText("-");
                            }
                            else
                            {
                                txtIlanGuncel1Fiyat.setText(Ucret);
                            }

                            if(ilanCity.equals("0"))
                            {
                                txtIlanGuncel1Konum.setText("");
                            }
                            else
                            {
                                Log.i("ilanCity" , ilanCity);
                                String sehir = Utils.getSehirAdi(ilanCity);
                                txtIlanGuncel1Konum.setText(sehir);
                            }

                        }

                        if(i == 1)
                        {
                            JSONObject tmp = ilanlar.getJSONObject("OutPutMessage").getJSONArray("Data").getJSONObject(i);

                            String ID = tmp.getString("ID");
                            String ilanCity = tmp.getString("ilanCity");
                            String Baslik = tmp.getString("Baslik");
                            String Ucret = null;
                            String Resimler = null;

                            if(tmp.has("Ucret"))
                            {
                                Ucret = tmp.getString("Ucret");
                            }
                            else
                            {
                                Log.i("Ilan Özet Bilgi" ,  "UcretYok");
                            }

                            if(tmp.has("Resimler"))
                            {
                                Resimler = tmp.getString("Resimler");
                            }
                            else
                            {
                                Log.i("Ilan Özet Bilgi" ,  "ResimYok");
                            }

                            Log.i("Ilan Özet Bilgi" , ID + "\t" + ilanCity + "\t" + Baslik + "\t" + Ucret + "\t" + Resimler);

                            txtIlanGuncel2Aciklama.setText(Baslik);

                            linGuncel2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    openIlanDetay(ilanDetayFragment , ID);

                                }
                            });



                            if(Resimler == null)
                            {
                                Glide.with(ctx).load(R.drawable.default_image).into(imgIlanGuncel2Photo);
                            }
                            else
                            {
                                if(Resimler.contains("|"))
                                {
                                    String [] images = Resimler.split(Pattern.quote("|"));
                                    Glide.with(ctx).load(App.IMAGE_URL + images[0]).into(imgIlanGuncel2Photo);
                                }
                                else
                                {
                                    Glide.with(ctx).load(App.IMAGE_URL + Resimler).into(imgIlanGuncel2Photo);
                                }
                            }

                            if(Ucret == null)
                            {
                                txtIlanGuncel2Fiyat.setText("-");
                            }
                            else
                            {
                                txtIlanGuncel2Fiyat.setText(Ucret);
                            }

                            if(ilanCity.equals("0"))
                            {
                                txtIlanGuncel2Konum.setText("");
                            }
                            else
                            {
                                String sehir = Utils.getSehirAdi(ilanCity);
                                txtIlanGuncel2Konum.setText(sehir);
                            }

                        }

                        if(i == 2)
                        {
                            JSONObject tmp = ilanlar.getJSONObject("OutPutMessage").getJSONArray("Data").getJSONObject(i);

                            String ID = tmp.getString("ID");
                            String ilanCity = tmp.getString("ilanCity");
                            String Baslik = tmp.getString("Baslik");
                            String Ucret = null;
                            String Resimler = null;

                            if(tmp.has("Ucret"))
                            {
                                Ucret = tmp.getString("Ucret");
                            }
                            else
                            {
                                Log.i("Ilan Özet Bilgi" ,  "UcretYok");
                            }

                            if(tmp.has("Resimler"))
                            {
                                Resimler = tmp.getString("Resimler");
                            }
                            else
                            {
                                Log.i("Ilan Özet Bilgi" ,  "ResimYok");
                            }

                            Log.i("Ilan Özet Bilgi" , ID + "\t" + ilanCity + "\t" + Baslik + "\t" + Ucret + "\t" + Resimler);

                            txtIlanGuncel3Aciklama.setText(Baslik);

                            linGuncel3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    openIlanDetay(ilanDetayFragment , ID);
                                }
                            });

                            if(Resimler == null)
                            {
                                Glide.with(ctx).load(R.drawable.default_image).into(imgIlanGuncel3Photo);
                            }
                            else
                            {
                                if(Resimler.contains("|"))
                                {
                                    String [] images = Resimler.split(Pattern.quote("|"));
                                    Glide.with(ctx).load(App.IMAGE_URL + images[0]).into(imgIlanGuncel3Photo);
                                }
                                else
                                {
                                    Glide.with(ctx).load(App.IMAGE_URL + Resimler).into(imgIlanGuncel3Photo);
                                }
                            }

                            if(Ucret == null)
                            {
                                txtIlanGuncel3Fiyat.setText("-");
                            }
                            else
                            {
                                txtIlanGuncel3Fiyat.setText(Ucret);
                            }

                            if(ilanCity.equals("0"))
                            {
                                txtIlanGuncel3Konum.setText("");
                            }
                            else
                            {
                                String sehir = Utils.getSehirAdi(ilanCity);
                                txtIlanGuncel3Konum.setText(sehir);
                            }

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<IlanKategoriResponse> call, Throwable t) {

                Log.i("ServisDurumu" , "Başarısız");

            }
        });

        return rootView;
    }


    private void loadFragmentwithBundle(Fragment fragment, Bundle bundle)

    {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragMain, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragMain ,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void openIlanDetay(Fragment fragment , String ID)
    {
        Log.i("IlanID" , ID);
        Bundle ilanID = new Bundle();
        ilanID.putString("ilanID" , ID);
        fragment.setArguments(ilanID);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragMain, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
