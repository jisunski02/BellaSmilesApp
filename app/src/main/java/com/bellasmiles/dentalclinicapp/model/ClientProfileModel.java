package com.bellasmiles.dentalclinicapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientProfileModel {

    @SerializedName("clientId")
    @Expose
    private String clientId;
    @SerializedName("clientFirstName")
    @Expose
    private String clientFirstName;
    @SerializedName("clientMiddleName")
    @Expose
    private String clientMiddleName;
    @SerializedName("clientLastName")
    @Expose
    private String clientLastName;
    @SerializedName("clientBirthdate")
    @Expose
    private String clientBirthdate;
    @SerializedName("clientGender")
    @Expose
    private String clientGender;
    @SerializedName("clientContactNo")
    @Expose
    private String clientContactNo;
    @SerializedName("clientEmailAddress")
    @Expose
    private String clientEmailAddress;
    @SerializedName("clientAddress")
    @Expose
    private String clientAddress;

    public String getClientId() {
        return clientId;
    }

    public String getClientFirstName() {
        return clientFirstName;
    }

    public String getClientMiddleName() {
        return clientMiddleName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public String getClientBirthdate() {
        return clientBirthdate;
    }

    public String getClientGender() {
        return clientGender;
    }

    public String getClientContactNo() {
        return clientContactNo;
    }

    public String getClientEmailAddress() {
        return clientEmailAddress;
    }

    public String getClientAddress() {
        return clientAddress;
    }
}
