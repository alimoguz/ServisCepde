package serviscepde.com.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.icu.util.Currency;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.yigitserin.currencyedittext.CurrencyEditText;

import java.text.NumberFormat;
import java.util.Locale;


public class TestActivity extends AppCompatActivity {


    Button btn;
    private String current = "";

    CurrencyEditText edt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        btn = findViewById(R.id.btn);
        edt = findViewById(R.id.edt);


        edt.setLocale(new Locale("tr","TR"));
        edt.setDecimalDigits(2);





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tmp = edt.getText().toString();
                Log.i("GetText" , tmp);

            }
        });




    }


}
