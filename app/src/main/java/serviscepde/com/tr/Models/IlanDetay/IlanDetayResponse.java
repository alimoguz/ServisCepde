package serviscepde.com.tr.Models.IlanDetay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IlanDetayResponse {

    @SerializedName("response")
    @Expose
    private IlanDetayResponseDetail detail;

    public IlanDetayResponseDetail getDetail() {
        return detail;
    }

    public void setDetail(IlanDetayResponseDetail detail) {
        this.detail = detail;
    }
}
