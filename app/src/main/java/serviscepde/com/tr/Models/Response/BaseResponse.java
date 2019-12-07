package serviscepde.com.tr.Models.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("response")
    @Expose
    private ResponseDetail responseDetail;

    public ResponseDetail getResponseDetail() {
        return responseDetail;
    }

    public void setResponseDetail(ResponseDetail responseDetail) {
        this.responseDetail = responseDetail;
    }
}
