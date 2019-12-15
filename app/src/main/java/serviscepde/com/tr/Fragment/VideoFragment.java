package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.R;

import static serviscepde.com.tr.SplashActivity.sharedPref;

public class VideoFragment extends Fragment implements MediaPlayer.OnCompletionListener {

    View generalView;

    VideoView videoOpening;

    LoginFragment loginFragment;

    Context context;

    private static String isLogged;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.video_fragment , container ,false);
        
        generalView = rootView;
        videoOpening = generalView.findViewById(R.id.videoOpening);
        context = generalView.getContext();

        sharedPref = context.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        isLogged = sharedPref.getString("Loggedin" , "0");

        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.serviscepde_splash;

        Log.i("Package Name" , getActivity().getPackageName());

        MediaController mc = new MediaController(context);
        mc.setVisibility(View.GONE);
        videoOpening.setMediaController(mc);
        Uri u = Uri.parse(path);
        videoOpening.setVideoURI(u);
        videoOpening.start();
        videoOpening.setOnCompletionListener(VideoFragment.this);


        return  rootView;
        
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if(!isLogged.equals("0"))
        {
            Intent intentMain = new Intent(context , MainActivity.class);
            startActivity(intentMain);
        }

        if(isLogged.equals("0"))
        {
            loginFragment = new LoginFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragSplash,loginFragment );
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }

        /*loginFragment = new LoginFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragSplash , loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

    }
}
