package serviscepde.com.tr.Fragment;

import android.content.Context;
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

import serviscepde.com.tr.R;

public class VideoFragment extends Fragment implements MediaPlayer.OnCompletionListener {

    View generalView;

    VideoView videoOpening;

    LoginFragment loginFragment;

    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.video_fragment , container ,false);
        
        generalView = rootView;
        videoOpening = generalView.findViewById(R.id.videoOpening);
        context = generalView.getContext();

        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.intro_video;

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

        loginFragment = new LoginFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragSplash , loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
