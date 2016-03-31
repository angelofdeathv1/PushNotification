package com.foxconnbc.pushnotification.model;

/**
 * Created by te-arambulaa on 3/30/2016.
 */
public class Notification {
    private int nID;
    private String sContent;
    private String sTimeStamp;

    public Notification() {

    }

    public Notification(int nID, String sName, String sTimeStamp) {
        this.nID = nID;
        this.sContent = sName;
        this.sTimeStamp = sTimeStamp;
    }

    public int getnID() {
        return nID;
    }

    public void setnID(int nID) {
        this.nID = nID;
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
