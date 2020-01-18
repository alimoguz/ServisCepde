package serviscepde.com.tr.Models;

public class Bildirim {

    private String ID;
    private String BildirimID;
    private String Status;
    private String Title;
    private String Message;
    private String create_at;

    public Bildirim(String ID, String bildirimID, String status, String title, String message, String create_at) {
        this.ID = ID;
        BildirimID = bildirimID;
        Status = status;
        Title = title;
        Message = message;
        this.create_at = create_at;
    }

    public String getID() {
        return ID;
    }

    public String getBildirimID() {
        return BildirimID;
    }

    public String getStatus() {
        return Status;
    }

    public String getTitle() {
        return Title;
    }

    public String getMessage() {
        return Message;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setBildirimID(String bildirimID) {
        BildirimID = bildirimID;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
}
