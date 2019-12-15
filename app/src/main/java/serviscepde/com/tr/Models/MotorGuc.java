package serviscepde.com.tr.Models;

public class MotorGuc {

    private String ID;
    private String motorGuc;

    public MotorGuc(String ID, String motorGuc) {
        this.ID = ID;
        this.motorGuc = motorGuc;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMotorGuc() {
        return motorGuc;
    }

    public void setMotorGuc(String motorGuc) {
        this.motorGuc = motorGuc;
    }
}
