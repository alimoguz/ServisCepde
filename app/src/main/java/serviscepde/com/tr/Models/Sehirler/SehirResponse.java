package serviscepde.com.tr.Models.Sehirler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SehirResponse {

    @SerializedName("response")
    @Expose
    private SehirResponseDetail response;

    public SehirResponseDetail getSehirResponseDetail() {
        return response;
    }

    public void setSehirResponseDetail(SehirResponseDetail response) {
        this.response = response;
    }
}
