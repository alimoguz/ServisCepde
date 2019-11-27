package serviscepde.com.tr.Models;

public class IlanOzetBilgi {

    private String ID;
    private String ilanCity;
    private String Baslik;
    private String Ucret;
    private String Resimler;

    public IlanOzetBilgi(String ID, String ilanCity, String baslik, String ucret, String resimler) {
        this.ID = ID;
        this.ilanCity = ilanCity;
        Baslik = baslik;
        Ucret = ucret;
        Resimler = resimler;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIlanCity() {
        return ilanCity;
    }

    public void setIlanCity(String ilanCity) {
        this.ilanCity = ilanCity;
    }

    public String getBaslik() {
        return Baslik;
    }

    public void setBaslik(String baslik) {
        Baslik = baslik;
    }

    public String getUcret() {
        return Ucret;
    }

    public void setUcret(String ucret) {
        Ucret = ucret;
    }

    public String getResimler() {
        return Resimler;
    }

    public void setResimler(String resimler) {
        Resimler = resimler;
    }
}
