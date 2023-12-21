package com.bellasmiles.dentalclinicapp;

import android.app.Application;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PayPalCheckout.setConfig(new CheckoutConfig(
                this,
                "AWLEXhYJMVroS877icavgN2Hks9Ex8SPP4um1uubrpxQpwY-zkJHtEMrWRu6C452gje7Q4RLXsIbL6Uo",
                Environment.SANDBOX,
                CurrencyCode.PHP,
                UserAction.PAY_NOW,
                "com.bellasmiles.dentalclinicapp://paypalpay"
        ));
    }
}
