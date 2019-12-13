package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import serviscepde.com.tr.R;


public class İletisimFragment extends Fragment {


    View generalView;
    private LinearLayout linNumber1,linNumber2;
    private Context ctx;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.iletisim_fragment, container, false);

        generalView = rootView;
        ctx = generalView.getContext();

        linNumber1 = generalView.findViewById(R.id.linNumber1);
        linNumber2 = generalView.findViewById(R.id.linNumber2);


        linNumber1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        linNumber2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return rootView;
    }
}