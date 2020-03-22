package serviscepde.com.tr.Adapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Fragment.NotificationFragment;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.Bildirim;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.NotificationDetailActivity;
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
        public TextView notification_item_date;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtBildirimBaslik =itemView.findViewById(R.id.txtBildirimBaslik);
            txtBildirimMetin =itemView.findViewById(R.id.txtBildirimMetin);
            txtBildirimZaman =itemView.findViewById(R.id.txtBildirimZaman);
            imgBildirimDurum =itemView.findViewById(R.id.imgBildirimDurum);
            notification_item_date = itemView.findViewById(R.id.notification_item_date);


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

            String content = bildirim.getMessage().replace("<br />", "");

            txtBildirimBaslik.setText(bildirim.getTitle());
            txtBildirimMetin.setText(content);
            notification_item_date.setText(getDateBeforeText(bildirim.getCreate_at()));

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
                                bildirim.setStatus("1");
                                NotificationFragment.bildirimAdapter.notifyDataSetChanged();
                                NotificationFragment.setBildirimSayi();
                                MainActivity.setBadgeCount(--MainActivity.count);
                            }

                            /*Glide.with(itemView).load(R.drawable.icon_notification_read).into(imgBildirimDurum);
                            NotificationFragment.bildirimAdapter.notifyDataSetChanged();*/

                            String content = bildirim.getMessage().replace("<br />", "");

                            Intent intent = new Intent(itemView.getContext(), NotificationDetailActivity.class);
                            intent.putExtra("title", bildirim.getTitle());
                            intent.putExtra("message", content);
                            itemView.getContext().startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {

                        }
                    });

                }
            });



        }
    }

    private String getDateBeforeText(String create_at) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date past = null;
        try {
            past = format.parse(create_at);
            Date now = new Date();
            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if(seconds<60)
            {
                return seconds+" saniye önce";
            }
            else if(minutes<60)
            {
                return minutes+" dakika önce";
            }
            else if(hours<24)
            {
                return hours+" saat önce";
            }
            if(days >= 1 && days < 2){
                return "Dün";
            }
            else if(days >= 2 && days < 7){
                return days + " gün önce";
            }
            else if(days >= 7 && days < 14){
                return "1 hafta önce";
            }
            else if(days >= 14){
                return days/7 + " hafta önce";
            }
            else if(days >= 30 && days < 60){
                return "1 ay önce";
            }
            else if(days >= 60){
                return days/30 + " ay önce";
            }
            else if(days >= 365 && days < 730){
                return "1 yıl önce";
            }
            else if(days >= 730){
                return days/365 + " yıl önce";
            }

        } catch (ParseException e) {
            Log.i("%%%%", "getDateBeforeText: %%%%"+e.getMessage());
        }
        return "";

    }
}
