package com.foxconnbc.pushnotification.model;

/**
 * Created by te-arambulaa on 3/30/2016.
 */
public class Notification {
    private String sID;
    private String sContent;
    private String sTimeStamp;

    public Notification() {

    }

    public Notification(String nID, String sName, String sTimeStamp) {
        this.sID = nID;
        this.sContent = sName;
        this.sTimeStamp = sTimeStamp;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsContent() {
        return sContent;
    }

    public void setsContent(String sContent) {
        this.sContent = sContent;
    }

    public String getsTimeStamp() {
        return sTimeStamp;
    }

    public void setsTimeStamp(String sTimeStamp) {
        this.sTimeStamp = sTimeStamp;
    }

    public String toString() {
        return "POJO";
    }
}
