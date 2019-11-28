package serviscepde.com.tr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    ArrayList<String> imagesPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        ImagePicker.create(this)
                .returnMode(ReturnMode.GALLERY_ONLY)
                .returnMode(ReturnMode.CAMERA_ONLY)
                .folderMode(true)
                .toolbarFolderTitle("Folder")
                .includeVideo(false)
                .multi()
                .limit(1)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);

            Bundle bundle1 = getIntent().getExtras();
            int pos = bundle1.getInt("position");

            for(int i = 0; i < images.size(); i++)
            {
                String tmp = images.get(i).getPath();
                imagesPath.add(tmp);
            }
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            Log.i("Resim/ler" , images.get(0).getPath() );
            Log.i("Resim/ler" , image.getPath());

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("imageList", imagesPath);
            intent.putExtras(bundle);

            if(pos == 1){
                setResult(100, intent);
            }
            else if(pos == 2){
                setResult(200, intent);
            }
            else if(pos == 3){
                setResult(300, intent);
            }

            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
