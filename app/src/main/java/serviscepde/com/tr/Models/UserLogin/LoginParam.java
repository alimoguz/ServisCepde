package serviscepde.com.tr.Models.UserLogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginParam {

    @SerializedName("GSM")
    @Expose
    private String gSM;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("FirebaseToken")
    @Expose
    private String firebaseToken;
    @SerializedName("DeviceID")
    @Expose
    private String deviceID;

    public String getGSM() {
        return gSM;
    }

    public void setGSM(String gSM) {
        this.gSM = gSM;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
