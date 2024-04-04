package lk.jiat.reserveme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HotelSingleViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    //map
    private GoogleMap map;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public static final String TAG = MainActivity.class.getName();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email = MyAdaptor.hEmail;
    String hemail = MyAdaptor.hemail;
    String priceValue = MyAdaptor.hPrice;
    ListenerRegistration listenerRegistration;
    String city = UserHomeActivity.city;
    String checkin = UserHomeActivity.checkin;
    String checkout = UserHomeActivity.checkout;
    String adults = UserHomeActivity.adults;
    String room = UserHomeActivity.room;
    long oneprice;
    String onepriceS;
    long total;
    static String total1;
    long number;
    private Context context;
    long days = UserHomeActivity.days;
    String daysNo = String.valueOf(days);
    private static final int REQUEST_CALL_PHONE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_single_view);

        context = this;

// Load image
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        ImageView imageview = findViewById(R.id.imageView32);
        StorageReference imageref = storageReference.child("hotel_images/" + hemail + "_image.jpg");
        imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageview);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to fetch image: " + e.getMessage());
            }
        });

        //        image slider
//        ImageSlider imageSlider = findViewById(R.id.image1);
//        ArrayList<SlideModel> slideModels = new ArrayList<>();
//
//        slideModels.add(new SlideModel(R.drawable.paolo_nicolello_2goxkj594nm_unsplash, ScaleTypes.FIT));
//        slideModels.add(new SlideModel(R.drawable.anmol_seth_hdbcjhndf48_unsplash, ScaleTypes.FIT));
//        slideModels.add(new SlideModel(R.drawable.sara_dubler_koei_7yytio_unsplash, ScaleTypes.FIT));
//
//        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
//        image slider

//Map api initialization
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //fusedlocation provider (Current location provider)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

//        data

        TextView name = findViewById(R.id.textView17);
        name.setText(email);
        TextView email1 = findViewById(R.id.textView43);
        TextView address = findViewById(R.id.textView23);
        TextView rate = findViewById(R.id.textView56);
        TextView price = findViewById(R.id.textView29);
//        EditText description = findViewById(R.id.editTextText4);
//        EditText password = findViewById(R.id.editTextText6);
//        EditText mobile = findViewById(R.id.editTextPhone2);
        TextView mobile = findViewById(R.id.textView41);

        if (listenerRegistration == null) {
            listenerRegistration = db.collection("hotel").whereEqualTo("name", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (!value.isEmpty()) {
                        List<DocumentSnapshot> doc = value.getDocuments();
                        doc.forEach(d -> {

                            address.setText(d.getString("address"));
                            rate.setText(d.getString("rate"));
                            price.setText(d.getString("price"));
                            email1.setText(d.getString("email"));

                            String mobileNumber = d.getString("mobile");
                            if (mobileNumber != null && !mobileNumber.isEmpty()) {
                                mobile.setText(mobileNumber);
                            } else {
                                mobile.setText("Phone number not available");
                            }

//                            ----------------------------map

                            //Map api initialization
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(HotelSingleViewActivity.this);

                            //fusedlocation provider (Current location provider)
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HotelSingleViewActivity.this);
                            GeoPoint geoPoint = d.getGeoPoint("Map");
                            if (geoPoint != null) {
                                double latitude = geoPoint.getLatitude();
                                double longitude = geoPoint.getLongitude();

                                // Create LatLng object using retrieved latitude and longitude
                                LatLng pharmacyLocation = new LatLng(latitude, longitude);
                                String pharmacyName = d.getString("name");

                                // Add marker to the map
                                map.addMarker(new MarkerOptions().position(pharmacyLocation).title(pharmacyName));

                                // Move camera to pharmacy location with appropriate zoom level
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(pharmacyLocation, 15)); // Adjust zoom level as needed
                            }

//                            --------------map
                        });
                    }
                }
            });
        }

//        other data coming from home selection

        TextView nightsNo = findViewById(R.id.daysNo);
        TextView adultsNo = findViewById(R.id.adultsNo);
        TextView roomsNo = findViewById(R.id.romsNo);
        TextView adultsNo1 = findViewById(R.id.guestsNo);
        TextView cin = findViewById(R.id.textView33);
        TextView cout = findViewById(R.id.textView34);
        TextView totalPrice = findViewById(R.id.price);

        calculatePrice();

        nightsNo.setText(daysNo);
        adultsNo1.setText(adults);
        adultsNo.setText(adults);
        roomsNo.setText(room);
        cin.setText(checkin);
        cout.setText(checkout);
        totalPrice.setText(total1);

//        call
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = mobile.getText().toString();
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));

                if (context != null) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                    } else {
                        // Permission is already granted, start the dialer intent
                        try {
                            context.startActivity(dialIntent);
                        } catch (ActivityNotFoundException e) {
                            Log.d("dial intent", "No activity to handle");
                        }
                    }
                } else {
                    Log.e("Context Error", "Context is null");
                }

            }
        });

//        book

        ExtendedFloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(HotelSingleViewActivity.this, UserBooksActivity.class);
                startActivity(i2);
            }
        });
    }

    public void calculatePrice() {
        long r = Long.parseLong(room);
        try {
            // Parse the priceValue string to a long
            number = Long.parseLong(priceValue);

            total = number * days * r;
            total1 = String.valueOf(total);

        } catch (NumberFormatException e) {
            e.printStackTrace();

            Log.e(TAG, "Error message", e);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

//        //map style and phase
//        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //zoom in zoom out
        map.getUiSettings().setZoomControlsEnabled(true);

        //to get current location of ours check permission and if not request


        //check if permission granted
        if (checkPermissions()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);
        } else {
            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }

    //get current location
    private boolean checkPermissions() {
        boolean permission = false;

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            permission = true;
        }

        return permission;
    }

    //check if the requested permissions are true or false
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Snackbar.make(findViewById(R.id.Maplayout), "Location permission denied", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }).show();
            }
        }
    }
}