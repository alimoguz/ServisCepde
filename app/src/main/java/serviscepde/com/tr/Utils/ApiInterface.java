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

    @POST("index.php?func=Users&file=FacebookFindID&ReturnType=1")
    Call<UserLoginResponse> controlFBId(@Body HashMap userLogin);

    @POST("index.php?func=Users&file=GSMCodeReSend&ReturnType=1")
    Call<UserLoginResponse> resendCode(@Body HashMap userLogin);

    @POST("index.php?func=Users&file=GSMApproval&ReturnType=1")
    Call<UserLoginResponse> verifyCode(@Body HashMap userLogin);

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

    @POST("index.php?func=Users&file=UsersFindID&ReturnType=1")
    Call<BaseResponse> kullaniciBilgileri(@Body HashMap token);

    @POST("index.php?func=ilanlar&file=List&ReturnType=1")
    Call<BaseResponse> kullanicininIlanlari(@Body HashMap body);

    @POST("index.php?func=ilanlar&file=Delete&ReturnType=1")
    Call<BaseResponse> ilanSil (@Body HashMap body);

    @POST("index.php?func=Genel&file=AracMarkaModelList&ReturnType=1")
    Call<BaseResponse> getMarkaModel();

    @POST("index.php?func=Users&file=UsersEdit&ReturnType=1")
    Call<BaseResponse> kullaniciDuzenle(@Body HashMap body);

    @POST("index.php?func=SendNotification&file=Add&ReturnType=1")
    Call<BaseResponse> bildirimGonder(@Body HashMap body);

    @POST("index.php?func=Users&file=BildirimlerimFindID&ReturnType=1")
    Call<BaseResponse> bildirimOku (@Body HashMap body);

    @POST("index.php?func=ilanlar&file=List&ReturnType=1")
    Call<BaseResponse> ilanFiltrele (@Body HashMap map);

    @POST("index.php?func=Users&file=SavedSearches&ReturnType=1")
    Call<BaseResponse> kayitliAramalarım(@Body HashMap body);

    @POST("index.php?func=Genel&file=AracKapasiteList&ReturnType=1")
    Call<BaseResponse> getKapasite();

    @POST("index.php?func=Genel&file=MotorGucuList&ReturnType=1")
    Call<BaseResponse> getMotorGucu();

    @POST("index.php?func=Genel&file=MotorHacmiList&ReturnType=1")
    Call<BaseResponse> getMotorHacim();



}
