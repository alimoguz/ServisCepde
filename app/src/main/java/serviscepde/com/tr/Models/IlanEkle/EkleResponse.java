package serviscepde.com.tr.Models.IlanEkle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EkleResponse {

    @SerializedName("response")
    @Expose
    private EkleResponseDetail detail;

    public EkleResponseDetail getDetail() {
        return detail;
    }

    public void setDetail(EkleResponseDetail detail) {
        this.detail = detail;
    }
}
