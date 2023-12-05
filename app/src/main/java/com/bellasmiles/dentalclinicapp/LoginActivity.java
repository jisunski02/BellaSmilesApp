package com.bellasmiles.dentalclinicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bellasmiles.dentalclinicapp.api.ApiClientRx;
import com.bellasmiles.dentalclinicapp.api.ApiService;
import com.bellasmiles.dentalclinicapp.constant.Constants;
import com.bellasmiles.dentalclinicapp.databinding.ActivityLoginBinding;
import com.bellasmiles.dentalclinicapp.databinding.DialogSignupBinding;
import com.bellasmiles.dentalclinicapp.model.ClientProfileModel;
import com.bellasmiles.dentalclinicapp.model.DoctorModel;
import com.bellasmiles.dentalclinicapp.model.LoginRegisterModel;
import com.bellasmiles.dentalclinicapp.model.ScheduleModel;
import com.bellasmiles.dentalclinicapp.sharedpreferences.LoginSharedPrefManager;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();

    String gender;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    LoginSharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefManager = new LoginSharedPrefManager(this);

        if(sharedPrefManager.isLoggedIn()){
            Constants.gotoActivity(this, MainActivity.class);
            finish();
        }

        apiService = ApiClientRx.getClient(Constants.BASE_URL).create(ApiService.class);

        binding.btnLogin.setOnClickListener(login->{
            if(!isValidUsername(binding.username) | !isValidPassword(binding.password)){
                return;
            }
            if(isValidUsername(binding.username) && isValidPassword(binding.password)){
                Dialog loadingDialog = new Dialog(LoginActivity.this);
                loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
                loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loadingDialog.setContentView(R.layout.dialog_loading);


                loadingDialog.setCancelable(false);
                loadingDialog.show();

                loginAccount(loadingDialog, binding.username.getText().toString(), binding.password.getText().toString());
            }

        });

        binding.tvSignup.setOnClickListener(view -> {

            Dialog registerDialog = new Dialog(this);
            registerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
            registerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            DialogSignupBinding binding1 = DialogSignupBinding.inflate(getLayoutInflater());
            registerDialog.setContentView(binding1.getRoot());

            List<String> genderList = new ArrayList<>();
            genderList.add("-Gender-");
            genderList.add("Male");
            genderList.add("Female");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    LoginActivity.this, android.R.layout.simple_spinner_item, genderList);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding1.spinnerGender.setAdapter(adapter);

            binding1.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0) {
                        // Do Nothing
                        gender = "";
                    } else {
                        gender  = adapter.getItem(position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            binding1.etDateofbirth.setOnClickListener(v3 -> {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateDialog = new DatePickerDialog(v3.getContext(), datePickerListener, mYear, mMonth, mDay);
                dateDialog.getDatePicker().setMaxDate(new Date().getTime());
                dateDialog.show();
            });

            datePickerListener = (datePicker, year, month, day) -> {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);
                @SuppressLint("SimpleDateFormat") String format = new SimpleDateFormat("yyyy/MM/dd").format(c.getTime());
                binding1.etDateofbirth.setError(null);
                binding1.etDateofbirth.setText(format);

            };


            binding1.btnSignup.setOnClickListener(v->{
                try {
                    if (!isValidFirstName(binding1.etFirstname) | !isValidLastName(binding1.etLastname) | !isValidGender(binding1.spinnerGender)
                            | !isValidContactNo(binding1.etContactno) | !isValidEmail(binding1.etEmailaddress)
                            | !isValidAddress(binding1.etAddrress) | !isValidBirthdate(binding1.etDateofbirth)) {
                        return;
                    }
                    if (isValidFirstName(binding1.etFirstname) && isValidLastName(binding1.etLastname) && isValidGender(binding1.spinnerGender)
                            && isValidContactNo(binding1.etContactno) && isValidEmail(binding1.etEmailaddress)
                            && isValidAddress(binding1.etAddrress) && isValidBirthdate(binding1.etDateofbirth)) {

                        Dialog loadingDialog = new Dialog(LoginActivity.this);
                        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
                        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        loadingDialog.setContentView(R.layout.dialog_loading);


                        loadingDialog.setCancelable(false);
                        loadingDialog.show();

                        registerAccount(loadingDialog, registerDialog, binding1.etFirstname.getText().toString(),
                                binding1.etMiddlename.getText().toString(), binding1.etLastname.getText().toString(),
                                gender, binding1.etContactno.getText().toString(), binding1.etEmailaddress.getText().toString(),
                                binding1.etAddrress.getText().toString(), binding1.etDateofbirth.getText().toString());
                    }
                }
                catch (Exception e){
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            });

            binding1.tvLogin.setOnClickListener(v2 -> registerDialog.dismiss());

            registerDialog.setCancelable(true);
            registerDialog.show();
        });
    }

    private void loginAccount(Dialog dialog, String username, String password) {

        disposable.add(
                apiService.loginAccount(username, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<LoginRegisterModel>() {
                            @Override
                            public void onSuccess(LoginRegisterModel response) {
                                String success = response.getSuccess();
                                String username = response.getUsername();

                                if(success.equals("1")){
                                    getClientProfile(dialog, username);
                                }
                                else if(success.equals("0")){
                                    Toast.makeText(LoginActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                                }

                                else{
                                    Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                                }

                            }
                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                if(Constants.isNetworkConnected(LoginActivity.this)){
                                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));
    }

    private void getClientProfile(Dialog dialog, String emailAddress){
        disposable.add(
                apiService.getClientProfile(emailAddress)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<ClientProfileModel>>() {
                            @Override
                            public void onSuccess(List<ClientProfileModel> response) {
                                dialog.dismiss();
                                for(int a = 0; a<response.size(); a++){
                                    ClientProfileModel clientProfileModel = response.get(a);
                                    sharedPrefManager.createClientLoginSession();
                                    sharedPrefManager.setClientId(clientProfileModel.getClientId());
                                    sharedPrefManager.setClientFirstName(clientProfileModel.getClientFirstName());
                                    sharedPrefManager.setClientMiddleName(clientProfileModel.getClientMiddleName());
                                    sharedPrefManager.setClientLastName(clientProfileModel.getClientLastName());
                                    sharedPrefManager.setClientGender(clientProfileModel.getClientGender());
                                    sharedPrefManager.setClientContactNo(clientProfileModel.getClientContactNo());
                                    sharedPrefManager.setClientEmailAddress(clientProfileModel.getClientEmailAddress());
                                    sharedPrefManager.setClientAddress(clientProfileModel.getClientAddress());
                                    sharedPrefManager.setClientBirthdate(clientProfileModel.getClientBirthdate());

                                    Toast.makeText(LoginActivity.this, "Logged in successful", Toast.LENGTH_SHORT).show();
                                    Constants.gotoActivity(LoginActivity.this, MainActivity.class);
                                    finish();
                                }

                            }
                            @Override
                            public void onError(Throwable e) {

                                Log.e("Failed", "Failed to fetch services");
                            }
                        }));
    }
    private void registerAccount(Dialog dialog, Dialog dialog2, String firstname,
                                 String middlename, String lastname,
                                 String gender, String contactno,
                                 String emailaddress, String address, String birthdate){
        disposable.add(
                apiService.registerAccount(firstname, middlename,
                                lastname, gender, contactno, emailaddress,
                                address, birthdate)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<LoginRegisterModel>() {
                            @Override
                            public void onSuccess(LoginRegisterModel response) {
                                dialog.dismiss();
                                String success = response.getSuccess();

                                if(success.equals("1")){
                                    dialog2.dismiss();
                                    Toast.makeText(LoginActivity.this, "Registered Successful", Toast.LENGTH_SHORT).show();
                                }
                                else if(success.equals("0")){
                                    Toast.makeText(LoginActivity.this, "Email already taken", Toast.LENGTH_SHORT).show();
                                }

                                else{
                                    Toast.makeText(LoginActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                                }

                            }
                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                
                                if(Constants.isNetworkConnected(LoginActivity.this)){
                                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));
    }

    private boolean isValidFirstName(EditText etFirstName){
        String firstname = etFirstName.getText().toString().trim();
        if (firstname.isEmpty()){
            etFirstName.setError("First name required");
            return false;
        }
        else if(firstname.length()<2){
            etFirstName.setError("First name too short");
            return false;
        }
        else{
            etFirstName.setError(null);
            return  true;
        }
    }

    private boolean isValidLastName(EditText etLastName){
        String lastname = etLastName.getText().toString().trim();
        if (lastname.isEmpty()){
            etLastName.setError("Last name required");
            return false;
        }
        else if(lastname.length()<2){
            etLastName.setError("Last name too short");
            return false;
        }
        else{
            etLastName.setError(null);
            return  true;
        }
    }

    private boolean isValidGender(Spinner sGender){
        if (gender.isEmpty()){
            ((TextView)sGender.getSelectedView()).setError("Gender is required");
            return false;
        }

        else{
            ((TextView)sGender.getSelectedView()).setError(null);
            return  true;
        }
    }

    private boolean isValidContactNo(EditText etContactNo){
        String contactNo = etContactNo.getText().toString().trim();
        if (contactNo.isEmpty()){
            etContactNo.setError("Mobile number required");
            return false;
        }

        else if(contactNo.length()<10){
            etContactNo.setError("Mobile number too short");
            return false;
        }

        else{
            etContactNo.setError(null);
            return  true;
        }
    }

    private boolean isValidEmail(EditText etEmail){
        String email = etEmail.getText().toString().trim();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.[a-z]+";
        final String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()){
            etEmail.setError("Email required");
            return false;
        }

        else if (!email.matches(emailPattern) && !email.matches(emailPattern2)) {
            etEmail.setError("Invalid format");
            return false;
        }
        else{
            etEmail.setError(null);
            return true;
        }
    }

    private boolean isValidAddress(EditText etAddress){
        String address = etAddress.getText().toString().trim();
        if (address.isEmpty()){
            etAddress.setError("Address required");
            return false;
        }
        else if(address.length()<5){
            etAddress.setError("Address is too short");
            return false;
        }
        else{
            etAddress.setError(null);
            return  true;
        }
    }

    private boolean isValidBirthdate(EditText etBirthdate){
        String address = etBirthdate.getText().toString().trim();
        if (address.isEmpty()){
            etBirthdate.setError("Birthdate required");
            return false;
        }

        else{
            etBirthdate.setError(null);
            return  true;
        }
    }

    private boolean isValidUsername(EditText etUsername){
        String username = etUsername.getText().toString().trim();
        if(username.isEmpty()){
            etUsername.setError("Username is required");
            return false;
        }

        else{
            etUsername.setError(null);
            return true;
        }
    }

    private boolean isValidPassword(EditText etPassword){
        String password = etPassword.getText().toString().trim();
        if(password.isEmpty()){
            etPassword.setError("Password is required");
            return false;
        }

        else{
            etPassword.setError(null);
            return true;
        }
    }
}