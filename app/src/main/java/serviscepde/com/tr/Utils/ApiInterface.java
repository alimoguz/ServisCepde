package serviscepde.com.tr.Utils;

import serviscepde.com.tr.Models.ForgetPassword.ForgetResponse;
import serviscepde.com.tr.Models.IlanDetay.IlanDetayResponse;
import serviscepde.com.tr.Models.IlanEkle.EkleResponse;
import serviscepde.com.tr.Models.IlanKategori.IlanKategoriResponse;
import serviscepde.com.tr.Models.Ilceler.IlceResponse;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.Models.Sehirler.SehirResponse;
import serviscepde.com.tr.Models.UserLogin.UserLoginResponse;
import serviscepde.com.tr.Models.UserRegister.UserRegisterResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {



    @POST("index.php?func=Users&file=LoginControl&ReturnType=1")
    Call<UserLoginResponse> getLogin(@Body HashMap userLogin);


    @POST("index.php?func=Users&file=UsersAdd&ReturnType=1")
    Call<UserRegisterResponse> getRegister(@Body HashMap userRegister);

    @POST("index.php?func=City&file=List&ReturnType=1")
    Call<SehirResponse> getSehirler();

    @POST("index.php?func=City&file=TownList&ReturnType=1")
    Call<IlceResponse> getIlceler();

    @POST("index.php?func=ilanlar&file=List&ReturnType=1")
    Call<IlanKategoriResponse> getIlanbyKategori(@Body HashMap kategoriType);

    @POST("index.php?func=Users&file=UsersForgotPassword&ReturnType=1")
    Call<ForgetResponse> getSifremiUnuttum(@Body HashMap gsm);

    @POST("index.php?func=ilanlar&file=Add&ReturnType=1")
    Call<EkleResponse> ilanEkle(@Body HashMap ilan);

    @POST("index.php?func=ilanlar&file=Details&ReturnType=1")
    Call<IlanDetayResponse> ilanDetay (@Body HashMap ilanDetay);

    @POST("index.php?func=ilanlar&file=List&ReturnType=1")
    Call<IlanKategoriResponse> getSonIlanlar(@Body HashMap sonIlan);

    @POST("index.php?func=ilanlar&file=List&ReturnType=1")
    Call<IlanKategoriResponse> searchIlan(@Body HashMap ilanAra);

    @POST("index.php?func=Users&file=Bildirimlerim&ReturnType=1")
    Call<BaseResponse> getBildirimler(@Body HashMap token);




}
