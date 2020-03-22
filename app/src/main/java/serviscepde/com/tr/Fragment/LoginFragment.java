package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.DialogInterface;
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

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import serviscepde.com.tr.App;
import serviscepde.com.tr.MainActivity;

import serviscepde.com.tr.Models.TempUser;
import serviscepde.com.tr.Models.UserLogin.UserLogin;
import serviscepde.com.tr.Models.UserLogin.UserLoginResponse;
import serviscepde.com.tr.Models.UserLogin.UserLoginResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.SplashActivity;
import serviscepde.com.tr.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.phoneEntryAndVerifyActivities.PhoneEntryActivity;
import serviscepde.com.tr.phoneEntryAndVerifyActivities.PhoneVerifyActivity;

import static com.facebook.FacebookSdk.getApplicationContext;
import static serviscepde.com.tr.App.TAG;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


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

    private String user_fb_id;
    private String user_fb_name;
    private String user_fb_surname;
    private String user_fb_email;

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
        TempUser.setDeviceID(deviceID);

        setFacebookButton();


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

                                if (status == 202)
                                {
                                    pDialog.dismiss();
                                    String error = jsonObject.getJSONArray("errorOther").getString(0);

                                    TempUser.setID(jsonObject.getJSONObject("OutPutMessage").getString("UserID"));
                                    TempUser.setPassword(password);
                                    TempUser.setFBToken(token);
                                    TempUser.setDeviceID(deviceID);
                                    TempUser.setGSM(number);

                                    invalidLogin = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                                    invalidLogin.setTitleText(error);
                                    invalidLogin.setCancelable(false);
                                    invalidLogin.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            Intent intent = new Intent(getApplicationContext(), PhoneVerifyActivity.class);
                                            intent.putExtra("startWithSend", true);
                                            startActivity(intent);
                                        }
                                    });
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
                            Log.e("%%%JsonError", e.getMessage());
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

    private void setFacebookButton() {
        callbackManager = CallbackManager.Factory.create();
        txtFacebookLogin.setFragment(LoginFragment.this);
        txtFacebookLogin.setPermissions(Arrays.asList(
                "public_profile", "email"));

        txtFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken == null){
                //Toast.makeText(ctx, "Başarıyla çıkış yaptınız.", Toast.LENGTH_LONG).show();
            }
            else{
                loadProfile(currentAccessToken);
            }
        }
    };

    private void loadProfile(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Log.i(TAG, "onCompleted: %%%" + object);
                    user_fb_name = object.getString("first_name");
                    user_fb_surname = object.getString("last_name");
                    user_fb_email = object.getString("email");
                    user_fb_id = object.getString("id");

                    TempUser.setFacebookID(user_fb_id);
                    TempUser.setUserName(user_fb_name);
                    TempUser.setSurName(user_fb_surname);
                    TempUser.setEmail(user_fb_email);
                    TempUser.setMeType("4");
                    TempUser.setPassword(user_fb_id);

                    controlForLogin();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name, last_name, email, id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void controlForLogin() {
        pDialog.show();
        HashMap<String , HashMap<String , String>> hashMap = new HashMap<>();
        HashMap<String , String> hashMap1 = new HashMap<>();
        hashMap1.put("FacebookID" , user_fb_id);
        hashMap.put("param" , hashMap1);

        Call<UserLoginResponse> fb_id_control_call = App.getApiService().controlFBId(hashMap);

        fb_id_control_call.enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {

                UserLoginResponseDetail userLoginResponseDetail = response.body().getUserLoginResponseDetail();
                String token = userLoginResponseDetail.getResult();
                JSONObject jsonObject = Utils.jwtToJsonObject(token);


                Log.i(TAG, "onResponse: %%% " + jsonObject);

                try {
                    if(jsonObject.get("errorOther") instanceof JSONArray){

                        if(jsonObject.getJSONArray("errorOther").length() > 0){
                            String error = jsonObject.getJSONArray("errorOther").getString(0);

                            if(error.equals("404")){
                                pDialog.dismiss();
                                //Bu arkadaş daha önce hiç giriş yapmamış. Kayıt olacak ama kayıttan önce telefon numarası iste.
                                askPhoneNumber();
                            }
                            else if(error.equals("402")){
                                pDialog.dismiss();
                                String GSM = jsonObject.getJSONObject("OutPutMessage").getJSONObject("Data").getString("GSM");
                                String UserID = jsonObject.getJSONObject("OutPutMessage").getJSONObject("Data").getString("ID");
                                TempUser.setGSM(GSM);
                                TempUser.setID(UserID);

                                Intent intent = new Intent(getApplicationContext(), PhoneVerifyActivity.class);
                                intent.putExtra("startWithSend", true);
                                startActivity(intent);
                            }
                        }
                        else{
                            HashMap<String , HashMap<String , String>> hashMap = new HashMap<>();
                            HashMap<String , String> hashMap1 = new HashMap<>();

                            String GSM = jsonObject.getJSONObject("OutPutMessage").getJSONObject("Data").getString("GSM");

                            hashMap1.put("GSM" , GSM);
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
                                                Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                pDialog.dismiss();
                                                Toast.makeText(ctx, "Şifrenizi değiştirdiğiniz için. Facebook ile giriş yapamazsınız.", Toast.LENGTH_LONG).show();
                                                LoginManager.getInstance().logOut();
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
                        }

                    }
                }
                catch (JSONException e) {
                    pDialog.dismiss();
                    Log.e("%%% JsonError", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.e("Hata Durumu" , t.getMessage());
            }
        });
    }

    private void askPhoneNumber() {
        Intent intent = new Intent(ctx, PhoneEntryActivity.class);
        startActivity(intent);
    }

    private void getFireBaseToken() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (!task.isSuccessful()) {
                    Log.w("FIREBASE", task.getException());
                }

                token = task.getResult().getToken();
                TempUser.setFBToken(token);
                Log.i("Token" , token);


            }
        });
    }


}
