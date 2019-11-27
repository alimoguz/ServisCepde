package serviscepde.com.tr.Models.LastAdvertisement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastAdvertisementResponse {

    @SerializedName("response")
    @Expose
    private  LastAdvertisementResponseDetail lastAdvertisementResponseDetail;

    public LastAdvertisementResponseDetail getLastAdvertisementResponseDetail() {
        return lastAdvertisementResponseDetail;
    }

    public void setLastAdvertisementResponseDetail(LastAdvertisementResponseDetail lastAdvertisementResponseDetail) {
        this.lastAdvertisementResponseDetail = lastAdvertisementResponseDetail;
    }
}
