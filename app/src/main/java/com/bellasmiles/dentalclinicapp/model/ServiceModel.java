package com.bellasmiles.dentalclinicapp.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceModel {

    @SerializedName("serviceId")
    @Expose
    private int serviceId;
    @SerializedName("serviceDesc")
    @Expose
    private String serviceDesc;
    @SerializedName("serviceCost")
    @Expose
    private String serviceCost;

    public ServiceModel(int serviceId, String serviceDesc, String serviceCost) {
        this.serviceId = serviceId;
        this.serviceDesc = serviceDesc;
        this.serviceCost = serviceCost;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public String getServiceCost() {
        return serviceCost;
    }

    @NonNull
    public String toString() {
        return serviceDesc;
    }
}
