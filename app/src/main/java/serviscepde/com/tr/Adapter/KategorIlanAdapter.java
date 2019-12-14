package serviscepde.com.tr.Adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Fragment.IlanDetayFragment;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.IlanOzetBilgi;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class KategorIlanAdapter extends RecyclerView.Adapter<KategorIlanAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<IlanOzetBilgi> list;
    private int selectedCategory;
    private String ID;

    public KategorIlanAdapter(int listItemLayout, ArrayList<IlanOzetBilgi> list, int selectedCategory) {
        this.listItemLayout = listItemLayout;
        this.list = list;
        this.selectedCategory = selectedCategory;
    }

    @NonNull
    @Override
    public KategorIlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout , parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull KategorIlanAdapter.ViewHolder holder, int position) {

        TextView txtIlanAciklama,txtIlanKonum,txtIlanFiyat;
        ImageView imgIlanPhoto;

        txtIlanAciklama = holder.txtIlanAciklama;
        txtIlanKonum = holder.txtIlanKonum;
        txtIlanFiyat = holder.txtIlanFiyat;

        imgIlanPhoto = holder.imgIlanPhoto;

        IlanOzetBilgi bilgi = list.get(position);

        holder.bindData(bilgi);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtIlanAciklama,txtIlanKonum,txtIlanFiyat;
        public ImageView imgIlanPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtIlanAciklama = itemView.findViewById(R.id.txtIlanAciklama);
            txtIlanKonum = itemView.findViewById(R.id.txtIlanKonum);
            txtIlanFiyat = itemView.findViewById(R.id.txtIlanFiyat);

            imgIlanPhoto = itemView.findViewById(R.id.imgIlanPhoto);


        }

        public void bindData(IlanOzetBilgi data)
        {

            txtIlanAciklama.setText(data.getBaslik());

            //Log.i("Resimler" , data.getResimler());

            if(data.getResimler() == null)
            {
                Glide.with(itemView).load(R.drawable.default_image).into(imgIlanPhoto);
            }


            else
            {
                if(data.getResimler().contains("|"))
                {
                    String [] images = data.getResimler().split(Pattern.quote("|"));
                    Glide.with(itemView).load(App.IMAGE_URL + images[0]).error(R.drawable.default_image).into(imgIlanPhoto);
                    Log.i("Resim" ,App.IMAGE_URL + images[0] );

                }
                else
                {
                    Glide.with(itemView).load(App.IMAGE_URL + data.getResimler()).error(R.drawable.default_image).into(imgIlanPhoto);
                    Log.i("Resim" ,App.IMAGE_URL + data.getResimler());

                }
            }

            if(data.getUcret() == null)
            {
                txtIlanFiyat.setText("Belirtilmedi");
            }
            if(data.getUcret().equals("0"))
            {
                txtIlanFiyat.setText("Belirtilmedi");
            }
            else
            {
                txtIlanFiyat.setText(data.getUcret());
            }

            Log.i("SehirId" , data.getIlanCity());

            if(data.getIlanCity().equals("0"))
            {
                txtIlanKonum.setText("");
            }
            else
            {
                Log.i("ilanCityAdapter" , data.getIlanCity());
                String sehir = Utils.getSehirAdi(data.getIlanCity());
                txtIlanKonum.setText(sehir);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ID = data.getID();

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
        }


    }




}
