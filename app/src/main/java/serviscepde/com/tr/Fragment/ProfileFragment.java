package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.App;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.Models.Response.ResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.SplashActivity;
import serviscepde.com.tr.Utils.Utils;


public class ProfileFragment extends Fragment {


    View generalView;
    private TextView txtUserName,txtPhoneNumber,txtProfiliDuzenle,txtCikisYap,txtIlanlarim,txtKayitliAramalar,txtBildirimGonder;
    private Context ctx;
    private String userToken;
    private IlanlarımFragment ilanlarimFragment;
    private KullanıcıDüzenleFragment kullaniciDuzenleFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        generalView = rootView;
        ctx = generalView.getContext();

        MainActivity.relHeader.setVisibility(View.GONE);

        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");


        ilanlarimFragment = new IlanlarımFragment();
        kullaniciDuzenleFragment = new KullanıcıDüzenleFragment();




        txtUserName = generalView.findViewById(R.id.txtUserName);
        txtPhoneNumber = generalView.findViewById(R.id.txtPhoneNumber);
        txtProfiliDuzenle = generalView.findViewById(R.id.txtProfiliDuzenle);
        txtCikisYap = generalView.findViewById(R.id.txtCikisYap);
        txtIlanlarim = generalView.findViewById(R.id.txtIlanlarim);
        txtKayitliAramalar = generalView.findViewById(R.id.txtKayitliAramalar);
        txtBildirimGonder = generalView.findViewById(R.id.txtBildirimGonder);

        txtProfiliDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragMain , kullaniciDuzenleFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        HashMap<String , String> hashMap1 = new HashMap<>();
        hashMap1.put("Token" , userToken);

        Call<BaseResponse> call = App.getApiService().kullaniciBilgileri(hashMap1);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                Log.i("Başarılı" , "onResponse");

                ResponseDetail detail = response.body().getResponseDetail();
                String token = detail.getResult();

                JSONObject user = Utils.jwtToJsonObject(token);

                try {
                    JSONObject userDetail = user.getJSONObject("OutPutMessage").getJSONObject("Data");

                    String name = userDetail.getString("UserName").concat(" ").concat(userDetail.getString("SurName"));
                    txtUserName.setText(name);
                    String GSM = userDetail.getString("GSM");
                    txtPhoneNumber.setText(GSM);
                    String meType = userDetail.getString("MeType");

                    String UserID = userDetail.getString("ID");

                    if(meType.equals("1"))
                    {
                        txtBildirimGonder.setVisibility(View.VISIBLE);
                    }

                    txtCikisYap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            sharedPref.edit().putString("Loggedin" , "0").apply();
                            Intent intent = new Intent( ctx , SplashActivity.class);
                            startActivity(intent);

                        }
                    });

                    txtIlanlarim.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle bundle = new Bundle();
                            bundle.putString("UserID" , UserID);

                            ilanlarimFragment.setArguments(bundle);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragMain , ilanlarimFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

                Log.i("Başarısız" , t.getMessage());

            }
        });






        return rootView;
    }
}