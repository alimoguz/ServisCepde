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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.Adapter.IlanlarımAdapter;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Models.IlanOzetBilgi;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.Models.Response.ResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;


public class IlanlarımFragment extends Fragment {


    View generalView;
    private TextView txtIlanimSayisi,txtIlanBulundu;
    private RecyclerView rvIlanlarim;
    private Context ctx;
    private ArrayList<IlanOzetBilgi> ilanlar = new ArrayList<>();
    private String userToken;
    private static IlanlarımAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ilanlarim_fragment, container, false);

        generalView = rootView;
        ctx = generalView.getContext();

        String UserId = getArguments().getString("UserID");

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        Log.i("UserId" , UserId);

        txtIlanimSayisi = generalView.findViewById(R.id.txtIlanimSayisi);
        txtIlanBulundu = generalView.findViewById(R.id.txtIlanBulundu);
        rvIlanlarim = generalView.findViewById(R.id.rvIlanlarim);

        clearAdapter();

        HashMap<String , Object> hashMap = new HashMap<>();
        HashMap<String , String> hashMap1 = new HashMap<>();

        hashMap1.put("me" , "1");
        hashMap1.put("start" , "0");
        hashMap1.put("limit" , "250");
        hashMap1.put("Approval" , "Me");

        hashMap.put("param" , hashMap1);
        hashMap.put("Token" , userToken);

        Call<BaseResponse> kullaniciIlan = App.getApiService().kullanicininIlanlari(hashMap);
        kullaniciIlan.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                Log.i("Başarılı" , "onResponse");

                ResponseDetail responseDetail = response.body().getResponseDetail();
                String token = responseDetail.getResult();

                JSONObject tmp = Utils.jwtToJsonObject(token);

                try {
                    JSONObject ilanlarim = tmp.getJSONObject("OutPutMessage");

                    Log.i("Total" , ilanlarim.getString("Total"));

                    String total = ilanlarim.getString("Total");

                    if(total.equals("0"))
                    {
                        txtIlanBulundu.setVisibility(View.GONE);
                        txtIlanimSayisi.setText("Hiç ilanınız yok");
                    }
                    else
                    {
                        txtIlanimSayisi.setText(total);

                        for(int i = 0; i < ilanlarim.getJSONArray("Data").length(); i++)
                        {

                            JSONObject ilan = ilanlarim.getJSONArray("Data").getJSONObject(i);

                            String ID = ilan.getString("ID");
                            String ilanCity = ilan.getString("ilanCity");
                            String Baslik = ilan.getString("Baslik");
                            String Ucret = null;
                            String Resimler = null;

                            if(ilan.has("Ucret"))
                            {
                                Ucret = ilan.getString("Ucret");
                            }
                            else
                            {
                                Log.i("Ilan Özet Bilgi" ,  "UcretYok");
                            }
                            if(ilan.has("Resimler"))
                            {
                                Resimler = ilan.getString("Resimler");
                            }
                            else
                            {
                                Log.i("Ilan Özet Bilgi" ,  "ResimYok");
                            }

                            IlanOzetBilgi bilgi = new IlanOzetBilgi(ID,ilanCity,Baslik,Ucret,Resimler);
                            ilanlar.add(bilgi);
                        }

                        adapter = new IlanlarımAdapter(R.layout.row_ilanlarim , ilanlar , userToken);
                        adapter.notifyDataSetChanged();
                        rvIlanlarim.setLayoutManager(new LinearLayoutManager(ctx));
                        rvIlanlarim.setItemAnimator(new DefaultItemAnimator());
                        rvIlanlarim.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

                Log.i("Başarısız" , t.getMessage());
            }
        });
        return rootView;
    }

    private void clearAdapter() {

        ilanlar.clear();

        if(adapter != null)
        {
            adapter.notifyDataSetChanged();
        }


    }
}