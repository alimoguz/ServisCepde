package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.Adapter.KategorIlanAdapter;
import serviscepde.com.tr.App;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.IlanKategori.IlanKategoriResponse;
import serviscepde.com.tr.Models.IlanKategori.IlanKategoriResponseDetail;
import serviscepde.com.tr.Models.IlanOzetBilgi;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;


public class SearchFragment extends Fragment {
    
    View generalView;

    private EditText edtArama;
    private TextView txtAra;
    private ImageView imgAra;
    private LinearLayout linFilter,linSirala;
    public static RecyclerView rvAramaIlanlar;

    private String userToken,searchText;
    private Context ctx;
    private ArrayList<IlanOzetBilgi> bilgiList = new ArrayList<>();

    FiltreFragment filtreFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.search_fragment, container, false);

        MainActivity.bottomNav.setVisibility(View.VISIBLE);
        MainActivity.relHeader.setVisibility(View.GONE);

        generalView = rootView;
        ctx = generalView.getContext();

        edtArama = generalView.findViewById(R.id.edtArama);
        txtAra = generalView.findViewById(R.id.txtAra);
        imgAra = generalView.findViewById(R.id.imgAra);
        linFilter = generalView.findViewById(R.id.linFilter);
        linSirala = generalView.findViewById(R.id.linSirala);
        rvAramaIlanlar = generalView.findViewById(R.id.rvAramaIlanlar);

        filtreFragment = new FiltreFragment();



        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);


        Bundle search = this.getArguments();

        if(search != null)
        {
            String text = search.getString("SearchText");
            Log.i("GelenText" , text);

            if(text.isEmpty())
            {
                Log.i("SearchTextGelmedi" , text);
            }
            if(!text.isEmpty())
            {
                Log.i("SearchTextGeldi" , text );
            }

        }



        imgAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtArama.getVisibility() == View.GONE)
                {
                    txtAra.setVisibility(View.GONE);
                    edtArama.setVisibility(View.VISIBLE);
                }
            }
        });

        HashMap<String , HashMap<String , Integer>> hashMap = new HashMap<>();
        HashMap<String , Integer> hashMap1 = new HashMap<>();

        hashMap1.put("start" , 0);
        hashMap1.put("limit" , 100);

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
                        bilgiList.add(bilgi);

                    }

                    Log.i("Liste1" , String.valueOf(bilgiList.size()));

                    KategorIlanAdapter adapter = new KategorIlanAdapter(R.layout.row_ilan , bilgiList ,0);
                    rvAramaIlanlar.setLayoutManager(new LinearLayoutManager(ctx));
                    rvAramaIlanlar.setItemAnimator(new DefaultItemAnimator());
                    rvAramaIlanlar.setAdapter(adapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<IlanKategoriResponse> call, Throwable t) {

            }
        });


        linFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragMain , filtreFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });




        return rootView;
    }
}