package serviscepde.com.tr.Models;

public class KayitliArama {

    private String ID;
    private String name;
    private String date;

    public KayitliArama(String ID, String name, String date) {
        this.ID = ID;
        this.name = name;
        this.date = date;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
