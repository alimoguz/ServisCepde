package serviscepde.com.tr.Models;

public class City {

    private String ID ;
    private String cityName;

    public City(String ID, String cityName) {
        this.ID = ID;
        this.cityName = cityName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
