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
import android.widget.TextView;

import java.util.HashMap;

import serviscepde.com.tr.R;


public class FiltreSonucFragment extends Fragment {


    View generalView;
    private Context ctx;

    private TextView txtFiltreSonuç;
    private RecyclerView rvFiltreSonuc;
    private String userToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.filtre_sonuc_fragment, container, false);

        generalView = rootView;
        ctx = generalView.getContext();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        HashMap<String , Object> body = new HashMap<>();

        Bundle b = this.getArguments();

        if(b != null)
        {
            body = (HashMap<String , Object>)b.getSerializable("finalHashMap");
        }

        Log.i("Body" , " " + body.toString());


        return rootView;
    }
}