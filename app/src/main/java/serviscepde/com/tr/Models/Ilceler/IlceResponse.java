package serviscepde.com.tr.Models.Ilceler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IlceResponse {

    @SerializedName("response")
    @Expose
    private IlceResponseDetail ılceResponseDetail;

    public IlceResponseDetail getIlceResponseDetail() {
        return ılceResponseDetail;
    }

    public void setIlceResponseDetail(IlceResponseDetail ılceResponseDetail) {
        this.ılceResponseDetail = ılceResponseDetail;
    }


}
