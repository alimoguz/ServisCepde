package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import serviscepde.com.tr.Adapter.KategorIlanAdapter;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Dialogs.DialogOrder;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.IlanKategori.IlanKategoriResponse;
import serviscepde.com.tr.Models.IlanKategori.IlanKategoriResponseDetail;
import serviscepde.com.tr.Models.IlanOzetBilgi;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IlanKategoriFragment extends Fragment {


    View generalView;

    private TextView txtIlanTipi,txtIlanSayisi;
    private RecyclerView rvIlanlar;
    private String categoryName;
    private LinearLayout linKategoriSirala,linKategoriFilter;
    private static String orderBY;

    private ArrayList<IlanOzetBilgi> orderList = new ArrayList<>();

    private static KategorIlanAdapter orderAdapter;
    private int selectedCategory;

    Context ctx;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ilan_kategori_fragment, container, false);

        generalView = rootView;

        txtIlanTipi = generalView.findViewById(R.id.txtIlanTipi);
        txtIlanSayisi = generalView.findViewById(R.id.txtIlanSayisi);
        rvIlanlar = generalView.findViewById(R.id.rvIlanlar);
        linKategoriSirala = generalView.findViewById(R.id.linKategoriSirala);
        linKategoriFilter = generalView.findViewById(R.id.linKategoriFilter);

        ctx = generalView.getContext();

        selectedCategory = getArguments().getInt("selectedCategory");

        MobileAds.initialize(ctx ,"ca-app-pub-3940256099942544/6300978111");

        AdView adView = generalView.findViewById(R.id.adViewKategori);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        clearAdapter();

        MainActivity.bottomNav.setVisibility(View.GONE);
        MainActivity.relHeader.setVisibility(View.GONE);

        Log.i("SelectedCategory" , String.valueOf(selectedCategory));

        linKategoriSirala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DialogOrder dialogOrder = new DialogOrder(MainActivity.act);
                dialogOrder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogOrder.setCanceledOnTouchOutside(false);
                dialogOrder.setCancelable(false);
                dialogOrder.show();

                dialogOrder.findViewById(R.id.txtFiyatArtan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearAdapter();
                        Log.i("ClickedOrder" , "txtFiyatArtan");
                        dialogOrder.dismiss();
                        orderBY = "Ucret ASC";
                        updateAdapter(orderBY);


                    }
                });
                dialogOrder.findViewById(R.id.txtFiyatAzalan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearAdapter();
                        Log.i("ClickedOrder" , "txtFiyatAzalan");
                        dialogOrder.dismiss();
                        orderBY = "Ucret DESC";
                        updateAdapter(orderBY);


                    }
                });
                dialogOrder.findViewById(R.id.txtTarihEski).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearAdapter();
                        Log.i("ClickedOrder" , "txtTarihEski");
                        dialogOrder.dismiss();
                        orderBY = "create_at ASC";
                        updateAdapter(orderBY);


                    }
                });
                dialogOrder.findViewById(R.id.txtTarihYeni).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearAdapter();
                        Log.i("ClickedOrder" , "txtTarihYeni");
                        dialogOrder.dismiss();
                        orderBY = "create_at DESC";
                        updateAdapter(orderBY);


                    }
                });
                dialogOrder.findViewById(R.id.txtBaslikAZ).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearAdapter();
                        Log.i("ClickedOrder" , "txtBaslikAZ");
                        dialogOrder.dismiss();
                        orderBY = "Baslik ASC";
                        updateAdapter(orderBY);


                    }
                });
                dialogOrder.findViewById(R.id.txtBaslikZA).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearAdapter();
                        Log.i("ClickedOrder" , "txtBaslikZA");
                        dialogOrder.dismiss();
                        orderBY = "Baslik DESC";
                        updateAdapter(orderBY);

                    }
                });
            }
        });

        linKategoriFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FiltreFragment filtreFragment= new FiltreFragment();

                Bundle category = new Bundle();
                category.putString("selectedCategory" , String.valueOf(selectedCategory));
                filtreFragment.setArguments(category);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragMain , filtreFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        HashMap<String , HashMap<String , Integer>> hashMap = new HashMap<>();
        HashMap<String , Integer> hashMap1 = new HashMap<>();

        hashMap1.put("start" , 0);
        hashMap1.put("Tipi" , selectedCategory);

        hashMap.put("param" , hashMap1);

        Call<IlanKategoriResponse>  ilanKategoriResponseCall = App.getApiService().getIlanbyKategori(hashMap);

        ilanKategoriResponseCall.enqueue(new Callback<IlanKategoriResponse>() {
            @Override
            public void onResponse(Call<IlanKategoriResponse> call, Response<IlanKategoriResponse> response) {

                IlanKategoriResponseDetail responseDetail = response.body().getIlanKategoriResponseDetail();
                String token = responseDetail.getResult();
                JSONObject ilanlar = Utils.jwtToJsonObject(token);


                try {

                    for(int i = 0; i < ilanlar.getJSONObject("OutPutMessage").getJSONArray("Data").length(); i++)
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
                        if(tmp.has("Fiyat"))
                        {
                            Ucret = tmp.getString("Fiyat");
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

                        IlanOzetBilgi bilgi = new IlanOzetBilgi(ID,ilanCity,Baslik,Ucret,Resimler);
                        orderList.add(bilgi);

                    }

                    Log.i("Liste1" , String.valueOf(orderList.size()));

                    decideCategorySetAdapter(orderList , selectedCategory);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<IlanKategoriResponse> call, Throwable t) {

            }
        });
        return rootView;
    }

    private void clearAdapter() {

        orderList.clear();

        if(orderAdapter != null)
        {
            orderAdapter.notifyDataSetChanged();
        }

    }

    private void updateAdapter(String orderBY) {

        HashMap<String , Object> hashMap = new HashMap<>();
        HashMap<String , Object> hashMap1 = new HashMap<>();

        if(orderBY != null)
        {
            hashMap1.put("OrderBy" , orderBY);
        }

        hashMap1.put("start" , 0);
        hashMap1.put("Tipi" , selectedCategory);

        hashMap.put("param" , hashMap1);

        Call<IlanKategoriResponse> ilanKategoriResponseCall = App.getApiService().getIlanbyKategori(hashMap);
        ilanKategoriResponseCall.enqueue(new Callback<IlanKategoriResponse>() {
            @Override
            public void onResponse(Call<IlanKategoriResponse> call, Response<IlanKategoriResponse> response) {

                IlanKategoriResponseDetail responseDetail = response.body().getIlanKategoriResponseDetail();
                String token = responseDetail.getResult();
                JSONObject ilanlar = Utils.jwtToJsonObject(token);


                try {

                    for(int i = 0; i < ilanlar.getJSONObject("OutPutMessage").getJSONArray("Data").length(); i++)
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

                        IlanOzetBilgi bilgi = new IlanOzetBilgi(ID,ilanCity,Baslik,Ucret,Resimler);
                        orderList.add(bilgi);

                    }

                    Log.i("Liste1" , String.valueOf(orderList.size()));

                    orderAdapter = new KategorIlanAdapter(R.layout.row_ilan , orderList ,0);
                    orderAdapter.notifyDataSetChanged();
                    rvIlanlar.setLayoutManager(new LinearLayoutManager(ctx));
                    rvIlanlar.setItemAnimator(new DefaultItemAnimator());
                    rvIlanlar.setAdapter(orderAdapter);




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<IlanKategoriResponse> call, Throwable t) {

            }
        });



    }

    public void decideCategorySetAdapter(ArrayList<IlanOzetBilgi> bilgiList , int category)
    {
        orderAdapter = new KategorIlanAdapter(R.layout.row_ilan , bilgiList ,category);
        orderAdapter.notifyDataSetChanged();
        rvIlanlar.setLayoutManager(new LinearLayoutManager(ctx));
        rvIlanlar.setItemAnimator(new DefaultItemAnimator());
        rvIlanlar.setAdapter(orderAdapter);

        if(category == 1)
        {
            categoryName = "Aracıma iş arıyorum";
            txtIlanTipi.setText(categoryName);
            txtIlanSayisi.setText(String.valueOf(bilgiList.size()));
            Log.i("Liste2" , bilgiList.size() + "\t" + category);
        }

        if(category == 2)
        {
            categoryName = "Aracıma şoför arıyorum";
            txtIlanTipi.setText(categoryName);
            txtIlanSayisi.setText(String.valueOf(bilgiList.size()));
            Log.i("Liste2" , bilgiList.size() + "\t" + category);
        }

        if(category == 3)
        {
            categoryName = "İşime araç arıyorum";
            txtIlanTipi.setText(categoryName);
            txtIlanSayisi.setText(String.valueOf(bilgiList.size()));
            Log.i("Liste2" , bilgiList.size() + "\t" + category);
        }

        if(category == 4)
        {
            categoryName = "Şoförüm İş Arıyorum";
            txtIlanTipi.setText(categoryName);
            txtIlanSayisi.setText(String.valueOf(bilgiList.size()));
            Log.i("Liste2" , bilgiList.size() + "\t" + category);
        }

        if(category == 5)
        {
            categoryName = "Satılık Araç";
            txtIlanTipi.setText(categoryName);
            txtIlanSayisi.setText(String.valueOf(bilgiList.size()));
            Log.i("Liste2" , bilgiList.size() + "\t" + category);
        }

        if(category == 6)
        {
            categoryName = "Kiralık Araç";
            txtIlanTipi.setText(categoryName);
            txtIlanSayisi.setText(String.valueOf(bilgiList.size()));
            Log.i("Liste2" , bilgiList.size() + "\t" + category);
        }

        if(category == 7)
        {
            categoryName = "Yedek Parça";
            txtIlanTipi.setText(categoryName);
            txtIlanSayisi.setText(String.valueOf(bilgiList.size()));
            Log.i("Liste2" , bilgiList.size() + "\t" + category);
        }

        if(category == 8)
        {
            categoryName = "Satılık Plaka";
            txtIlanTipi.setText(categoryName);
            txtIlanSayisi.setText(String.valueOf(bilgiList.size()));
            Log.i("Liste2" , bilgiList.size() + "\t" + category);
        }
    }
}