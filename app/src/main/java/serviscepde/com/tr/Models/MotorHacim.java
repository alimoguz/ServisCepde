package serviscepde.com.tr.Models;

public class MotorHacim {

    private String ID;
    private String motorHacim;

    public MotorHacim(String ID, String motorHacim) {
        this.ID = ID;
        this.motorHacim = motorHacim;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMotorHacim() {
        return motorHacim;
    }

    public void setMotorHacim(String motorHacim) {
        this.motorHacim = motorHacim;
    }
}
