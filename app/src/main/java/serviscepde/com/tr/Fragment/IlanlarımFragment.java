package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.R;


public class IlanlarımFragment extends Fragment {


    View generalView;
    private TextView txtIlanimSayisi;
    private RecyclerView rvIlanlarim;
    private Context ctx;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ilanlarim_fragment, container, false);

        generalView = rootView;
        ctx = generalView.getContext();

        String UserId = getArguments().getString("UserID");

        Log.i("UserId" , UserId);

        txtIlanimSayisi = generalView.findViewById(R.id.txtIlanimSayisi);
        rvIlanlarim = generalView.findViewById(R.id.rvIlanlarim);

        HashMap<String , Object> hashMap = new HashMap<>();
        HashMap<String , String> hashMap1 = new HashMap<>();

        hashMap1.put("start" , "0");
        hashMap1.put("UserID" , UserId);

        hashMap.put("param" , hashMap1);

        Call<BaseResponse> kullaniciIlan = App.getApiService().kullanicininIlanlari(hashMap);
        kullaniciIlan.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                Log.i("Başarılı" , "onResponse");

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

                Log.i("Başarısız" , t.getMessage());

            }
        });


        return rootView;
    }
}