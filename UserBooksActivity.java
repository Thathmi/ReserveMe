package lk.jiat.reserveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.concurrent.Executor;

import io.github.muddz.styleabletoast.StyleableToast;
import lk.jiat.reserveme.model.Booking;

import android.Manifest;

public class UserBooksActivity extends AppCompatActivity {
    String city = UserHomeActivity.city;
    String checkin = UserHomeActivity.checkin;
    String checkout = UserHomeActivity.checkout;
    String adult = UserHomeActivity.adults;
    String rooms = UserHomeActivity.room;
    String user = EmailHolder.getUserEmail();
    ;
    String mobile = UserProfileFragment.uMobile;
    String email = MyAdaptor.hEmail;
    String imgPath;
    String randomID;
    public static final String TAG = MainActivity.class.getName();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListenerRegistration listenerRegistration;
    String price = HotelSingleViewActivity.total1;

    //       ------------------------NOTIFICATION -----------------------
    private NotificationManager notificationManager;
    private String chanelId = "info";
    //       ------------------------NOTIFICATION -----------------------
    private static final int PERMISSION_REQUEST_POST_NOTIFICATIONS = 123; // You can use any unique integer value
    //      ******  SMS *****
    private static final int PERMISSION_REQUEST_SMS = 1001;

    //      ******  SMS *****

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);

        // notification  permission
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(UserBooksActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_POST_NOTIFICATIONS);

        } else {
        }

//          ------------------------    NOTIFICATION -----------------------

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(chanelId, "INFO", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            channel.setDescription("this is Booking information ");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.setVibrationPattern(new long[]{0, 400, 200, 400});
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

//            ------------------------    NOTIFICATION -----------------------

//        load static data
        TextView name1 = findViewById(R.id.name);
        TextView cin1 = findViewById(R.id.textView78);
        TextView cout1 = findViewById(R.id.textView79);
        TextView adults1 = findViewById(R.id.textView80);
        TextView room1 = findViewById(R.id.textView81);
        TextView price11 = findViewById(R.id.textView83);

        name1.setText(email);
        cin1.setText(checkin);
        cout1.setText(checkout);
        adults1.setText(adult);
        room1.setText(rooms);
        price11.setText(price);

        // Generate a random ID using current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

// Convert the current time in milliseconds to a string
        randomID = Long.toString(currentTimeMillis);

//book button
        findViewById(R.id.button13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CoordinatorLayout cl = findViewById(R.id.coorl1);
                Snackbar sb = Snackbar.make(cl, "Confirm booking?", Snackbar.LENGTH_LONG);

                sb.setAction("YES", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//             * DATABASE *
                        if (listenerRegistration == null) {

                            Booking booking = new Booking(randomID, user, email, checkin, checkout, adult, rooms, price);
//                                         Hotel hotel = new Hotel(name, password,email,mobile,address,city);
                            db.collection("booking").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    StyleableToast.makeText(UserBooksActivity.this, "Booking success", Toast.LENGTH_LONG, R.style.success).show();
//          ------------------------    NOTIFICATION -----------------------

                                    notification();
//    ******  SMS permission *****

                                    // Check if the permission is already granted
                                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                        // Permission is not granted, request it
                                        ActivityCompat.requestPermissions(UserBooksActivity.this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SMS);
                                    } else {
                                        // Permission is already granted, proceed with sending SMS
                                        sendSMS(mobile, "You booking ");
                                    }
//    ******  SMS permission *****

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserBooksActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.i(TAG, e.toString());
                                }
                            });
                        }
//             * DATABASE *
                    }
                });
                sb.show();
            }
        });

//         go back
        findViewById(R.id.button14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(UserBooksActivity.this, HotelSingleViewActivity.class);
                startActivity(i2);
            }
        });
    }

    //    SMS
    private void sendSMS(String phoneNumber, String message) {
        try {
//            Toast.makeText(this, "mssg sent", Toast.LENGTH_SHORT).show();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }
    private void notification() {
        //                pending intent
        PendingIntent pendingIntent = PendingIntent
                .getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), BookingListActivity.class), PendingIntent.FLAG_IMMUTABLE);
//                pending intent

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), chanelId)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.iconnew)
                .setContentTitle("Booking confirmed!")
                .setContentText("Thank you for Booking. Click here to view you bookings.")
                .setColor(Color.BLUE)
                .setContentIntent(pendingIntent)
                .build();

        //            Show the notification
        notificationManager.notify(1, notification);

        Intent i = new Intent(UserBooksActivity.this, UserHomeActivity.class);
        startActivity(i);
//
//          ------------------------    NOTIFICATION -----------------------
    }
}