package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.App;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.Bildirim;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.Models.Response.ResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;


public class NotificationFragment extends Fragment {


    View generalView;
    private TextView txtBildirimSayisi,txt;
    private RecyclerView rvBildirimler;
    private ImageView imgBildirimIcon;
    private Context ctx;
    private String userToken;
    private int count = 0;

    private List<Bildirim> bildirimList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.notification_fragment, container, false);

        generalView = rootView;
        ctx = generalView.getContext();

        MainActivity.relHeader.setVisibility(View.GONE);

        txtBildirimSayisi = generalView.findViewById(R.id.txtBildirimSayisi);
        txt = generalView.findViewById(R.id.txt);
        rvBildirimler = generalView.findViewById(R.id.rvBildirimler);
        imgBildirimIcon = generalView.findViewById(R.id.imgBildirimIcon);

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        HashMap<String , String> hashMap = new HashMap<>();

        hashMap.put("Token" , userToken);

        Call<BaseResponse> call = App.getApiService().getBildirimler(hashMap);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                ResponseDetail responseDetail = response.body().getResponseDetail();
                String token = responseDetail.getResult();
                JSONObject jsonObject = Utils.jwtToJsonObject(token);

                try {
                    JSONArray bildirimler = jsonObject.getJSONObject("OutPutMessage").getJSONArray("Data");

                    for (int i = 0; i <= bildirimler.length(); i++){

                        JSONObject tmp = bildirimler.getJSONObject(i);
                        int x;

                        String ID = tmp.getString("ID");
                        String BildirimID = tmp.getString("BildirimID");
                        String Status = tmp.getString("Status");
                        String Title = tmp.getString("Title");
                        String Message = tmp.getString("Message");
                        String create_at = tmp.getString("create_at");

                        x = Integer.parseInt(Status);

                        if(x == 1)
                        {
                            ++count;
                        }

                        Bildirim bildirim = new Bildirim(ID , BildirimID , Status , Title , Message , create_at);
                        bildirimList.add(bildirim);

                    }

                    if(count == 0)
                    {
                        txtBildirimSayisi.setVisibility(View.GONE);
                        txt.setText("Hiç bildirim yok");
                        imgBildirimIcon.setVisibility(View.GONE);

                    }
                    if(count != 0)
                    {
                        txtBildirimSayisi.setText(String.valueOf(count));
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