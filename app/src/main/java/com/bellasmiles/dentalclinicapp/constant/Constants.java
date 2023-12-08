package com.bellasmiles.dentalclinicapp.constant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.view.Window;
import android.widget.Button;

import com.bellasmiles.dentalclinicapp.LoginActivity;
import com.bellasmiles.dentalclinicapp.R;
import com.bellasmiles.dentalclinicapp.databinding.DialogLoginornotBinding;

public class Constants {

    public static String BASE_URL = "https://bella-smiles-dental.online/mobilescripts/";

    public static void gotoActivity(Context fromActivity, Class<?> toActivity){
        Intent intent = new Intent(fromActivity, toActivity);
        fromActivity.startActivity(intent);
    }

    public static void showDialog(Context context, Class<?> cls){

        Dialog showDialog = new Dialog(context);
        showDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
        showDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DialogLoginornotBinding binding = DialogLoginornotBinding.inflate(showDialog.getLayoutInflater());
        showDialog.setContentView(binding.getRoot());

        binding.btnConfirm.setOnClickListener(v->{
            gotoActivity(context, cls);
        });

        binding.btnCancel.setOnClickListener(v->{
            showDialog.dismiss();
        });

        showDialog.setCancelable(false);
        showDialog.show();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
