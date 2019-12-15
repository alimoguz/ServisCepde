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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.Adapter.KayitliAramaAdapter;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Models.KayitliArama;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.Models.Response.ResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;


public class KayitliAramaFragment extends Fragment {


    View generalView;
    private RecyclerView rvKayıtlıAramalarim;
    private Context ctx;
    private String userToken;

    private ArrayList<KayitliArama> aramalarim = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.kayitli_arama_fragment, container, false);

        generalView = rootView;
        ctx = generalView.getContext();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        rvKayıtlıAramalarim = generalView.findViewById(R.id.rvKayıtlıAramalarim);

        HashMap<String , String> body = new HashMap<>();

        body.put("Token" , userToken);


        Call<BaseResponse> call = App.getApiService().kayitliAramalarım(body);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                ResponseDetail detail = response.body().getResponseDetail();
                String token = detail.getResult();
                JSONObject kayitliArama = Utils.jwtToJsonObject(token);


                try {
                    if(kayitliArama.getJSONObject("OutPutMessage").get("Data") instanceof JSONArray)
                    {
                        for(int i = 0; i < kayitliArama.getJSONObject("OutPutMessage").getJSONArray("Data").length(); i++ )
                        {
                            JSONObject tmp = kayitliArama.getJSONObject("OutPutMessage").getJSONArray("Data").getJSONObject(i);

                            String ID = tmp.getString("ID");
                            String name = tmp.getString("Name");
                            String date = tmp.getString("create_at");



                            Log.i("Kayıtlı arama info" , name + date);
                            KayitliArama aramam = new KayitliArama(ID , name , date);
                            aramalarim.add(aramam);

                        }

                        Log.i("AramaSize" , " " + aramalarim.size());

                        KayitliAramaAdapter adapter = new KayitliAramaAdapter(R.layout.row_arama , aramalarim , userToken);
                        rvKayıtlıAramalarim.setLayoutManager(new LinearLayoutManager(ctx));
                        rvKayıtlıAramalarim.setItemAnimator(new DefaultItemAnimator());
                        rvKayıtlıAramalarim.setAdapter(adapter);


                    }
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
}