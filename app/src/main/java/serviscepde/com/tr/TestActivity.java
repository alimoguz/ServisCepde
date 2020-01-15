package serviscepde.com.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.icu.util.Currency;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import me.abhinay.input.CurrencyEditText;
import me.abhinay.input.CurrencySymbols;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.Ilce;
import serviscepde.com.tr.Models.Ilceler.IlceResponse;
import serviscepde.com.tr.Models.Ilceler.IlceResponseDetail;

import serviscepde.com.tr.R;

import serviscepde.com.tr.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    CurrencyEditText etInput;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        etInput = findViewById(R.id.etInput);
        btn = findViewById(R.id.btn);
        etInput.setDecimals(true);
        //Make sure that Decimals is set as false if a custom Separator is used
        etInput.setSeparator(".");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean test = etInput.getText().toString().isEmpty();
                Log.i("Bool" , " " + test);
                /*double cleanOutput = etInput.getCleanDoubleValue();

                Log.i("Value" , " " + cleanOutput);*/

            }
        });




    }


}
