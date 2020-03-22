package serviscepde.com.tr.phoneEntryAndVerifyActivities;

import androidx.appcompat.app.AppCompatActivity;
import br.com.sapereaude.maskedEditText.MaskedEditText;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.App;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.TempUser;
import serviscepde.com.tr.Models.UserLogin.UserLoginResponse;
import serviscepde.com.tr.Models.UserLogin.UserLoginResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.SplashActivity;
import serviscepde.com.tr.Utils.Utils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static serviscepde.com.tr.App.TAG;
import static serviscepde.com.tr.App.getApiService;

public class PhoneVerifyActivity extends AppCompatActivity {

    LinearLayout resend_full_view;
    LinearLayout resend_button;
    LinearLayout resend_timer_layout;
    TextView timer_text;
    TextView txtNextButton;
    MaskedEditText edtCode;

    Timer timer;
    TimerTask timerTask;

    public int counter;
    public int counter1;

    SweetAlertDialog pDialog;

    boolean NOBACK;
    int back_press_counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            boolean sendFirst = bundle.getBoolean("startWithSend", false);
            NOBACK = bundle.getBoolean("NOBACK", false);

            if(sendFirst){
                sendNewMessageCode();
            }
        }

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f1a400"));
        pDialog.setTitleText("Lütfen Bekleyiniz");
        pDialog.setCancelable(false);

        resend_full_view = findViewById(R.id.resend_full_view);
        resend_timer_layout = findViewById(R.id.resend_timer_layout);
        resend_button = findViewById(R.id.resend_button);
        timer_text = findViewById(R.id.timer_text);
        txtNextButton = findViewById(R.id.txtNextButton);
        edtCode = findViewById(R.id.edtCode);

        resend_timer_layout.setVisibility(View.GONE);

        resend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNewMessageCode();
                resend_full_view.setVisibility(View.GONE);
                resend_timer_layout.setVisibility(View.VISIBLE);
                counter = 3;
                counter1 = 0;
                new CountDownTimer(180000, 1000){
                    public void onTick(long millisUntilFinished){
                        if(counter1 == 0){
                            counter--;
                            counter1 = 59;
                        }
                        else{
                            counter1--;
                        }

                        if(counter1 > 9){
                            timer_text.setText("0"+counter+":"+counter1);
                        }
                        else{
                            timer_text.setText("0"+counter+":0"+counter1);
                        }

                    }
                    public  void onFinish(){
                        resend_full_view.setVisibility(View.VISIBLE);
                        resend_timer_layout.setVisibility(View.GONE);
                    }
                }.start();
            }
        });

        txtNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyCode();
            }
        });
    }

    private void VerifyCode() {
        pDialog.show();
        HashMap<String , HashMap<String , String>> hashMap = new HashMap<>();
        HashMap<String , String> hashMap1 = new HashMap<>();
        hashMap1.put("GSMLink" , edtCode.getText().toString());
        hashMap1.put("UserID" , TempUser.getID());
        hashMap.put("param" , hashMap1);

        Call<UserLoginResponse> fb_id_control_call = App.getApiService().verifyCode(hashMap);

        fb_id_control_call.enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {

                UserLoginResponseDetail userLoginResponseDetail = response.body().getUserLoginResponseDetail();
                String token = userLoginResponseDetail.getResult();
                JSONObject jsonObject = Utils.jwtToJsonObject(token);


                Log.i(TAG, "onResponse: %%% " + jsonObject);

                try {
                    if(jsonObject.get("OutPutMessage") instanceof JSONObject){
                        String status = ((JSONObject) jsonObject.get("OutPutMessage")).getString("Status");

                        if(status.equals("200")){
                            FastLogin();
                        }
                        else if(status.equals("404")){
                            pDialog.dismiss();
                            Toast.makeText(PhoneVerifyActivity.this, "Doğrulama kodu hatalı.", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    pDialog.dismiss();
                    Log.e("JsonError", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.e("Hata Durumu" , t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(NOBACK){
            Toast.makeText(PhoneVerifyActivity.this, "Lütfen telefon numaranızı doğrulayınız.", Toast.LENGTH_SHORT).show();
        }
        else{
            super.onBackPressed();
            LoginManager.getInstance().logOut();
        }
    }

    private void FastLogin() {
        HashMap<String , HashMap<String , String>> hashMap = new HashMap<>();
        HashMap<String , String> hashMap1 = new HashMap<>();

        hashMap1.put("GSM" , TempUser.getGSM());
        hashMap1.put("Password" , TempUser.getPassword());
        hashMap1.put("FirebaseToken" , TempUser.getFBToken());
        hashMap1.put("DeviceID" , TempUser.getDeviceID());
        hashMap1.put("DeviceType" , "1");
        hashMap.put("param" , hashMap1);

        Call<UserLoginResponse> userLoginResponseCall = App.getApiService().getLogin(hashMap);

        userLoginResponseCall.enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {

                Log.i("Response", response.toString());

                UserLoginResponseDetail userLoginResponseDetail = response.body().getUserLoginResponseDetail();

                Log.i("Request", call.request().body().toString());
                Log.i("Giriş Durumu" , " " + userLoginResponseDetail.getStatus());

                String token = userLoginResponseDetail.getResult();
                JSONObject jsonObject = Utils.jwtToJsonObject(token);

                Log.i(TAG, "onResponse: " + token);


                try {

                    if(jsonObject.get("OutPutMessage") instanceof  JSONObject)
                    {
                        Log.i(TAG, "onResponse: " + " Json Object Geldi");

                        int status = jsonObject.getJSONObject("OutPutMessage").getInt("Status");

                        if (status == 200)
                        {
                            String loggedIn = jsonObject.getJSONObject("OutPutMessage").getJSONObject("Data").getString("Loggedin");
                            Log.i("Loggedin" , loggedIn);
                            SplashActivity.editor.putString("userToken" , token);
                            SplashActivity.editor.putString("Loggedin" , loggedIn);
                            SplashActivity.editor.apply();
                            Intent intent = new Intent(PhoneVerifyActivity.this , MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }


                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                    Log.e("JsonError", e.getMessage());
                }


            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.e("Hata Durumu" , t.getMessage());

            }
        });
    }

    private void sendNewMessageCode() {
        HashMap<String , HashMap<String , String>> hashMap = new HashMap<>();
        HashMap<String , String> hashMap1 = new HashMap<>();
        hashMap1.put("GSM" , TempUser.getGSM());
        hashMap1.put("UserID" , TempUser.getID());
        hashMap.put("param" , hashMap1);

        Log.i(TAG, "%%%% sendNewMessageCode: "+ hashMap);
        Call<UserLoginResponse> fb_id_control_call = App.getApiService().resendCode(hashMap);

        fb_id_control_call.enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {

                UserLoginResponseDetail userLoginResponseDetail = response.body().getUserLoginResponseDetail();
                String token = userLoginResponseDetail.getResult();
                JSONObject jsonObject = Utils.jwtToJsonObject(token);


                Log.i(TAG, "onResponse: %%% " + jsonObject);

                try {
                    if(jsonObject.get("OutPutMessage") instanceof JSONObject){
                        String status = ((JSONObject) jsonObject.get("OutPutMessage")).getString("Status");

                        if(status.equals("200")){
                            Toast.makeText(PhoneVerifyActivity.this, "Doğrulama kodu gönderildi.", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Log.e("JsonError", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                Log.e("%%% Hata Durumu" , t.getMessage());
            }
        });
    }
}
