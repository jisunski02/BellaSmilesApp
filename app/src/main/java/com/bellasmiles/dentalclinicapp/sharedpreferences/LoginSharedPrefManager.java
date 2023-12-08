package com.bellasmiles.dentalclinicapp.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class LoginSharedPrefManager {

    SharedPreferences login_preferences;
    SharedPreferences.Editor login_preferences_editor;
    Context context_login_preferences;
    final int Private_Mode_LOGIN = 0;
    @NonNull
    private static final String login_pref_name = "newLogin";

    @NonNull
    private static final String login_IS_LOGIN = "IsLoggedIn";

    //Student
    private final String KEY_CLIENT_ID = "KEY_CLIENT_ID";
    private final String KEY_CLIENT_FIRSTNAME = "KEY_CLIENT_FIRSTNAME";
    private final String KEY_CLIENT_MIDDLENAME = "KEY_CLIENT_MIDDLENAME";
    private final String KEY_CLIENT_LASTNAME = "KEY_CLIENT_LASTNAME";
    private final String KEY_CLIENT_BIRTHDATE = "KEY_CLIENT_BIRTHDATE";
    private final String KEY_CLIENT_GENDER = "KEY_CLIENT_GENDER";
    private final String KEY_CLIENT_CONTACTNO = "KEY_CLIENT_CONTACTNO";
    private final String KEY_CLIENT_EMAILADDRESS = "KEY_CLIENT_EMAILADDRESS";
    private final String KEY_CLIENT_ADDRESS = "KEY_CLIENT_ADDRESS";


    public LoginSharedPrefManager(Context context){
        this.context_login_preferences = context;
        login_preferences = context_login_preferences.getSharedPreferences(login_pref_name, Private_Mode_LOGIN);
        login_preferences_editor = login_preferences.edit();
    }

    public void createClientLoginSession(){
        login_preferences_editor.putBoolean(login_IS_LOGIN, true);
        login_preferences_editor.commit();
    }


    public void setClientId(String id){
        login_preferences_editor.putString(KEY_CLIENT_ID, id);
        login_preferences_editor.commit();
    }

    public String getClientId() {
        return login_preferences.getString(KEY_CLIENT_ID, null);
    }

    public void setClientFirstName(String firstname){
        login_preferences_editor.putString(KEY_CLIENT_FIRSTNAME, firstname);
        login_preferences_editor.commit();
    }

    public String getClientFirstName() {
        return login_preferences.getString(KEY_CLIENT_FIRSTNAME, null);
    }

    public void setClientMiddleName(String middlename){
        login_preferences_editor.putString(KEY_CLIENT_MIDDLENAME, middlename);
        login_preferences_editor.commit();
    }

    public String getClientMiddleName() {
        return login_preferences.getString(KEY_CLIENT_MIDDLENAME, null);
    }

    public void setClientLastName(String lastname){
        login_preferences_editor.putString(KEY_CLIENT_LASTNAME, lastname);
        login_preferences_editor.commit();
    }

    public String getClientLastName() {
        return login_preferences.getString(KEY_CLIENT_LASTNAME, null);
    }

    public void setClientBirthdate(String birthdate){
        login_preferences_editor.putString(KEY_CLIENT_BIRTHDATE, birthdate);
        login_preferences_editor.commit();
    }

    public String getClientBirthdate() {
        return login_preferences.getString(KEY_CLIENT_BIRTHDATE, null);
    }

    public void setClientGender(String gender){
        login_preferences_editor.putString(KEY_CLIENT_GENDER, gender);
        login_preferences_editor.commit();
    }

    public String getClientGender() {
        return login_preferences.getString(KEY_CLIENT_GENDER, null);
    }

    public void setClientContactNo(String contactno){
        login_preferences_editor.putString(KEY_CLIENT_CONTACTNO, contactno);
        login_preferences_editor.commit();
    }

    public String getClientContactNo() {
        return login_preferences.getString(KEY_CLIENT_CONTACTNO, null);
    }

    public void setClientEmailAddress(String emailaddress){
        login_preferences_editor.putString(KEY_CLIENT_EMAILADDRESS, emailaddress);
        login_preferences_editor.commit();
    }

    public String getClientEmailAddress() {
        return login_preferences.getString(KEY_CLIENT_EMAILADDRESS, null);
    }


    public void setClientAddress(String address){
        login_preferences_editor.putString(KEY_CLIENT_ADDRESS, address);
        login_preferences_editor.commit();
    }

    public String getClientAddress() {
        return login_preferences.getString(KEY_CLIENT_ADDRESS, null);
    }


    public boolean isLoggedIn(){
        return login_preferences.getBoolean(login_IS_LOGIN, false);
    }

    public void logoutClient(){
        login_preferences_editor.remove(KEY_CLIENT_ID);
        login_preferences_editor.remove(KEY_CLIENT_FIRSTNAME);
        login_preferences_editor.remove(KEY_CLIENT_MIDDLENAME);
        login_preferences_editor.remove(KEY_CLIENT_LASTNAME);
        login_preferences_editor.remove(KEY_CLIENT_BIRTHDATE);
        login_preferences_editor.remove(KEY_CLIENT_GENDER);
        login_preferences_editor.remove(KEY_CLIENT_CONTACTNO);
        login_preferences_editor.remove(KEY_CLIENT_EMAILADDRESS);
        login_preferences_editor.remove(KEY_CLIENT_ADDRESS);
        login_preferences_editor.putBoolean(login_IS_LOGIN, false);
        login_preferences_editor.clear();
        login_preferences_editor.commit();
    }

}