package serviscepde.com.tr.Models;

public class Ilce {

    private String cityID;
    private String ilceID;
    private String ilceName;

    public Ilce(String cityID, String ilceID, String ilceName) {
        this.cityID = cityID;
        this.ilceID = ilceID;
        this.ilceName = ilceName;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getIlceID() {
        return ilceID;
    }

    public void setIlceID(String ilceID) {
        this.ilceID = ilceID;
    }

    public String getIlceName() {
        return ilceName;
    }

    public void setIlceName(String ilceName) {
        this.ilceName = ilceName;
    }
}
