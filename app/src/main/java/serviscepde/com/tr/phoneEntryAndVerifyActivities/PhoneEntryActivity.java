package serviscepde.com.tr.phoneEntryAndVerifyActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import br.com.sapereaude.maskedEditText.MaskedEditText;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.App;
import serviscepde.com.tr.DownloadClass;
import serviscepde.com.tr.Models.TempUser;
import serviscepde.com.tr.Models.UserRegister.UserRegisterResponse;
import serviscepde.com.tr.Models.UserRegister.UserRegisterResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static serviscepde.com.tr.App.TAG;

public class PhoneEntryActivity extends AppCompatActivity {

    private MaskedEditText phoneEditText;
    private TextView nextButton;
    private TextView txtKullanim;
    private CheckBox checkBoxKullanim;
    private String FacebookID;
    private String MeType;
    private String UserName;
    private String SurName;
    private String Email;
    private String Password;
    private String GSM;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_entry);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f1a400"));
        pDialog.setTitleText("Lütfen Bekleyiniz");
        pDialog.setCancelable(false);

        FacebookID = TempUser.getFacebookID();
        MeType = TempUser.getMeType();
        UserName = TempUser.getUserName();
        SurName = TempUser.getSurName();
        Email = TempUser.getEmail();
        Password = TempUser.getPassword();

        phoneEditText = findViewById(R.id.edtNumara);
        nextButton = findViewById(R.id.txtNextButton);
        txtKullanim = findViewById(R.id.txtKullanim);
        checkBoxKullanim = findViewById(R.id.checkBoxKullanim);

        txtKullanim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.serviscepde.com/sozlesme/uyelik-sozlesmesi";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GSM = phoneEditText.getText().toString();

                if(GSM.startsWith("0(5") && GSM.length() == 16){
                    if(checkBoxKullanim.isChecked()){
                        RegisterAndVerify();
                    }
                    else{
                        Toast.makeText(PhoneEntryActivity.this, "Lütfen kullanıcı sözleşmesini kabul ediniz.", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    phoneEditText.setError("Lütfen geçerli bir telefon numarası giriniz.");
                }
            }
        });
    }

    private void RegisterAndVerify() {
        HashMap<String , HashMap<String , String>> node = new HashMap<>();
        HashMap<String , String> body = new HashMap<>();
        pDialog.show();

        body.put("FacebookID" , FacebookID);
        body.put("MeType" , MeType);
        body.put("UserName" , UserName);
        body.put("SurName" , SurName);
        body.put("Email" , Email);
        body.put("Password" , Password);
        body.put("GSM" , GSM);

        node.put("param" , body);

        Log.i(TAG, "RegisterAndVerify: %%%%" + node);
        Call<UserRegisterResponse> userRegisterResponseCall = App.getApiService().getRegister(node);
        userRegisterResponseCall.enqueue(new Callback<UserRegisterResponse>() {
            @Override
            public void onResponse(Call<UserRegisterResponse> call, Response<UserRegisterResponse> response) {

                UserRegisterResponseDetail userRegisterResponseDetail = response.body().getUserRegisterResponseDetail();
                String token = userRegisterResponseDetail.getResult();

                JSONObject jsonObject = Utils.jwtToJsonObject(token);

                try {
                    JSONArray errors = jsonObject.getJSONArray("errorOther");

                    if(errors.length() > 0){
                        pDialog.dismiss();
                        Toast.makeText(PhoneEntryActivity.this, errors.get(0).toString(), Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(jsonObject.get("OutPutMessage") instanceof  JSONObject)
                        {

                            int status = jsonObject.getJSONObject("OutPutMessage").getInt("Status");
                            if(status == 200)
                            {
                                pDialog.dismiss();
                                String user_id = jsonObject.getJSONObject("OutPutMessage").getString("Data");
                                TempUser.setGSM(GSM);
                                TempUser.setID(user_id);
                                Intent intent = new Intent(PhoneEntryActivity.this, PhoneVerifyActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        if(jsonObject.get("OutPutMessage") instanceof JSONArray)
                        {
                            pDialog.dismiss();

                        }
                    }


                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<UserRegisterResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: %%% Hata ", t.getCause());
                pDialog.dismiss();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LoginManager.getInstance().logOut();
    }
}
