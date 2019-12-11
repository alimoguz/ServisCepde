package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import serviscepde.com.tr.Adapter.KategorIlanAdapter;
import serviscepde.com.tr.App;
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

    TextView txtIlanTipi,txtIlanSayisi;

    RecyclerView rvIlanlar;

    String categoryName;

    ArrayList<IlanOzetBilgi> bilgiList = new ArrayList<>();

    Context ctx;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ilan_kategori_fragment, container, false);

        generalView = rootView;

        txtIlanTipi = generalView.findViewById(R.id.txtIlanTipi);
        txtIlanSayisi = generalView.findViewById(R.id.txtIlanSayisi);
        rvIlanlar = generalView.findViewById(R.id.rvIlanlar);

        ctx = generalView.getContext();

        int selectedCategory = getArguments().getInt("selectedCategory");

        MainActivity.bottomNav.setVisibility(View.GONE);
        MainActivity.relHeader.setVisibility(View.GONE);

        Log.i("SelectedCategory" , String.valueOf(selectedCategory));


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
                        bilgiList.add(bilgi);

                    }

                    Log.i("Liste1" , String.valueOf(bilgiList.size()));

                    decideCategorySetAdapter(bilgiList , selectedCategory);




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

    public void decideCategorySetAdapter(ArrayList<IlanOzetBilgi> bilgiList , int category)
    {

        KategorIlanAdapter adapter = new KategorIlanAdapter(R.layout.row_ilan , bilgiList ,category);
        adapter.notifyDataSetChanged();
        rvIlanlar.setLayoutManager(new LinearLayoutManager(ctx));
        rvIlanlar.setItemAnimator(new DefaultItemAnimator());
        rvIlanlar.setAdapter(adapter);

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
    }
}