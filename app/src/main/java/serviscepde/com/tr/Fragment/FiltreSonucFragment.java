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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.Adapter.FiltreIlanAdapter;
import serviscepde.com.tr.Adapter.KategorIlanAdapter;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Models.IlanOzetBilgi;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.Models.Response.ResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;


public class FiltreSonucFragment extends Fragment {


    View generalView;
    private Context ctx;

    private TextView txtFiltreSonuç;
    private RecyclerView rvFiltreSonuc;
    private String userToken;
    ArrayList<IlanOzetBilgi> bilgiList = new ArrayList<>();
    private static FiltreIlanAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.filtre_sonuc_fragment, container, false);

        generalView = rootView;
        ctx = generalView.getContext();

        rvFiltreSonuc = generalView.findViewById(R.id.rvFiltreSonuc);

        clearAdapter();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        HashMap<String , Object> body = new HashMap<>();

        Bundle b = this.getArguments();

        if(b != null)
        {
            body = (HashMap<String , Object>)b.getSerializable("finalHashMap");
        }



        Log.i("Body" , " " + body.toString());

        Call<BaseResponse> call = App.getApiService().ilanFiltrele(body);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                ResponseDetail detail = response.body().getResponseDetail();
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
                        bilgiList.add(bilgi);

                    }

                    Log.i("Liste1" , String.valueOf(bilgiList.size()));

                    adapter = new FiltreIlanAdapter(R.layout.row_ilan , bilgiList , 0);
                    adapter.notifyDataSetChanged();
                    rvFiltreSonuc.setLayoutManager(new LinearLayoutManager(ctx));
                    rvFiltreSonuc.setItemAnimator(new DefaultItemAnimator());
                    rvFiltreSonuc.setAdapter(adapter);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });


        return rootView;
    }

    private void clearAdapter() {

        bilgiList.clear();

        if(adapter != null)
        {
            adapter.notifyDataSetChanged();
        }
    }
}