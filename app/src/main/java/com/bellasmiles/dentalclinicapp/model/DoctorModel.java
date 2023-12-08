package com.bellasmiles.dentalclinicapp.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorModel {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("middleName")
    @Expose
    private String middleName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("contactNo")
    @Expose
    private String contactNo;
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("address")
    @Expose
    private String address;

    public DoctorModel(int id, String firstName, String middleName, String lastName, String gender, String contactNo, String emailAddress, String address) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.contactNo = contactNo;
        this.emailAddress = emailAddress;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getAddress() {
        return address;
    }

    @NonNull
    public String toString() {
        return firstName+ " "+middleName+" " +lastName;
    }

}
