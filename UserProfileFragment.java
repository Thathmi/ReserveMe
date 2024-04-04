package lk.jiat.reserveme;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.Executor;

import io.github.muddz.styleabletoast.StyleableToast;
import lk.jiat.reserveme.model.Booking;

public class UserProfileFragment extends Fragment {

    static String hName;
    ListenerRegistration listenerRegistration;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email = EmailHolder.getUserEmail();
    String fname1;
    String lname1;
    String mobile1;
    static String uMobile;
    String password1;
    Switch s;

    public UserProfileFragment() {

    }

    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    // Password validation
//    private boolean isValidPassword(String password2) {
//
//        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";
//
//        return password2.matches(passwordPattern);
//    }

//    private boolean isValidMobile(String mobile2) {
//
//        String mobilePattern = "^07\\d{8}$";
//
//        return mobile2.matches(mobilePattern);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        s = view.findViewById(R.id.switch2);

        EditText fname = view.findViewById(R.id.editTextText8);
        EditText lname = view.findViewById(R.id.editTextText7);
        EditText password = view.findViewById(R.id.editTextText10);
        EditText mobile = view.findViewById(R.id.editTextText9);
        TextView email1 = view.findViewById(R.id.textView128);

//  ---------  LOAD DATA  --------------

        if (listenerRegistration == null) {
            listenerRegistration = db.collection("user").whereEqualTo("email", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (!value.isEmpty()) {
                        List<DocumentSnapshot> doc = value.getDocuments();
                        doc.forEach(d -> {

                            fname.setText(d.getString("fname"));
                            lname.setText(d.getString("lname"));
                            mobile.setText(d.getString("mobile"));
                            uMobile = d.getString("mobile");
                            email1.setText(d.getString("email"));
                            password.setText(d.getString("password"));
                        });
                    }
                }
            });
        }

//  -----------  UPDATE  ---------------

        view.findViewById(R.id.button16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences fingerprintSharedPreferences1 = getActivity().getSharedPreferences("Authentication", Context.MODE_PRIVATE);
                String fingerprintStatus1 = fingerprintSharedPreferences1.getString("aValue", null);

                if ("enable".equals(fingerprintStatus1)) {
                    // Fingerprint is enabled
                    //                fingerprint

                    BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                            .setTitle("User verification")
                            .setDescription("Please verify you fingerprint or face for update the profile")
                            .setNegativeButtonText("cancel")
                            .build();

                    getPrompt().authenticate(promptInfo);
//                fingerprint
                } else {
                    // Fingerprint is not enabled or the value is null
                    updateUser();
                }
                fname1 = fname.getText().toString();
                lname1 = lname.getText().toString();
                mobile1 = mobile.getText().toString();
                password1 = password.getText().toString();
            }
        });

//  -----------   LOGOUT   --------------

        view.findViewById(R.id.button18).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
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

        //    Check if the device has a fingerprint sensor
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

//  ----------   FINGERPRINT for update ---------------

    private BiometricPrompt getPrompt() {
        Executor executor = ContextCompat.getMainExecutor(getContext());
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                StyleableToast.makeText(getActivity(), errString.toString(), Toast.LENGTH_LONG, R.style.invalid).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
//                Toast.makeText(getActivity(), "Matched", Toast.LENGTH_SHORT).show();

                updateUser();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                StyleableToast.makeText(getActivity(), "Fingerprint doesn't match!", Toast.LENGTH_LONG, R.style.invalid).show();
            }
        };
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);
        return biometricPrompt;
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

    private void updateUser() {
//               * DATABASE *
//                update database
            db.collection("user")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    documentSnapshot.getReference().update("fname", fname1,
                                                    "lname", lname1,
                                                    "mobile", mobile1,
                                                    "password", password1)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    StyleableToast.makeText(getActivity(), "Updated successfuly", Toast.LENGTH_SHORT, R.style.success).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    StyleableToast.makeText(getActivity(), "Failed to update", Toast.LENGTH_SHORT, R.style.invalid).show();
                                                }
                                            });
                                }
                            } else {
                                StyleableToast.makeText(getActivity(), "No matching user found", Toast.LENGTH_LONG, R.style.invalid).show();
                            }
                        }
                    });
//                * DATABASE *
    }
}