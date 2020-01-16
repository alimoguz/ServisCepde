package serviscepde.com.tr.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Fragment.NotificationFragment;
import serviscepde.com.tr.Models.Bildirim;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.R;



public class BildirimAdapter extends RecyclerView.Adapter<BildirimAdapter.ViewHolder> {

    private ArrayList<Bildirim> bildirimList;
    private int bildirimItemLayout;
    private String userToken;
    private SweetAlertDialog bildirimAlert;

    public BildirimAdapter(int bildirimItemLayout , ArrayList<Bildirim> bildirimList , String userToken) {
        this.bildirimList = bildirimList;
        this.bildirimItemLayout = bildirimItemLayout;
        this.userToken = userToken;
    }

    @NonNull
    @Override
    public BildirimAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(bildirimItemLayout , parent , false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BildirimAdapter.ViewHolder holder, int position) {

        TextView txtBildirimBaslik,txtBildirimMetin,txtBildirimZaman;
        ImageView imgBildirimDurum;

        Bildirim bildirim = bildirimList.get(position);

        holder.bindData(bildirim);


    }

    @Override
    public int getItemCount() {
        return bildirimList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtBildirimBaslik,txtBildirimMetin,txtBildirimZaman;
        public ImageView imgBildirimDurum;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtBildirimBaslik =itemView.findViewById(R.id.txtBildirimBaslik);
            txtBildirimMetin =itemView.findViewById(R.id.txtBildirimMetin);
            txtBildirimZaman =itemView.findViewById(R.id.txtBildirimZaman);
            imgBildirimDurum =itemView.findViewById(R.id.imgBildirimDurum);


        }

        public void bindData(Bildirim bildirim)
        {

            if(bildirim.getStatus().equals("0"))
            {
                Glide.with(itemView).load(R.drawable.icon_notification_unread).into(imgBildirimDurum);
            }
            if(bildirim.getStatus().equals("1"))
            {
                Glide.with(itemView).load(R.drawable.icon_notification_read).into(imgBildirimDurum);
            }

            txtBildirimBaslik.setText(bildirim.getTitle());
            txtBildirimMetin.setText(bildirim.getMessage());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    HashMap<String , Object> hashMap = new HashMap<>();
                    HashMap<String , String> hashMap1 = new HashMap<>();

                    hashMap1.put("ID" , bildirim.getID());
                    hashMap.put("param" , hashMap1);
                    hashMap.put("Token" , userToken);

                    Log.i("BildirimClick--" , bildirim.getStatus());
                    Call<BaseResponse> call = App.getApiService().bildirimOku(hashMap);
                    call.enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                            Log.i("BildirimClick++" , bildirim.getStatus());
                            if(bildirim.getStatus().equals("0"))
                            {
                                Glide.with(itemView).clear(imgBildirimDurum);
                                Glide.with(itemView).load(R.drawable.icon_notification_read).into(imgBildirimDurum);
                                NotificationFragment.bildirimAdapter.notifyDataSetChanged();
                            }

                            bildirimAlert = new SweetAlertDialog(itemView.getContext() , SweetAlertDialog.NORMAL_TYPE);
                            bildirimAlert.setTitleText(bildirim.getTitle());
                            bildirimAlert.setContentText(bildirim.getMessage());
                            bildirimAlert.show();


                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {

                        }
                    });

                }
            });



        }
    }
}
