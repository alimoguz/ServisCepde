package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.App;
import serviscepde.com.tr.DownloadClass;
import serviscepde.com.tr.Models.City;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.Models.Response.ResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;


public class BildirimGonderFragment extends Fragment {


    View generalView;
    private Context ctx;

    private AutoCompleteTextView acBildirimIl;
    private EditText edtBildirimBaslik,edtBildirimMesaj;
    private TextView txtBildirimGonderLast;

    private String userToken;
    private String cityId,bildirimBaslik,bildirimMesaj;
    private SweetAlertDialog emptyAlert,servisAlert;

    private List<City> sehirler = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private SweetAlertDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.bildirim_gonder_fragment, container, false);

        generalView = rootView;
        ctx = generalView.getContext();

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        acBildirimIl = generalView.findViewById(R.id.acBildirimIl);
        edtBildirimBaslik = generalView.findViewById(R.id.edtBildirimBaslik);
        edtBildirimMesaj = generalView.findViewById(R.id.edtBildirimMesaj);
        txtBildirimGonderLast = generalView.findViewById(R.id.txtBildirimGonderLast);

        pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f1a400"));
        pDialog.setTitleText("Lütfen Bekleyiniz");
        pDialog.setCancelable(false);

        sehirler = DownloadClass.getCities();
        cityNames = DownloadClass.getCityNames();

        Utils.setAutoCompleteAdapter(acBildirimIl , cityNames , ctx);

        acBildirimIl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cityId = parent.getItemAtPosition(position).toString();
                cityId = DownloadClass.getCityIdWithName(cityId);

            }
        });

        txtBildirimGonderLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bildirimBaslik = edtBildirimBaslik.getText().toString();
                bildirimMesaj = edtBildirimMesaj.getText().toString();

                if(cityId.isEmpty() || bildirimBaslik.isEmpty() || bildirimMesaj.isEmpty())
                {
                    emptyAlert = new SweetAlertDialog(ctx , SweetAlertDialog.ERROR_TYPE);
                    emptyAlert.setTitleText("Tüm alanlar doldurulmalı");
                    emptyAlert.show();
                }

                else
                {
                    pDialog.show();
                    HashMap<String , String> users = new HashMap<>();
                    users.put("CityID" , cityId);

                    HashMap<String , Object> param = new HashMap<>();

                    param.put("Users" , users);
                    param.put("Message" , bildirimMesaj);
                    param.put("Title" , bildirimBaslik);
                    param.put("NotificationID" , "52");

                    HashMap<String , Object> body = new HashMap<>();

                    body.put("param" , param);
                    body.put("Token" , userToken);


                    Call<BaseResponse> call = App.getApiService().bildirimGonder(body);
                    call.enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                            ResponseDetail detail = response.body().getResponseDetail();
                            String token = detail.getResult();
                            JSONObject sonuc = Utils.jwtToJsonObject(token);

                            servisAlert = new SweetAlertDialog(ctx , SweetAlertDialog.ERROR_TYPE);
                            servisAlert.setTitleText("Bildirim yönetici onayından sonra yayınlanacaktır");
                            servisAlert.show();


                            try {
                                int status = sonuc.getJSONObject("OutPutMessage").getJSONObject("Data").getInt("Status");
                                if(status == 200)
                                {
                                    pDialog.dismiss();
                                    servisAlert = new SweetAlertDialog(ctx , SweetAlertDialog.ERROR_TYPE);
                                    servisAlert.setTitleText("Bildirim yönetici onayından sonra yayınlanacaktır");
                                    servisAlert.show();
                                }

                                else
                                {
                                    pDialog.dismiss();
                                    servisAlert = new SweetAlertDialog(ctx , SweetAlertDialog.ERROR_TYPE);
                                    servisAlert.setTitleText("Bir hata oluştu lütfen daha sonra tekrar deneyiniz");
                                    servisAlert.show();
                                }

                            } catch (JSONException e) {
                                pDialog.dismiss();
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            pDialog.dismiss();

                        }
                    });



                }

            }
        });




        return rootView;
    }
}