package serviscepde.com.tr.Utils;


import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TimePicker;

import serviscepde.com.tr.App;
import serviscepde.com.tr.R;
import com.google.gson.Gson;


import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;


public class Utils {


    static String sehir,tmp ;


    public static boolean isValidEmailAddress(String email)
    {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


    public static JSONObject jwtToJsonObject(String token)
    {
        Object jwtObject = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(App.key)).parseClaimsJws(token).getBody().get("result");

        Gson gson = new Gson();
        String json = gson.toJson(jwtObject);
        JSONObject jsonObject = null;


        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  jsonObject;

    }


    public static String getDeviceID(Context context)
    {
        String devID =  Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return devID;
    }

    public static String getSehirAdi(String cityID)
    {

        if(cityID.equals("1")) {  return "ADANA"; }
        if(cityID.equals("2")) {  return "ADIYAMAN"; }
        if(cityID.equals("3")) {  return "AFYONKARAHİSAR"; }
        if(cityID.equals("4")) {  return "AĞRI"; }
        if(cityID.equals("5")) {  return "AKSARAY"; }
        if(cityID.equals("6")) {  return "AMASYA"; }
        if(cityID.equals("7")) {  return "ANKARA"; }
        if(cityID.equals("8")) {  return "ANTALYA"; }
        if(cityID.equals("9")) {  return "ARDAHAN"; }
        if(cityID.equals("10")) {  return "ARTVİN"; }
        if(cityID.equals("11")) {  return "AYDIN"; }
        if(cityID.equals("12")) {  return "BALIKESİR"; }
        if(cityID.equals("13")) {  return "BARTIN"; }
        if(cityID.equals("14")) {  return "BATMAN"; }
        if(cityID.equals("15")) {  return "BAYBURT"; }
        if(cityID.equals("16")) {  return "BİLECİK"; }
        if(cityID.equals("17")) {  return "BİNGÖL"; }
        if(cityID.equals("18")) {  return "BİTLİS"; }
        if(cityID.equals("19")) {  return "BOLU"; }
        if(cityID.equals("20")) {  return "BURDUR"; }
        if(cityID.equals("21")) {  return "BURSA"; }
        if(cityID.equals("22")) {  return "ÇANAKKALE"; }
        if(cityID.equals("23")) {  return "ÇANKIRI"; }
        if(cityID.equals("24")) {  return "ÇORUM"; }
        if(cityID.equals("25")) {  return "DENİZLİ"; }
        if(cityID.equals("26")) {  return "DİYARBAKIR"; }
        if(cityID.equals("27")) {  return "DÜZCE"; }
        if(cityID.equals("28")) {  return "EDİRNE"; }
        if(cityID.equals("29")) {  return "ELAZIĞ"; }
        if(cityID.equals("30")) {  return "ERZİNCAN"; }
        if(cityID.equals("31")) {  return "ERZURUM"; }
        if(cityID.equals("32")) {  return "ESKİŞEHİR"; }
        if(cityID.equals("33")) {  return "GAZİANTEP"; }
        if(cityID.equals("34")) {  return "GİRESUN"; }
        if(cityID.equals("35")) {  return "GÜMÜŞHANE"; }
        if(cityID.equals("36")) {  return "HAKKARİ"; }
        if(cityID.equals("37")) {  return "HATAY"; }
        if(cityID.equals("38")) {  return "IĞDIR"; }
        if(cityID.equals("39")) {  return "ISPARTA"; }
        if(cityID.equals("40")) {  return "İSTANBUL"; }
        if(cityID.equals("41")) {  return "İZMİR"; }
        if(cityID.equals("42")) {  return "KAHRAMANMARAŞ"; }
        if(cityID.equals("43")) {  return "KARABÜK"; }
        if(cityID.equals("44")) {  return "KARAMAN"; }
        if(cityID.equals("45")) {  return "KARS"; }
        if(cityID.equals("46")) {  return "KASTAMONU"; }
        if(cityID.equals("47")) {  return "KAYSERİ"; }
        if(cityID.equals("48")) {  return "KIRIKKALE"; }
        if(cityID.equals("49")) {  return "KIRKLARELİ"; }
        if(cityID.equals("50")) {  return "KIRŞEHİR"; }
        if(cityID.equals("51")) {  return "KİLİS"; }
        if(cityID.equals("52")) {  return "KOCAELİ"; }
        if(cityID.equals("53")) {  return "KONYA"; }
        if(cityID.equals("54")) {  return "KÜTAHYA"; }
        if(cityID.equals("55")) {  return "MALATYA"; }
        if(cityID.equals("56")) {  return "MANİSA"; }
        if(cityID.equals("57")) {  return "MARDİN"; }
        if(cityID.equals("58")) {  return "MERSİN"; }
        if(cityID.equals("59")) {  return "MUĞLA"; }
        if(cityID.equals("60")) {  return "MUŞ"; }
        if(cityID.equals("61")) {  return "NEVŞEHİR"; }
        if(cityID.equals("62")) {  return "NİĞDE"; }
        if(cityID.equals("63")) {  return "ORDU"; }
        if(cityID.equals("64")) {  return "OSMANİYE"; }
        if(cityID.equals("65")) {  return "RİZE"; }
        if(cityID.equals("66")) {  return "SAKARYA"; }
        if(cityID.equals("67")) {  return "SAMSUN"; }
        if(cityID.equals("68")) {  return "SİİRT"; }
        if(cityID.equals("69")) {  return "SİNOP"; }
        if(cityID.equals("70")) {  return "SİVAS"; }
        if(cityID.equals("71")) {  return "ŞANLIURFA"; }
        if(cityID.equals("72")) {  return "ŞIRNAK"; }
        if(cityID.equals("73")) {  return "TEKİRDAĞ"; }
        if(cityID.equals("74")) {  return "TOKAT"; }
        if(cityID.equals("75")) {  return "TRABZON"; }
        if(cityID.equals("76")) {  return "TUNCELİ"; }
        if(cityID.equals("77")) {  return "UŞAK"; }
        if(cityID.equals("78")) {  return "VAN"; }
        if(cityID.equals("79")) {  return "YALOVA"; }
        if(cityID.equals("80")) {  return "YOZGAT"; }
        if(cityID.equals("81")) {  return "ZONGULDAK"; }
        else return "";


    }

    public static void setAutoCompleteAdapter(AutoCompleteTextView autoCompleteTextView , List<String> list , Context ctx)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, R.layout.dropdown_item, list);
        autoCompleteTextView.setAdapter(adapter);
    }

    public static String trimmer(String trim)
    {
        if (trim != null && trim.substring(trim.length() - 1).equals(","))
        {
            trim = trim.substring(0 , trim.length() - 1);
        }
        return  trim;
    }

    public static String SwitchTrimmer(String trim)
    {
        if (trim != null && trim.substring(trim.length() - 1).equals("|"))
        {
            trim = trim.substring(0 , trim.length() - 1);
        }
        return  trim;
    }


    public static String imageToString(ArrayList<String> photos)
    {
        String tmp = "";
        for(int i = 0; i < photos.size(); i++)
        {
            if(photos.get(i) != null)
            {
                tmp = tmp.concat(photos.get(i)).concat(",");
            }
        }
        return tmp ;
    }

    public static void openTimeDialog(EditText editText , Context ctx)
    {
        final  Calendar takvim = Calendar.getInstance();
        int saat = takvim.get(Calendar.HOUR_OF_DAY);
        int dakika = takvim.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                editText.setText(hourOfDay + ":" + minute);

            }
        },saat,dakika,true);

        tpd.setButton(TimePickerDialog.BUTTON_POSITIVE , "Seç" , tpd);
        tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE , "İptal" , tpd);
        tpd.show();

    }

    private static String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public static ArrayList<String> pathToBase64(ArrayList<String> photos , Context context)
    {

        ArrayList<String> base64Photo = new ArrayList<>();

        for(int i = 0 ; i < photos.size(); i++)
        {
            String filePath = "file://"+ photos.get(i);
            Uri myUri = Uri.parse(filePath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), myUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                byte [] imgBytes = byteArrayOutputStream.toByteArray();
                String image = Base64.encodeToString(imgBytes, Base64.DEFAULT);

                base64Photo.add(image);
            } catch (IOException e) {
            }

        }



        return base64Photo;
    }


    public static byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        if(inputStream==null) {
            return null;
        }
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static byte[] compress(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        /*from  ww w.j  av a2  s  . c  om*/
        compress(bais, baos);

        byte[] output = baos.toByteArray();

        try {
            baos.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {

                }
            }

            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {

                }
            }

        }

        return output;
    }

    public static void compress(InputStream is, OutputStream os) {

        GZIPOutputStream gos = null;
        try {
            gos = new GZIPOutputStream(os);
            int count;
            byte data[] = new byte[1024];
            while ((count = is.read(data, 0, 1024)) != -1) {
                gos.write(data, 0, count);
            }

            gos.finish();

            gos.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                gos.close();
            } catch (IOException e) {

            }
        }

    }







}
