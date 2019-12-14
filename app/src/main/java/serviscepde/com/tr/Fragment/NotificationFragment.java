package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.Adapter.BildirimAdapter;
import serviscepde.com.tr.App;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.Bildirim;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.Models.Response.ResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.SplashActivity;
import serviscepde.com.tr.Utils.Utils;

import static serviscepde.com.tr.App.TAG;


public class NotificationFragment extends Fragment {


    View generalView;
    private TextView txtBildirimSayisi,txt;
    public static RecyclerView rvBildirimler;
    private ImageView imgBildirimIcon;
    private Context ctx;
    private String userToken;
    private int count = 0;
    public static BildirimAdapter bildirimAdapter;

    private ArrayList<Bildirim> bildirimList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.notification_fragment, container, false);
        MainActivity.bottomNav.setVisibility(View.VISIBLE);

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

        if(userToken.equals("0"))
        {
            SweetAlertDialog girisAlert = new SweetAlertDialog(ctx , SweetAlertDialog.WARNING_TYPE);
            girisAlert.setTitleText("Devam edebilmek için lütfen önce giriş yapın");
            girisAlert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent intent = new Intent(ctx , SplashActivity.class);
                    startActivity(intent);
                }
            });
            girisAlert.show();
        }

        else
        {

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
                        if(jsonObject.get("OutPutMessage") instanceof  JSONObject)
                        {
                            Log.i(TAG, "onResponse: " + " Json Object Geldi");


                            for (int i = 0; i < jsonObject.getJSONObject("OutPutMessage").getJSONArray("Data").length(); i++){

                                JSONObject tmp = jsonObject.getJSONObject("OutPutMessage").getJSONArray("Data").getJSONObject(i);
                                int x;

                                String ID = tmp.getString("ID");
                                String BildirimID = tmp.getString("BildirimID");
                                String Status = tmp.getString("Status");
                                String Title = tmp.getString("Title");
                                String Message = tmp.getString("Message");
                                String create_at = tmp.getString("create_at");

                                x = Integer.parseInt(Status);

                                if(x == 0)
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

                            bildirimAdapter = new BildirimAdapter(R.layout.row_notification , bildirimList , userToken);
                            bildirimAdapter.notifyDataSetChanged();
                            rvBildirimler.setLayoutManager(new LinearLayoutManager(ctx));
                            rvBildirimler.setItemAnimator(new DefaultItemAnimator());
                            rvBildirimler.setAdapter(bildirimAdapter);



                        }

                        if(jsonObject.get("OutPutMessage") instanceof  JSONArray)
                        {
                            Log.i(TAG, "onResponse: " + " Json Array Geldi");
                            txtBildirimSayisi.setVisibility(View.GONE);
                            txt.setText("Hiç bildirim yok");
                            imgBildirimIcon.setVisibility(View.GONE);
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

        return null;


    }
}