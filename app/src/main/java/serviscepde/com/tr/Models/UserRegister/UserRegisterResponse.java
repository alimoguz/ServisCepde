package serviscepde.com.tr.Models.UserRegister;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRegisterResponse {


    @SerializedName("response")
    @Expose
    UserRegisterResponseDetail userRegisterResponseDetail;

    public UserRegisterResponseDetail getUserRegisterResponseDetail() {
        return userRegisterResponseDetail;
    }

    public void setUserRegisterResponseDetail(UserRegisterResponseDetail userRegisterResponseDetail) {
        this.userRegisterResponseDetail = userRegisterResponseDetail;
    }
}
