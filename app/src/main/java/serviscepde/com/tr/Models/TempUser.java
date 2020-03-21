package serviscepde.com.tr.Models;

public class TempUser {
    public static String FacebookID;
    public static String MeType;
    public static String UserName;
    public static String SurName;
    public static String Email;
    public static String Password;
    public static String GSM;
    public static String ID;
    public static String FBToken;
    public static String DeviceID;

    public static String getFBToken() {
        return FBToken;
    }

    public static void setFBToken(String FBToken) {
        TempUser.FBToken = FBToken;
    }

    public static String getDeviceID() {
        return DeviceID;
    }

    public static void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public static String getID() {
        return ID;
    }

    public static void setID(String ID) {
        TempUser.ID = ID;
    }

    public static String getEmail() {
        return Email;
    }

    public static void setEmail(String email) {
        Email = email;
    }



    public static String getFacebookID() {
        return FacebookID;
    }

    public static void setFacebookID(String facebookID) {
        FacebookID = facebookID;
    }

    public static String getMeType() {
        return MeType;
    }

    public static void setMeType(String meType) {
        MeType = meType;
    }

    public static String getUserName() {
        return UserName;
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }

    public static String getSurName() {
        return SurName;
    }

    public static void setSurName(String surName) {
        SurName = surName;
    }

    public static String getPassword() {
        return Password;
    }

    public static void setPassword(String password) {
        Password = password;
    }

    public static String getGSM() {
        return GSM;
    }

    public static void setGSM(String GSM) {
        TempUser.GSM = GSM;
    }

    public void resetTempUser(){
        this.FacebookID = "";
        this.MeType = "";
        this.UserName = "";
        this.SurName = "";
        this.Password = "";
        this.GSM = "";
    }
}
