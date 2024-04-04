package lk.jiat.reserveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Executable;
import java.util.concurrent.Executor;

import io.github.muddz.styleabletoast.StyleableToast;

public class MainActivity extends AppCompatActivity {
    static String email = UserSignInFragment.uEmail;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, UserLoginActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MainActivity.this, HotelLoginActivity.class);
                startActivity(i2);
            }
        });
        redirectToAppropriateScreen();
    }


    // logic - SHARED PREFERENCES
    private void redirectToAppropriateScreen() {
        SharedPreferences sellerSharedPreferences = getSharedPreferences("SellerDetails", Context.MODE_PRIVATE);
        String savedSellerEmail = sellerSharedPreferences.getString("email", null);
        String savedSellerPassword = sellerSharedPreferences.getString("password", null);

        SharedPreferences customerSharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String savedCustomerEmail = customerSharedPreferences.getString("email", null);
        String savedCustomerPassword = customerSharedPreferences.getString("password", null);

        if (savedSellerEmail != null && savedSellerPassword != null) {
            // ---------------  Seller is logged in ------------------------
//            have shared preferences
            loginCheck();
            EmailHolder.setSellerEmail(savedSellerEmail);
            userType = "seller";

        } else if (savedCustomerEmail != null && savedCustomerPassword != null) {
            //  ----------------------    Customer is logged in ------------------
            loginCheck();
            EmailHolder.setUserEmail(savedCustomerEmail);
            userType = "user";

        } else {
            // Neither seller nor customer is logged in
//            redirectToHome();
        }
    }

    private void loginCheck() {
        SharedPreferences fingerprintSharedPreferences = MainActivity.this.getSharedPreferences("Authentication", Context.MODE_PRIVATE);
        String fingerprintStatus = fingerprintSharedPreferences.getString("aValue", null);

        Button button1 = findViewById(R.id.button); // Replace R.id.button1 with your button's ID
        Button button2 = findViewById(R.id.button2); // Replace R.id.button2 with your button's ID

        if ("enable".equals(fingerprintStatus)) {
            button1.setVisibility(View.GONE); // Hide button 1
            button2.setVisibility(View.GONE); // Hide button 2
            //                fingerprint
            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("User login verification")
                    .setDescription("Please verify your fingerprint or face to login")
                    .setNegativeButtonText("cancel")
                    .build();

            getPrompt().authenticate(promptInfo);
//                fingerprint

        } else {
            // Fingerprint is not enabled or the value is null - Go directly to home
            if ("seller".equals(userType)) {
                loginHotel();
            } else {
                loginUser();
            }
        }
    }

    private BiometricPrompt getPrompt() {
        Executor executor = ContextCompat.getMainExecutor(MainActivity.this);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                StyleableToast.makeText(MainActivity.this, errString.toString(), Toast.LENGTH_LONG, R.style.invalid).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                if ("seller".equals(userType)) {
                    loginHotel();
                } else {
                    loginUser();
                }

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                StyleableToast.makeText(MainActivity.this, "Fingerprint doesn't match!", Toast.LENGTH_LONG, R.style.invalid).show();
            }
        };
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);
        return biometricPrompt;
    }

    private void loginHotel() {
        Intent intent = new Intent(MainActivity.this, HotelHomeActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    private void loginUser() {
        Intent customerIntent = new Intent(MainActivity.this, UserHomeActivity.class);
        startActivity(customerIntent);
        MainActivity.this.finish();
    }
}