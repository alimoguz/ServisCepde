package serviscepde.com.tr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.HashMap;

import serviscepde.com.tr.Fragment.FiltreSonucFragment;

public class FilterResultActivity extends AppCompatActivity {

    private String userToken;
    private FrameLayout fragFiltre;
    private FiltreSonucFragment sonucFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_result);

        fragFiltre = findViewById(R.id.fragFiltre);

        SharedPreferences sharedPref = this.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        sonucFragment = new FiltreSonucFragment();

        Intent filterResult = getIntent();
        HashMap<String , Object> param = (HashMap<String , Object>)filterResult.getSerializableExtra("paramHash");

        Log.i("HashMapSize" , " " + param.size());
        HashMap<String , Object> finalHashMap = new HashMap<>();
        finalHashMap.put("param" , param);

        HashMap<String , Object> getParameters;
        getParameters = param;

        getParameters.remove("start");
        getParameters.remove("IsSaved");
        getParameters.remove("SavedName");

        if(getParameters.get("Tipi") != null)
        {
            int category = Integer.parseInt(getParameters.get("Tipi").toString());
            getParameters.remove("Tipi");
            if(category > 0 && category <=7)
            {
                getParameters.put("ID" , category);
            }
        }


        Log.i("GetParameters" , " " + getParameters.size());

        finalHashMap.put("GetParameters" , getParameters);
        finalHashMap.put("Token" , userToken);

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
