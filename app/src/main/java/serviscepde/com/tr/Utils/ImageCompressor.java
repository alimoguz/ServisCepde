package serviscepde.com.tr.Utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

public class ImageCompressor extends AsyncTask<String , Void, ArrayList<String>> {
    private ArrayList<String> uris;
    private Context context;
    private OnCompressTaskCompleted onCompressTaskCompleted;

    public ImageCompressor(ArrayList<String> uris, Context context, OnCompressTaskCompleted onCompressTaskCompleted) {
        this.uris = uris;
        this.context = context;
        this.onCompressTaskCompleted = onCompressTaskCompleted;
    }


    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        ArrayList<String> base64Photo = Utils.pathToBase64(uris , context);
        return base64Photo;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        onCompressTaskCompleted.onCompressTaskCompleted(strings);
        super.onPostExecute(strings);

    }
}
