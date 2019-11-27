package serviscepde.com.tr.Models.UserRegister;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRegisterResponseDetail {

    @SerializedName("status")
    @Expose
    private float status;

    @SerializedName("result")
    @Expose
    private String result;


    public float getStatus() {
        return status;
    }

    public void setStatus(float status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
