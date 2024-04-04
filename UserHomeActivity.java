package lk.jiat.reserveme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.github.muddz.styleabletoast.StyleableToast;
import nl.joery.animatedbottombar.AnimatedBottomBar;

//public class UserHomeActivity extends AppCompatActivity  {
public class UserHomeActivity extends AppCompatActivity implements DatePickerFragment.DatePickerListener, NumberPicker.OnValueChangeListener, roomDialog.roomDialogListner, AdultDialog.AdultDialogListner {

    AnimatedBottomBar bottomBar;
    private TextView dateView1;
    private TextView dateView2;
    private int datevalue;
    static TextView rooms;
    static TextView adult;
    static TextView search;
    static String city;
    static String checkin;
    static String checkout;
    static String adults;
    static String room;
    static long days;
    private boolean loggedIn = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        loggedIn = true;
        bottomBar = findViewById(R.id.bottom_bar1);

        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {

                if (tab1.getId() == R.id.uhome) {
//                    replace(new UserMainHomeFragment());
                } else if (tab1.getId() == R.id.ubooking) {
                    Intent i2 = new Intent(UserHomeActivity.this, UserMainHomeActivity.class);
                    startActivity(i2);
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });

//        image slider
        ImageSlider imageSlider = findViewById(R.id.image1);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.paolo_nicolello_2goxkj594nm_unsplash, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.anmol_seth_hdbcjhndf48_unsplash, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.sara_dubler_koei_7yytio_unsplash, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

//        image slider

        dateView1 = findViewById(R.id.textView9);
        dateView2 = findViewById(R.id.textView14);
        rooms = findViewById(R.id.textView10);
        adult = findViewById(R.id.textView15);
        search = findViewById(R.id.autoCompleteTextView);

//        date picker
        findViewById(R.id.textView9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.setCancelable(false);
                dialogFragment.show(getSupportFragmentManager(), "Date picker");
                datevalue = 1;
            }
        });

//        checout date

        findViewById(R.id.textView14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment1 = new DatePickerFragment();
                dialogFragment1.setCancelable(false);
                dialogFragment1.show(getSupportFragmentManager(), "Date picker");
                datevalue = 2;
            }
        });


//        rooms
        findViewById(R.id.textView10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        findViewById(R.id.textView15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAdult();
            }
        });

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (adult.getText().toString().equals("")) {
                    StyleableToast.makeText(UserHomeActivity.this, "Enter number of guests", Toast.LENGTH_LONG, R.style.invalid).show();
                }else if (adult.getText().toString().equals("0")) {
                    StyleableToast.makeText(UserHomeActivity.this, "Enter valid number of guests", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (rooms.getText().toString().equals("")) {
                    StyleableToast.makeText(UserHomeActivity.this, "nter Number of rooms", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (rooms.getText().toString().equals("0")) {
                    StyleableToast.makeText(UserHomeActivity.this, "Enter valid number of rooms", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (dateView1.getText().toString().equals("")) {
                    StyleableToast.makeText(UserHomeActivity.this, "Enter checking date", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (dateView2.getText().toString().equals("")) {
                    StyleableToast.makeText(UserHomeActivity.this, "Enter checkout date", Toast.LENGTH_LONG, R.style.invalid).show();
                }else if (search.getText().toString().equals("")) {
                    StyleableToast.makeText(UserHomeActivity.this, "Enter City", Toast.LENGTH_LONG, R.style.invalid).show();
                } else {

                    AutoCompleteTextView city1 = findViewById(R.id.autoCompleteTextView);
                    city = city1.getText().toString();
                    checkin = dateView1.getText().toString();
                    checkout = dateView2.getText().toString();
                    adults = adult.getText().toString();
                    room = rooms.getText().toString();
                    calculateDays();
                }

            }
        });

        String cities[] = new String[]{"Colombo", "Mount Lavinia", "Kesbewa", "Moratuwa", "Maharagama", "Ratnapura", "Kandy", "Negombo", "Sri Jayewardenepura Kotte", "Kalmunai", "Trincomalee", "Galle", "Jaffna", "Athurugiriya", "Weligama", "Matara", "Kolonnawa", "Gampaha", "Badulla", "Kalutara", "Bentota", "Matale",
                "Mannar",
                "Pothuhera",
                "Kurunegala",
                "Mabole",
                "Hatton",
                "Hambantota",
                "Oruwala"};

        AutoCompleteTextView tv = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> a = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cities);
        tv.setAdapter(a);

    }

    @Override
    public void onBackPressed() {
        if (loggedIn) {

        } else {
            // Allow normal back button behavior for other cases
            super.onBackPressed();
        }
    }
    public void openDialog() {

        roomDialog rd = new roomDialog();
        rd.show(getSupportFragmentManager(), "room dialog");
    }

    public void openDialogAdult() {

        AdultDialog ad = new AdultDialog();
        ad.show(getSupportFragmentManager(), "Adult dialog");
    }

    @Override

    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);

        // Use the desired date format for display
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String date = sdf.format(cal.getTime());

        if (datevalue == 1) {
            dateView1.setText(date);
        } else {
            dateView2.setText(date);
        }
    }

    @Override
    public void applyText(int value1) {
// display rooms
        rooms.setText(String.valueOf(value1));
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    @Override
    public void applyTextadult(int adults) {
        adult.setText(String.valueOf(adults));
    }

    //    days
    public void calculateDays() {
        String checkInDateStr = dateView1.getText().toString();
        String checkOutDateStr = dateView2.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        try {
            Date checkInDate = sdf.parse(checkInDateStr);
            Date checkOutDate = sdf.parse(checkOutDateStr);

            if (checkInDate != null && checkOutDate != null) {
                if (checkOutDate.after(checkInDate)) {
                    long differenceInMillis = checkOutDate.getTime() - checkInDate.getTime();
                    long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMillis);

                    // Use differenceInDays as needed (e.g., display, store, etc.)
                    Intent i2 = new Intent(UserHomeActivity.this, HotelListActivity.class);
                    startActivity(i2);
                    days = differenceInDays;
                } else {
                    StyleableToast.makeText(UserHomeActivity.this, "Check-out date should be after Check-in date", Toast.LENGTH_LONG, R.style.invalid).show();
                }
            } else {
                Toast.makeText(UserHomeActivity.this, "Invalid date format", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(UserHomeActivity.this, "Error parsing dates", Toast.LENGTH_SHORT).show();
        }
    }
}