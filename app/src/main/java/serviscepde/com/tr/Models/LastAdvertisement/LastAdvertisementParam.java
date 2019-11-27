package serviscepde.com.tr.Models.LastAdvertisement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastAdvertisementParam {

    @SerializedName("start")
    @Expose
    private float start;

    @SerializedName("limit")
    @Expose
    private float limit;

    public float getStart() {
        return start;
    }

    public void setStart(float start) {
        this.start = start;
    }

    public float getLimit() {
        return limit;
    }

    public void setLimit(float limit) {
        this.limit = limit;
    }
}
