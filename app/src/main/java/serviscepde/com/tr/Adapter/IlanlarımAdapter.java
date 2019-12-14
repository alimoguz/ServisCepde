package serviscepde.com.tr.Adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Fragment.IlanDetayFragment;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.IlanOzetBilgi;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.Models.Response.ResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;

import static serviscepde.com.tr.App.TAG;

public class IlanlarımAdapter extends RecyclerView.Adapter<IlanlarımAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<IlanOzetBilgi> list;
    private String ID;
    private String userToken;
    private SweetAlertDialog silmeOnay;

    public IlanlarımAdapter(int listItemLayout, ArrayList<IlanOzetBilgi> list , String userToken) {
        this.listItemLayout = listItemLayout;
        this.list = list;
        this.userToken = userToken;
    }

    @NonNull
    @Override
    public IlanlarımAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout , parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull IlanlarımAdapter.ViewHolder holder, int position) {

        IlanOzetBilgi bilgi = list.get(position);
        holder.bindData(bilgi , position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIlanim;
        TextView txtIlanimAciklama,txtIlanimKonum,txtIlanimFiyat,txtIlanSil,txtIlanGuncelle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtIlanimAciklama = itemView.findViewById(R.id.txtIlanimAciklama);
            txtIlanimKonum = itemView.findViewById(R.id.txtIlanimKonum);
            txtIlanimFiyat = itemView.findViewById(R.id.txtIlanimFiyat);
            txtIlanSil = itemView.findViewById(R.id.txtIlanSil);
            txtIlanGuncelle = itemView.findViewById(R.id.txtIlanGuncelle);

            imgIlanim = itemView.findViewById(R.id.imgIlanim);

        }

        public void bindData(IlanOzetBilgi data , int position)
        {
            txtIlanimAciklama.setText(data.getBaslik());

            if(data.getResimler() != null)
            {
                if(data.getResimler().contains("|"))
                {
                    String [] images = data.getResimler().split(Pattern.quote("|"));
                    Glide.with(itemView).load(App.IMAGE_URL + images[0]).error(R.drawable.default_image).into(imgIlanim);
                    Log.i("Resim" ,App.IMAGE_URL + images[0] );

                }
                else
                {
                    Glide.with(itemView).load(App.IMAGE_URL + data.getResimler()).error(R.drawable.default_image).into(imgIlanim);
                    Log.i("Resim" ,App.IMAGE_URL + data.getResimler());

                }

            }

            if(data.getUcret() == null)
            {
                txtIlanimFiyat.setText("-");
            }
            else
            {
                txtIlanimFiyat.setText(data.getUcret());
            }

            if(data.getIlanCity().equals("0"))
            {
                txtIlanimKonum.setText("");
            }
            else
            {
                Log.i("ilanCityAdapter" , data.getIlanCity());
                String sehir = Utils.getSehirAdi(data.getIlanCity());
                txtIlanimKonum.setText(sehir);
            }
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i(TAG, "onClick: Büyük viewa tıklandı");

                    ID = data.getID();

                    Log.i(TAG, "onClick: İlan Id" + ID);

                    IlanDetayFragment detayFragment = new IlanDetayFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("ilanID" , ID);
                    detayFragment.setArguments(bundle);

                    MainActivity.fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
                    MainActivity.fragmentTransaction.replace(R.id.fragMain , detayFragment);
                    MainActivity.fragmentTransaction.addToBackStack(null);
                    MainActivity.fragmentTransaction.commit();
                    
                }
            });


            txtIlanSil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i(TAG, "onClick: Sile Tıklandı");

                    ID = data.getID();

                    silmeOnay = new SweetAlertDialog(itemView.getContext() , SweetAlertDialog.WARNING_TYPE);
                    silmeOnay.setTitleText("İlanı silmek istediğinize emin misiniz ?");
                    silmeOnay.setConfirmButton("Evet", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            Log.i("İlan sile basıldı" , "sil");

                            HashMap<String , Object> hashMap = new HashMap<>();
                            HashMap<String , String> hashMap1 = new HashMap<>();

                            hashMap1.put("ID" , ID);
                            hashMap.put("param" , hashMap1);
                            hashMap.put("Token" , userToken);

                            Log.i("ID" , ID);
                            Log.i("Token" , userToken);



                            Call<BaseResponse> ilanSil = App.getApiService().ilanSil(hashMap);
                            ilanSil.enqueue(new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                                    Log.i(TAG, "onResponse:");


                                    ResponseDetail detail = response.body().getResponseDetail();
                                    String token = detail.getResult();

                                    JSONObject ilanSilme = Utils.jwtToJsonObject(token);


                                    try {
                                        int status = ilanSilme.getJSONObject("OutPutMessage").getInt("Status");

                                        if(status == 200)
                                        {
                                            Log.i(TAG, "onResponse: ilan Başarıyla silindi");
                                            removeAt(position);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(Call<BaseResponse> call, Throwable t) {

                                    Log.i(TAG, "onFailure: İlan silinemedi" + t.getMessage());

                                }
                            });

                            silmeOnay.dismiss();



                        }
                    }).setCancelButton("Hayır", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            Log.i("İlan silinmedi" , "silinmedi");

                            silmeOnay.dismiss();
                        }
                    }).show();


                }
            });


            txtIlanGuncelle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i(TAG, "onClick: Güncelle Tıklandı");

                }
            });





        }

        public void removeAt(int position) {
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        }


    }
}
