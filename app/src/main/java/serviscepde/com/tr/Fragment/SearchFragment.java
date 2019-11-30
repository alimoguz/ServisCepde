package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.R;


public class SearchFragment extends Fragment {
    
    View generalView;

    private EditText edtArama;
    private TextView txtAra;
    private ImageView imgAra;
    private LinearLayout linFilter,linSirala;
    private RecyclerView rvAramaIlanlar;

    private String userToken,searchText;
    private Context ctx;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.search_fragment, container, false);

        generalView = rootView;
        ctx = generalView.getContext();

        edtArama = generalView.findViewById(R.id.edtArama);
        txtAra = generalView.findViewById(R.id.txtAra);
        imgAra = generalView.findViewById(R.id.imgAra);
        linFilter = generalView.findViewById(R.id.linFilter);
        linSirala = generalView.findViewById(R.id.linSirala);
        rvAramaIlanlar = generalView.findViewById(R.id.rvAramaIlanlar);

        MainActivity.relHeader.setVisibility(View.GONE);

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);


        imgAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtArama.getVisibility() == View.GONE)
                {
                    txtAra.setVisibility(View.GONE);
                    edtArama.setVisibility(View.VISIBLE);
                }
            }
        });




        return rootView;
    }
}