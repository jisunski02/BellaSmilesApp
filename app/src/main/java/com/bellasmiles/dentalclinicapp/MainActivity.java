package com.bellasmiles.dentalclinicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bellasmiles.dentalclinicapp.adapter.ScheduleAdapter;
import com.bellasmiles.dentalclinicapp.api.ApiClientRx;
import com.bellasmiles.dentalclinicapp.api.ApiService;
import com.bellasmiles.dentalclinicapp.constant.Constants;
import com.bellasmiles.dentalclinicapp.databinding.ActivityMainBinding;
import com.bellasmiles.dentalclinicapp.databinding.DialogProfileBinding;
import com.bellasmiles.dentalclinicapp.interfaces.OnItemClickListener;
import com.bellasmiles.dentalclinicapp.model.DoctorModel;
import com.bellasmiles.dentalclinicapp.model.ScheduleModel;
import com.bellasmiles.dentalclinicapp.model.ServiceModel;
import com.bellasmiles.dentalclinicapp.sharedpreferences.LoginSharedPrefManager;
import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivityMainBinding binding;
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    List<DoctorModel> doctorList = new ArrayList<>();
    List<ServiceModel> serviceList = new ArrayList<>();
    List<String> scheduleList = new ArrayList<>();

    ScheduleAdapter scheduleAdapter;
    List<ScheduleModel> timeList = new ArrayList<>();

    LoginSharedPrefManager sharedPrefManager;
    String selectedDoctor = "", selectedService = "", selectedTimeSchedule = "", serviceCost, doctorId, scheduleId, clientId, serviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefManager = new LoginSharedPrefManager(this);

        apiService = ApiClientRx.getClient(Constants.BASE_URL).create(ApiService.class);

        binding.toolbar.ivHistory.setOnClickListener(view -> {
           Constants.gotoActivity(this, BookingHistoryActivity.class);
        });

        binding.toolbar.ivProfile.setOnClickListener(view -> {

            Dialog profileDialog = new Dialog(MainActivity.this);
            profileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
            profileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            DialogProfileBinding binding1 = DialogProfileBinding.inflate(profileDialog.getLayoutInflater());
            profileDialog.setContentView(binding1.getRoot());

            binding1.tvFullname.setText(sharedPrefManager.getClientFirstName()+" "+sharedPrefManager.getClientMiddleName()+" "+sharedPrefManager.getClientLastName());
            binding1.tvAddress.setText(sharedPrefManager.getClientAddress());
            binding1.tvBirthdate.setText(sharedPrefManager.getClientBirthdate());
            binding1.tvEmailaddress.setText(sharedPrefManager.getClientEmailAddress());
            binding1.tvGender.setText(sharedPrefManager.getClientGender());
            binding1.tvContactnumberr.setText(sharedPrefManager.getClientContactNo());

            binding1.btnLogout.setOnClickListener(v->{
                sharedPrefManager.logoutClient();
                Constants.gotoActivity(this, LoginActivity.class);
                finish();
                finishAffinity();
                    });

            profileDialog.setCancelable(true);
            profileDialog.show();
        });

        binding.ivScheduledate.setOnClickListener(view -> {
            showDatePicker();
        });

        binding.btnConfirm.setOnClickListener(view -> {

            if(!isValidDoctor(binding.spinnerDoctor) | !isValidService(binding.spinnerService) | !isValidSchedule()){
                return;
            }

            if(isValidDoctor(binding.spinnerDoctor) && isValidService(binding.spinnerService) && isValidSchedule()){
                Intent intent = new Intent(this, TransactionSummaryActivity.class);
                intent.putExtra("selectedDoctor", selectedDoctor);
                intent.putExtra("selectedService", selectedService);
                intent.putExtra("selectedTimeSchedule", selectedTimeSchedule);
                intent.putExtra("serviceCost", serviceCost);
                intent.putExtra("doctorId", doctorId);
                intent.putExtra("scheduleId", scheduleId);
                intent.putExtra("clientId", sharedPrefManager.getClientId());
                intent.putExtra("serviceId", serviceId);
                startActivity(intent);
            }


        });

        binding.rvTimeschedule.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        binding.rvTimeschedule.setLayoutManager(linearLayoutManager);

        getDoctors();
        getServices();
        getSchedule();
    }

    private void getDoctors(){
        doctorList.clear();
        disposable.add(
                apiService.getDoctors()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<DoctorModel>>() {
                            @Override
                            public void onSuccess(List<DoctorModel> response) {

                                DoctorModel doctorModel1 = new DoctorModel(0,"-Please select a Doctor-", "","","",
                                        "","","");
                                doctorList.add(doctorModel1);

                                for(int a = 0; a<response.size(); a++){
                                    DoctorModel doctorModel = response.get(a);


                                    String doctorName = doctorModel.getFirstName()+" " +doctorModel.getMiddleName()+" "+doctorModel.getLastName();

                                    DoctorModel doctorModel2 = new DoctorModel(
                                            doctorModel.getId(), doctorModel.getFirstName(), doctorModel.getMiddleName(),
                                            doctorModel.getLastName(), doctorModel.getGender(), doctorModel.getContactNo(),
                                            doctorModel.getEmailAddress(), doctorModel.getAddress());


                                    doctorList.add(doctorModel2);

                                }

                                ArrayAdapter<DoctorModel> adapter = new ArrayAdapter<>(
                                        MainActivity.this, android.R.layout.simple_spinner_item, doctorList);

                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.spinnerDoctor.setAdapter(adapter);

                                binding.spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        if (position == 0) {
                                            // Do Nothing
                                            selectedDoctor = "";
                                        } else {
                                            DoctorModel doctorModel1 = doctorList.get(position);
                                            doctorId = String.valueOf(doctorModel1.getId());
                                            selectedDoctor = doctorModel1.getFirstName()+" "+doctorModel1.getMiddleName()+" "+doctorModel1.getLastName();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                            @Override
                            public void onError(Throwable e) {

                                Log.e("Failed", "Failed to fetch countries");
                            }
                        }));
    }

    private void getServices(){
        serviceList.clear();
        disposable.add(
                apiService.getServices()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<ServiceModel>>() {
                            @Override
                            public void onSuccess(List<ServiceModel> response) {

                                ServiceModel serviceModel1 = new ServiceModel(0, "-Please select a Service","");
                                serviceList.add(serviceModel1);

                                for(int a = 0; a<response.size(); a++){
                                    ServiceModel serviceModel = response.get(a);

                                    String serviceID = String.valueOf(serviceModel.getServiceId());
                                    String serviceDesc = serviceModel.getServiceDesc();
                                    String sCost= serviceModel.getServiceCost();

                                    ServiceModel serviceModel2 = new ServiceModel(Integer.parseInt(serviceID), serviceDesc, sCost);

                                    serviceList.add(serviceModel2);

                                    ArrayAdapter<ServiceModel> adapter = new ArrayAdapter<>(
                                            MainActivity.this, android.R.layout.simple_spinner_item, serviceList);

                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    binding.spinnerService.setAdapter(adapter);

                                    binding.spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            if (position == 0) {
                                                // Do Nothing
                                                selectedService = "";

                                            } else {
                                                ServiceModel serviceModel3 = serviceList.get(position);
                                                serviceId = String.valueOf(serviceModel3.getServiceId());
                                                selectedService = serviceModel3.getServiceDesc();
                                                serviceCost = serviceModel3.getServiceCost();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                }

                            }
                            @Override
                            public void onError(Throwable e) {

                                Log.e("Failed", "Failed to fetch services");
                            }
                        }));
    }


    private void getSchedule(){
        scheduleList.clear();
        disposable.add(
                apiService.getSchedules()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<ScheduleModel>>() {
                            @Override
                            public void onSuccess(List<ScheduleModel> response) {

                                for(int a = 0; a<response.size(); a++){
                                    ScheduleModel scheduleModel = response.get(a);


                                    String date = scheduleModel.getDate();

                                    boolean isExist = isExistSchedule(date);
                                    if(!isExist){
                                        scheduleList.add(date);
                                    }


                                }

                            }
                            @Override
                            public void onError(Throwable e) {

                                Log.e("Failed", "Failed to fetch services");
                            }
                        }));
    }

    private void getScheduleTimeByDate(String date){
        selectedTimeSchedule = "";
        disposable.add(
                apiService.getScheduleTimeByDate(date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<ScheduleModel>>() {
                            @Override
                            public void onSuccess(List<ScheduleModel> response) {

                                    for (int a = 0; a < response.size(); a++) {
                                        ScheduleModel scheduleModel = response.get(a);

                                        String id = String.valueOf(scheduleModel.getId());
                                        String date = scheduleModel.getDate();
                                        String startTime = scheduleModel.getStartTime();
                                        String endTime = scheduleModel.getEndTime();

                                        ScheduleModel scheduleModel1 = new ScheduleModel(Integer.parseInt(id), date, startTime, endTime);
                                        timeList.add(scheduleModel1);

                                    }

                                    scheduleAdapter = new ScheduleAdapter(MainActivity.this, timeList);
                                    binding.rvTimeschedule.setAdapter(scheduleAdapter);

                                    if(scheduleAdapter.getItemCount() == 0){
                                        Toast.makeText(MainActivity.this, "No appointment schedule for this date", Toast.LENGTH_SHORT).show();
                                    }

                                    scheduleAdapter.setOnItemClickListener(new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            ScheduleModel scheduleModel = timeList.get(position);

                                            scheduleId = String.valueOf(scheduleModel.getId());
                                            selectedTimeSchedule = scheduleModel.getDate()+"  " +scheduleModel.getStartTime()+" - "+scheduleModel.getEndTime();

                                        }
                                    });
                            }
                            @Override
                            public void onError(Throwable e) {

                                if(Constants.isNetworkConnected(MainActivity.this)){
                                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));
    }

    private boolean isExistSchedule(String strSched) {

        for (int i = 0; i < scheduleList.size(); i++) {
            String region = scheduleList.get(i);
            if (region.equals(strSched)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        //Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
        //Api Call Here
        timeList.clear();
        binding.tvDate.setText(date);
        getScheduleTimeByDate(date);
    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                mYear,
                mMonth,
                mDay
        );

        int month = mMonth+1;
        String currentDate = mYear +"-"+ month +"-"+ mDay;
       // Toast.makeText(this, currentDate, Toast.LENGTH_SHORT).show();
        scheduleList.add(currentDate);

        dpd.show(MainActivity.this.getFragmentManager(), "DatePickerDialog");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;

        for (int i = 0;i <scheduleList.size(); i++) {

            try {
                date = sdf.parse(scheduleList.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar = dateToCalendar(date);
            System.out.println(calendar.getTime());

            List<Calendar> dates = new ArrayList<>();
            dates.add(calendar);
            Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
            dpd.setSelectableDays(disabledDays1);
        }

    }

    private boolean isValidDoctor(Spinner sDoctor){
        if (selectedDoctor.isEmpty()){
            ((TextView)sDoctor.getSelectedView()).setError("Doctor is required");
            return false;
        }

        else{
            ((TextView)sDoctor.getSelectedView()).setError(null);
            return  true;
        }
    }
    private boolean isValidService(Spinner sService){
        if (selectedService.isEmpty()){
            ((TextView)sService.getSelectedView()).setError("Service is required");
            return false;
        }

        else{
            ((TextView)sService.getSelectedView()).setError(null);
            return  true;
        }
    }

    private boolean isValidSchedule(){
        if (selectedTimeSchedule.isEmpty()){
            Toast.makeText(this, "Select a time of schedule", Toast.LENGTH_SHORT).show();
            return false;
        }

        else{

            return  true;
        }
    }
    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


}