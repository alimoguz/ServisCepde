package serviscepde.com.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

public class FilterResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_result);

        Intent filterResult = getIntent();
        HashMap<String , Object> param = (HashMap<String , Object>)filterResult.getSerializableExtra("paramHash");

        Log.i("HashMapSize" , " " + param.size() );


    }
}
