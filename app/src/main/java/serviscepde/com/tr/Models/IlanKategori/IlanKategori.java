package serviscepde.com.tr.Models.IlanKategori;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IlanKategori {

    @SerializedName("start")
    @Expose
    private Integer start;
    @SerializedName("Tipi")
    @Expose
    private Integer tipi;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getTipi() {
        return tipi;
    }

    public void setTipi(Integer tipi) {
        this.tipi = tipi;
    }
}
