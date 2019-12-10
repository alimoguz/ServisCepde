package serviscepde.com.tr.Adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import serviscepde.com.tr.App;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private String[] imageUrls;

    public ViewPagerAdapter(Context context, String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Picasso.get()
                .load(App.IMAGE_URL.concat(imageUrls[position]))
                .fit()
                .centerCrop()
                .into(imageView);
        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}













/*package serviscepde.com.tr.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import serviscepde.com.tr.R;

public class ViewPagerAdapter extends PagerAdapter {

    private String[] photos;
    private LayoutInflater inflater;
    private Context context;




    public ViewPagerAdapter(String[] photos, LayoutInflater inflater, Context context) {
        this.photos = photos;
        this.inflater = inflater;
        this.context = context;
    }

    @Override
    public int getCount() {
        return photos.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.viewpager_image , container ,false);

        assert view != null;
        final ImageView imgPager = view.findViewById(R.id.imgPager);

        Log.i("GelenFoto" , Arrays.toString(photos));

        Picasso.get()
                .load(photos[position])
                .fit()
                .centerCrop()
                .into(imgPager);

        //Glide.with(context).load(photos.get(position)).into(imgPager);
        container.addView(view, 0);

        return  view;


    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}*/
