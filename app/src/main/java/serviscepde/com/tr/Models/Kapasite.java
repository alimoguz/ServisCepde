package serviscepde.com.tr.Models;

public class Kapasite {

    private String ID;
    private String capacity;

    public Kapasite(String ID, String capacity) {
        this.ID = ID;
        this.capacity = capacity;
    }

    public String getID() {
        return ID;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}
