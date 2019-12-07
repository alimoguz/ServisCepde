package serviscepde.com.tr.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import serviscepde.com.tr.Models.Bildirim;
import serviscepde.com.tr.R;



public class BildirimAdapter extends RecyclerView.Adapter<BildirimAdapter.ViewHolder> {

    private ArrayList<Bildirim> bildirimList;
    private int bildirimItemLayout;

    public BildirimAdapter(int bildirimItemLayout , ArrayList<Bildirim> bildirimList) {
        this.bildirimList = bildirimList;
        this.bildirimItemLayout = bildirimItemLayout;
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



        }
    }
}
