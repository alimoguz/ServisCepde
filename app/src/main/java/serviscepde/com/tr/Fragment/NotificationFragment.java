package serviscepde.com.tr.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.R;


public class NotificationFragment extends Fragment {


    View generalView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.notification_fragment, container, false);

        generalView = rootView;

        MainActivity.relHeader.setVisibility(View.GONE);


        return rootView;
    }
}