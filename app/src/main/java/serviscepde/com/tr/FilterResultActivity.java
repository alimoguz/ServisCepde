package serviscepde.com.tr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Iterator;

import serviscepde.com.tr.Fragment.FiltreSonucFragment;

public class FilterResultActivity extends AppCompatActivity {

    private String userToken;
    private FrameLayout fragFiltre;
    private FiltreSonucFragment sonucFragment;

    public static FragmentTransaction fragmentTransaction;
    public static FragmentManager fragmentManager;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_result);

        fragFiltre = findViewById(R.id.fragFiltre);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        SharedPreferences sharedPref = this.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        sonucFragment = new FiltreSonucFragment();

        HashMap<String , Object> finalHashMap = new HashMap<>();

        Intent filterResult = getIntent();
        HashMap<String , Object> param = (HashMap<String , Object>)filterResult.getSerializableExtra("paramHash");

        Log.i("HashMapSize" , " " + param.size());
        finalHashMap.put("Token" , userToken);
        finalHashMap.put("param" , param);


        Log.i("FinalHashMapSize" ," " + finalHashMap.size());

        loadFilterResult(finalHashMap);



    }

    private void loadFilterResult(HashMap<String, Object> finalHashMap) {

        Bundle bundleHash = new Bundle();
        bundleHash.putSerializable("finalHashMap" , finalHashMap);
        sonucFragment.setArguments(bundleHash);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragFiltre , sonucFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
