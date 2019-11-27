package serviscepde.com.tr.Models.ForgetPassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetParam {

    @SerializedName("param")
    @Expose
    private ForgetPassword password;

    public ForgetPassword getPassword() {
        return password;
    }

    public void setPassword(ForgetPassword password) {
        this.password = password;
    }
}
