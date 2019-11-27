package serviscepde.com.tr.Models.UserRegister;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterParam {


    @SerializedName("UserName")
    @Expose
    private String UserName;
    @SerializedName("Email")
    @Expose
    private String Email;
    @SerializedName("Password")
    @Expose
    private String Password;
    @SerializedName("GSM")
    @Expose
    private String GSM;


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGSM() {
        return GSM;
    }

    public void setGSM(String GSM) {
        this.GSM = GSM;
    }
}
