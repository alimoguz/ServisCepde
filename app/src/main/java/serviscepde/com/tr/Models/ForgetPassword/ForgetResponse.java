package serviscepde.com.tr.Models.ForgetPassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetResponse {

    @SerializedName("response")
    @Expose
    private ForgetResponseDetail detail;

    public ForgetResponseDetail getDetail() {
        return detail;
    }

    public void setDetail(ForgetResponseDetail detail) {
        this.detail = detail;
    }
}
