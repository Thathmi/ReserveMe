package lk.jiat.reserveme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;

import androidx.biometric.BiometricManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;

public class HotelMainHomeFragment extends Fragment {
    private EditText Latitude;

    private EditText Longitude;
    static String hName;
    ListenerRegistration listenerRegistration;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email = EmailHolder.getSellerEmail();
    Switch s;

    public HotelMainHomeFragment() {
        // Required empty public constructor
    }

    public static HotelMainHomeFragment newInstance(String param1, String param2) {
        HotelMainHomeFragment fragment = new HotelMainHomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    private boolean isValidPassword(String password2) {

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";

        return password2.matches(passwordPattern);
    }

    private boolean isValidMobile(String mobile2) {

        String mobilePattern = "^07\\d{8}$";

        return mobile2.matches(mobilePattern);
    }
    private boolean isValidPrice(String price2) {

        String mobilePattern ="^\\d+$";

        return price2.matches(mobilePattern);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hotel_main_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        s = view.findViewById(R.id.switch1);

        TextView name = view.findViewById(R.id.textView37);
        TextView address = view.findViewById(R.id.textView16);
        TextView city = view.findViewById(R.id.textView36);
        EditText price = view.findViewById(R.id.editTextText2);
        EditText description = view.findViewById(R.id.editTextText4);
        EditText password = view.findViewById(R.id.editTextText6);
        EditText mobile = view.findViewById(R.id.editTextPhone2);
        TextView email2 = view.findViewById(R.id.textView55);
        Latitude = view.findViewById(R.id.LatitudeTxtE);
        Longitude = view.findViewById(R.id.LongitudeTe);

//------------- LOAD DATA ------------------

        db.collection("hotel")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot d : task.getResult()) {

                                name.setText(d.getString("name"));
                                hName = d.getString("name");
                                address.setText(d.getString("address"));
                                city.setText(d.getString("city"));
                                price.setText(d.getString("price"));
                                description.setText(d.getString("description"));
                                mobile.setText(d.getString("mobile"));
                                email2.setText(d.getString("email"));
                                password.setText(d.getString("password"));

                                GeoPoint geoPoint = d.getGeoPoint("Map");

                                // Extract latitude and longitude from the GeoPoint
                                double latitude = 0.0;
                                double longitude = 0.0;
                                if (geoPoint != null) {
                                    latitude = geoPoint.getLatitude();
                                    longitude = geoPoint.getLongitude();
                                }
                                Latitude.setText(String.valueOf(latitude));
                                Longitude.setText(String.valueOf(longitude));

                            }
                        }
                    }
                });

// ------------------   UPDATE  ---------------------
        view.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String price1 = price.getText().toString();
                String address1 = address.getText().toString();
                String mobile1 = mobile.getText().toString();
                String description1 = description.getText().toString();
                String password1 = password.getText().toString();

                String latitudeString = Latitude.getText().toString();
                String longitudeString = Longitude.getText().toString();

                double latitude = 0.0;
                double longitude = 0.0;
                try {
                    latitude = Double.parseDouble(latitudeString);
                    longitude = Double.parseDouble(longitudeString);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                GeoPoint geoPoint = new GeoPoint(latitude, longitude);
                if (!isValidPassword(password1)) {
                    StyleableToast.makeText(getActivity(), "Password must be one upper case one lower case and letters numbers and symbols", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (!isValidMobile(mobile1)) {
                    StyleableToast.makeText(getActivity(), "Please enter valid mobile", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (!isValidPrice(price1)) {
                    StyleableToast.makeText(getActivity(), "Please enter valid price", Toast.LENGTH_LONG, R.style.invalid).show();
                }else {
// Databasel
                    db.collection("hotel")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                            documentSnapshot.getReference().update("price", price1,
                                                            "address", address1,
                                                            "mobile", mobile1,
                                                            "description", description1,
                                                            "password", password1,
                                                            "Map", geoPoint)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            StyleableToast.makeText(getActivity(), "Updated successfuly", Toast.LENGTH_LONG, R.style.success).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            StyleableToast.makeText(getActivity(), "Failed to update", Toast.LENGTH_LONG, R.style.invalid).show();
                                                        }
                                                    });
                                        }
                                    } else {
                                        StyleableToast.makeText(getActivity(), "No matching user found", Toast.LENGTH_LONG, R.style.invalid).show();
                                    }
                                }
                            });
                }

            }
        });

// -----------------    LOGOUT   --------------------
        view.findViewById(R.id.button19).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("SellerDetails", Context.MODE_PRIVATE);
                // Get the SharedPreferences editor to remove stored values
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Remove the stored email and password values
                editor.remove("email");
                editor.remove("password");

                // Apply changes to remove the values
                editor.apply();

                SharedPreferences sharedPreferences1 = requireActivity().getSharedPreferences("Authentication", Context.MODE_PRIVATE);
                // Get the SharedPreferences editor to remove stored values
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();

                // Remove the stored email and password values
                editor1.remove("aValue");


                // Apply changes to remove the values
                editor1.apply();

                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

// -------------    FINGERPRINT SWITCH  ------------------

        //  ********   Check if the device has a fingerprint sensor
        BiometricManager biometricManager = BiometricManager.from(requireContext());
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // Device supports biometric authentication
                checkFingerprintSwitch();
                s.setEnabled(true);
                s.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Check if the user is already logged in via SharedPreferences and redirect to home page
                        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Authentication", Context.MODE_PRIVATE);

                        if (s.isChecked()) {
                            saveAuthenticationDetails("enable");
                        } else {
                            saveAuthenticationDetails("disable");
                        }
                    }
                });

                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                // Device doesn't support biometric authentication
                s.setEnabled(false);
                saveAuthenticationDetails("disable");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                // Biometric sensor is currently unavailable
                s.setEnabled(false);
                saveAuthenticationDetails("disable");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Biometric sensor is available, but no fingerprints are enrolled
                // Handle this case or prompt the user to enroll fingerprints
                break;
        }
    }

    private void checkFingerprintSwitch() {
        SharedPreferences fingerprintSharedPreferences = getActivity().getSharedPreferences("Authentication", Context.MODE_PRIVATE);
        String fingerprintStatus = fingerprintSharedPreferences.getString("aValue", null);

        if ("enable".equals(fingerprintStatus)) {
            // Fingerprint is enabled
            s.setChecked(true);
        } else {
            // Fingerprint is not enabled or the value is null
            s.setChecked(false);
        }
    }

    private void saveAuthenticationDetails(String aValue) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Authentication", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("aValue", aValue);
        editor.apply();
    }

}