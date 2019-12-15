package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.Adapter.KategorIlanAdapter;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Models.IlanKategori.IlanKategoriResponse;
import serviscepde.com.tr.Models.IlanKategori.IlanKategoriResponseDetail;
import serviscepde.com.tr.Models.IlanOzetBilgi;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;


public class KayitliAramaSonucFragment extends Fragment {


    View generalView;
    private RecyclerView rvKayitliAramaSonuc;
    private Context ctx;
    private ArrayList<IlanOzetBilgi> ilanList = new ArrayList<>();
    private static String ID,userToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.kayitli_arama_sonuc_fragment, container, false);

        generalView = rootView;
        ctx = generalView.getContext();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);


        rvKayitliAramaSonuc = generalView.findViewById(R.id.rvKayitliAramaSonuc);

        HashMap<String , Object> hashMap = new HashMap<>();
        HashMap<String , Object> hashMap1 = new HashMap<>();

        Bundle aramaID = this.getArguments();

        if(aramaID != null)
        {
            ID = getArguments().getString("AramaID");

        }

        hashMap1.put("SavedID" , ID);
        hashMap1.put("start" , 0);

        hashMap.put("param" , hashMap1);
        hashMap.put("Token" , userToken);


        Call<IlanKategoriResponse> baseResponseCall = App.getApiService().getIlanbyKategori(hashMap);
        baseResponseCall.enqueue(new Callback<IlanKategoriResponse>() {
            @Override
            public void onResponse(Call<IlanKategoriResponse> call, Response<IlanKategoriResponse> response) {

                IlanKategoriResponseDetail detail = response.body().getIlanKategoriResponseDetail();
                String token = detail.getResult();

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
                        ilanList.add(bilgi);
                    }

                    KategorIlanAdapter adapter = new KategorIlanAdapter(R.layout.row_ilan , ilanList , 0);
                    adapter.notifyDataSetChanged();
                    rvKayitliAramaSonuc.setLayoutManager(new LinearLayoutManager(ctx));
                    rvKayitliAramaSonuc.setItemAnimator(new DefaultItemAnimator());
                    rvKayitliAramaSonuc.setAdapter(adapter);





                }

                catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<IlanKategoriResponse> call, Throwable t) {

            }
        });


        return rootView;
    }
}