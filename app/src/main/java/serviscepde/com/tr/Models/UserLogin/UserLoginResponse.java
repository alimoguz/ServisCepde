package serviscepde.com.tr.Models.UserLogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginResponse {

    @SerializedName("response")
    @Expose
    UserLoginResponseDetail userLoginResponseDetail;

    public UserLoginResponseDetail getUserLoginResponseDetail() {
        return userLoginResponseDetail;
    }

    public void setUserLoginResponseDetail(UserLoginResponseDetail userLoginResponseDetail) {
        this.userLoginResponseDetail = userLoginResponseDetail;
    }
}
