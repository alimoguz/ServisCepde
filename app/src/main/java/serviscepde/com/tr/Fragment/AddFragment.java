package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import serviscepde.com.tr.AddingActivity;
import serviscepde.com.tr.R;

import java.util.ArrayList;
import java.util.List;


public class AddFragment extends Fragment {


    View generalView;
    private int selectedCategory = 0;
    ArrayList<String> imagesPath = new ArrayList<>();
    Context ctx;
    Intent addingActivity;

    LinearLayout linDialogIsimeArac,linDialogAracimaIs,linDialogAracimaSofor,linDialogSoforeIs,linDialogSatilikArac,linDialogKiralikArac,linDialogYedekParca;

    private Bundle bundle = new Bundle();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.add_fragment, container, false);

        generalView = rootView;

        ctx = generalView.getContext();

        linDialogIsimeArac = generalView.findViewById(R.id.linDialogIsimeArac);
        linDialogAracimaIs = generalView.findViewById(R.id.linDialogAracimaIs);
        linDialogAracimaSofor = generalView.findViewById(R.id.linDialogAracimaSofor);
        linDialogSoforeIs = generalView.findViewById(R.id.linDialogSoforeIs);
        linDialogSatilikArac = generalView.findViewById(R.id.linDialogSatilikArac);
        linDialogKiralikArac = generalView.findViewById(R.id.linDialogKiralikArac);
        linDialogYedekParca = generalView.findViewById(R.id.linDialogYedekParca);

       addingActivity = new Intent(ctx , AddingActivity.class);




        linDialogIsimeArac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 1;
                bundle.putInt("selectedCategory" , selectedCategory);
                choosePhoto();

            }
        });

        linDialogAracimaIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 2;
                bundle.putInt("selectedCategory" , selectedCategory);
                choosePhoto();

            }
        });

        linDialogAracimaSofor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 3;
                bundle.putInt("selectedCategory" , selectedCategory);
                choosePhoto();

            }
        });

        linDialogSoforeIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 4;
                bundle.putInt("selectedCategory" , selectedCategory);
                choosePhoto();

            }
        });

        linDialogSatilikArac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 5;
                bundle.putInt("selectedCategory" , selectedCategory);
                choosePhoto();

            }
        });

        linDialogKiralikArac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 6;
                bundle.putInt("selectedCategory" , selectedCategory);
                choosePhoto();

            }
        });

        linDialogYedekParca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedCategory = 7;
                bundle.putInt("selectedCategory" , selectedCategory);
                choosePhoto();

            }
        });

        return rootView;
    }

    private void choosePhoto()
    {
        /*ImagePicker.create(this)
                .returnMode(ReturnMode.GALLERY_ONLY)
                .returnMode(ReturnMode.CAMERA_ONLY)
                .folderMode(true)
                .toolbarFolderTitle("Folder")
                .includeVideo(false)
                .multi()
                .limit(3)
                .start();*/

        bundle.putStringArrayList("imageList" , imagesPath);
        addingActivity.putExtras(bundle);
        startActivity(addingActivity);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("%%%%", "onActivityResult: %%%%"+requestCode+"----"+resultCode);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);

            for(int i = 0; i < images.size(); i++)
            {
                String tmp = images.get(i).getPath();
                imagesPath.add(tmp);
            }
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            Log.i("Resim/ler" , images.get(0).getPath() );
            Log.i("Resim/ler" , image.getPath());
            bundle.putStringArrayList("imageList" , imagesPath);
            addingActivity.putExtras(bundle);
            startActivity(addingActivity);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}