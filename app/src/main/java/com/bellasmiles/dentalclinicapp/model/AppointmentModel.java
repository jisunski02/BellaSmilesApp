package com.bellasmiles.dentalclinicapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentModel {
    @SerializedName("transactionNo")
    @Expose
    private String transactionNo;
    @SerializedName("docFirstName")
    @Expose
    private String docFirstName;
    @SerializedName("docMiddleName")
    @Expose
    private String docMiddleName;
    @SerializedName("docLastName")
    @Expose
    private String docLastName;
    @SerializedName("servDesc")
    @Expose
    private String servDesc;
    @SerializedName("servCost")
    @Expose
    private String servCost;
    @SerializedName("schedDate")
    @Expose
    private String schedDate;
    @SerializedName("schedStartTime")
    @Expose
    private String schedStartTime;
    @SerializedName("schedEndTime")
    @Expose
    private String schedEndTime;
    @SerializedName("schedDuration")
    @Expose
    private String schedDuration;

    public AppointmentModel(String transactionNo, String docFirstName, String docMiddleName, String docLastName, String servDesc, String servCost, String schedDate, String schedStartTime, String schedEndTime, String schedDuration) {
        this.transactionNo = transactionNo;
        this.docFirstName = docFirstName;
        this.docMiddleName = docMiddleName;
        this.docLastName = docLastName;
        this.servDesc = servDesc;
        this.servCost = servCost;
        this.schedDate = schedDate;
        this.schedStartTime = schedStartTime;
        this.schedEndTime = schedEndTime;
        this.schedDuration = schedDuration;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public String getDocFirstName() {
        return docFirstName;
    }

    public String getDocMiddleName() {
        return docMiddleName;
    }

    public String getDocLastName() {
        return docLastName;
    }

    public String getServDesc() {
        return servDesc;
    }

    public String getServCost() {
        return servCost;
    }

    public String getSchedDate() {
        return schedDate;
    }

    public String getSchedStartTime() {
        return schedStartTime;
    }

    public String getschedEndTime() {
        return schedEndTime;
    }

    public String getSchedDuration() {
        return schedDuration;
    }
}
