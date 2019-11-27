package serviscepde.com.tr.Models.ForgetPassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetPassword {

    @SerializedName("GSM")
    @Expose
    private String gSM;

    public String getGSM() {
        return gSM;
    }

    public void setGSM(String gSM) {
        this.gSM = gSM;
    }
}
