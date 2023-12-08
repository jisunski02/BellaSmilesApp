package com.bellasmiles.dentalclinicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bellasmiles.dentalclinicapp.adapter.DoctorAdapter;
import com.bellasmiles.dentalclinicapp.adapter.ServicesAdapter;
import com.bellasmiles.dentalclinicapp.api.ApiClientRx;
import com.bellasmiles.dentalclinicapp.api.ApiService;
import com.bellasmiles.dentalclinicapp.constant.Constants;
import com.bellasmiles.dentalclinicapp.databinding.ActivityHomeBinding;
import com.bellasmiles.dentalclinicapp.databinding.DialogProfileBinding;
import com.bellasmiles.dentalclinicapp.model.DoctorModel;
import com.bellasmiles.dentalclinicapp.model.ServiceModel;
import com.bellasmiles.dentalclinicapp.sharedpreferences.LoginSharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    List<DoctorModel> doctorList = new ArrayList<>();

    DoctorAdapter doctorAdapter;

    List<ServiceModel> serviceList = new ArrayList<>();

    ServicesAdapter servicesAdapter;

    LoginSharedPrefManager sharedPrefManager;

    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cvHome.setVisibility(View.GONE);

        sharedPrefManager = new LoginSharedPrefManager(this);
        apiService = ApiClientRx.getClient(Constants.BASE_URL).create(ApiService.class);

        getDoctors();
        getServices();

        binding.toolbar.ivBack.setVisibility(View.GONE);

        binding.toolbar.btnBooknow.setOnClickListener(v->{
            Constants.gotoActivity(this, MainActivity.class);
        });

        binding.toolbar.ivHistory.setOnClickListener(v->{
            Constants.gotoActivity(this, BookingHistoryActivity.class);
        });

        binding.toolbar.ivProfile.setOnClickListener(view -> {
            try {
                Dialog profileDialog = new Dialog(HomeActivity.this);
                profileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
                profileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                DialogProfileBinding binding1 = DialogProfileBinding.inflate(profileDialog.getLayoutInflater());
                profileDialog.setContentView(binding1.getRoot());

                binding1.tvFullname.setText(sharedPrefManager.getClientFirstName() + " " + sharedPrefManager.getClientMiddleName() + " " + sharedPrefManager.getClientLastName());
                binding1.tvAddress.setText(sharedPrefManager.getClientAddress());
                binding1.tvBirthdate.setText(sharedPrefManager.getClientBirthdate());
                binding1.tvEmailaddress.setText(sharedPrefManager.getClientEmailAddress());
                binding1.tvGender.setText(sharedPrefManager.getClientGender());
                binding1.tvContactnumberr.setText(sharedPrefManager.getClientContactNo());

                if(sharedPrefManager.getClientAddress().isEmpty()){
                    binding1.tvAddress.setText("N/A");
                }

                if(sharedPrefManager.getClientContactNo().isEmpty()){
                    binding1.tvContactnumberr.setText("N/A");
                }

                if(sharedPrefManager.getClientGender().isEmpty()){
                    binding1.tvGender.setText("N/A");
                }

                if(sharedPrefManager.getClientBirthdate().isEmpty()){
                     binding1.tvBirthdate.setText("N/A");
                }

                binding1.btnLogout.setOnClickListener(v -> {
                    sharedPrefManager.logoutClient();
                    Constants.gotoActivity(this, LoginActivity.class);
                    finish();
                    finishAffinity();
                });

                profileDialog.setCancelable(true);
                profileDialog.show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDoctors(){
        showDialog();
        doctorList.clear();
        disposable.add(
                apiService.getDoctors()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<DoctorModel>>() {
                            @Override
                            public void onSuccess(List<DoctorModel> response) {

                                for(int a = 0; a<response.size(); a++){
                                    DoctorModel doctorModel = response.get(a);

                                    DoctorModel doctorModel2 = new DoctorModel(
                                            doctorModel.getId(), doctorModel.getFirstName(), doctorModel.getMiddleName(),
                                            doctorModel.getLastName(), doctorModel.getGender(), doctorModel.getContactNo(),
                                            doctorModel.getEmailAddress(), doctorModel.getAddress());

                                    doctorList.add(doctorModel2);

                                }

                                binding.rvDoctors.setHasFixedSize(true);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this,LinearLayoutManager.HORIZONTAL, false);
                                binding.rvDoctors.setLayoutManager(linearLayoutManager);
                                doctorAdapter = new DoctorAdapter(HomeActivity.this, doctorList);
                                binding.rvDoctors.setAdapter(doctorAdapter);
                            }
                            @Override
                            public void onError(Throwable e) {

                                if(Constants.isNetworkConnected(HomeActivity.this)){
                                    Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Toast.makeText(HomeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));
    }

    private void getServices(){
        doctorList.clear();
        disposable.add(
                apiService.getServices()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<ServiceModel>>() {
                            @Override
                            public void onSuccess(List<ServiceModel> response) {

                                for(int a = 0; a<response.size(); a++){
                                    ServiceModel serviceModel = response.get(a);

                                    ServiceModel serviceModel1 = new ServiceModel(
                                            serviceModel.getServiceId(), serviceModel.getServiceDesc(),
                                            serviceModel.getServiceCost()
                                    );

                                    serviceList.add(serviceModel1);

                                }

                                binding.rvServices.setHasFixedSize(true);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
                                binding.rvServices.setLayoutManager(linearLayoutManager);
                                servicesAdapter = new ServicesAdapter(HomeActivity.this, serviceList);
                                binding.rvServices.setAdapter(servicesAdapter);

                                loadingDialog.dismiss();
                                binding.cvHome.setVisibility(View.VISIBLE);

                            }
                            @Override
                            public void onError(Throwable e) {
                                loadingDialog.dismiss();
                                if(Constants.isNetworkConnected(HomeActivity.this)){
                                    Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Toast.makeText(HomeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));
    }

    private void showDialog(){
        loadingDialog = new Dialog(HomeActivity.this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setContentView(R.layout.dialog_loading);


        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }
}