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
}
