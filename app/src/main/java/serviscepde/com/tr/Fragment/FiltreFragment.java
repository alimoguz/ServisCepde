package serviscepde.com.tr.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import serviscepde.com.tr.R;


public class FiltreFragment extends Fragment {


    View generalView;
    private ArrayList<String> kategoriler = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.filtre_fragment, container, false);

        generalView = rootView;

        kategoriler.add("Tüm Kategoriler");
        kategoriler.add("İşime araç arıyorum");
        kategoriler.add("Aracıma iş arıyorum");
        kategoriler.add("Aracıma şoför arıyorum");
        kategoriler.add("Şoförüm iş arıyorum");
        kategoriler.add("Satılık araç");
        kategoriler.add("Kiralık araç");
        kategoriler.add("Yedek parça");


        return rootView;
    }
}