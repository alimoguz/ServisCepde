package serviscepde.com.tr.Models.IlanKategori;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IlanKategoriResponse {

    @SerializedName("response")
    @Expose
    private IlanKategoriResponseDetail ılanKategoriResponseDetail;

    public IlanKategoriResponseDetail getIlanKategoriResponseDetail() {
        return ılanKategoriResponseDetail;
    }

    public void setIlanKategoriResponseDetail(IlanKategoriResponseDetail ılanKategoriResponseDetail) {
        this.ılanKategoriResponseDetail = ılanKategoriResponseDetail;
    }
}
