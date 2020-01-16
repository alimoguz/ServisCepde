package serviscepde.com.tr.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Models.ForgetPassword.ForgetResponse;
import serviscepde.com.tr.Models.ForgetPassword.ForgetResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordFragment extends Fragment {

    View generalView;

    MaskedEditText edtNumara;
    TextView txtSifreSifirla;

    SweetAlertDialog alertNumara;

    LoginFragment loginFragment;

    String telefon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.forget_password_fragment, container ,false);

        generalView = rootView;

        txtSifreSifirla = generalView.findViewById(R.id.txtSifreSifirla);
        edtNumara = generalView.findViewById(R.id.edtNumara);

         loginFragment = new LoginFragment();


        txtSifreSifirla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                telefon = edtNumara.getText().toString();

                if(telefon.isEmpty())
                {
                    alertNumara = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                    alertNumara.setTitleText("Numara boş bırakılamaz");
                    alertNumara.show();
                }

                HashMap<String , HashMap<String , String>> hashMap = new HashMap<>();
                HashMap<String , String> hashMap1 = new HashMap<>();

                hashMap1.put("GSM" , telefon);
                hashMap.put("param" , hashMap1);


                Call<ForgetResponse> responseCall = App.getApiService().getSifremiUnuttum(hashMap);

                responseCall.enqueue(new Callback<ForgetResponse>() {
                    @Override
                    public void onResponse(Call<ForgetResponse> call, Response<ForgetResponse> response) {

                        ForgetResponseDetail detail = response.body().getDetail();

                        String token = detail.getResult();

                        JSONObject object = Utils.jwtToJsonObject(token);


                        try {
                            int status = object.getJSONObject("OutPutMessage").getInt("Status");

                            Log.i("Status" , String.valueOf(status));

                            String msg;

                            if(status == 200)
                            {
                                msg = object.getJSONObject("OutPutMessage").getString("SuccessMessage");
                                alertNumara = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.SUCCESS_TYPE);
                                alertNumara.setTitleText(msg);
                                alertNumara.show();

                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragSplash , loginFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }

                            if(status == 201)
                            {
                                msg = object.getJSONObject("OutPutMessage").getString("ErrorMessage");
                                alertNumara = new SweetAlertDialog(generalView.getContext() , SweetAlertDialog.ERROR_TYPE);
                                alertNumara.setTitleText(msg);
                                alertNumara.show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure(Call<ForgetResponse> call, Throwable t) {

                    }
                });
            }
        });

        return  rootView;

    }
}
