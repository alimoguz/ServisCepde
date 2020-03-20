package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import serviscepde.com.tr.App;
import serviscepde.com.tr.MainActivity;

import serviscepde.com.tr.Models.UserLogin.UserLoginResponse;
import serviscepde.com.tr.Models.UserLogin.UserLoginResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.SplashActivity;
import serviscepde.com.tr.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;
import static serviscepde.com.tr.App.TAG;

public class LoginFragment extends Fragment {

    View generalView;

    TextView txtKayitOl,txtGirisYap,txtUyeOlmadanDevam;
    LoginButton txtFacebookLogin;
    EditText edtSifre;
    ImageView imgBanner;
    RelativeLayout relSifremiUnuttum;
    MaskedEditText edtCepTelefonu;

    String number,password;
    String token = "";

    Context ctx;
    int status;

    SweetAlertDialog emailAlert, passwordAlert,invalidLogin;

    SignupFragment signupFragment;
    ForgetPasswordFragment forgetPasswordFragment;
    SweetAlertDialog pDialog;

    private CallbackManager callbackManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.login_fragment , container , false);

        generalView = rootView;

        imgBanner = generalView.findViewById(R.id.imgBanner);

        edtSifre = generalView.findViewById(R.id.edtSifre);
        edtCepTelefonu = generalView.findViewById(R.id.edtCepTelefonu);

        relSifremiUnuttum = generalView.findViewById(R.id.relSifremiUnuttum);
        txtKayitOl = generalView.findViewById(R.id.txtKayitOl);
        txtGirisYap = generalView.findViewById(R.id.txtGirisYap);
        txtUyeOlmadanDevam = generalView.findViewById(R.id.txtUyeOlmadanDevam);
        txtFacebookLogin = generalView.findViewById(R.id.txtFacebookLogin);

        ctx = getContext();

        pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f1a400"));
        pDialog.setTitleText("Giriş yapılıyor");
        pDialog.setCancelable(false);


        signupFragment = new SignupFragment();
        forgetPasswordFragment = new ForgetPasswordFragment();

        getFireBaseToken();

        String deviceID = Utils.getDeviceID(ctx);

        callbackManager = CallbackManager.Factory.create();
        txtFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "İptal edildi", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Facebook login hata oluştu", Toast.LENGTH_SHORT).show();
            }
        });


        txtGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = edtCepTelefonu.getText().toString();
                password = edtSifre.getText().toString();

                pDialog.show();


                Log.i("MaskedText" , edtCepTelefonu.getMask() + edtCepTelefonu.getRawText() + edtCepTelefonu.getText().toString());
                Log.i("Number" , number);



                if(number.isEmpty())
                {
                    emailAlert = new SweetAlertDialog(generalView.getContext(), SweetAlertDialog.ERROR_TYPE);
                    emailAlert.setTitleText("Telefon boş bırakılamaz");
                    emailAlert.show();
                }

                if(password.isEmpty())
                {
                    passwordAlert = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                    passwordAlert.setTitleText("Şifre boş bırakılamaz");
                    passwordAlert.show();
                }


                HashMap<String , HashMap<String , String>> hashMap = new HashMap<>();
                HashMap<String , String> hashMap1 = new HashMap<>();

                hashMap1.put("GSM" , number);
                hashMap1.put("Password" , password);
                hashMap1.put("FirebaseToken" , token);
                hashMap1.put("DeviceID" , deviceID);
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
                                    Intent intent = new Intent(ctx , MainActivity.class);
                                    startActivity(intent);
                                }

                                if (status == 201)
                                {
                                    pDialog.dismiss();
                                    invalidLogin = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                                    invalidLogin.setTitleText(jsonObject.getJSONObject("OutPutMessage").getString("ErrorMessage"));
                                    invalidLogin.show();
                                }
                            }
                            if(jsonObject.get("OutPutMessage") instanceof JSONArray)
                            {
                                Log.i(TAG, "onResponse: " + " Json Array Geldi");

                                String error = jsonObject.getJSONArray("errorOther").getString(0);
                                pDialog.dismiss();

                                invalidLogin = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                                invalidLogin.setTitleText(error);
                                invalidLogin.show();
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

                Log.i("Email and Password" , number + "\t" + password);


            }
        });


        txtKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragSplash , signupFragment );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        relSifremiUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragSplash , forgetPasswordFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        txtUyeOlmadanDevam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx , MainActivity.class);
                startActivity(intent);

            }
        });


        return rootView;

    }

    private void getFireBaseToken() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (!task.isSuccessful()) {
                    Log.w("FIREBASE", task.getException());
                }


                token = task.getResult().getToken();
                Log.i("Token" , token);


            }
        });
    }


}
