package serviscepde.com.tr.Models.LastAdvertisement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastAdvertisement {

    @SerializedName("token")
    @Expose
    private String Token;
    @SerializedName("param")
    @Expose
    private LastAdvertisementParam lastAdvertisementParam;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public LastAdvertisementParam getLastAdvertisementParam() {
        return lastAdvertisementParam;
    }

    public void setLastAdvertisementParam(LastAdvertisementParam lastAdvertisementParam) {
        this.lastAdvertisementParam = lastAdvertisementParam;
    }
}
