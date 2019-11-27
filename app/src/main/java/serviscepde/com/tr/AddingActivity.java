package serviscepde.com.tr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import serviscepde.com.tr.Fragment.AracaIsFragment;
import serviscepde.com.tr.Fragment.AracaSoforFragment;
import serviscepde.com.tr.Fragment.IseAracFragment;
import serviscepde.com.tr.Fragment.KiralikAracFragment;
import serviscepde.com.tr.Fragment.SatilikAracFragment;
import serviscepde.com.tr.Fragment.SoforeIsFragment;
import serviscepde.com.tr.Fragment.YedekParcaFragment;

import serviscepde.com.tr.R;

import java.util.ArrayList;


public class AddingActivity extends AppCompatActivity {

    IseAracFragment iseAracFragment;
    AracaIsFragment aracaIsFragment;
    AracaSoforFragment aracaSoforFragment;
    SoforeIsFragment soforeIsFragment;
    SatilikAracFragment satilikAracFragment;
    KiralikAracFragment kiralikAracFragment;
    YedekParcaFragment yedekParcaFragment;

    FrameLayout fragAdd;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent(getApplicationContext() , MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_activity);

        fragAdd = findViewById(R.id.fragAdd);

        Bundle bundle = getIntent().getExtras();

        int selectedCategory = bundle.getInt("selectedCategory" );
        ArrayList<String> imagesPath = bundle.getStringArrayList("imageList");

        Log.i("SelectedCategory", " " + selectedCategory);
        Log.i("ImagesPath" , imagesPath.toString());

        iseAracFragment = new IseAracFragment();
        aracaIsFragment = new AracaIsFragment();
        aracaSoforFragment = new AracaSoforFragment();
        soforeIsFragment = new SoforeIsFragment();
        satilikAracFragment = new SatilikAracFragment();
        kiralikAracFragment = new KiralikAracFragment();
        yedekParcaFragment = new YedekParcaFragment();

        Bundle photoList = new Bundle();
        photoList.putStringArrayList("photoList" , imagesPath);

        Log.i("Photos" , "" + imagesPath.size());





        if(selectedCategory == 1)
        {
            loadFragmentWithPhoto(iseAracFragment ,photoList);
        }

        if(selectedCategory == 2)
        {
            loadFragmentWithPhoto(aracaIsFragment,photoList);
        }

        if(selectedCategory == 3)
        {
            loadFragmentWithPhoto(aracaSoforFragment,photoList);
        }

        if(selectedCategory == 4)
        {
            loadFragmentWithPhoto(soforeIsFragment,photoList);
        }

        if(selectedCategory == 5)
        {
            loadFragmentWithPhoto(satilikAracFragment,photoList);
        }

        if(selectedCategory == 6)
        {
            loadFragmentWithPhoto(kiralikAracFragment,photoList);
        }

        if(selectedCategory == 7)
        {
            loadFragmentWithPhoto(yedekParcaFragment,photoList);
        }
    }


    private void loadFragmentWithPhoto(Fragment fragment , Bundle photoList)

    {
        Log.i("Fragment" , fragment.toString());

        fragment.setArguments(photoList);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragAdd , fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }




}
