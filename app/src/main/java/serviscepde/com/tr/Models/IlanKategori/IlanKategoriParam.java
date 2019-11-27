package serviscepde.com.tr.Models.IlanKategori;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IlanKategoriParam {

    @SerializedName("param")
    @Expose
    private  IlanKategori ılanKategori;

    public IlanKategori getIlanKategori() {
        return ılanKategori;
    }

    public void setIlanKategori(IlanKategori ılanKategori) {
        this.ılanKategori = ılanKategori;
    }
}
