package serviscepde.com.tr.Adapter;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Fragment.KayitliAramaSonucFragment;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.IlanKategori.IlanKategoriResponse;
import serviscepde.com.tr.Models.IlanKategori.IlanKategoriResponseDetail;
import serviscepde.com.tr.Models.IlanOzetBilgi;
import serviscepde.com.tr.Models.KayitliArama;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;

public class KayitliAramaAdapter extends RecyclerView.Adapter<KayitliAramaAdapter.ViewHolder> {

    private ArrayList<KayitliArama> kayitliAramas = new ArrayList<>();
    private int listItemLayout;
    private SweetAlertDialog aramaAlert;
    private String ID;
    private String userToken;
    private ArrayList<IlanOzetBilgi> ilanList  = new ArrayList<>();

    public KayitliAramaAdapter(int listItemLayout , ArrayList<KayitliArama> kayitliAramas , String userToken) {
        this.kayitliAramas = kayitliAramas;
        this.listItemLayout = listItemLayout;
        this.userToken = userToken;
    }

    @NonNull
    @Override
    public KayitliAramaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout , parent , false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull KayitliAramaAdapter.ViewHolder holder, int position) {

        KayitliArama arama = kayitliAramas.get(position);
        holder.bindData(arama);

    }

    @Override
    public int getItemCount() {
        return kayitliAramas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtKayitliArama,txtAramaTarih;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtKayitliArama = itemView.findViewById(R.id.txtKayitliArama);
            txtAramaTarih = itemView.findViewById(R.id.txtAramaTarih);

        }

        public void bindData(KayitliArama arama)
        {
            txtKayitliArama.setText(arama.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ID = arama.getID();

                    //"{\"Token\": \"\(token)\", \"param\": {\"SavedID\": \""+clickedID+"\", \"start\": 0}}"



                    KayitliAramaSonucFragment sonucFragment = new KayitliAramaSonucFragment();
                    Bundle aramaID = new Bundle();
                    aramaID.putString("AramaID" , ID);
                    sonucFragment.setArguments(aramaID);


                    MainActivity.fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
                    MainActivity.fragmentTransaction.replace(R.id.fragMain , sonucFragment);
                    MainActivity.fragmentTransaction.addToBackStack(null);
                    MainActivity.fragmentTransaction.commit();

                }
            });


        }
    }
}
