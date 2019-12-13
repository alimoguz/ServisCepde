package serviscepde.com.tr.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import serviscepde.com.tr.Models.KayitliArama;
import serviscepde.com.tr.R;

public class KayitliAramaAdapter extends RecyclerView.Adapter<KayitliAramaAdapter.ViewHolder> {

    private ArrayList<KayitliArama> kayitliAramas = new ArrayList<>();
    private int listItemLayout;
    private SweetAlertDialog aramaAlert;

    public KayitliAramaAdapter(int listItemLayout , ArrayList<KayitliArama> kayitliAramas) {
        this.kayitliAramas = kayitliAramas;
        this.listItemLayout = listItemLayout;
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

                    aramaAlert = new SweetAlertDialog(itemView.getContext() , SweetAlertDialog.WARNING_TYPE);
                    aramaAlert.setTitleText("Bu aramaya ait ilan bulunamadı");
                    aramaAlert.show();

                }
            });


        }
    }
}
